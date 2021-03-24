package com.alibaba.fastjson.deserializer.javabean;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author ylyue
 * @since 2021/3/23
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestDO implements Serializable {

    private static final long serialVersionUID = 3987648902475498726L;

    private int inta;
    private Integer intb;
    private long longa;
    private Long longb;
    private boolean booleana;
    private Boolean booleanb;
    private String str;
    private String[] arrayStr;
    private long[] arrayLong;
    private List<String> list;
    private TestEnum testEnum;
    private Date date;
    private DateTime dateTime;
//    private LocalDate localDate;
//    private LocalTime localTime;
//    private LocalDateTime localDateTime;
    private Map<String, Object> map;
    private JSONObject jsonObject;
    private JSONArray jsonArray;
    private List<JSONObject> jsonObjectList;

}
