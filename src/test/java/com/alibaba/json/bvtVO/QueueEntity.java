package com.alibaba.json.bvtVO;

/**
 * Project: napoli.domain File Created at 2009-6-3 $Id: QueueEntity.java 55142 2010-08-24 01:43:14Z guolin.zhuanggl $ Copyright
 * 2008 Alibaba.com Croporation Limited. All rights reserved. This software is the confidential and proprietary
 * information of Alibaba Company. ("Confidential Information"). You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the license agreement you entered into with Alibaba.com.
 */

import java.util.HashMap;
import java.util.Map;

/**
 * 用于存Queue配置信息的类，这里的Queue就是指业务上对应的�1�7�辑Queue，可能会分布到多台机器上
 * 
 * @author xiaosong.liangxs
 */
public class QueueEntity {

    private int                         id;
    private String                      description;

    private Map<Integer, PhysicalQueue> pqMap = new HashMap<Integer, PhysicalQueue>();

    /**
     * @return the pqMap
     */
    public Map<Integer, PhysicalQueue> getPqMap() {
        return pqMap;
    }

    /**
     * @param pqMap the pqMap to set
     */
    public void setPqMap(Map<Integer, PhysicalQueue> pqMap) {
        this.pqMap = pqMap;
    }

    public QueueEntity(){
    }

    /**
     * @return the pqMap
     */
    public Map<Integer, PhysicalQueue> getPhysicalQueueMap() {
        return pqMap;
    }

    /**
     * @param pqMap the pqMap to set
     */
    public void setPhysicalQueueMap(Map<Integer, PhysicalQueue> pqMap) {
        this.pqMap = pqMap;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
