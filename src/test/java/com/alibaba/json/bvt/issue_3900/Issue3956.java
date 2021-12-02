package com.alibaba.json.bvt.issue_3900;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import junit.framework.TestCase;
import org.junit.Assert;

import java.time.LocalDateTime;

/**
 * @author : ganyu
 * <p> @Date: 2021/12/2 16:55 </p>
 */
public class Issue3956 extends TestCase {

    public void test_for_issue() throws Exception {
        String str = "{\"event_time\": \"2021-11-23 22:42:49.426000\"}";
        Event obj = JSON.parseObject(str, Event.class);
        Assert.assertTrue(obj.getEventTime().getYear() == 2021);
    }
    
    private static class Event {
        @JSONField(name = "event_time", format = "yyyy-MM-dd HH:mm:ss.SSSSSS")
        private LocalDateTime eventTime;
    
        public LocalDateTime getEventTime() {
            return eventTime;
        }
    
        // test error
        public Event(LocalDateTime eventTime) {
            this.eventTime = eventTime;
        }
    
        // test pass
//        public void setEventTime(LocalDateTime eventTime) {
//            this.eventTime = eventTime;
//        }
    }
}