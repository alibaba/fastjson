package com.alibaba.json.bvt;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import java.util.List;
import junit.framework.TestCase;
import org.junit.Test;

/**
 * ArrayListTypeFieldDeserializer test
 *
 * @author maxiaoyao
 * @date 2018-05-08 16:04
 */

public class ArrayListTypeFieldDeserializerTest extends TestCase {

    @Test(expected = JSONException.class)
    public void parseObjectListPropertyWithOutSquareBracketsThrowException() {
        String body = "{\"sources\":\"5,6\"}";
        JSONObject.parseObject(body, ListPropertyBean.class);
    }

    private static class ListPropertyBean {

        private List<Integer> sources;

        public List<Integer> getSources() {
            return sources;
        }

        public void setSources(List<Integer> sources) {
            this.sources = sources;
        }
    }

}
