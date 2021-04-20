package com.alibaba.json.bvt.issue_3000

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.annotation.JSONField
import com.alibaba.fastjson.parser.ParserConfig
import lombok.Data
import org.junit.AfterClass
import org.junit.Assert
import org.junit.BeforeClass
import org.junit.Test

class Issue3083TestBean {
    var is_subscribe = 0
    var subscribe = 0
    var isHave = 0
    var _have = 0
    var normal = 0
    var Abnormal = 0
}
class Issue3083TestBean2 {
    var isOk: Int? = 0
}

class Issue3083 {
    private val s = "{'is_subscribe':1,'subscribe':2,'isHave':3, '_have':4, 'normal':5, 'Abnormal':6}"
    private val s2 =
        "{\"is_subscribe\":1,\"subscribe\":2,\"isHave\":3, \"_have\":4, \"normal\":5, \"Abnormal\":6}"
    private val s3083_1 = "{'isOk':3083}"
    private val s3083_2 = "{\"isOk\":3083}"
    private val test_strs = arrayOf(s, s2)
    private val test_strs2 = arrayOf(s3083_1, s3083_2)

    //@Test
    fun test_for_issue() {
        for (test_str in test_strs) {
            val b = JSON.parseObject(test_str, Issue3083TestBean::class.java);
            println("${b.is_subscribe}--${b.subscribe}--${b.isHave}--${b._have}--${b.normal}--${b.Abnormal}")
            Assert.assertEquals(1, b.is_subscribe);
            Assert.assertEquals(2, b.subscribe);
            Assert.assertEquals(3, b.isHave);
            Assert.assertEquals(4, b._have);
            Assert.assertEquals(5, b.normal);
            Assert.assertEquals(6, b.Abnormal);
        }
    }

    //@Test
    fun test_for_issue2() {
        for (test_str in test_strs2) {
            val b = JSON.parseObject(test_str, Issue3083TestBean2::class.java);
            Assert.assertEquals(3083, b.isOk);
            //Assert.assertEquals(3083, b.getOk());
        }
    }
}