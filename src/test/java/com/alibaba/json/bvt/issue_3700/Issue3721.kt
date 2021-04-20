package com.alibaba.json.bvt.issue_3700

import com.alibaba.fastjson.JSON
import org.junit.Test

class Issue3721 {
    //@Test
    fun kotlinFunSerializeErrorTest() {
        //val temp = fun() {};
        val temp = ObjTest(2);
        try {
            /**
             *当执行该序列化时，会抛出异常，该异常会设置TypeUtils类中的静态变量kotlin_error变更为true,导致系统中所有的对象的反序列化出现
             * com.alibaba.fastjson.JSONException: default constructor not found.异常
             */
            JSON.toJSONString(temp)
        } catch (ex: Exception) {
            println(ex.message)
        }


        val toJSONString = JSON.toJSONString(ObjTest(1))
        val parseObject = JSON.parseObject(toJSONString, ObjTest::class.java)

        println(parseObject)

    }

}
class ObjTest(val ids: Long) {
    // with kotlin 5009
    // without kotlin 5004
}