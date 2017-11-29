package com.alibaba.json.bvt.issue_1200;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import junit.framework.TestCase;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wenshao on 01/07/2017.
 */
public class Issue1299 extends TestCase {
    public void test_for_issue() throws Exception {
        String jsonStr = "{\"code\":201,\"data\":{\"materials\":[{\"material\":\"locale\",\"success\":true,"
                + "\"material_id\":356,\"id\":\"5099\"}],\"unitInfo\":{\"languages\":[\"'en_US'\",\"ru_RU\"],"
                + "\"unitName\":\"PC_ROCKBROS\",\"sceneKey\":\"shop_activity_page\",\"domain\":\"shopcdp.aliexpress"
                + ".com\",\"format\":\"HTML\",\"unitId\":\"1625\",\"id\":1761,\"rootPath\":\"shopcdp\","
                + "\"userId\":\"jianqing.zengjq\",\"platforms\":[\"pc\",\"mobile\"],\"status\":2}},\"success\":true}";

        UnitsSaveResponse response = JSON.parseObject(jsonStr, UnitsSaveResponse.class);
        Class<?> dataClass = response.getData().getClass();
        System.out.println(dataClass);
    }

    public static class ServiceResult<T> extends BaseResultDo implements Serializable {
        @JSONField(
                name = "data"
        )
        private T data;

        public ServiceResult() {
        }

        public T getData() {
            return this.data;
        }

        public void setData(T data) {
            this.data = data;
        }
    }

    public static class UnitsSaveResponse extends ServiceResult<UnitSave> {



    }

    public static class UnitSave implements Serializable {

        private SaveUnitInfo unitInfo;

        private List materials;

        public SaveUnitInfo getUnitInfo() {
            return unitInfo;
        }

        public void setUnitInfo(SaveUnitInfo unitInfo) {
            this.unitInfo = unitInfo;
        }

        public List getMaterials() {
            return materials;
        }

        public void setMaterials(List materials) {
            this.materials = materials;
        }
    }

    public static class SaveUnitInfo {

    }

    public static class BaseResultDo{

    }


}
