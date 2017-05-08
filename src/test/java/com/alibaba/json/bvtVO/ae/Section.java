package com.alibaba.json.bvtVO.ae;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONType;

import java.util.List;

/**
 * Created by huangliang on 17/4/11.
 */
@JSONType(typeName = "section")
public class Section implements Area{
    public String type;
    public String templateId;
    public List<Area> children;
    public JSONObject style;
    public static final String SPLIT = "::";

    public Section() {
    }


    public String getSimpleTemplateId(){
        return templateId;
    }

    public String getTemplateId() {
        StringBuilder builder = new StringBuilder();
        builder.append(templateId);
        builder.append(SPLIT);
        if(children != null){
            for(Area child : children){
                builder.append(child.getTemplateId());
            }
        }
        return builder.toString();
    }

    public String getType() {
        return type;
    }

    public int describeContents() {
        return 0;
    }

}
