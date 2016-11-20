package com.alibaba.json.bvt.bug;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import junit.framework.TestCase;

import java.util.Date;

/**
 * Created by wenshao on 2016/11/13.
 */
public class Issue744 extends TestCase {

    public static class Model {
        @JSONField(format="yyyy-MM-dd'T'HH:mm:ss")
        public Date date;
    }

    public void test() {
        String text = "{\"date\":\"9999-09-08T00:00:00\"}";
        Model model =JSON.parseObject(text, Model.class);

        String text2 = JSON.toJSONString(model);
        System.out.println(text2);
    }
}