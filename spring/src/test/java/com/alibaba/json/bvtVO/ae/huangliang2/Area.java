package com.alibaba.json.bvtVO.ae.huangliang2;

import com.alibaba.fastjson.annotation.JSONType;

/**
 * Created by huangliang on 17/5/8.
 */
@JSONType(seeAlso = { Section.class, FloorV1.class,FloorV2.class  })
public interface Area {
    public static final String TYPE_SECTION = "section";
    public static final String TYPE_FLOORV1 = "floorV1";
    public static final String TYPE_FLOORV2 = "floorV2";

    String getName();
}
