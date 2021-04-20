package com.alibaba.json.bvt.issue_3700;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.LongCodec;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

public class TestLongWriter {

    @Before
    public void initialize(){
        SerializeConfig.getGlobalInstance().put(Long.class, ToStringSerializer.instance);
    }

    @After
    public void restore(){
        SerializeConfig.getGlobalInstance().put(Long.class, LongCodec.instance);
    }

    @Test
    public void test01_normalLongWriter() {
        assertEquals("\"9223372036854775807\"", JSON.toJSONString(Long.MAX_VALUE));
        assertEquals("\"-9223372036854775808\"", JSON.toJSONString(Long.MIN_VALUE));

        Long[] longArr = new Long[2];
        longArr[0] = Long.MAX_VALUE;
        longArr[1] = Long.MIN_VALUE;
        assertEquals("[\"9223372036854775807\",\"-9223372036854775808\"]", JSON.toJSONString(longArr));
    }

    @Test
    public void test02_collectionLongWriter() {
        Collection<Long> coll = new ArrayList<Long>();
        coll.add(Long.MAX_VALUE);
        coll.add(Long.MIN_VALUE);
        assertEquals("[\"9223372036854775807\",\"-9223372036854775808\"]", JSON.toJSONString(coll));
    }

    @Test
    public void test03_linkedListLongWriter() {
        Collection<Long> coll = new LinkedList<Long>();
        coll.add(Long.MAX_VALUE);
        assertEquals("[\"9223372036854775807\"]", JSON.toJSONString(coll));
    }

    @Test
    public void test04_hashSetLongWriter() {
        Collection<Long> coll = new HashSet<Long>();
        coll.add(Long.MAX_VALUE);
        coll.add(Long.MIN_VALUE);
        assertEquals("[\"9223372036854775807\",\"-9223372036854775808\"]", JSON.toJSONString(coll));
    }

    @Test
    public void test05_linkedHashSetLongWriter() {
        Collection<Long> coll = new LinkedHashSet<Long>();
        coll.add(Long.MAX_VALUE);
        assertEquals("[\"9223372036854775807\"]", JSON.toJSONString(coll));
    }
}
