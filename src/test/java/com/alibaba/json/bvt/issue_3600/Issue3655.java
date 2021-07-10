package com.alibaba.json.bvt.issue_3600;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.junit.Assert;
import org.junit.Test;

public class Issue3655 {
    private final static String jsonStr =
            "{\"data\":\"\",\"data2\":\"\",\"data3\":\"\",\"data4\":\"\",\"data5\":\"\",\"data6\":\"\",\"data7\":\"\",\"data8\":\"\",\"data9\":\"\"}";

    @Test
    public void test_inherit_from_abstract_class_1() {
        issue3655_b b = new issue3655_b(null, null, null, null, null, null, null, null, null);
        String result = JSON.toJSONString(b, SerializerFeature.WriteNullStringAsEmpty);
        System.out.println(result);
        Assert.assertEquals(jsonStr, result);
    }

    @Test
    public void test_inherit_from_abstract_class_2() {
        issue3655_c c = new issue3655_c(null, null, null, null, null, null, null, null, null);
        String result = JSON.toJSONString(c, SerializerFeature.WriteNullStringAsEmpty);
        System.out.println(result);
        Assert.assertEquals(jsonStr, result);
    }

    public static class issue3655_b extends issue3655_a {
        private String data;
        private String data2;
        private String data3;
        private String data4;
        private String data5;
        private String data6;
        private String data7;
        private String data8;
        private String data9;

        public String getData() {
            return data;
        }

        public String getData2() {
            return data2;
        }

        public String getData3() {
            return data3;
        }

        public String getData4() {
            return data4;
        }

        public String getData5() {
            return data5;
        }

        public String getData6() {
            return data6;
        }

        public String getData7() {
            return data7;
        }

        public String getData8() {
            return data8;
        }

        public String getData9() {
            return data9;
        }

        public void setData(String data) {
            this.data = data;
        }

        public void setData2(String data2) {
            this.data2 = data2;
        }

        public void setData3(String data3) {
            this.data3 = data3;
        }

        public void setData4(String data4) {
            this.data4 = data4;
        }

        public void setData5(String data5) {
            this.data5 = data5;
        }

        public void setData6(String data6) {
            this.data6 = data6;
        }

        public void setData7(String data7) {
            this.data7 = data7;
        }

        public void setData8(String data8) {
            this.data8 = data8;
        }

        public void setData9(String data9) {
            this.data9 = data9;
        }

        public issue3655_b(
                String data, String data2, String data3, String data4, String data5,
                String data6, String data7, String data8, String data9) {
            this.data = data;
            this.data2 = data2;
            this.data3 = data3;
            this.data4 = data4;
            this.data5 = data5;
            this.data6 = data6;
            this.data7 = data7;
            this.data8 = data8;
            this.data9 = data9;
        }
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class issue3655_c extends issue3655_a {
        private String data;
        private String data2;
        private String data3;
        private String data4;
        private String data5;
        private String data6;
        private String data7;
        private String data8;
        private String data9;
    }

    public static abstract class issue3655_a {
        public abstract Object getData();

        public abstract Object getData2();

        public abstract Object getData3();

        public abstract Object getData4();

        public abstract Object getData5();

        public abstract Object getData6();

        public abstract Object getData7();

        public abstract Object getData8();

        public abstract Object getData9();
    }

}