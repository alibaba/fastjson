package com.derbysoft.spitfire.fastjson.dto;

import java.util.ArrayList;
import java.util.List;

public class ErrorsDTO extends AbstractDTO {
    private List<WarningDTO> errors = new ArrayList<WarningDTO>();

    public List<WarningDTO> getErrors() {
        return errors;
    }

    public void setErrors(List<WarningDTO> errors) {
        this.errors = errors;
    }
}
