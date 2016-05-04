package com.alibaba.json.bvt.parser.taobao;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;

public class SpecialStringTest extends TestCase {
    public void test_for_special() throws Exception {
        VO vo = new VO();
        vo.value = "{\"aurl\"";
        String text = JSON.toJSONString(vo);
        VO vo1 = JSON.parseObject(text, VO.class);
        Assert.assertEquals(vo1.value, vo.value);
    }
    
    public void test_for_special_1() throws Exception {
        VO vo = new VO();
        vo.value = "{\"aurl\":\"http://a.m.taobao.com/i529666038203.htm\",\"eurl\":\"http://click.mz.simba.taobao.com/ecpm?e=FKzStLpktUcmgME64bmjnBsQmLP5zomMI9WwdvViswDtdMUS1TLPryFiqQmsaUcblU3hrUulblXi4Nf5jVnFI3mESrWAJFi8UK7RDtIZydUyXElRAMLwo3HZWQvTKXBpyitB%2BgALy7j45JkIPnsiapEFjIWbdXJAnae9i5WIlhTnQ%2FthEaQ9IuT5J4gzB5T%2FcKP7YijzmvIZWnX1fL8Wv2yOkjnv1RfOuAwHNITyYhs0036Nbzw1rue9DcuU1VaInAsdAQs%2BcFbs41NPY6%2FbqjqRHfjhCyty&u=http%3A%2F%2Fa.m.taobao.com%2Fi529666038203.htm&k=289\",\"tbgoodslink\":\"http://i.mmcdn.cn/simba/img/TB120WTMpXXXXazXXXXSutbFXXX.jpg\",\"tmpl\":\"\"}";
        String text = JSON.toJSONString(vo);
        VO vo1 = JSON.parseObject(text, VO.class);
        Assert.assertEquals(vo1.value, vo.value);
    }
    
    public static class VO {
        public String value;
    }
}
