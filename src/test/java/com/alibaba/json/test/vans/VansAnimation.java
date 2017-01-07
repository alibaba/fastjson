package com.vans.test.vansdemo.vo;

import com.alibaba.fastjson.JSONObject;
import com.vans.test.vansdemo.util.ModelUtils;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by xiaolin_kxl on 17/1/5.
 */
public class VansAnimation implements Serializable{
    public String fps;
    public String name;
    public ArrayList<String> tracks;
    public VansAnimation(JSONObject object){
        if(object == null){
            return;
        }
        fps = ModelUtils.nullToEmpty(object.getString("fps"));
        name = ModelUtils.nullToEmpty(object.getString("name"));
        fps = ModelUtils.nullToEmpty(object.getString("fps"));
        tracks = ModelUtils.convertJSONArray(object.getJSONArray("tracks"));
    }
    public VansAnimation(){

    }
}