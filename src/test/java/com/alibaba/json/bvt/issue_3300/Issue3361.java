package com.alibaba.json.bvt.issue_3300;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import junit.framework.TestCase;

import java.util.Date;

@Slf4j
public class Issue3361 extends TestCase {
    public void test_for_issue() throws Exception {
        Model model = new Model();
        model.setOldDate(new Date());
        log.info("{}", model);

        FastJsonConfig config = new FastJsonConfig();
        config.setSerializerFeatures(SerializerFeature.WriteMapNullValue);
        config.setWriteContentLength(false);
        JSON.DEFFAULT_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS";
        config.setDateFormat(JSON.DEFFAULT_DATE_FORMAT);
        String string = JSON.toJSONString(model,
                config.getSerializeConfig(),
                config.getSerializeFilters(),
                config.getDateFormat(),
                JSON.DEFAULT_GENERATE_FEATURE,
                config.getSerializerFeatures());
        log.info("{}", string);

        Model model2 = JSON.parseObject(string, Model.class);
        log.info("{}", model2);

        Model model3 = JSON.parseObject(string, new TypeReference<Model>() {
        }.getType());
        log.info("{}", model3);
    }


    @Getter
    @Setter
    @ToString
    public static class Model {

        private Date oldDate;
    }
}
