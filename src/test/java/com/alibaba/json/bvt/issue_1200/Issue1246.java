package com.alibaba.json.bvt.issue_1200;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import junit.framework.TestCase;
import org.springframework.util.LinkedMultiValueMap;

/**
 * Created by kimmking on 06/06/2017.
 */
public class Issue1246 extends TestCase {
    public void test_for_issue() throws Exception {
        B b = new B();
        b.setX("xx");
        String test = JSON.toJSONString( b );
        System.out.println(test);
        assertEquals("{}",test);
    }

    public static class A{
        private String x;

        public String getX() {
            return x;
        }

        public void setX(String x) {
            this.x = x;
        }
    }

    public static class B extends A{
        private String x;

        @Override
        @JSONField(serialize = false)
        public String getX() {
            return x;
        }

        @Override
        public void setX(String x) {
            this.x = x;
        }
    }
}
