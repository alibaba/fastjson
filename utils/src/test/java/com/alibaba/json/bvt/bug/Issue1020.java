package com.alibaba.json.bvt.bug;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

import java.time.LocalDate;

/**
 * Created by wenshao on 11/03/2017.
 */
public class Issue1020 extends TestCase {
    public void test_null() throws Exception {
        Vo vo = JSON.parseObject("{\"ld\":null}", Vo.class);
        assertNull(vo.ld);

    }

    public void test_empty() throws Exception {
        Vo vo = JSON.parseObject("{\"ld\":\"\"}", Vo.class);
        assertNull(vo.ld);

    }

    public static class Vo {
        public LocalDate ld;
    }
}
