package com.alibaba.json.bvt.issue_3300;

import com.alibaba.fastjson.serializer.*;
import junit.framework.TestCase;

public class Issue3443 extends TestCase {
    public void testCustomJsonSerializerAndAfterFilter() throws Exception {
        SerializeWriter serializeWriter = new SerializeWriter();
        try {
            JSONSerializer jsonSerializer = new JSONSerializer(serializeWriter, new SerializeConfig());

            Parameter parameter = new Parameter();
            parameter.setParameterDesc(new ParameterDesc("vipExpireDate", "VIP expire date."));

            jsonSerializer.config(SerializerFeature.DisableCircularReferenceDetect, true);
            jsonSerializer.getAfterFilters().add(new CustomFilter());
            jsonSerializer.write(parameter);
            assertEquals("{\"parameterDesc\":{\"ParameterDesc\":\"VIP expire date.\"}}", serializeWriter.toString());
        } finally {
            serializeWriter.close();
        }
    }

    static class Parameter {
        private ParameterDesc parameterDesc;

        public ParameterDesc getParameterDesc() {
            return parameterDesc;
        }

        public void setParameterDesc(ParameterDesc parameterType) {
            this.parameterDesc = parameterType;
        }
    }

    static class ParameterDesc {
        private String parameterName;
        private String parameterUsage;
        // do some work...

        public ParameterDesc(String parameterName, String parameterUsage) {
            this.parameterName = parameterName;
            this.parameterUsage = parameterUsage;
        }


    }

    static class CustomFilter extends AfterFilter {

        @Override
        public void writeAfter(Object object) {
            if (object instanceof ParameterDesc) {
                writeKeyValue("ParameterDesc", "VIP expire date.");
            }
        }
    }
}
