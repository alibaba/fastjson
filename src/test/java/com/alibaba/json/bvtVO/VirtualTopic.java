package com.alibaba.json.bvtVO;

import java.util.HashMap;
import java.util.Map;

public class VirtualTopic {

    private static final long         serialVersionUID = 1115397330651723322L;

    private Map<Integer, QueueEntity> queueMap         = new HashMap<Integer, QueueEntity>();
    private Integer                   queueCount;

    private int                       queueLimit;

    private String                    description;

    public VirtualTopic(){

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the queueList
     */
    public Map<Integer, QueueEntity> getQueueMap() {
        return queueMap;
    }

    /**
     * @param queueList the queueList to set
     */
    public void setQueueMap(Map<Integer, QueueEntity> queueMap) {
        this.queueMap = queueMap;
    }

    /**
     * @return the queueCount
     */
    public Integer getQueueCount() {
        if (queueCount != null) {
            return queueCount;
        } else {
            return queueMap.size();
        }
    }

    /**
     * @param queueCount the queueCount to set
     */
    public void setQueueCount(Integer queueCount) {
        this.queueCount = queueCount;
    }

    public boolean propertiesChanged(Object other) {
        if (!(other instanceof VirtualTopic)) {
            return false;
        }
        VirtualTopic vt = (VirtualTopic) other;
        int size = queueMap.size();
        if (size != vt.queueMap.size()) {
            return true;
        }
        for (Integer id : queueMap.keySet()) {
            if (!vt.queueMap.containsKey(id)) {
                return true;
            }
        }
        return false;
    }

    public boolean relationChanged(Object other) {
        if (!(other instanceof VirtualTopic)) {
            return false;
        }
        VirtualTopic vt = (VirtualTopic) other;
        int size = queueMap.size();
        if (size != vt.queueMap.size()) {
            return true;
        }
        for (Integer id : queueMap.keySet()) {
            if (!vt.queueMap.containsKey(id)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param queueEntity
     */
    public void addQueue(QueueEntity queueEntity) {
        this.queueMap.put(queueEntity.getId(), queueEntity);
    }

    public int getQueueLimit() {
        return queueLimit;
    }

    public void setQueueLimit(int queueLimit) {
        this.queueLimit = queueLimit;
    }

}
