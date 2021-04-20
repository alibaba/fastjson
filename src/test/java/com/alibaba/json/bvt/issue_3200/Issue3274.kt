package com.alibaba.json.bvt.issue_3200

import com.alibaba.fastjson.JSON
import junit.framework.Assert.assertEquals
import org.junit.Test
import java.util.*

class TestFJ {

    @Test
    fun test() {
        val str = """
            {"data": {"id": "1", "name":"n1"}}
        """.trimIndent()


        val d1 = JSON.parseObject(str, Data2::class.java)

        val data = JSON.parseObject(str)
        val d2 = data.getObject("data", Data::class.java)

        assertEquals(1, d1.data.id)
        assertEquals(1, d2.id)
    }
}

data class Data(
        val id: Int = 0,
        val name: String = "",
        val date: Date? = null
)
data class Data2(
        val data: Data
)