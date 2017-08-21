package com.alibaba.json.bvt.typeRef;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.annotation.JSONField;
import junit.framework.TestCase;

/**
 * Created by wenshao on 09/02/2017.
 */
public class TypeReferenceTest13 extends TestCase {
    public void test_typeRef() throws Exception {
        String json = "{\"result\":{}}";
        for (int i = 0; i < 100; ++i) {
            {
                SearchResult<ResultItem, CountFacet> searchResult = parseSearchResult(
                        json, ResultItem.class, CountFacet.class);
            }
            {
                SearchResult<ResultItem1, CountFacet1> searchResult = parseSearchResult(
                        json, ResultItem1.class, CountFacet1.class);
            }
        }
    }

    public static <I, F> SearchResult<I, F> parseSearchResult(String resultStr, Class<I> itemType,
                                                              Class<F> facetType) {
        SearchResult<I, F> searchResult = JSON.parseObject(resultStr, new TypeReference<SearchResult<I, F>>() {
        });

        return searchResult;
    }

    public static class ResultItem {

    }

    public static class CountFacet {

    }

    public static class ResultItem1 {

    }

    public static class CountFacet1 {

    }

    public static class SearchResult<I, F> extends BaseResult {

        /**
         * 大的结果对象，包含结果数据、耗时、数量统计等信息
         */
        @JSONField(name = "result")
        private ResultDO<I, F> result;

        /**
         * 目前没有用到
         */
        @JSONField(name = "tracer")
        private String         tracer;

        public String getTracer() {
            return tracer;
        }

        public void setTracer(String tracer) {
            this.tracer = tracer;
        }

        public ResultDO<I, F> getResult() {
            return result;
        }

        public void setResult(ResultDO<I, F> result) {
            this.result = result;
        }
    }

    public static class BaseResult {

    }

    public static class ResultDO<I, F> {

    }
}
