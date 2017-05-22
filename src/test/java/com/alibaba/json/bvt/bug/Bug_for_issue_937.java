package com.alibaba.json.bvt.bug;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import junit.framework.TestCase;
import org.junit.Assert;

/**
 * Created by wuwen on 2016/12/7.
 */
public class Bug_for_issue_937 extends TestCase {

    public void test_for_issue() throws Exception {
        String json = "{outPara:{name:\"user\"}}";
        Out<Info> out = returnOut(json, Info.class);
        Assert.assertEquals("user", out.getOutPara().getName());
    }

    public static <T> Out<T> returnOut(String jsonStr, Class<T> c2) {
        return JSON.parseObject(jsonStr, new TypeReference<Out<T>>(c2) {
        });
    }

    public static class Out<T> {
        private T outPara;

        public void setOutPara(T t) {
            outPara = t;
        }

        public T getOutPara() {
            return outPara;
        }

        public Out() {
        }

        public Out(T t) {
            setOutPara(t);
        }
    }

    public static class Info {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
