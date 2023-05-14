package com.derbysoft.spitfire.fastjson.dto;

import java.util.ArrayList;
import java.util.List;

public class SuccessDTO extends AbstractDTO {
    private List<String> messages = new ArrayList<String>();

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> infos) {
        this.messages = infos;
    }
}
