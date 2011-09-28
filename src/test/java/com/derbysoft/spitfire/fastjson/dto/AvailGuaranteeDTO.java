package com.derbysoft.spitfire.fastjson.dto;

public class AvailGuaranteeDTO extends AbstractDTO{
    private GuaranteeType guaranteeType;
    private CardCode cardCode;
    private TPAExtensionsDTO tpaExtensions;

    public GuaranteeType getGuaranteeType() {
        return guaranteeType;
    }

    public void setGuaranteeType(GuaranteeType guaranteeType) {
        this.guaranteeType = guaranteeType;
    }

    public CardCode getCardCode() {
        return cardCode;
    }

    public void setCardCode(CardCode cardCode) {
        this.cardCode = cardCode;
    }

    public TPAExtensionsDTO getTpaExtensions() {
        return tpaExtensions;
    }

    public void setTpaExtensions(TPAExtensionsDTO tpaExtensions) {
        this.tpaExtensions = tpaExtensions;
    }
}
