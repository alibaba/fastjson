package com.alibaba.json.bvt.bug;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONType;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

public class Issue804 extends TestCase {
    public void test_issue() throws Exception {
        String json = "{\n" +
                "    \"@type\":\"com.alibaba.json.bvt.bug.Issue804$Room\",\n" +
                "    \"children\":[],\n" +
                "    \"name\":\"Room2_1\",\n" +
                "    \"parent\":{\n" +
                "        \"@type\":\"com.alibaba.json.bvt.bug.Issue804$Area\",\n" +
                "        \"children\":[\n" +
                "            {\"$ref\":\"$\"},\n" +
                "            {\n" +
                "                \"@type\":\"com.alibaba.json.bvt.bug.Issue804$Room\",\n" +
                "                \"children\":[],\n" +
                "                \"name\":\"Room_2\",\n" +
                "                \"parent\":{\"$ref\":\"$.parent\"}\n" +
                "            }\n" +
                "        ],\n" +
                "        \"name\":\"Area1\",\n" +
                "        \"parent\":{\n" +
                "            \"@type\":\"com.alibaba.json.bvt.bug.Issue804$Area\",\n" +
                "            \"children\":[\n" +
                "                {\"$ref\":\"$.parent\"}\n" +
                "            ],\n" +
                "            \"name\":\"Area0\"\n" +
                "        }\n" +
                "    }\n" +
                "}";

        Room room = (Room) JSON.parse(json);

        assertSame(room, room.parent.children.get(0));
    }

    @JSONType
    public static class Node {
        protected String name;
        protected Node parent;
        protected List<Node> children = new ArrayList<Node>();

        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public Node getParent() {
            return parent;
        }
        public void setParent(Node parent) {
            this.parent = parent;
        }
        public List<Node> getChildren() {
            return children;
        }
        public void setChildren(List<Node> children) {
            this.children = children;
        }
    }

    @JSONType
    public static class Area extends Node{

    }

    @JSONType
    public static class Room extends Node{

    }
}
