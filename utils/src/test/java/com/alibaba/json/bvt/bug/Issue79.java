package com.alibaba.json.bvt.bug;

import java.util.List;

import org.junit.Assert;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;

public class Issue79 extends TestCase {

    public void test_for_issue_79() throws Exception {
        SearchResult result = JSON.parseObject("{\"present\":{\"records\":[{}]}}", SearchResult.class);
        Assert.assertNotNull(result.present);
        Assert.assertNotNull(result.present.records);
        Assert.assertNotNull(result.present.records.get(0));
        Assert.assertNotNull(result.present.records.get(0) instanceof PresentRecord);
    }

    public static class SearchResult {

        public Present present;
    }

    public static class Present {

        public List<PresentRecord> records;
    }

    public static class PresentRecord {

    }
}
