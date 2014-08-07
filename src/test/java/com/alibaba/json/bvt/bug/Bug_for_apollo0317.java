package com.alibaba.json.bvt.bug;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class Bug_for_apollo0317 extends TestCase {

    public void test_for_apollo0317() throws Exception {
        String text = "广州市白云区优网通信线缆厂是一家集专业设计、生产、销售、电工解决方案提供为一体的中型企业，旗下主打品牌(简称:普禄克Pluke）；公厂拥有自己的研发团队，自主研发改造的高性能电缆生产流水线使产品的性价比大幅度提升，在技术上处于行业领先。\r\n　　普禄克Pluke销售服务网络覆盖全国各省市以及南美、东南亚等地区。产品广范应用在军队通信网，政府网，企业网，电信网，电力网，煤炭网，水利网，广电网，校园网 电梯设备、机电设备、汽车、电子、等行业，其中超五类六类网络线缆，彩色网络跳线，设备连接及控制传输电缆，电器连接线多年来获得客户的高度肯定。?普禄克PLUKE在不断创新中为客户创造价值，在工厂战略的指导下，凭借在售前咨询，系统设计，产品采购，工程施工等方面的综合优势和我们多面的工程服务经验，可以根据客户的要求，提供切实可行的技术方案及系统产品。\r\n 工厂未来将着力于商业模式的创新转换，为合作伙伴提供一个共同成长、双赢的、持续发展的商业平台。\r\n 我们企业的宗旨是：销售最好的产品、追求最佳的售后服务、推广最新的办公理念！";

        VO vo = new VO();
        vo.setBrandintroduction(text);
        
        Object[] array = new Object[] {vo, vo, vo};
        
        String json = JSON.toJSONString(vo, SerializerFeature.DisableCircularReferenceDetect);
        System.out.println(json);
    }

    public static class VO {

        private String brandintroduction;

        public String getBrandintroduction() {
            return brandintroduction;
        }

        public void setBrandintroduction(String brandintroduction) {
            this.brandintroduction = brandintroduction;
        }

    }
}
