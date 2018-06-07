package com.alibaba.json.bvt.issue_1700;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class Issue1763 extends TestCase {
    public void test_for_issue() throws Exception {
        String s = "{\"result\":{\"modelList\":[{\"sourceId\":\"81900002\"},{\"sourceId\":\"81900002\"},{\"sourceId\":\"81892012\"},{\"sourceId\":\"2062014\"},{\"sourceId\":\"2082007\"},{\"sourceId\":\"2082007\"},{\"sourceId\":\"2082007\"}]}}";


        Method method = ProcurementOrderInteractiveServiceForCloud.class.getMethod("queryOrderMateriel", Map.class);
        Type type = method.getGenericReturnType();

        BaseResult<InteractiveOrderMaterielQueryResult> baseResult = JSON.parseObject(s, type);
        InteractiveOrderMaterielQueryResult result = baseResult.getResult();

        assertEquals(7, result.getModelList().size());
        assertEquals(InteractiveOrderMaterielModel.class, result.getModelList().get(0).getClass());
    }

    public static class BaseResult<T> {
        private T result;

        public T getResult() {
            return result;
        }

        public void setResult(T result) {
            this.result = result;
        }
    }

    public static class BasePageQueryResult<T> extends BaseResult<T>{
        private List<T> modelList;

        public List<T> getModelList() {
            return modelList;
        }

        public void setModelList(List<T> modelList) {
            this.modelList = modelList;
        }
    }

    public static class InteractiveOrderMaterielModel {
        private String sourceId;

        public String getSourceId() {
            return sourceId;
        }

        public void setSourceId(String sourceId) {
            this.sourceId = sourceId;
        }
    }

    public static class InteractiveOrderMaterielQueryResult<T extends InteractiveOrderMaterielModel> extends BasePageQueryResult<T> {

    }

    public interface ProcurementOrderInteractiveServiceForCloud {

        BaseResult<InteractiveOrderMaterielQueryResult> queryOrderMateriel(Map param);

    }

}
