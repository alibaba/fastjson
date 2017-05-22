/*
 * Copyright 2011 Alibaba.com All right reserved. This software is the
 * confidential and proprietary information of Alibaba.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Alibaba.com.
 */
package com.alibaba.json.bvt.bug;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.json.bvtVO.OptionKey;
import com.alibaba.json.bvtVO.OptionValue;
import com.alibaba.json.bvtVO.TempAttachMetaOption;

/**
 * 类SerDeserTest.java的实现描述：TODO 类实现描述
 * 
 * @author lei.yaol 2011-12-27 下午03:44:18
 */
public class SerDeserTest extends TestCase {
    protected void setUp() throws Exception {
        com.alibaba.fastjson.parser.ParserConfig.global.addAccept("com.alibaba.json.bvtVO.");
    }

    /** 用于被FastJson序列和反序列化的对象 */
    private static Map<OptionKey, OptionValue<?>> options;

    static {
        options = new HashMap<OptionKey, OptionValue<?>>();

        TempAttachMetaOption attach = new TempAttachMetaOption();
        attach.setId(1000);
        attach.setName("test_name");
        attach.setPath("http://alibaba-inc.com/test.txt");

        ArrayList<TempAttachMetaOption> attachList = new ArrayList<TempAttachMetaOption>();
        attachList.add(attach);

        // 设置value
        OptionValue<ArrayList<TempAttachMetaOption>> optionValue = new OptionValue<ArrayList<TempAttachMetaOption>>();
        optionValue.setValue(attachList);

        options.put(OptionKey.TEMPALTE_ATTACH_META, optionValue);
    }

    public void test_for_yaolei() {
        // 序列化toJSONString()
        String jsonString = JSON.toJSONString(options);
        System.out.println(jsonString);
        {
            // 反序列化parse()
            HashMap<OptionKey, OptionValue<?>> deserOptions = (HashMap<OptionKey, OptionValue<?>>) JSON.parseObject(jsonString,
                                                                                                                    new TypeReference<HashMap<OptionKey, OptionValue<?>>>() {

                                                                                                                    });
            System.out.println(deserOptions.get(OptionKey.TEMPALTE_ATTACH_META));
        }

        // 序列化toJSONString(,)
        jsonString = JSON.toJSONString(options, SerializerFeature.WriteClassName);
        System.out.println(jsonString);
        // 反序列化parse()
        HashMap<OptionKey, OptionValue<?>> deserOptions = (HashMap<OptionKey, OptionValue<?>>) JSON.parse(jsonString);
        System.out.println(deserOptions.get(OptionKey.TEMPALTE_ATTACH_META));
    }
}
