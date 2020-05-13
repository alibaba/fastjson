package com.alibaba.json.bvt.bug;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

public class Bug_for_42283905 extends TestCase {

    public void test_0() throws Exception {

        String text;
        {
            List<Group> groups = new ArrayList<Group>();

            Command c0 = new Command(1);
            Command c1 = new Command(2);
            Command c2 = new Command(3);

            c1.setPre(c0);
            c2.setPre(c1);

            {
                Group group = new Group("g0");
                group.getBattleCommandList().add(c0);
                groups.add(group);
            }

            {
                Group group = new Group("g1");
                group.getBattleCommandList().add(c1);
                groups.add(group);
            }
            
            {
                Group group = new Group("g2");
                group.getBattleCommandList().add(c2);
                groups.add(group);
            }
            text = JSON.toJSONString(groups);
        }

        System.out.println(text);

        List<Group> groups = JSON.parseObject(text, new TypeReference<List<Group>>() {
        });
        Group g0 = groups.get(0);
        Group g1 = groups.get(1);

        System.out.println(JSON.toJSONString(groups));
    }

    public static class Group {

        private String        name;

        private List<Command> battleCommandList = new ArrayList<Command>();

        public Group(){

        }

        public Group(String name){
            this.name = name;
        }

        public List<Command> getBattleCommandList() {
            return battleCommandList;
        }

        public void setBattleCommandList(List<Command> battleCommandList) {
            this.battleCommandList = battleCommandList;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }

    public static class Command {

        private int id;

        public Command(){

        }

        public Command(int id){
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        private Command pre;

        public Command getPre() {
            return pre;
        }

        public void setPre(Command pre) {
            this.pre = pre;
        }

        public String toString() {
            return "{id:" + id + "}";
        }
    }
}
