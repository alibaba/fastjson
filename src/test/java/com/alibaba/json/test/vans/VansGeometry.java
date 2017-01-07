package com.vans.test.vansdemo.vo;

import com.alibaba.fastjson.JSONObject;
import com.vans.test.vansdemo.util.ModelUtils;

import java.io.Serializable;

/**
 * Created by xiaolin_kxl on 17/1/5.
 */
public class VansGeometry implements Serializable{
    public String uuid;
    public String type;
    public VansGeometryData data;

    public VansGeometry(JSONObject object){
        if(object == null){
            return;
        }
        uuid = ModelUtils.nullToEmpty(object.getString("uuid"));
        type = ModelUtils.nullToEmpty(object.getString("type"));
        data = new VansGeometryData(object.getJSONObject("data"));
    }
    public VansGeometry(){

    }
}