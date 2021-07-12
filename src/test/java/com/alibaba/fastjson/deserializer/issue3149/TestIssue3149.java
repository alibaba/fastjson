
package com.alibaba.fastjson.deserializer.issue3149;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


public class TestIssue3149 {

    @Test
    public void testIssue3149() {
        List<TEST.CirTest1> cir1 = new ArrayList<>();
        TEST.CirTest1 c1 = new TEST.CirTest1();
        cir1.add(c1);

        List<TEST.CirTest2> cir2 = new ArrayList<>();
        TEST.CirTest2 c2 = new TEST.CirTest2();
        cir2.add(c2);

        c1.setCir2(cir2);
        c2.setCir1(cir1);

        List<TEST.CirTest3> cir3 = new ArrayList<>();
        TEST.CirTest3 c3 = new TEST.CirTest3();
        cir3.add(c3);
        c3.setCir1(cir1);
        JSONObject jo = new JSONObject();
        jo.put("cir3", cir3);
        JSONArray jaCir3 = jo.getJSONArray("cir3");
        JSONObject getJSONObject = jaCir3.getJSONObject(0);
        Assert.assertEquals("{\"cir1\":[{\"cir2\":[{\"cir1\":[{\"cir2\":[{\"cir1\":[{\"$ref\":\"$.cir1[0].cir2[0].cir1[0]\"}]}]}]}]}]}",
                getJSONObject.toJSONString());

    }

    static class TEST {

        public static class CirTest1 {
            private List<CirTest2> cir2;

            public List<CirTest2> getCir2() {
                return cir2;
            }

            public void setCir2(List<CirTest2> cir2In) {
                this.cir2 = cir2In;
            }

        }

        static class CirTest2 {
            private List<CirTest1> cir1;

            public List<CirTest1> getCir1() {
                return cir1;
            }

            public void setCir1(List<CirTest1> cir1In) {
                this.cir1 = cir1In;
            }

        }


        static class CirTest3 {

            private List<CirTest1> cir1;

            public List<CirTest1> getCir1() {
                return cir1;
            }

            public void setCir1(List<CirTest1> cir1In) {
                this.cir1 = cir1In;
            }

        }

    }
}



