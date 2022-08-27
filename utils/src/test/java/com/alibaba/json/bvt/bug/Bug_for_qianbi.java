package com.alibaba.json.bvt.bug;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

public class Bug_for_qianbi extends TestCase {
    public void test_for_bug() throws Exception {
        String json = "\n" +
                "[{\"a\":\"1A18810QBYZN5T3M3CH6K3\",\"r\":[\"44304\",\"103467\"]},{\"a\":\"1A188104CTUW5TXFGCJPDW\",\"r\":[\"24391\",\"56132\",\"44304\",\"15567\"]},{\"a\":\"1A18812GJ9P37TOGKPT8BQ\",\"r\":[\"24539\",\"44304\",\"56259\"]} ,{\"a\":\"1A188104CTUW5TXFGCJPDW\",\"r\":[\"24391\",\"44304\",\"15567\"]}]";

        JSON.parse(json);
    }
}
