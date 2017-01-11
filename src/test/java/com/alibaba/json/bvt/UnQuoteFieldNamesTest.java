package com.alibaba.json.bvt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import junit.framework.TestCase;
import org.junit.Assert;

import java.util.Collections;
import java.util.Map;

/**
 * Created by wenshao on 11/01/2017.
 */
public class UnQuoteFieldNamesTest extends TestCase {
    public void test_for_issue() throws Exception {
        Map map = Collections.singletonMap("value", 123);

        String json = JSON.toJSONString(map
                , SerializeConfig.globalInstance
                , new SerializeFilter[0]
                , null
                , JSON.DEFAULT_GENERATE_FEATURE & ~SerializerFeature.QuoteFieldNames.mask
        );
        assertEquals("{value:123}", json);
    }
}
