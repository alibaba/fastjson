package com.derbysoft.spitfire.fastjson.dto;

import java.util.List;

public class RoomRateDTO extends AbstractDTO{
    private List<RateDTO> rates;

    public List<RateDTO> getRates() {
        return rates;
    }

    public void setRates(List<RateDTO> rates) {
        this.rates = rates;
    }
}
