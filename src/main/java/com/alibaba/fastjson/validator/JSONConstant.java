package com.alibaba.fastjson.validator;

public interface JSONConstant {
     /**
      * json structure constant
      */
     char OBJ_START = '{'; //start of an object
     char OBJ_END   = '}'; //end of an object
     char ARR_START = '['; //start of an array
     char ARR_END   = ']'; //end of an array
     char SEP_COLON = ':'; //an value correspond to key
     char SEP_COMMA = ','; //next key pair or next value

     char BOOL_T = 't'; //a true
     char BOOL_F = 'f'; //a false

     char NULL_START = 'n'; //a null
     char CONTROL_CHARACTER = 0x20; //Json do not allow control characters under 0x20 exit

     /**
      * Json Escape character
      */
     char QUOTATION_MARK          = '"';
     char REVERSE_SOLIDUS         = '\\';
     char SOLIDUS                 = '/';
     char BACKSPACE               = 'b';
     char FORMFEED                = 'f';
     char NEWLINE                 = 'n';
     char CARRIAGE_RETURN         = 'r';
     char HORIZONTAL_TAB          = 't';
     char FOUR_HEXADECIMAL_DIGITS = 'u';
     char[] ESCAPE_CHARACTER = {
                QUOTATION_MARK,
                REVERSE_SOLIDUS,
                SOLIDUS,
                BACKSPACE,
                FORMFEED,
                NEWLINE,
                CARRIAGE_RETURN,
                HORIZONTAL_TAB};
     /**
      * Json Number
      */
     char NUMBER_DOT   = '.';
     char NUMBER_e     = 'e';
     char NUMBER_E     = 'E';
     char NUMBER_PLUS  = '+';
     char NUMBER_MINUS = '-';
     char NUMBER_ZERO  = '0';


}
