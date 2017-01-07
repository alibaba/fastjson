package com.vans.test.vansdemo.vo;

import com.alibaba.fastjson.JSONObject;
import com.vans.test.vansdemo.util.ModelUtils;

import java.io.Serializable;

/**
 * Created by xiaolin_kxl on 17/1/5.
 */
public class VansMetaData implements Serializable{
    public String version;
    public String type;
    public String generator;
    public VansMetaData(JSONObject object){
        if(object == null){
            return;
        }
        version = ModelUtils.nullToEmpty(object.getString("version"));
        type = ModelUtils.nullToEmpty(object.getString("type"));
        generator = ModelUtils.nullToEmpty(object.getString("generator"));
    }
    public VansMetaData(){

    }
}
