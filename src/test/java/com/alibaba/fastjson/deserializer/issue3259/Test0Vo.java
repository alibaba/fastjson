package com.alibaba.fastjson.deserializer.issue3259;

import java.util.Map;

public class Test0Vo {
    private Map<Long, Test1Vo> voMap;

    public Map<Long, Test1Vo> getVoMap() {
        return voMap;
    }

    public void setVoMap(Map<Long, Test1Vo> voMap) {
        this.voMap = voMap;
    }
}