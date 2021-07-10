package com.alibaba.json.bvt.issue_3700

import com.alibaba.fastjson.JSON
import org.junit.Test
import kotlin.test.assertNotNull

class Issue3721 {
    @Test
    fun kotlinFunSerializeErrorTest() {
        val temp = fun() {};
        val tempStr = JSON.toJSONString(temp)
        assertNotNull(tempStr)
        val toJSONString = JSON.toJSONString(ObjTest(1))
        assertNotNull(toJSONString)
        val parseObject = JSON.parseObject(toJSONString, ObjTest::class.java)
        assertNotNull(parseObject)
        println(parseObject)
    }
    @Test
    fun kotlinFunOneParamWithFunInsideSerializeErrorTest(){
        // CS304 (manually written) Issue link: https://github.com/alibaba/fastjson/issues/3721
        val temp = fun(x:Double){
            val tempInside = fun (){}
        }
        val tempStr = JSON.toJSONString(temp)
        assertNotNull(tempStr)
        val toJSONString = JSON.toJSONString(ObjTest(1))
        assertNotNull(toJSONString)
        val parseObject = JSON.parseObject(toJSONString, ObjTest::class.java)
        assertNotNull(parseObject)
        println(parseObject)
    }
}

class ObjTest(val ids: Long) {
    // with kotlin 5009
    // without kotlin 5004
}