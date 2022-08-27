package com.alibaba.json.test.dubbo;

import java.io.Serializable;

public class Tiger implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -768303386469936078L;

    public Tiger(){
    }

    private String  tigerName;
    private Boolean tigerSex;

    public String getTigerName() {
        return tigerName;
    }

    public void setTigerName(String tigerName) {
        this.tigerName = tigerName;
    }

    public Boolean getTigerSex() {
        return tigerSex;
    }

    public void setTigerSex(Boolean tigerSex) {
        this.tigerSex = tigerSex;
    }

}