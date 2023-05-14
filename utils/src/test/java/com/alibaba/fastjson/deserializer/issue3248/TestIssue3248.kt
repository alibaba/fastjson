package com.alibaba.fastjson.deserializer.issue3248

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.annotation.JSONField
import org.junit.Assert
import org.junit.Test

/**
 * https://github.com/alibaba/fastjson/issues/3248
 * @author 佐井
 * @since 2020-06-10 09:19
 */
class TestIssue3248 {

    @JSONField(name = "namex")
    var name = ""

    var isTest: Boolean = false

    @Test
    fun test() {

        val test = TestIssue3248().also {
            it.name = "my name"
            it.isTest = true
        }
        val raw = JSON.toJSONString(test)
        val parsed = JSON.parseObject(raw, TestIssue3248::class.java)
        Assert.assertEquals(test.name, parsed.name)
        Assert.assertEquals(test.isTest, parsed.isTest)

    }
}