package com.alibaba.json.bvtVO.kaola.goods;

import java.io.Serializable;

public class Consult implements Serializable {

    private int isShow;
    private String consultUrl;
    private int needAuth;

    public int getIsShow() {
        return isShow;
    }

    public void setIsShow(int isShow) {
        this.isShow = isShow;
    }

    public String getConsultUrl() {
        return consultUrl;
    }

    public void setConsultUrl(String consultUrl) {
        this.consultUrl = consultUrl;
    }

    public int getNeedAuth() {
        return needAuth;
    }

    public void setNeedAuth(int needAuth) {
        this.needAuth = needAuth;
    }
}
