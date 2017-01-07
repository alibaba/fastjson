package com.vans.test.vansdemo.vo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.vans.test.vansdemo.util.ModelUtils;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by xiaolin_kxl on 16/12/28.
 */

public class VansGeometryData implements Serializable{
    public ArrayList<ArrayList<Float>> uvs;
    public VansGeometryDataMetaData metaData;
    public ArrayList<Float> normals;
    public String name;
    public ArrayList<Integer> faces;
    public ArrayList<Float> vertices;
    public VansGeometryData(JSONObject object){
        if(object == null){
            return;
        }
        metaData = new VansGeometryDataMetaData(object.getJSONObject("metadata"));
        name = ModelUtils.nullToEmpty(object.getString("name"));
        normals = ModelUtils.convertFloatJSONArray(object.getJSONArray("normals"));
        faces = ModelUtils.convertIntegerJSONArray(object.getJSONArray("faces"));
        vertices = ModelUtils.convertFloatJSONArray(object.getJSONArray("vertices"));
        JSONArray array = object.getJSONArray("uvs");
        if(array == null && array.size() == 0){
            return;
        }
        uvs = new ArrayList<ArrayList<Float>>();
        int length = array.size();
        for(int i = 0; i < length; i++){
            JSONArray array1 = array.getJSONArray(i);
            if(array1 == null && array1.size() == 0){
                continue;
            }
            uvs.add(ModelUtils.convertFloatJSONArray(array1));
        }
    }
    public VansGeometryData(){

    }
}
