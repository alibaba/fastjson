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
import java.util.ArrayList;

/**
 * @author wb_jianhui.shijh
 */
public class IEventDto implements Serializable {

    private static final long serialVersionUID = -3903138261314727539L;

    private String            source;

    private ArrayList<IEvent> eventList = new ArrayList<IEvent>();

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
    
    public ArrayList<IEvent> getEventList() {
        return eventList;
    }

    public void setEventList(ArrayList<IEvent> eventList) {
        this.eventList = eventList;
    }

    @Override
    public String toString() {
        return "IEventDto [source=" + source + ", eventList=" + eventList + "]";
    }

}
