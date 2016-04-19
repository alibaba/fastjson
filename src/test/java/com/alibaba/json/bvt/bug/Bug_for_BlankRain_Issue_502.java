package com.alibaba.json.bvt.bug;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;

public class Bug_for_BlankRain_Issue_502 extends TestCase {

    public void test_for_issue() throws Exception {
        People a1 = new People();
        a1.set姓名("A");
        a1.set类型("B");
        a1.set状态("C");
        a1.set满意度("D");
        a1.set统计("E");
        a1.set时间("F");

        String text = JSON.toJSONString(a1);
        Assert.assertEquals("{\"姓名\":\"A\",\"时间\":\"F\",\"满意度\":\"D\",\"状态\":\"C\",\"类型\":\"B\",\"统计\":\"E\"}", text);
        System.out.println(text);
        
        People a2 = JSON.parseObject(text, People.class);
        Assert.assertEquals(a1.get姓名(), a2.get姓名());
        Assert.assertEquals(a1.get类型(), a2.get类型());
        Assert.assertEquals(a1.get状态(), a2.get状态());
        Assert.assertEquals(a1.get满意度(), a2.get满意度());
        Assert.assertEquals(a1.get统计(), a2.get统计());
        Assert.assertEquals(a1.get时间(), a2.get时间());
    }

    public static class People {

        private String 姓名;
        private String 类型;
        private String 状态;
        private String 满意度;
        private String 统计;
        private String 时间;

        static List<String> head() {
            List<String> h = new ArrayList<String>();

            h.add("姓名");
            h.add("类型");
            h.add("状态");
            h.add("满意度");
            h.add("统计");
            h.add("时间");
            return h;
        }

        public String get姓名() {
            return 姓名;
        }

        public void set姓名(String 姓名) {
            this.姓名 = 姓名;
        }

        public String get类型() {
            return 类型;
        }

        public void set类型(String 类型) {
            this.类型 = 类型;
        }

        public String get状态() {
            return 状态;
        }

        public void set状态(String 状态) {
            this.状态 = 状态;
        }

        public String get满意度() {
            return 满意度;
        }

        public void set满意度(String 满意度) {
            this.满意度 = 满意度;
        }

        public String get统计() {
            return 统计;
        }

        public void set统计(String 统计) {
            this.统计 = 统计;
        }

        public String get时间() {
            return 时间;
        }

        public void set时间(String 时间) {
            this.时间 = 时间;
        }

    }

}
