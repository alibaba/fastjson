package com.alibaba.fastjson.validator;

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


}
























