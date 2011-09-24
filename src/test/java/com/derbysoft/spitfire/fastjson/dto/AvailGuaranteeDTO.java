package com.derbysoft.spitfire.fastjson.dto;

public class AvailGuaranteeDTO extends AbstractDTO{
    private GuaranteeType guaranteeType;
    private CardCode cardCode;
    private TPAExtensionsDTO tpaExtensions;

    public void setGuaranteeType(GuaranteeType guaranteeType) {
        this.guaranteeType = guaranteeType;
    }

    public void setCardCode(CardCode cardCode) {
        this.cardCode = cardCode;
    }

    public void setTpaExtensions(TPAExtensionsDTO tpaExtensions) {
        this.tpaExtensions = tpaExtensions;
    }
}
