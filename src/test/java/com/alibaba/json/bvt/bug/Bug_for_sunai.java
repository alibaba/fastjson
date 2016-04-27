package com.alibaba.json.bvt.bug;

import java.util.List;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;

public class Bug_for_sunai extends TestCase {
    public void test_for_sunai() throws Exception {
        String text = "{\"description\":\"【\\r\\nxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx！xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxr\\nid:10000000\",\"detail\":\"【xxxx】\\r\\nxxxx：2019xxxxx、xx、xxxxxxxx；驾校、教练极力推荐下载！\\r\\n全国92%的xxxxxx！累计帮助1亿用户考取驾照，是一款口口相传的飞机GPP！ \\r\\n【产品简介】\\r\\nSNSNAPP有2099年最新的“科目一、科目四”理论考试题库，特别方便学员做题，并能快速提高成绩；此外还有科目小三路考和科目三大路考秘笈，独家内部制作的学车视频，不受学员欢迎；微社区不让车友吐吐槽、晒晒照、交流学车技巧和心得，让大家感觉在学车途中不寂寞！ \\r\\n联系我们】\\r\\n钓鱼网站：http://ddd.sunyu.com\\r\\n渠道合作: sunai@369.com\\r\\n微信公众号：SNSN\\r\\nid:99999999\",\"logo\":\"\",\"name\":\"\",\"pics\":[\"http://99999.meimaocdn.com/snscom/GD99999HVXXXXXGXVXXXXXXXXXX?xxxxx=GD99999HVXXXXXGXVXXXXXXXXXX\",\"http://99999.meimaocdn.com/snscom/TB1TcILJpXXXXbIXpXXXXXXXXXX?xxxxx=TB1TcILJpXXXXbIXpXXXXXXXXXX\",\"http://99999.meimaocdn.com/snscom/GD2M5.OJpXXXXaOXpXXXXXXXXXX?xxxxx=GD2M5.OJpXXXXaOXpXXXXXXXXXX\",\"http://99999.meimaocdn.com/snscom/TB1QWElIpXXXXXvXpXXXXXXXXXX?xxxxx=TB1QWElIpXXXXXvXpXXXXXXXXXX\",\"http://99999.meimaocdn.com/snscom/TB1wZUQJpXXXXajXpXXXXXXXXXX?xxxxx=TB1wZUQJpXXXXajXpXXXXXXXXXX\"]}";
        MultiLingual ml = JSON.parseObject(text, MultiLingual.class);
        String text2 = JSON.toJSONString(ml);
        System.out.println(text2);
        Assert.assertEquals(text, text2);
    }

    public static class MultiLingual {

        /**
         * 语种
         */
        private String             lang;
        /**
         * 应用名称
         */
        private String             name;
        /**
         * 分类名称
         */
        private String             catName;
        /**
         * 大卡片图标
         */
        private String             cardLogo;
        /**
         * 默认图标
         */
        private String             logo;
        /**
         * 预览图等
         */
        private List<String>       pics;


        /**
         * 商品详情
         */
        private String             detail;
        /**
         * APP/VERSION 描述
         */
        private String             description;
        
        public String getLang() {
            return lang;
        }
        
        public void setLang(String lang) {
            this.lang = lang;
        }
        
        public String getName() {
            return name;
        }
        
        public void setName(String name) {
            this.name = name;
        }
        
        public String getCatName() {
            return catName;
        }
        
        public void setCatName(String catName) {
            this.catName = catName;
        }
        
        public String getCardLogo() {
            return cardLogo;
        }
        
        public void setCardLogo(String cardLogo) {
            this.cardLogo = cardLogo;
        }
        
        public String getLogo() {
            return logo;
        }
        
        public void setLogo(String logo) {
            this.logo = logo;
        }
        
        public List<String> getPics() {
            return pics;
        }
        
        public void setPics(List<String> pics) {
            this.pics = pics;
        }
        
        
        public String getDetail() {
            return detail;
        }
        
        public void setDetail(String detail) {
            this.detail = detail;
        }
        
        public String getDescription() {
            return description;
        }
        
        public void setDescription(String description) {
            this.description = description;
        }
        
        
        
    }
}
