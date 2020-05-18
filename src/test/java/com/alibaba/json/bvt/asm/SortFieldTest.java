package com.alibaba.json.bvt.asm;

import org.junit.Assert;
import junit.framework.TestCase;

import java.util.LinkedHashMap;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class SortFieldTest extends TestCase {

    public void test_0() throws Exception {
        V0 entity = new V0();

        String text = JSON.toJSONString(entity, SerializerFeature.UseSingleQuotes, SerializerFeature.SortField);

        Assert.assertEquals("{'f0':0,'f1':0,'f10':0,'f11':0,'f12':0,'f13':0,'f14':0,'f2':0,'f3':0,'f4':0,'f5':0,'f6':0,'f7':0,'f8':0,'f9':0}", text);

        LinkedHashMap object = JSON.parseObject(text, LinkedHashMap.class);
        text = JSON.toJSONString(object, SerializerFeature.UseSingleQuotes, SerializerFeature.SortField);
        Assert.assertEquals("{'f0':0,'f1':0,'f10':0,'f11':0,'f12':0,'f13':0,'f14':0,'f2':0,'f3':0,'f4':0,'f5':0,'f6':0,'f7':0,'f8':0,'f9':0}", text);

    }
    
public void test_1() throws Exception {
    V1 entity = new V1();

    String text = JSON.toJSONString(entity, SerializerFeature.SortField);
    System.out.println(text);

    // 按字段顺序输出
    // {"f1":0,"f2":0,"f3":0,"f4":0,"f5":0} 
    Assert.assertEquals("{\"f1\":0,\"f2\":0,\"f3\":0,\"f4\":0,\"f5\":0}", text);

    JSONObject object = JSON.parseObject(text);
    text = JSON.toJSONString(object, SerializerFeature.SortField);
    Assert.assertEquals("{\"f1\":0,\"f2\":0,\"f3\":0,\"f4\":0,\"f5\":0}", text);

}

public static class V1 {

    private int f2;
    private int f1;
    private int f4;
    private int f3;
    private int f5;

    public int getF2() { return f2;}
    public void setF2(int f2) {this.f2 = f2;}
    public int getF1() {return f1;}
    public void setF1(int f1) {this.f1 = f1;}
    public int getF4() {return f4;}
    public void setF4(int f4) {this.f4 = f4;}
    public int getF3() {return f3;}
    public void setF3(int f3) {this.f3 = f3;}
    public int getF5() {return f5;}
    public void setF5(int f5) {this.f5 = f5;}
}

    public static class V0 {

        private int f5;
        private int f4;
        private int f3;
        private int f2;
        private int f1;
        private int f0;
        private int f6;
        private int f7;
        private int f8;
        private int f9;
        private int f14;
        private int f13;
        private int f12;
        private int f11;
        private int f10;

        public int getF5() {
            return f5;
        }

        public void setF5(int f5) {
            this.f5 = f5;
        }

        public int getF4() {
            return f4;
        }

        public void setF4(int f4) {
            this.f4 = f4;
        }

        public int getF3() {
            return f3;
        }

        public void setF3(int f3) {
            this.f3 = f3;
        }

        public int getF2() {
            return f2;
        }

        public void setF2(int f2) {
            this.f2 = f2;
        }

        public int getF1() {
            return f1;
        }

        public void setF1(int f1) {
            this.f1 = f1;
        }

        public int getF0() {
            return f0;
        }

        public void setF0(int f0) {
            this.f0 = f0;
        }

        public int getF6() {
            return f6;
        }

        public void setF6(int f6) {
            this.f6 = f6;
        }

        public int getF7() {
            return f7;
        }

        public void setF7(int f7) {
            this.f7 = f7;
        }

        public int getF8() {
            return f8;
        }

        public void setF8(int f8) {
            this.f8 = f8;
        }

        public int getF9() {
            return f9;
        }

        public void setF9(int f9) {
            this.f9 = f9;
        }

        public int getF14() {
            return f14;
        }

        public void setF14(int f14) {
            this.f14 = f14;
        }

        public int getF13() {
            return f13;
        }

        public void setF13(int f13) {
            this.f13 = f13;
        }

        public int getF12() {
            return f12;
        }

        public void setF12(int f12) {
            this.f12 = f12;
        }

        public int getF11() {
            return f11;
        }

        public void setF11(int f11) {
            this.f11 = f11;
        }

        public int getF10() {
            return f10;
        }

        public void setF10(int f10) {
            this.f10 = f10;
        }

    }

}
