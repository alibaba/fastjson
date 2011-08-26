package com.alibaba.json.test.dubbo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


public class Tigers implements Serializable {

    private static final long serialVersionUID = -1565572802090235506L;

    public Tigers(){
    }

    public Tigers(Tiger tiger){
        this.map = new HashMap<String, Tiger>();
        this.map.put("1st", tiger);
        this.tiger = tiger;
    }

    private Tiger              tiger;
    private Map<String, Tiger> map;

    public Map<String, Tiger> getMap() {
        return map;
    }

    public void setMap(Map<String, Tiger> map) {
        this.map = map;
    }

    public Tiger getTiger() {
        return tiger;
    }

    public void setTiger(Tiger tiger) {
        this.tiger = tiger;
    }

}