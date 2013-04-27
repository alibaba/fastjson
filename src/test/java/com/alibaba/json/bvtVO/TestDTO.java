package com.alibaba.json.bvtVO;

import javax.xml.bind.annotation.XmlRootElement;

/**
 */
@XmlRootElement(name = "TestDTO")
public class TestDTO {

    private String       channel;
    private String       txCode;


    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getTxCode() {
        return txCode;
    }

    public void setTxCode(String txCode) {
        this.txCode = txCode;
    }


}
