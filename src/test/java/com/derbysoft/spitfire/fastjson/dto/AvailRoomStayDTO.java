package com.derbysoft.spitfire.fastjson.dto;

import com.alibaba.fastjson.annotation.JSONField;

public class AvailRoomStayDTO extends AbstractDTO {
    private RoomTypeDTO roomType;

    private RatePlanDTO ratePlan;

    private RoomRateDTO roomRate;

    private Integer quantity;

    private ProviderChainDTO providerChain;

    private LanguageType languageType;

    private TPAExtensionsDTO tpaExtensions;

    public RoomTypeDTO getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomTypeDTO roomType) {
        this.roomType = roomType;
    }

    public RatePlanDTO getRatePlan() {
        return ratePlan;
    }

    public void setRatePlan(RatePlanDTO ratePlan) {
        this.ratePlan = ratePlan;
    }

    public RoomRateDTO getRoomRate() {
        return roomRate;
    }

    public void setRoomRate(RoomRateDTO roomRate) {
        this.roomRate = roomRate;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @JSONField(name="PC")
    public ProviderChainDTO getProviderChain() {
        return providerChain;
    }

    @JSONField(name="PC")
    public void setProviderChain(ProviderChainDTO providerChain) {
        this.providerChain = providerChain;
    }

    @JSONField(name="LT")
    public LanguageType getLanguageType() {
        return languageType;
    }

    @JSONField(name="LT")
    public void setLanguageType(LanguageType languageType) {
        this.languageType = languageType;
    }

    public TPAExtensionsDTO getTpaExtensions() {
        return tpaExtensions;
    }

    public void setTpaExtensions(TPAExtensionsDTO tpaExtensions) {
        this.tpaExtensions = tpaExtensions;
    }
}
