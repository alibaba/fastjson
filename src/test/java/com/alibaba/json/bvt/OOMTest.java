package com.alibaba.json.bvt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.SymbolTable;

import org.junit.Assert;
import junit.framework.TestCase;

public class OOMTest extends TestCase {
    public void test_oom() throws Exception {
        
        for (int i = 0; i < 1000 * 1000; ++i) {
            String text = "{\"" + i + "\":0}";
            JSON.parse(text);
        }
        
        Assert.assertEquals(SymbolTable.MAX_SIZE, ParserConfig.getGlobalInstance().getSymbolTable().size());
    }
}
