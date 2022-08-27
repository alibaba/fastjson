package com.alibaba.json.bvt.compatible;

import com.alibaba.fastjson.util.FieldInfo;
import com.alibaba.fastjson.util.TypeUtils;
import junit.framework.TestCase;

import java.util.List;

/**
 * Created by wenshao on 20/03/2017.
 */
public class TypeUtilsComputeGettersTest extends TestCase {
    public void test_for_computeGetters() {
        List<FieldInfo> fieldInfoList = TypeUtils.computeGetters(Model.class, null);
        assertEquals(1, fieldInfoList.size());
        assertEquals("id", fieldInfoList.get(0).name);
    }

    public static class Model {
        private int id;

        public int getId() {
            return id;
        }
    }
}
