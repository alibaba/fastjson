package com.alibaba.json.bvt.issue_1300;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.serializer.SerializerFeature;
import junit.framework.TestCase;

/**
 * Created by wenshao on 16/07/2017.
 */
public class Issue1319 extends TestCase {
    public void test_for_issue() throws Exception {
        MyTest test = new MyTest(1, MyEnum.Test1);
        String result = JSON.toJSONString(test, SerializerFeature.WriteClassName);
        System.out.println(result);
        test = JSON.parseObject(result, MyTest.class);
        System.out.println(JSON.toJSONString(test));
        assertEquals(MyEnum.Test1, test.getMyEnum());
        assertEquals(1, test.value);
    }

    @JSONType(seeAlso = {OtherEnum.class, MyEnum.class})
    interface EnumInterface{

    }
    @JSONType(typeName = "myEnum")
    enum MyEnum implements EnumInterface {
        Test1,
        Test2
    }
    @JSONType(typeName = "other")
    enum OtherEnum implements EnumInterface {
        Other
    }
    static class MyTest{
        private int value;
        private EnumInterface myEnum;

        public MyTest() {
        }

        public MyTest(int property, MyEnum enumProperty) {
            this.value = property;
            this.myEnum = enumProperty;
        }
        public int getValue() {
            return value;
        }
        public EnumInterface getMyEnum() {
            return myEnum;
        }
        public void setMyEnum(EnumInterface myEnum) {
            this.myEnum = myEnum;
        }
        public void setValue(int value) {
            this.value = value;
        }
    }
}
