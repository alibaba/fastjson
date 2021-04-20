package com.alibaba.json.bvt.issue_3000

import com.alibaba.fastjson.JSON
import org.junit.Assert
import org.junit.Test

class TestBean {
    var is_subscribe = 0
    var subscribe = 0
    var isHave = 0
    var _have = 0
    var normal = 0
    var Abnormal = 0
}
class Issue3083 {
    private val s = "{'is_subscribe':1,'subscribe':2,'isHave':3, '_have':4, 'normal':5, 'Abnormal':6}"
    private val s2 = "{\"is_subscribe\":1,\"subscribe\":2,\"isHave\":3, \"_have\":4, \"normal\":5, \"Abnormal\":6}"
    // TODO
    //@Test
    fun test_for_issue() {
        val b = JSON.parseObject(s2, TestBean::class.java)
        println("${b.is_subscribe}--${b.subscribe}--${b.isHave}--${b._have}--${b.normal}--${b.Abnormal}")
        Assert.assertEquals(1,b.is_subscribe);
        Assert.assertEquals(2,b.subscribe);
        Assert.assertEquals(3,b.isHave);
        Assert.assertEquals(4,b._have);
        Assert.assertEquals(5,b.normal);
        Assert.assertEquals(6,b.Abnormal);
    }
}