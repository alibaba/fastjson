package com.alibaba.json.bvt.bug;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;

public class Bug_for_80108116 extends TestCase {

    public void test_for_dateFormat() throws Exception {
        VO vo = new VO();
        vo.setDate(new SimpleDateFormat("yyyy-MM-dd").parse("2012-07-12"));

        List<VO> voList = new ArrayList<VO>();
        voList.add(vo);

        String text = JSON.toJSONString(voList);
        Assert.assertEquals("[{\"date\":\"2012-07-12\"}]", text);
    }

    public static class VO {

        private Date date;

        @JSONField(format = "yyyy-MM-dd")
        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }
    }

}
