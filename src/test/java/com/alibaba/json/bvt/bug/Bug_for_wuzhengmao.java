package com.alibaba.json.bvt.bug;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;

public class Bug_for_wuzhengmao extends TestCase {

    public void test_0() throws Exception {
        Node node1 = new Node();
        node1.setId(1);
        Node node2 = new Node();
        node2.setId(2);
        node1.setParent(node2);
        
        List<Node> list = Arrays.asList(new Node[] { node1, node2 });
        String json = JSON.toJSONString(list, true);
        System.out.println(json);
        List<Node> result = JSON.parseArray(json, Node.class);
        Assert.assertEquals(2, result.size());
        Assert.assertEquals(1, result.get(0).getId());
        Assert.assertEquals(2, result.get(1).getId());
        Assert.assertEquals(result.get(0).getParent(), result.get(1));
    }

    static class Node {

        int  id;
        Node parent;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public Node getParent() {
            return parent;
        }

        public void setParent(Node parent) {
            this.parent = parent;
        }
    }

}
