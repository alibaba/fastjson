package com.alibaba.json.bvt.bug;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import junit.framework.TestCase;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by wenshao on 06/12/2016.
 */
public class Issue912 extends TestCase {
    public void test_for_issue() throws Exception {
        String allMethods = "{\"mList\":[{\"className\":\"com.qa.scftemplate.contract.ISCFServiceForDyjAction\",\"methodName\":\"getArrayInt\",\"parameterSize\":1,\"parameters\":[{\"clazz\":\"[I\",\"clsList\":null,\"isGenericity\":false,\"value\":\"\"}],\"returnType\":\"[I\",\"url\":\"tcp://SCFServiceForDyj/SCFServiceForDyjActionService\"},{\"className\":\"com.qa.scftemplate.contract.ISCFServiceForDyjAction\",\"methodName\":\"getArrayPrimative\",\"parameterSize\":7,\"parameters\":[{\"clazz\":\"[I\",\"clsList\":null,\"isGenericity\":false,\"value\":\"\"},{\"clazz\":\"[F\",\"clsList\":null,\"isGenericity\":false,\"value\":\"\"},{\"clazz\":\"[S\",\"clsList\":null,\"isGenericity\":false,\"value\":\"\"},{\"clazz\":\"[D\",\"clsList\":null,\"isGenericity\":false,\"value\":\"\"},{\"clazz\":\"[J\",\"clsList\":null,\"isGenericity\":false,\"value\":\"\"},{\"clazz\":\"[B\",\"clsList\":null,\"isGenericity\":false,\"value\":\"\"},{\"clazz\":\"[C\",\"clsList\":null,\"isGenericity\":false,\"value\":\"\"}],\"returnType\":\"[Ljava.lang.String;\",\"url\":\"tcp://SCFServiceForDyj/SCFServiceForDyjActionService\"}]}";
        JsonBean jsonBean = getJsonData(allMethods, JsonBean.class);

        assertEquals(2, jsonBean.getmList().size());
        SCFMethod m1 = jsonBean.getmList().get(0);
        assertNotNull(m1);
    }

    public static <T> T getJsonData(String json, Class<T> clazz) {
        T jd = (T) JSON.parseObject(json, clazz,
                Feature.IgnoreNotMatch,
                Feature.AutoCloseSource
        );
        return jd;
    }

    public static class JsonBean {
        private List<SCFMethod> mList;

        public List<SCFMethod> getmList() {
            return mList;
        }

        public void setmList(List<SCFMethod> mList) {
            this.mList = mList;
        }
    }

    public static class SCFMethod {
        public String className;
        public String url;
        public String methodName;
        public int parameterSize;
        public List<SCFMethodParameter> parameters = new LinkedList<SCFMethodParameter>();
        public Class<?> returnType;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public Class<?> getReturnType() {
            return returnType;
        }

        public void setReturnType(Class<?> returnType) {
            this.returnType = returnType;
        }

        public String getMethodName() {
            return methodName;
        }

        public void setMethodName(String methodName) {
            this.methodName = methodName;
        }

        public int getParameterSize() {
            return parameterSize;
        }

        public void setParameterSize(int parameterSize) {
            this.parameterSize = parameterSize;
        }

        public List<SCFMethodParameter> getParameters() {
            return parameters;
        }

        public void setParameters(List<SCFMethodParameter> parameters) {
            this.parameters = parameters;
        }
    }

    public static class SCFMethodParameter implements Cloneable {
        public Class<?> clazz;
        public Object value;
        public boolean isGenericity = false;
        public List<Class<?>> clsList;

        public boolean getIsGenericity() {
            return isGenericity;
        }

        public void setIsGenericity(boolean isGenericity) {
            this.isGenericity = isGenericity;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }

        public Class<?> getClazz() {
            return clazz;
        }

        public void setClazz(Class<?> clazz) {
            this.clazz = clazz;
        }

        public List<Class<?>> getClsList() {
            return clsList;
        }

        public void setClsList(List<Class<?>> clsList) {
            this.clsList = clsList;
        }
    }
}
