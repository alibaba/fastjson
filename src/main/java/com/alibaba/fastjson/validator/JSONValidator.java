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
    public RuntimeException valideta() {
        TrimLeftSpace();
        if(firstByte() != OBJ_START && firstByte() != ARR_START) {
            return new InvalidJSONException("Json file should start with { or [");
        }
        if(firstByte() == OBJ_START) {
            //
            TrimLeftSpace();
            if(len() == 0) {
                return null;
            }
        } else if(firstByte() == ARR_START) {
            //
            TrimLeftSpace();
            if (len() == 0) {
                return null;
            }
        }
        return new InvalidJSONException("Extra characters after parsing");
    }


    /**
     * Validate state of Object
     */
    public void validateObj() {
        Expect(OBJ_START);
        if(firstByte() == OBJ_END) {
            moveOne();
            return;
        }

    }

    /**
     * Verify a string of digit between 0 and 9
     * It should be return before the end of the byte date because json file can't finish with number
     */
    public void validateDigit() {
        if(firstByte() < '0' || firstByte() > '9') {
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
        throw new UnexceptedEOFException("there are another root");
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
            validateDigit();
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

    }
}



































































































