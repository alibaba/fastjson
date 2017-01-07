package com.vans.test.vansdemo.vo;

import com.alibaba.fastjson.JSONObject;
import com.vans.test.vansdemo.util.ModelUtils;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by xiaolin_kxl on 17/1/5.
 */
public class VansObject implements Serializable {
    public String uuid;
    public String type;
    //        public ArrayList<Float> matrix;
    public ArrayList<VansObjectChildren> children;
    public VansObject(JSONObject object){
        if(object == null){
            return;
        }
        uuid = ModelUtils.nullToEmpty(object.getString("uuid"));
        type = ModelUtils.nullToEmpty(object.getString("type"));
        children = ModelUtils.convertJSONArray(object.getJSONArray("children"), new ModelUtils.EntryConverter<VansObjectChildren>() {
            @Override
            public VansObjectChildren convert(Object obj) {
                return new VansObjectChildren((JSONObject) obj);
            }
        });
//            matrix = ModelUtils.convertJSONArray(object.getJSONArray("matrix"));
    }
    public VansObject(){

    }
}
