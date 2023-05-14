package com.alibaba.json.bvtVO.ae.huangliang2;

import com.alibaba.fastjson.annotation.JSONType;

/**
 * Created by huangliang on 17/5/8.
 */
@JSONType(typeName = "floorV2")
public class FloorV2 implements Floor {
    public String type;

    public String templateId;

    @Override
    public String getName() {
        return templateId;
    }
}
