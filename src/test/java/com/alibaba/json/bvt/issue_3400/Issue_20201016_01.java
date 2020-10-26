package com.alibaba.json.bvt.issue_3400;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.serializer.SerializerFeature;
import junit.framework.TestCase;

public class Issue_20201016_01 extends TestCase {
    public void testToString() {
        UserConfig user = new UserConfig();
        user.setAccount("account");
        user.setName("name");

        Config config = new Config();
        config.setCreator(user);
        config.setOwner(user);

        String s = JSON.toJSONString(config, SerializerFeature.WriteMapNullValue,
                SerializerFeature.QuoteFieldNames, SerializerFeature.WriteNullListAsEmpty);

        System.out.println(s);
    }


    public void testFastJson() {
        String s = "{\"agent\":null,\"creator\":{\"account\":\"account\",\"name\":\"name\",\"workid\":null},\"owner\":{\"$ref\":\"$.creator\"}}";

        System.out.println( JSON.parseObject(s, Config.class));
    }

    public static class Config {
        private UserConfig creator;
        private UserConfig owner;
        private UserConfig agent;

        public UserConfig getCreator() {
            return creator;
        }
        public void setCreator(UserConfig creator) {
            this.creator = creator;
        }
        public UserConfig getOwner() {
            return owner;
        }
        public void setOwner(UserConfig owner) {
            this.owner = owner;
        }
        public UserConfig getAgent() {
            return agent;
        }
        public void setAgent(UserConfig agent) {
            this.agent = agent;
        }
    }

    public static class UserConfig {
        private String workid;
        private String name;
        private String account;

        public String getWorkid() {
            return workid;
        }
        public void setWorkid(String workid) {
            this.workid = workid;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public String getAccount() {
            return account;
        }
        public void setAccount(String account) {
            this.account = account;
        }
    }
}
