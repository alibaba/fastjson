package com.alibaba.json.bvtVO.ae;

import com.alibaba.fastjson.annotation.JSONType;

/**
 * Created by huangliang on 17/4/12.
 */
@JSONType(seeAlso = { FloorV2.class, FloorV1.class, Section.class })
public interface Area {

    public String SECTION_TYPE = "section";

    public String FLOORV1_TYPE = "floorV1";

    public String FLOORV2_TYPE = "floorV2";

    public String getTemplateId();

    public String getType();
}
