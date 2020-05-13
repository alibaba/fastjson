package com.derbysoft.spitfire.fastjson.dto;

import java.util.List;

public class RatePlanDTO extends AbstractDTO{
    private String code;
    private String name;
    private FreeMealDTO freeMeal;
    private PaymentType paymentType;
    private List<ChargeItemDTO> taxes;
    private List<ChargeItemDTO> serviceCharges;
    private boolean needGuarantee;
    private CancelPolicyDTO cancelPolicy;
    private List<AvailGuaranteeDTO> availGuarantees;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FreeMealDTO getFreeMeal() {
        return freeMeal;
    }

    public void setFreeMeal(FreeMealDTO freeMeal) {
        this.freeMeal = freeMeal;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public List<ChargeItemDTO> getTaxes() {
        return taxes;
    }

    public void setTaxes(List<ChargeItemDTO> taxes) {
        this.taxes = taxes;
    }

    public List<ChargeItemDTO> getServiceCharges() {
        return serviceCharges;
    }

    public void setServiceCharges(List<ChargeItemDTO> serviceCharges) {
        this.serviceCharges = serviceCharges;
    }

    public boolean isNeedGuarantee() {
        return needGuarantee;
    }

    public void setNeedGuarantee(boolean needGuarantee) {
        this.needGuarantee = needGuarantee;
    }

    public CancelPolicyDTO getCancelPolicy() {
        return cancelPolicy;
    }

    public void setCancelPolicy(CancelPolicyDTO cancelPolicy) {
        this.cancelPolicy = cancelPolicy;
    }

    public List<AvailGuaranteeDTO> getAvailGuarantees() {
        return availGuarantees;
    }

    public void setAvailGuarantees(List<AvailGuaranteeDTO> availGuarantees) {
        this.availGuarantees = availGuarantees;
    }
}
