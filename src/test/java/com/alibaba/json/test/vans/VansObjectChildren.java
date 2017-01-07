package com.vans.test.vansdemo.vo;

import com.alibaba.fastjson.JSONObject;
import com.vans.test.vansdemo.util.ModelUtils;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by xiaolin_kxl on 17/1/5.
 */
public class VansObjectChildren implements Serializable{
    public String name;
    public String uuid;
    public String type;
    public boolean visible;
    public String geometry;
    public boolean castShadow;
    public boolean receiveShadow;
    //        public ArrayList<Float> matrix;
    public ArrayList<VansObjectChildren> children;
    public VansObjectChildren(JSONObject object){
        if(object == null){
            return;
        }
        uuid = ModelUtils.nullToEmpty(object.getString("uuid"));
        type = ModelUtils.nullToEmpty(object.getString("type"));
        name = ModelUtils.nullToEmpty(object.getString("name"));
        geometry = ModelUtils.nullToEmpty(object.getString("geometry"));
        visible = object.getBooleanValue("visible");
        castShadow = object.getBooleanValue("castShadow");
        receiveShadow = object.getBooleanValue("receiveShadow");
        children = ModelUtils.convertJSONArray(object.getJSONArray("children"), new ModelUtils.EntryConverter<VansObjectChildren>() {
            @Override
            public VansObjectChildren convert(Object obj) {
                return new VansObjectChildren((JSONObject) obj);
            }
        });
//            matrix = ModelUtils.convertJSONArray(object.getJSONArray("matrix"));
    }
    public VansObjectChildren(){

    }
}
