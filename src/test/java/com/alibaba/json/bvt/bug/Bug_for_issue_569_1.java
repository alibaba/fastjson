package com.alibaba.json.bvt.bug;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import junit.framework.TestCase;
import org.junit.Assert;

import java.util.List;

/**
 * Created by wenshao on 16/8/11.
 */
public class Bug_for_issue_569_1 extends TestCase {
    public void test_for_issue() throws Exception {
        String str = "{\"bList\":[{\"data\":[0,1]},{\"data\":[1,2]},{\"data\":[2,3]},{\"data\":[3,4]},{\"data\":[4,5]},{\"data\":[5,6]},{\"data\":[6,7]},{\"data\":[7,8]},{\"data\":[8,9]},{\"data\":[9,10]}]}";
        A<Integer> aInteger;
        A<Long> aLong;

//        aInteger = JSON.parseObject(str, new TypeReference<A<Integer>>() {
//        });
//        Assert.assertEquals(aInteger.getbList().get(0).getData().get(0).getClass().getName(), Integer.class.getName());
//
        aLong = JSON.parseObject(str, new TypeReference<A<Long>>() {
        });
        Assert.assertEquals(aLong.getbList().get(0).getData().get(0).getClass().getName(), Long.class.getName());

    }



    public static class A<T> {
        private List<B<T>> bList;

        public List<B<T>> getbList() {
            return bList;
        }

        public void setbList(List<B<T>> bList) {
            this.bList = bList;
        }
    }

    public static class B<T> {
        private List<T> data;

        public List<T> getData() {
            return data;
        }

        public void setData(List<T> data) {
            this.data = data;
        }
    }
}
