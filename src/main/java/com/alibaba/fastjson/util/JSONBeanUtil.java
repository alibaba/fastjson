package com.alibaba.fastjson.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author GrainRain <yung2mao@126.com>
 *     此处实现以属性点形式获取json中某个属性值，对于一些需要转换为特定bean对象的场景，可以自定义实现接口从而实现返回需要的数据
 **/
public class JSONBeanUtil {

    private static JSONBeanUtil jsonBeanUtil;
    private JSONBeanUtil(){}
    public static synchronized JSONBeanUtil  getInstance(){
        if(jsonBeanUtil==null) {
            jsonBeanUtil = new JSONBeanUtil();
            return jsonBeanUtil;
        }
        return jsonBeanUtil;
    }

    public <T> T getAsObject(String json, Class<T> clazz){
        return JSON.parseObject(json,clazz);
    }

    //以属性点形式获取json的某个String类型属性值
    public String getField(String json,String field){
        String[] fields = field.split("\\.");
        JSONObject preResult = getPreJsonObject(json,fields);
        return preResult.getString(fields[fields.length-1]);
    }

    //以属性点的形式，按自定义的需求解析json并返回数据
    public <T> List<T> getListFields(String json,String field,CusJsonToObject<T> cusJsonToObject){
        String[] fields = field.split("\\.");
        JSONObject preResult = getPreJsonObject(json,fields);
        JSONArray resultJsonArray = preResult.getJSONArray(fields[fields.length-1]);
        List<T> list = new ArrayList<>();
        for (int i = 0; i < resultJsonArray.size(); i++) {
            T instance = cusJsonToObject.getInstance(resultJsonArray.get(i));
            list.add(instance);
        }
        return list;
    }

    private JSONObject getPreJsonObject(String json,String[] fields){
        JSONObject jsonObject = JSON.parseObject(json);
        JSONObject result = jsonObject;
        for(int i =0; i< fields.length-1;i++){
            result = result.getJSONObject(fields[i]);
        }
        return result;
    }

    public JSONObject getJsonObject(String json,String[] fields){
        JSONObject jsonObject = JSON.parseObject(json);
        JSONObject result = jsonObject;
        for(int i =0; i< fields.length;i++){
            result = result.getJSONObject(fields[i]);
        }
        return result;
    }
}
