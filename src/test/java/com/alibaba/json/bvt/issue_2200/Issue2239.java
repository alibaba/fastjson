package com.alibaba.json.bvt.issue_2200;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import junit.framework.TestCase;

import java.util.List;

public class Issue2239 extends TestCase {
    public void test_for_issue() throws Exception {

        String json = "{\"page\":{}}";

        BaseResponse<Bean> bean = JSON.parseObject(json,
                new TypeReference<BaseResponse<Bean>>() {
                });
//        bean.getPage().getList(); // 得到的是空
    }

    public static class Bean {

    }

    public static class BaseResponse<T> {

        private PageBean<T> page;



        public PageBean<T> getPage() {
            return page;
        }
    }

    public static class PageBean<T> {

        private List<T> list;

        public List<T> getList() {
            return list;
        }

        public void setList(List<T> list) {
            this.list = list;
        }
    }
}
