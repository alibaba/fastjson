package com.alibaba.fastjson.validator;

import org.springframework.web.servlet.tags.EscapeBodyTag;

public class JSONValidator extends JSONBytes {

    JSONValidator(byte[] bytes) {
        super(bytes);
    }
    /**
     * entry function
     * The root of json can only be object or array.
     * We need to verify that it starts with { or [.
     */
    public RuntimeException validator() {
        TrimLeftSpace();
        if(firstByte() != OBJ_START && firstByte() != ARR_START) {
            return new InvalidJSONException("Json file should start with { or [");
        }
        if(firstByte() == OBJ_START) {
            validateObj();
            TrimLeftSpace();
            if(len() == 0) {
                return null;
            }
        } else if(firstByte() == ARR_START) {
            validateArr();
            TrimLeftSpace();
            if (len() == 0) {
                return null;
            }
        }
        return new InvalidJSONException("Extra characters after parsing");
    }


    /**
     * verify the state of an Object
     */
    public void validateObj() {
        Expect(OBJ_START);
        if(firstByte() == OBJ_END) {
            moveOne();
            return;
        }

        while(true) {
            TrimLeftSpace();
            validateStr();

            TrimLeftSpace();
            Expect(SEP_COLON);

            TrimLeftSpace();
            validateValue();

            TrimLeftSpace();

            if(firstByte() == SEP_COMMA) {
                moveOne();
            } else if(firstByte() == OBJ_END) {
                moveOne();
                return;
            }else {
                throw new InvalidJSONException("expect any one of the following characters: ','  '}'");
            }
        }
    }

    /**
     * verify the state of an Array
     */
    public void validateArr() {
        Expect(ARR_START);
        if(firstByte() == ARR_END) {
            moveOne();
            return;
        }

        while(true) {
            TrimLeftSpace();
            validateValue();

            TrimLeftSpace();

            if(firstByte() == SEP_COMMA) {
                moveOne();
            } else if(firstByte() == ARR_END) {
                moveOne();
                return;
            }else {
                throw new InvalidJSONException("expect any one of the following characters: ','  ']'");
            }
        }
    }

    /**
     * Verify a string of digit between 0 and 9
     * It should be return before the end of the byte date because json file can't finish with number
     */
    public void validateDigit() {
        TrimLeftSpace();
        if(firstByte() < '0' || firstByte() > '9') {
            System.out.println(firstByte());
            throw new InvalidJSONException("expect any one of the following characters: '0'  '1'  '2'  '3'  '4'  '5'  '6'  '7'  '8'  '9'\n" + getPartOfjson());
        }
        moveOne();
        int nmove = 0;
        for( byte b : getJsonBytes()) {
            if((char)b < '0' || (char)b > '9') {
                moveX(nmove);
                return;
            }
            nmove++;
        }
        throw new UnexceptedEOFException("The file can't be ended by a digit");
    }

    /**
     * verify json number and we use validateDigit to verify a string of digit
     */
    public void validateNumber() {
        if(firstByte() == NUMBER_MINUS) {
            moveOne();
        }
        if(firstByte() == NUMBER_ZERO) {
            moveOne();
        } else if(firstByte() >= '1' || firstByte() <= '9') {
            moveOne();
            if(firstByte() >= '0' && firstByte() <= '9') {
                validateDigit();
            }
        } else {
            throw new InvalidJSONException("expect any one of the following characters: '-'  '0'  '1'  '2'  '3'  '4'  '5'  '6'  '7'  '8'  '9'");
        }
        if(firstByte() == NUMBER_DOT) {
            moveOne();
            validateDigit();
        }
        if(firstByte() != NUMBER_e && firstByte()!= NUMBER_E) {
            return;
        }
        moveOne();
        if(firstByte() == NUMBER_PLUS || firstByte() == NUMBER_MINUS) {
            moveOne();
        }
        validateDigit();
        return;
    }

    /**
     * verify json Escape character
     * verify do we have \  +   "、\、/、b、f、n、r、t
     */
    public void validateEsc() {
        boolean escap = false;
        for(char c : ESCAPE_CHARACTER) {
            if(c == firstByte()) {
                escap = true;
            }
        }
        if(escap){
            moveOne();
            TrimLeftSpace();
            return;
        } else if(firstByte() == FOUR_HEXADECIMAL_DIGITS){
            for(int i = 1; i <= 4; i++) {
                char b = byteX(i);
                if(!(b >= '0' && b <= '9') &&
                   !(b >= 'A' && b <= 'Z') &&
                   !(b >= 'a' && b<= 'z')) {
                    throw new StringEscapeException("expect to get unicode characters consisting of \\u and 4 hexadecimal digits");
                }
            }
            moveX(5);
            TrimLeftSpace();
            return;
        }
        throw new StringEscapeException("expect to get unicode characters consisting of \\u and 4 hexadecimal digits, or any one of the following characters: '\"'  '\\'  '/'  'b'  'f'  'n'  'r'  't'");
    }

    public void validateStr() {
        Expect(QUOTATION_MARK);
        while(getPosition() < getMaxPosition()){
            int nmove = 0;
            for(byte b : getJsonBytes()) {
                char c = (char)b;
                if(c == QUOTATION_MARK){
                    moveX(++nmove);
                    return;
                } else if(c == REVERSE_SOLIDUS) {
                    nmove++;
                    moveX(nmove);
                    validateEsc();
                    break;
                } else if(c < CONTROL_CHARACTER) {
                    throw new InvalidJSONException("control characters are not allowed in string type(< 0x20)");
                }
                nmove++;
            }
        }
        throw new UnexceptedEOFException(new String(getJsonBytes()));
    }

    /**
     * there are only seven value type and we are going to test them one by one
     */
    public void validateValue() {
        char b = (char)firstByte();
        switch (b) {
            case QUOTATION_MARK:
                validateStr();
                break;
            case OBJ_START:
                validateObj();
                break;
            case ARR_START:
                validateArr();
                break;
            case BOOL_T:
                if(byteX(1) != 'r' || byteX(2) != 'u' || byteX(3) != 'e') {
                    throw new InvalidJSONException("expect a bool value: true");
                }
                moveX(4);
                return;
            case BOOL_F:
                if(byteX(1) != 'a' || byteX(2) != 'l' || byteX(3) != 's' || byteX(4) != 'e') {
                    throw new InvalidJSONException("expect a bool value: false");
                }
                moveX(5);
                return;
            case NULL_START:
                if(byteX(1) != 'u' || byteX(2) != 'l' || byteX(3) != 'l'){
                    throw new InvalidJSONException("expect a null value: null");
                }
                moveX(4);
                return;
            default:
                if(b == NUMBER_MINUS || b == NUMBER_ZERO || (b >= '1' && b <= '9')) {
                    validateNumber();
                } else {
                   throw new InvalidJSONException("expect any one of the following characters: '\"'  '{'  '['  't'  'f'  'n'  '-'  '0'  '1'  '2'  '3'  '4'  '5'  '6'  '7'  '8'  '9'");
                }
        }
        return;
    }


}
































































































































































