package com.alibaba.fastjson.deserializer.javabean;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author ylyue
 * @since 2021/3/23
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestDO implements Serializable {

    private static final long serialVersionUID = 3987648902475498726L;

    private Long id;
    private String str;
    private Date date;
    private boolean bool;
    private JSONObject jsonObject;
    private JSONArray jsonArray;
    private List<JSONObject> jsonObjectList;

}
