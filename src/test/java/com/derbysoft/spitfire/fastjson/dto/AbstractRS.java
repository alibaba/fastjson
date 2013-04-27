package com.derbysoft.spitfire.fastjson.dto;

public class AbstractRS extends AbstractDTO {
    private SuccessDTO success;
    private WarningsDTO warnings;
    private ErrorsDTO errors;

    public SuccessDTO getSuccess() {
        return success;
    }

    public void setSuccess(SuccessDTO success) {
        this.success = success;
    }

    public WarningsDTO getWarnings() {
        return warnings;
    }

    public void setWarnings(WarningsDTO warnings) {
        this.warnings = warnings;
    }

    public ErrorsDTO getErrors() {
        return errors;
    }

    public void setErrors(ErrorsDTO errors) {
        this.errors = errors;
    }
}
