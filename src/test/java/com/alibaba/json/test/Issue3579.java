package com.alibaba.json.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

/**
 * https://github.com/alibaba/fastjson/issues/3579
 *
 * @author 小谢
 * @since 2021年01月14日
 * @Desc 加上SerializerFeature.WriteClassName来写入类名称，但是加上SerializerFeature.WriteClassName就会引起Bug
 */
public class Issue3579 {
    @Test
    public void test3579() {
        Assert.assertEquals("1", JSON.toJSONString(new BigDecimal("1"), SerializerFeature.WriteClassName));
    }

}
