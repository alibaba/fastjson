package com.alibaba.json.bvt.bug;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.parser.ParserConfig;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class Bug_for_dragoon26 extends TestCase {
    protected void setUp() throws Exception {
        ParserConfig.global.addAccept("com.alibaba.json.bvt.bug.Bug_for_dragoon26");
    }

    public void test_0() throws Exception {
        MonitorConfigMessage message = new MonitorConfigMessage();
        MonitorConfig config = new MonitorConfig();
        message.setContent(config);

        AlarmReceiver receiver1 = new AlarmReceiver(2001L);
        AlarmReceiver receiver2 = new AlarmReceiver(2002L);
        AlarmReceiver receiver3 = new AlarmReceiver(2003L);

        ArrayList<MonitorItem> items = new ArrayList<MonitorItem>();
        {
            MonitorItem item1 = new MonitorItem();
            item1.setId(1001L);

            MonitorItemAlarmRule rule = new MonitorItemAlarmRule();

            rule.getAlarmReceivers().add(receiver1);
            rule.getAlarmReceivers().add(receiver2);

            item1.getRules().add(rule);
            items.add(item1);
        }

        {

            MonitorItem item = new MonitorItem();
            item.setId(1002L);

            MonitorItemAlarmRule rule = new MonitorItemAlarmRule();

            rule.getAlarmReceivers().add(receiver1);
            rule.getAlarmReceivers().add(receiver3);

            item.getRules().add(rule);
            items.add(item);
        }
        {
            
            MonitorItem item = new MonitorItem();
            item.setId(1003L);
            
            MonitorItemAlarmRule rule = new MonitorItemAlarmRule();
            
            rule.getAlarmReceivers().add(receiver2);
            rule.getAlarmReceivers().add(receiver3);
            
            item.getRules().add(rule);
            items.add(item);
        }

        config.setMonitorItems(items);

        String text = JSON.toJSONString(message, SerializerFeature.WriteClassName);
        System.out.println(JSON.toJSONString(message, SerializerFeature.WriteClassName, SerializerFeature.PrettyFormat));

        MonitorConfigMessage message2 = (MonitorConfigMessage) JSON.parse(text);
        System.out.println(JSON.toJSONString(message2, SerializerFeature.WriteClassName, SerializerFeature.PrettyFormat));
    }

    public static class MonitorConfigMessage {

        private Object content;

        public Object getContent() {
            return content;
        }

        public void setContent(Object content) {
            this.content = content;
        }

    }

    public static class MonitorConfig {

        private Map<Long, MonitorItem> monitorItems = new HashMap<Long, MonitorItem>();

        @JSONField(name = "MonitorItems")
        public Collection<MonitorItem> getMonitorItems() {
            return monitorItems.values();
        }

        @JSONField(name = "MonitorItems")
        public void setMonitorItems(Collection<MonitorItem> items) {
            for (MonitorItem item : items) {
                this.monitorItems.put(item.getId(), item);
            }
        }
    }

    public static class MonitorItem extends MonitorItemBase<MonitorItemAlarmRule> {

    }

    public static class MonitorItemBase <K extends AlarmRuleBase> {

        private Long                       id;
        private List<K> rules = new ArrayList<K>();

        @JSONField(name = "mid")
        public Long getId() {
            return id;
        }

        @JSONField(name = "mid")
        public void setId(Long id) {
            this.id = id;
        }

        public List<K> getRules() {
            return rules;
        }

        public void setRules(List<K> rules) {
            this.rules = rules;
        }

    }
    
    public static class AlarmRuleBase {
        
    }

    public static class MonitorItemAlarmRule extends AlarmRuleBase {

        private List<AlarmReceiver> alarmReceivers = new ArrayList<AlarmReceiver>();

        public List<AlarmReceiver> getAlarmReceivers() {
            return alarmReceivers;
        }

        public void setAlarmReceivers(List<AlarmReceiver> alarmReceivers) {
            this.alarmReceivers = alarmReceivers;
        }

    }

    public static class AlarmReceiver {

        private Long id;

        public AlarmReceiver(){

        }

        public AlarmReceiver(Long id){
            this.id = id;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

    }
}
