package com.alibaba.json.bvt.serializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.Lists;
import com.youku.haibao.client.common.ItemPageResult;
import com.youku.haibao.client.common.ModulePageResult;
import com.youku.haibao.client.dto.*;
import com.youku.haibao.client.dto.item.ItemDTO;
import junit.framework.TestCase;
import org.junit.Assert;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class NoneStringKeyTest_2 extends TestCase {

    public void test_0() throws Exception {
        SubjectDTO subjectDTO = new SubjectDTO();
        com.youku.haibao.client.common.ItemPageResult<AbstractItemDTO> itemPageResult = new ItemPageResult<AbstractItemDTO>();
        Map<Integer, AbstractItemDTO> map = new HashMap<Integer, AbstractItemDTO>();
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setItemId(123213L);
        map.put(1, itemDTO);
        itemPageResult.setItem(map);

        subjectDTO.setModuleResult(new ModulePageResult<ModuleDTO>());
        subjectDTO.getModuleResult().setModules(Lists.newArrayList(new ModuleDTO()));
        ComponentDTO componentDTO = new ComponentDTO();
        componentDTO.setItemResult(itemPageResult);
        subjectDTO.getModuleResult().getModules().get(0).setComponents(Lists.newArrayList(componentDTO));

        final String json = JSON.toJSONString(subjectDTO, SerializerFeature.WriteNonStringKeyAsString, SerializerFeature.WriteClassName);
        System.out.println(json);
        SubjectDTO target = JSON.parseObject
                (json, SubjectDTO.class, Feature.NonStringKeyAsString);
        System.out.println(target.toString());
    }

}
