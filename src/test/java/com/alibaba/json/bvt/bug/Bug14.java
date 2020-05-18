package com.alibaba.json.bvt.bug;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;

public class Bug14 extends TestCase {

    public void test_0() throws Exception {
        double f = -5.5000009;
        Long i = 4294967295l;
        System.out.println(BigInteger.valueOf(i));
        System.out.println(Math.round(f));
        List<AB> list = new ArrayList<AB>();
        list.add(new AB("2a", "3b"));
        list.add(new AB("4a", "6b"));
        list.add(new AB("6a", "7{sdf<>jgh\n}b"));
        list.add(new AB("8a", "9b"));
        list.add(new AB("10a", "11ba"));
        list.add(new AB("12a", "13b"));
        String[] abc = new String[] { "sf", "sdf", "dsffds", "sdfsdf{fds}" };
        Map<String, AB> map = new LinkedHashMap();
        int k = 0;
        for (AB a : list) {
            map.put(String.valueOf(k++), a);
        }
        System.out.println(JSON.toJSON(list));
        System.out.println(JSON.toJSON(abc));
        System.out.println(JSON.toJSON(new AB("10a", "11ba")));
        System.out.println(JSON.toJSON(map));

    }

    private static class AB {

        private String a;
        private String b;

        public AB(){
            super();
        }

        public AB(String a, String b){
            super();
            this.a = a;
            this.b = b;
        }

        public String getA() {
            return a;
        }

        public String getB() {
            return b;
        }

    }
}
