package com.alibaba.json.bvt.typeRef;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.parser.ParserConfig;
import java.util.List;
import junit.framework.TestCase;

import java.io.Serializable;

/**
 * Created by wenshao on 09/02/2017.
 */
public class TypeReferenceTest14 extends TestCase {
    public void test_0() throws Exception {
        String str = "{\"result\":{\"item\":[{\"key\":\"123\"}]}}";

        ParserConfig config = new ParserConfig();
        JSONObject.parseObject(str, OpenSearchResponse.class, config);
        JSONObject.parseObject(str
                , new TypeReference<OpenSearchResponse<JSONObject>>() {}.getType()
                , config, JSON.DEFAULT_PARSER_FEATURE);

        int size = config.getDeserializers().size();
        for (int i = 0; i < 100 * 1; ++i) {
            JSONObject.parseObject(str
                    , new TypeReference<OpenSearchResponse<JSONObject>>() {}.getType()
                    , config, JSON.DEFAULT_PARSER_FEATURE);
            assertEquals(size, config.getDeserializers().size());
        }
    }

    public static class OpenSearchResponse<T> {

        private OpenSearchResult<T> result;

        public OpenSearchResult<T> getResult() {
            return result;
        }

        public void setResult(OpenSearchResult<T> result) {
            this.result = result;
        }


    }

    public static class OpenSearchResult<T> {
        private List<T> item;

        public List<T> getItem() {
            return item;
        }

        public void setItem(List<T> item) {
            this.item = item;
        }
    }
}
