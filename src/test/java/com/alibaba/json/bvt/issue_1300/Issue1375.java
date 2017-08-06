package com.alibaba.json.bvt.issue_1300;

import com.alibaba.fastjson.serializer.CollectionCodec;
import com.alibaba.fastjson.serializer.ListSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by wenshao on 06/08/2017.
 */
public class Issue1375 extends TestCase {
    public void test_issue() throws Exception {
        assertSame(CollectionCodec.instance
                , SerializeConfig.getGlobalInstance()
                        .get(LinkedList.class));
        assertTrue(SerializeConfig.getGlobalInstance()
                        .get(ArrayList.class) instanceof ListSerializer);
    }
}
