package com.alibaba.fastjson.serializer.issue3489;

import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.util.IOUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author wangzn
 * @since 2020/10/13 15:25
 */
public class TestIssues3489 {

    @Test
    public void testIssues3489(){
        IOUtils.DEFAULT_PROPERTIES.setProperty("fastjson.asmEnable","false");
        new SerializeConfig();
        Assert.assertEquals(false, SerializeConfig.getGlobalInstance().isAsmEnable());
        IOUtils.DEFAULT_PROPERTIES.remove("fastjson.asmEnable");
    }
}
