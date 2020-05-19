package com.alibaba.json.bvt.bug;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;

public class Bug_for_javaeye_litterJava extends TestCase {
    public void test_for_bug() throws Exception {
        Group group = new Group();
        group.setId(123L);
        group.setName("xxx");
        group.getClzes().add(Group.class);
        
        String text = JSON.toJSONString(group);
        JSON.parseObject(text, Group.class);
    }

    public static class Group {

        private Long        id;
        private String      name;
        private List<Class> clzes = new ArrayList<Class>();

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<Class> getClzes() {
            return clzes;
        }

        public void setClzes(List<Class> clzes) {
            this.clzes = clzes;
        }

    }

}
