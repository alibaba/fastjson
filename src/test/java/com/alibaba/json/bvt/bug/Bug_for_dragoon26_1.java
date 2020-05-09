package com.alibaba.json.bvt.bug;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.parser.ParserConfig;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class Bug_for_dragoon26_1 extends TestCase {
    protected void setUp() throws Exception {
        ParserConfig.global.addAccept("com.alibaba.json.bvt.bug.Bug_for_dragoon26_1");
    }

    public void test_0() throws Exception {

        List<MonitorItemAlarmRule> rules = new ArrayList<MonitorItemAlarmRule>();

        AlarmReceiver receiver1 = new AlarmReceiver(1L);

        {
            MonitorItemAlarmRule rule = new MonitorItemAlarmRule();

            rule.getAlarmReceivers().add(receiver1);
            rules.add(rule);
        }
        {
            MonitorItemAlarmRule rule = new MonitorItemAlarmRule();

            rule.getAlarmReceivers().add(receiver1);
            rules.add(rule);
        }

        String text = JSON.toJSONString(rules, SerializerFeature.WriteClassName);
        System.out.println(JSON.toJSONString(rules, SerializerFeature.WriteClassName, SerializerFeature.PrettyFormat));

        List<MonitorItemAlarmRule> message2 = (List<MonitorItemAlarmRule>) JSON.parse(text);
        System.out.println(JSON.toJSONString(message2, SerializerFeature.WriteClassName, SerializerFeature.PrettyFormat));
    }

    public static class MonitorItemAlarmRule {

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
