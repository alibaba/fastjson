package com.alibaba.json.bvt.ref;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class RefTest14 extends TestCase {

    public void test_0() throws Exception {
        Group admin = new Group("admin");

        User jobs = new User("jobs");
        User sager = new User("sager");
        User sdh5724 = new User("sdh5724");

        admin.getMembers().add(jobs);
        jobs.getGroups().add(admin);

        admin.getMembers().add(sager);
        sager.getGroups().add(admin);

        admin.getMembers().add(sdh5724);
        sdh5724.getGroups().add(admin);
        
        sager.setReportTo(sdh5724);
        jobs.setReportTo(sdh5724);

        SerializeConfig serializeConfig = new SerializeConfig();
        serializeConfig.setAsmEnable(false);
        String text = JSON.toJSONString(admin, serializeConfig, SerializerFeature.PrettyFormat);
        System.out.println(text);
        
        ParserConfig config = new ParserConfig();
        config.setAsmEnable(false);
        
        JSON.parseObject(text, Group.class, config, 0);
    }

    public static class Group {

        private String     name;

        private List<User> members = new ArrayList<User>();

        public Group(){

        }

        public Group(String name){
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<User> getMembers() {
            return members;
        }

        public void setMembers(List<User> members) {
            this.members = members;
        }

        public String toString() {
            return this.name;
        }
    }

    public static class User {

        private String      name;

        private List<Group> groups = new ArrayList<Group>();

        private User        reportTo;

        public User(){

        }

        public User getReportTo() {
            return reportTo;
        }

        public void setReportTo(User reportTo) {
            this.reportTo = reportTo;
        }

        public User(String name){
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<Group> getGroups() {
            return groups;
        }

        public void setGroups(List<Group> groups) {
            this.groups = groups;
        }

        public String toString() {
            return this.name;
        }
    }
}
