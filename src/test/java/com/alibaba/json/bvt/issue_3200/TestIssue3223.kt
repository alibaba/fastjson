package com.alibaba.json.bvt.issue_3200;

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.alibaba.fastjson.parser.ParserConfig
import com.alibaba.fastjson.serializer.SerializerFeature
import org.junit.Assert
import org.junit.Test
import java.util.concurrent.atomic.AtomicInteger
import kotlin.properties.Delegates

/**
 * kotlin集合测试
 * @author 佐井
 * @since 2020-06-05 20:35
 */


class TestIssue3223 {

    @Test
    fun test() {
        val cfg = ParserConfig.getGlobalInstance()
        cfg.addAccept("com.")
        cfg.addAccept("net.")
        cfg.addAccept("java.")
        cfg.addAccept("kotlin.")
        cfg.addAccept("org.")
        val n = NullableKotlin()
        //nullable
        n.nullableList = listOf("nullableList")
        n.nullableMap = mapOf("nullableMap" to "nullableMap")
        n.nullableSet = setOf("nullableSet")
        //empty
        n.emptyList = listOf("emptyList")
        n.emptyMap = mapOf("emptyMap" to "emptyMap")
        n.emptySet = setOf("emptySet")
        //delegate
        n.delegateList = listOf("delegateList")
        n.delegateMap = mapOf("delegateMap" to "delegateMap")
        n.delegateSet = setOf("delegateSet")
        //basic
        n.atomicInt = AtomicInteger(10)
        n.longValue = 1
        n.json = JSON.parseObject(JSON.toJSONString(mapOf("a" to "b")))
        val raw = JSON.toJSONString(n, SerializerFeature.WriteClassName)
        val d = JSON.parseObject(raw, NullableKotlin::class.java)
        Assert.assertTrue(n == d)

    }

}


class NullableKotlin {
    //map
    var nullableMap: Map<String, String>? = null
    var emptyMap: Map<String, String> = emptyMap()
    var delegateMap by Delegates.notNull<Map<String, String>>()

    //set
    var nullableSet: Set<String>? = null
    var emptySet: Set<String> = emptySet()
    var delegateSet by Delegates.notNull<Set<String>>()

    //list
    var nullableList: List<String>? = null
    var emptyList: List<String> = emptyList()
    var delegateList by Delegates.notNull<List<String>>()

    //basic
    var atomicInt: AtomicInteger? = null
    var longValue: Long? = null
    var json: JSONObject? = null
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NullableKotlin

        if (nullableMap != other.nullableMap) return false
        if (emptyMap != other.emptyMap) return false
        if (nullableSet != other.nullableSet) return false
        if (emptySet != other.emptySet) return false
        if (nullableList != other.nullableList) return false
        if (emptyList != other.emptyList) return false
        if (atomicInt?.get() != other.atomicInt?.get()) return false
        if (longValue != other.longValue) return false
        if (json != other.json) return false

        return true
    }

    override fun hashCode(): Int {
        var result = nullableMap?.hashCode() ?: 0
        result = 31 * result + emptyMap.hashCode()
        result = 31 * result + (nullableSet?.hashCode() ?: 0)
        result = 31 * result + emptySet.hashCode()
        result = 31 * result + (nullableList?.hashCode() ?: 0)
        result = 31 * result + emptyList.hashCode()
        result = 31 * result + (atomicInt?.hashCode() ?: 0)
        result = 31 * result + (longValue?.hashCode() ?: 0)
        result = 31 * result + (json?.hashCode() ?: 0)
        return result
    }


}