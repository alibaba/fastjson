package com.alibaba.json.bvt.issue_1200;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import junit.framework.TestCase;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by kimmking on 22/06/2017.
 */
public class Issue1285 extends TestCase {

    protected void setUp() throws Exception {
        JSON.defaultTimeZone = TimeZone.getTimeZone("Asia/Shanghai");
        JSON.defaultLocale = Locale.CHINA;
    }

    public void test_for_issue() throws Exception {

        Date date = new Date();
        SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
        df1.setTimeZone(JSON.defaultTimeZone);
        SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        df2.setTimeZone(JSON.defaultTimeZone);
        String json1 = "{\"createDate\":\""+df1.format(date)+"\"}";
        String json2 = "{\"createDate\":\""+df2.format(date)+"\"}";

        AccountExt accountExt = new AccountExt();
        accountExt.setCreateDate(date);

        String json = null;

        json = JSON.toJSONStringWithDateFormat(accountExt,"yyyy-MM-dd");
        assertEquals(json1, json);

        json = JSON.toJSONString(accountExt, SerializeConfig.globalInstance, null, "yyyy-MM-dd", JSON.DEFAULT_GENERATE_FEATURE);
        assertEquals(json1, json);

        json = JSON.toJSONString(accountExt);
        assertEquals(json2, json);

        json = JSON.toJSONString(accountExt, SerializeConfig.globalInstance,null,"yyyy-MM-dd",JSON.DEFAULT_GENERATE_FEATURE | SerializerFeature.FormatAnnotationFirst.getMask());
        assertEquals(json2, json);
    }

    public static class AccountExt {

        @JSONField (format="yyyy-MM-dd HH:mm:ss")
        private Date createDate;

        public Date getCreateDate() {
            return createDate;
        }

        public void setCreateDate(Date createDate) {
            this.createDate = createDate;
        }
    }


}
