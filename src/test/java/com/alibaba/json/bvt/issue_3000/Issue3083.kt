package com.alibaba.json.bvt.issue_3000

import com.alibaba.fastjson.JSON

class TestBean {
    var is_subscribe = 0
    var subscribe = 0
    var isHave = 0
    var _have = 0
    var normal = 0
    var Abnormal = 0
}

fun main(args: Array<String>) {
    val s = "{'is_subscribe':1,'subscribe':2,'isHave':3, '_have':4, 'normal':5, 'Abnormal':6}"
    val b = JSON.parseObject(s, TestBean::class.java)
    println("${b.is_subscribe}--${b.subscribe}--${b.isHave}--${b._have}--${b.normal}--${b.Abnormal}")
}
