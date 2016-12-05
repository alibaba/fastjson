package com.alibaba.json.bvt.bug;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONWriter;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import junit.framework.TestCase;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.charset.CodingErrorAction;

/**
 * Created by wenshao on 27/11/2016.
 */
public class Issue918 extends TestCase {
    public void test_for_issue() throws Exception {
        Person person = new Person("test1", 10);
        serialTest(person);

        ApiResponse<Object> apiResponse = new ApiResponse<Object>();
        Result<Object> result = new Result<Object>();
        result.setData(new Object());
        apiResponse.setResult(result);

        serialTest(apiResponse);
    }

    public static class JsonSerializer {
        private static final String NAMESPACE = "teslaSpace";
        private static final Charset CHARSET = Charset.forName("UTF-8");

        public static <T> void serialize(T obj, OutputStream out) {
            setTeslaJson();
            JSONWriter writer = null;
            try {
                writer = new JSONWriter(new OutputStreamWriter(out, CHARSET.newEncoder().onMalformedInput(CodingErrorAction.IGNORE)));
                writer.config(SerializerFeature.QuoteFieldNames, true);
                writer.config(SerializerFeature.SkipTransientField, true);
                writer.config(SerializerFeature.SortField, true);
                writer.config(SerializerFeature.WriteEnumUsingToString, false);
                writer.config(SerializerFeature.WriteClassName, true);
                writer.config(SerializerFeature.DisableCircularReferenceDetect, true);
                writer.writeObject(obj);
                writer.flush();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (writer != null) {
                    try {
                        writer.close();
                    } catch (Exception e) {
                    }
                }
            }
        }

        public static <T> T deserialize(byte[] in) {
            setTeslaJson();
            return (T) JSON.parse(in, 0, in.length, CHARSET.newDecoder(), Feature.AllowArbitraryCommas,
                    Feature.IgnoreNotMatch, Feature.SortFeidFastMatch, Feature.DisableCircularReferenceDetect,
                    Feature.AutoCloseSource);
        }


        private static void setTeslaJson() {
            if (!JSON.DEFAULT_TYPE_KEY.equals(NAMESPACE)) {
                JSON.setDefaultTypeKey(NAMESPACE);
            }
            if (!SerializeConfig.globalInstance.getTypeKey().equals(NAMESPACE)) {
                SerializeConfig.globalInstance.setTypeKey(NAMESPACE);
            }
        }

    }


    public static class Person {
        private String name;
        private Integer age;

        public Person() {
        }

        public Person(String name, Integer age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

        @Override
        public String toString() {
            return "Person{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    '}';
        }
    }

    public static class Result<TYPE> {//假设泛型类型为TYPE, github页面样式错误，不能显示出
        private TYPE data;

        public Result() {
        }

        public TYPE getData() {
            return data;
        }

        public void setData(TYPE data) {
            this.data = data;
        }
    }

    public static class ApiResponse<TYPE> {//假设泛型类型为TYPE, github页面样式错误，不能显示出
        private Result<TYPE> result; //假设泛型类型为TYPE, github页面样式错误，不能显示出

        public ApiResponse() {
        }

        public Result<TYPE> getResult() {
            return result;
        }

        public void setResult(Result<TYPE> result) {
            this.result = result;
        }
    }




    private static Object serialTest(Object object) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            JsonSerializer.serialize(object, bos);

            return JsonSerializer.deserialize(bos.toByteArray());
        } finally {
            try {
                bos.close();
            } catch (IOException e) {
            }
        }
    }
}
