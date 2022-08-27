package com.alibaba.json.bvt.bug;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by wuwen on 2017/2/16.
 */
public class Issue1013 extends TestCase {

    public void test_for_issue() throws Exception {
        TestDomain domain = new TestDomain();

        String json = JSON.toJSONString(domain);

        TestDomain domain1 = JSON.parseObject(json, TestDomain.class);

        assertEquals(domain.getList(), domain1.getList());
    }

    public void test_for_issue_1() throws Exception {

        TestDomain domain1 = JSON.parseObject("{\"list\":[]}", TestDomain.class);
        TestDomain domain2 = JSON.parseObject("{\"list\":[1, 2]}", TestDomain.class);

        assertEquals(0, domain1.getList().size());
        assertEquals(Arrays.asList(1, 2), domain2.getList());
    }

    public void test_for_issue_2() throws Exception {

        TestDomain domain1 = JSON.parseObject("{\"list\":null}", TestDomain.class);

        assertEquals(1, domain1.getList().size());
    }

    public void test_for_issue3() throws Exception {
        TestDomain2 domain = new TestDomain2();

        String json = JSON.toJSONString(domain);

        TestDomain2 domain1 = JSON.parseObject(json, TestDomain2.class);

        assertEquals(domain.list, domain1.list);
    }

    public void test_for_issue_4() throws Exception {

        TestDomain2 domain1 = JSON.parseObject("{\"list\":[1, 2]}", TestDomain2.class);

        assertEquals(Arrays.asList(1, 2), domain1.list);
    }

    public void test_for_issue_5() throws Exception {

        TestDomain2 domain1 = JSON.parseObject("{\"list\":null}", TestDomain2.class);

        assertNull(domain1.list);
    }

    public void test_for_issue_6() throws Exception {

        TestDomain3 domain3 = JSON.parseObject("{\"list\":null}", TestDomain3.class);

        assertNull(domain3.list);
    }

    static class TestDomain {
        private List<Integer> list;

        public TestDomain(){
            list = new ArrayList<Integer>();
            list.add(1);
        }

        public List<Integer> getList(){
            return list;
        }
    }

    static class TestDomain2 {
        public List<Integer> list;

        public TestDomain2(){
            list = new ArrayList<Integer>();
            list.add(1);
        }
    }

    static class TestDomain3 {
        private List<Integer> list;

        public TestDomain3(){
            list = new ArrayList<Integer>();
            list.add(1);
        }

        public List<Integer> getList(){
            return list;
        }

        public void setList(List<Integer> list) {
            this.list = list;
        }
    }
}
