package com.alibaba.json.bvtVO;

public class PhysicalQueue {

    private QueueEntity queue;
    private Integer     weight;
    private Integer     capacity;
    private int         inRate;
    private int         outRate;

    // Napoli 1.1 新加属性
    private boolean     sendable;
    private boolean     receivable;

    public PhysicalQueue(){

    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public int getInRate() {
        return inRate;
    }

    public void setInRate(int inRate) {
        this.inRate = inRate;
    }

    public int getOutRate() {
        return outRate;
    }

    public void setOutRate(int outRate) {
        this.outRate = outRate;
    }

    public boolean relationChanged(Object other) {
        return false;
    }

    public QueueEntity getQueue() {
        return queue;
    }

    public void setQueue(QueueEntity queue) {
        this.queue = queue;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public boolean isSendable() {
        return sendable;
    }

    public void setSendable(boolean sendable) {
        this.sendable = sendable;
    }

    public boolean isReceivable() {
        return receivable;
    }

    public void setReceivable(boolean receivable) {
        this.receivable = receivable;
    }

}
