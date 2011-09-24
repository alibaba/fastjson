package com.derbysoft.spitfire.fastjson.dto;

public class ResponseHeader {
    private String taskId;
    
    public ResponseHeader() {
        
    }

    public ResponseHeader(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
}
