package com.alibaba.json.bvt.bug;

import java.lang.reflect.Type;

import junit.framework.TestCase;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.util.TypeUtils;

public class Issue96 extends TestCase {

    public void test_for_issue() throws Exception {
        Page<Sub> page = new Page<Sub>(new Sub(1));
        Type type = new TypeReference<Page<Sub>>() {
        }.getType();
        // this is ok
        Page<Sub> page1 = JSON.parseObject(JSON.toJSONString(page), type);
        System.out.println(page1.sub.getClass());
    }

    public void xx_testCast() {
        Page<Sub> page = new Page<Sub>(new Sub(1));
        Type type = new TypeReference<Page<Sub>>() {
        }.getType();
        ParserConfig parserconfig = ParserConfig.getGlobalInstance();
        // !!!! this will fail:
        // !!!! com.alibaba.fastjson.JSONException: can not cast to : Page<Sub> TypeUtils.java:719
        Page<Sub> page1 = TypeUtils.cast(page, type, parserconfig);
        System.out.println(page1.sub.getClass());
    }

    static class Page<T> {

        public Page(){
            super();
        }

        public Page(T sub){
            super();
            this.sub = sub;
        }

        T sub;

        public T getSub() {
            return sub;
        }

        public void setSub(T sub) {
            this.sub = sub;
        }
    }

    static class Sub {

        public Sub(){
            super();
        }

        public Sub(int id){
            super();
            this.id = id;
        }

        int id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
