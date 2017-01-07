package com.vans.test.vansdemo.vo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.vans.test.vansdemo.util.ModelUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaolin_kxl on 16/12/28.
 */
public class VansData implements Serializable{

    public ArrayList<Integer> textures;
    public ArrayList<String> images;
    public VansObject object;
    public VansMetaData metadata;
    public ArrayList<VansGeometry> geometries;
    public ArrayList<VansAnimation> animations;
    public Object materials;

    public VansData(JSONObject jsonObject){
        if(jsonObject == null){
            return;
        }
        textures = ModelUtils.convertJSONArray(jsonObject.getJSONArray("textures"));
        images = ModelUtils.convertJSONArray(jsonObject.getJSONArray("images"));
        animations = ModelUtils.convertJSONArray(jsonObject.getJSONArray("animations"), new ModelUtils.EntryConverter<VansAnimation>() {
            @Override
            public VansAnimation convert(Object obj) {
                return new VansAnimation((JSONObject) obj);
            }
        });
        object = new VansObject(jsonObject.getJSONObject("object"));
        metadata = new VansMetaData(jsonObject.getJSONObject("metadata"));
        geometries = ModelUtils.convertJSONArray(jsonObject.getJSONArray("geometries"), new ModelUtils.EntryConverter<VansGeometry>() {
            @Override
            public VansGeometry convert(Object obj) {
                return new VansGeometry((JSONObject) obj);
            }
        });
    }
    public VansData(){

    }

}