package com.derbysoft.spitfire.fastjson.dto;

import java.util.List;

public class ProviderChainDTO extends  AbstractDTO{
    private List<UniqueIDDTO> providers;


    public List<UniqueIDDTO> getProviders() {
        return providers;
    }

    public void setProviders(List<UniqueIDDTO> providers) {
        this.providers = providers;
    }
}
