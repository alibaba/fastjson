package com.alibaba.json.bvt.parser.deser.list;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;

public class ArrayListEnumFieldDeserializerTest extends TestCase {

    public void test_enums() throws Exception {
        Entity a = JSON.parseObject("{units:['NANOSECONDS', 'SECONDS', 3, null]}", Entity.class);
        Assert.assertEquals(TimeUnit.NANOSECONDS, a.getUnits().get(0));
    }

    public static class Entity {

        private List<TimeUnit> units = new ArrayList<TimeUnit>();

        public List<TimeUnit> getUnits() {
            return units;
        }

        public void setUnits(List<TimeUnit> units) {
            this.units = units;
        }

    }
}
