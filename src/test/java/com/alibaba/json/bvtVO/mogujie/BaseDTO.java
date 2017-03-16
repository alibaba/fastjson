package com.alibaba.json.bvtVO.mogujie;

import java.io.Serializable;

/**
 * Created by wenshao on 16/03/2017.
 */
public class BaseDTO implements Serializable {
    /**
     *  serialVersionUID
     */
    private static final long serialVersionUID = -1;

    /**
     * version
     */
    private String            version;

    /**
     * is online test
     */
    private Boolean           onlineTest = Boolean.FALSE;

    /**
     * http referer
     */
    private String referer;

    public String getVersion() {
        return version;
    }
    public void setVersion(String version) {
        this.version = version;
    }
    public Boolean isOnlineTest() {
        return onlineTest;
    }
    public void setOnlineTest(Boolean onlineTest) {
        this.onlineTest = onlineTest;
    }
    public String getReferer() {
        return referer;
    }
    public void setReferer(String referer) {
        this.referer = referer;
    }
    @Override
    public String toString() {
        return "BaseDTO [version=" + version + ", onlineTest=" + onlineTest + ", referer=" + referer + "]";
    }
}
