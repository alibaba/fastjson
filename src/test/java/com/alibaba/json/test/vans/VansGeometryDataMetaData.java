package com.vans.test.vansdemo.vo;

import com.alibaba.fastjson.JSONObject;
import com.vans.test.vansdemo.util.ModelUtils;

import java.io.Serializable;

/**
 * Created by xiaolin_kxl on 16/12/28.
 */

public class VansGeometryDataMetaData implements Serializable {
    public int version;
    public int uvs;
    public int normals;
    public int faces;
    public int vertices;
    public String generator;

    public VansGeometryDataMetaData(JSONObject object) {
        if (object == null) {
            return;
        }
        generator = ModelUtils.nullToEmpty(object.getString("generator"));
        version = object.getIntValue("version");
        uvs = object.getIntValue("uvs");
        normals = object.getIntValue("normals");
        faces = object.getIntValue("faces");
        vertices = object.getIntValue("vertices");
    }

    public VansGeometryDataMetaData() {

    }
}

