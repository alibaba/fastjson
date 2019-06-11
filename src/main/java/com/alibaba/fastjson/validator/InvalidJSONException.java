package com.alibaba.fastjson.validator;

public class InvalidJSONException extends RuntimeException{
    public InvalidJSONException(String errorMessage) {
        super(errorMessage);
    }
}
