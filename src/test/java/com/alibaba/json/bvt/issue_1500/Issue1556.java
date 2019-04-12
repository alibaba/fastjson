package com.alibaba.json.bvt.issue_1500;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import junit.framework.TestCase;

import java.io.Serializable;
import java.util.Date;

public class Issue1556 extends TestCase {
    public void test_for_issue() throws Exception {
        ClassForData classForData = new ClassForData();
        classForData.setDataName("dataname");
        SubCommonClass commonClass = new SubCommonClass(new Date());

        FirstSubClass firstSubClass = new FirstSubClass();
        firstSubClass.setAddr("It is addr");
        firstSubClass.setCommonInfo(commonClass);

        SecondSubClass secondSubClass = new SecondSubClass();
        secondSubClass.setName("It is name");
        secondSubClass.setCommonInfo(firstSubClass.getCommonInfo());

        classForData.setFirst(firstSubClass);
        classForData.setSecond(secondSubClass);

        ApiResult<ClassForData> apiResult = ApiResult.valueOfSuccess(classForData);
        ParserConfig config = new ParserConfig();
        config.setAutoTypeSupport(true);

        String jsonString = JSON.toJSONString(apiResult, SerializerFeature.WriteClassName);//这里加上SerializerFeature.DisableCircularReferenceDetect
        System.out.println(jsonString);
        Object obj = JSON.parse(jsonString, config);//这里加上Feature.DisableCircularReferenceDetect  这样的话 是可以避免空值的  ，但是$ref 还有啥意思呢
        System.out.println(JSON.toJSONString(obj));
    }

    public static class ApiResult<T> implements Serializable {
        private String msg;
        private int code;
        private T data;

        public ApiResult() {
        }

        public ApiResult(int code, String msg,T data) {
            this.code = code;
            this.msg = msg;
            this.data = data;
        }

        public String getMsg() {
            return msg;
        }
        public int getCode() {
            return code;
        }
        public void setMsg(String msg) {
            this.msg = msg;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public T getData() {
            return data;
        }

        public void setData(T data) {
            this.data = data;
        }

        public static <T> ApiResult<T> valueOfSuccess(T data) {
            return new ApiResult<T>(0, "Success", data);
        }

    }

    public static class ClassForData implements Serializable {

        private String dataName;

        private FirstSubClass first;

        private SecondSubClass second;


        public String getDataName() {
            return dataName;
        }

        public void setDataName(String dataName) {
            this.dataName = dataName;
        }

        public FirstSubClass getFirst() {
            return first;
        }

        public void setFirst(FirstSubClass first) {
            this.first = first;
        }

        public SecondSubClass getSecond() {
            return second;
        }

        public void setSecond(SecondSubClass second) {
            this.second = second;
        }
    }

    public static class FirstSubClass  implements Serializable{

        private String addr;//仅仅做下和second的区分

        private  SubCommonClass commonInfo;


        public String getAddr() {
            return addr;
        }

        public void setAddr(String addr) {
            this.addr = addr;
        }

        public SubCommonClass getCommonInfo() {
            return commonInfo;
        }

        public void setCommonInfo(SubCommonClass commonInfo) {
            this.commonInfo = commonInfo;
        }

    }

    public static class SecondSubClass implements Serializable{

        private String name;

        private  SubCommonClass commonInfo;


        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public SubCommonClass getCommonInfo() {
            return commonInfo;
        }

        public void setCommonInfo(SubCommonClass commonInfo) {
            this.commonInfo = commonInfo;
        }
    }


    public static class SubCommonClass  implements Serializable {

        private Date demoDate;

        public SubCommonClass(){
        }

        public SubCommonClass(Date demoDate){
            this.demoDate = demoDate;
        }

        public Date getDemoDate() {
            return demoDate;
        }

        public void setDemoDate(Date demoDate) {
            this.demoDate = demoDate;
        }
    }
}
