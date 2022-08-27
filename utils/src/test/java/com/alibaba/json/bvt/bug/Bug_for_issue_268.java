package com.alibaba.json.bvt.bug;

import java.util.EnumSet;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;

public class Bug_for_issue_268 extends TestCase {

    public void test_for_issue() throws Exception {
        V1 vo = new V1();
        vo.units = EnumSet.of(TimeUnit.DAYS, TimeUnit.HOURS);

        String text = JSON.toJSONString(vo);
        Assert.assertEquals("{\"units\":[\"HOURS\",\"DAYS\"]}", text);

        V1 vo1 = JSON.parseObject(text, V1.class);
        Assert.assertNotNull(vo1);
        Assert.assertEquals(vo.units, vo1.units);
    }
    
    public void test_for_issue_private() throws Exception {
        VO vo = new VO();
        vo.units = EnumSet.of(TimeUnit.DAYS, TimeUnit.HOURS);

        String text = JSON.toJSONString(vo);
        Assert.assertEquals("{\"units\":[\"HOURS\",\"DAYS\"]}", text);

        VO vo1 = JSON.parseObject(text, VO.class);
        Assert.assertNotNull(vo1);
        Assert.assertEquals(vo.units, vo1.units);
    }

    private static class VO {

        private EnumSet<TimeUnit> units;

        public EnumSet<TimeUnit> getUnits() {
            return units;
        }

        public void setUnits(EnumSet<TimeUnit> units) {
            this.units = units;
        }

    }
    
    public static class V1 {

        private EnumSet<TimeUnit> units;

        public EnumSet<TimeUnit> getUnits() {
            return units;
        }

        public void setUnits(EnumSet<TimeUnit> units) {
            this.units = units;
        }

    }
}
