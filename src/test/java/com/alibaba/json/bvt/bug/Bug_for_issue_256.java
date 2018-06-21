package com.alibaba.json.bvt.bug;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import junit.framework.TestCase;

public class Bug_for_issue_256 extends TestCase {

    public void test_for_issue() throws Exception {
        List<AisleDeployInfo> list3 = new ArrayList<AisleDeployInfo>();
        AisleDeployInfo aisleDeployInfo = new AisleDeployInfo();
        aisleDeployInfo.setId(1L);
        aisleDeployInfo.setProvinceArea("3,4,5");
        list3.add(aisleDeployInfo);

        AisleDeployInfo aisleDeployInfo1 = new AisleDeployInfo();
        aisleDeployInfo1.setId(2L);
        aisleDeployInfo1.setProvinceArea("3,4,5");
        list3.add(aisleDeployInfo1);

        List<AisleDeployInfo> list4 = new ArrayList<AisleDeployInfo>();
        list4.add(aisleDeployInfo);

        Map<String, List<AisleDeployInfo>> map3 = new HashMap<String, List<AisleDeployInfo>>();
        map3.put("1", list3);
        map3.put("2", list4);

        String str = JSON.toJSONString(map3);
        Map<String, List<AisleDeployInfo>> map1 = JSON.parseObject(str, new TypeReference<Map<String, List<AisleDeployInfo>>>(){});
        List<AisleDeployInfo> aList = map1.get("1");

        if (aList != null && aList.size() > 0) {
            for (int i = 0; i < aList.size(); i++) {
                System.out.println(aList.get(i).getId());
            }
        }
    }
    
    public static class AisleDeployInfo {
        private long id;
        private String provinceArea;
        
        public long getId() {
            return id;
        }

        
        public void setId(long id) {
            this.id = id;
        }


        
        public String getProvinceArea() {
            return provinceArea;
        }


        
        public void setProvinceArea(String provinceArea) {
            this.provinceArea = provinceArea;
        }
        
        
    }

    public static class Model extends HashMap {

    }
}
