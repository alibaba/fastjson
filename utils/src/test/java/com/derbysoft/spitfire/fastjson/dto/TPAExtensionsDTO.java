package com.derbysoft.spitfire.fastjson.dto;


import java.util.HashMap;
import java.util.Map;

public class TPAExtensionsDTO extends AbstractDTO {
    private Map<String,String> elements = new HashMap<String, String>();

    public Map<String,String> getElements() {
        return elements;
    }

    public void setElements(Map<String, String> elements) {
        this.elements = elements;
    }

    public void setElement(String key, String value) {
        elements.put(key,value);
    }
}
