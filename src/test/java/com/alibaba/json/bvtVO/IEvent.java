/**
 * Project: eve.services
 * 
 * File Created at 2011-12-21
 * $Id$
 * 
 * Copyright 2008 Alibaba.com Corporation Limited.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * Alibaba Company. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Alibaba.com.
 */
package com.alibaba.json.bvtVO;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Map;

/**
 * �¼�����
 * 
 * @author wb_jianhui.shijh
 */
public class IEvent implements Serializable {

    private static final long   serialVersionUID = -791431935700654454L;

    /**
     * �¼������
     */
    private String              name;

    /**
     * �¼�����Դ
     */
    private String              source;

    /**
     * �¼����
     */
    private Map<String, Object> detailData;

    /**
     * �¼�����ʱ��
     */
    private Timestamp           generateTime;

    /**
     * ���¼����������һ��Ψһ��־��ID.
     */
    private String              externalId;

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Map<String, Object> getDetailData() {
        return detailData;
    }

    public void setDetailData(Map<String, Object> detailData) {
        this.detailData = detailData;
    }

    public Timestamp getGenerateTime() {
        return generateTime;
    }

    public void setGenerateTime(Timestamp generateTime) {
        this.generateTime = generateTime;
    }

    @Override
    public String toString() {
        return "IEvent [name=" + name + ", source=" + source + ", externalId=" + externalId
                + ", generateTime=" + generateTime + ", detailData=" + detailData + "]";
    }

}
