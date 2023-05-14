package com.alibaba.json.bvt.bug;

import junit.framework.TestCase;
import cn.com.tx.domain.notifyDetail.NotifyDetail;
import cn.com.tx.domain.pagination.Pagination;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

public class Bug_for_generic_1 extends TestCase {

    public void test() {
        String json2 = "{\"@type\":\"cn.com.tx.domain.pagination.Pagination\",\"fromIndex\":0,\"list\":[{\"@type\":\"cn.com.tx.domain.notifyDetail.NotifyDetail\",\"args\":[\"61354557\",\"依依\",\"六\"],\"destId\":60721687,\"detailId\":3155063,\"display\":false,\"foundTime\":{\"@type\":\"java.sql.Timestamp\",\"val\":1344530416000},\"hotId\":0,\"srcId\":1000,\"templateId\":482},{\"@type\":\"cn.com.tx.domain.notifyDetail.NotifyDetail\",\"args\":[\"14527269\",\"懒洋洋\",\"///最佳拍档,非常\",\"24472950\"],\"destId\":60721687,\"detailId\":3151609,\"display\":false,\"foundTime\":{\"@type\":\"java.sql.Timestamp\",\"val\":1344354485000},\"hotId\":0,\"srcId\":1000,\"templateId\":40},{\"@type\":\"cn.com.tx.domain.notifyDetail.NotifyDetail\",\"args\":[\"51090218\",\"天之涯\",\"天会黑，人会变。三分\"],\"destId\":60721687,\"detailId\":3149221,\"display\":false,\"foundTime\":{\"@type\":\"java.sql.Timestamp\",\"val\":1344247529000},\"hotId\":0,\"srcId\":1000,\"templateId\":459},{\"@type\":\"cn.com.tx.domain.notifyDetail.NotifyDetail\",\"args\":[\"51687981\",\"摹然回首梦已成年\",\"星星在哪里都很亮的,\"],\"destId\":60721687,\"detailId\":3149173,\"display\":false,\"foundTime\":{\"@type\":\"java.sql.Timestamp\",\"val\":1344247414000},\"hotId\":0,\"srcId\":1000,\"templateId\":459},{\"@type\":\"cn.com.tx.domain.notifyDetail.NotifyDetail\",\"args\":[\"41486427\",\"寒江蓑笠\",\"双休了\"],\"destId\":60721687,\"detailId\":3148148,\"display\":false,\"foundTime\":{\"@type\":\"java.sql.Timestamp\",\"val\":1344244730000},\"hotId\":0,\"srcId\":1000,\"templateId\":459}],\"maxLength\":5,\"nextPage\":2,\"pageIndex\":1,\"prevPage\":1,\"toIndex\":5,\"totalPage\":3,\"totalResult\":13}";
        cn.com.tx.domain.pagination.Pagination<cn.com.tx.domain.notifyDetail.NotifyDetail> pagination = JSON.parseObject(json2,
                                                                                                                         new TypeReference<Pagination<NotifyDetail>>() {
                                                                                                                         });

    }
}
