package com.alibaba.fastjson.deserializer.issue3582

import com.alibaba.fastjson.JSON
import org.junit.Assert.assertEquals
import org.junit.Test

class TestIssue3582 {

    @Test
    fun test() {
        val obj = Data1(
            f1 = listOf(Data(1)),
            f2 = mapOf(
                1 to listOf(
                    Data(2),
                    Data(22)
                ),
                2 to listOf(
                    Data(3),
                    Data(33)
                )
            )
        )

        val str = JSON.toJSONString(obj)
        val parsed = JSON.parseObject(str, Data1::class.java)
        assertEquals(obj, parsed)
    }
}

data class Data(
    val id: Int
)

data class Data1(
    val f1: List<Data> = listOf(),
    val f2: Map<Int, List<Data>> = mapOf()
)