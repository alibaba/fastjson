package com.alibaba.json.bvt.issue_2700;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.serializer.MiscCodec;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.spi.Module;

import junit.framework.TestCase;

public class Issue2752 extends TestCase {

    public void test_for_issue() {
        Pageable pageRequest = new PageRequest(0, 10, new Sort(new Sort.Order("id, desc")));
        SerializeConfig config = new SerializeConfig();
        config.register(new MyModule());
        String result = JSON.toJSONString(pageRequest, config);
        assertTrue(result.indexOf("\"property\":\"id, desc\"") != -1);
    }

    public class MyModule implements Module {

        @Override
        public ObjectDeserializer createDeserializer(ParserConfig config, Class type) {
            if (type.getName().equals("org.springframework.data.domain.Sort")) {
                return MiscCodec.instance;
            }
            return null;
        }

        @Override
        public ObjectSerializer createSerializer(SerializeConfig config, Class type) {
            if (type.getName().equals("org.springframework.data.domain.Sort")) {
                return MiscCodec.instance;
            }
            return null;
        }
    }

}
