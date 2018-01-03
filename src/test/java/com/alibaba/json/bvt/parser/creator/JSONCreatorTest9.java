package com.alibaba.json.bvt.parser.creator;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONCreator;
import com.alibaba.fastjson.annotation.JSONField;
import junit.framework.TestCase;

public class JSONCreatorTest9 extends TestCase {
    public void test_for_yk() throws Exception {
        String text = "{\"videoid\":\"XNzBxOCU0NjYxCg==\",\"videoName\":\"xxx\"}";

        YoukuVideoDTO dto = JSON.parseObject(text, YoukuVideoDTO.class);
        assertEquals("XNzBxOCU0NjYxCg==", dto.videoId);
        assertEquals("xxx", dto.videoName);

    }

    public static class YoukuVideoDTO {
        private String videoId;

        private String videoName;

        @JSONCreator
        public YoukuVideoDTO(@JSONField(name = "videoid") String videoId) {
            this.videoId = videoId;
        }

        public String getVideoId() {
            return videoId;
        }

        public String getVideoName() {
            return videoName;
        }

        public void setVideoName(String videoName) {
            this.videoName = videoName;
        }
    }
}
