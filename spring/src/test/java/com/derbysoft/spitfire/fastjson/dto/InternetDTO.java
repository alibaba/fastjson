package com.derbysoft.spitfire.fastjson.dto;

public class InternetDTO extends AbstractDTO{

    private InternetType internetType;
    private String chargeInfo;

    private TPAExtensionsDTO tpaExtensions;

    public InternetType getInternetType() {
        return internetType;
    }

    public void setInternetType(InternetType internetType) {
        this.internetType = internetType;
    }

    public String getChargeInfo() {
        return chargeInfo;
    }

    public void setChargeInfo(String chargeInfo) {
        this.chargeInfo = chargeInfo;
    }

    public TPAExtensionsDTO getTpaExtensions() {
        return tpaExtensions;
    }

    public void setTpaExtensions(TPAExtensionsDTO tpaExtensions) {
        this.tpaExtensions = tpaExtensions;
    }
}
