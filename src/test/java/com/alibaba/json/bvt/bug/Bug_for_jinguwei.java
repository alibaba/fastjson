package com.alibaba.json.bvt.bug;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;

public class Bug_for_jinguwei extends TestCase {

    public void test_null() throws Exception {
        VO vo = new VO();
        vo.setList(new ArrayList<String>());
        vo.getList().add(null);
        vo.getList().add(null);

        System.out.println(JSON.toJSONString(vo));
    }

    public static class VO {
        private List<String> list;

        public List<String> getList() {
            return list;
        }

        public void setList(List<String> list) {
            this.list = list;
        }

    }
}
