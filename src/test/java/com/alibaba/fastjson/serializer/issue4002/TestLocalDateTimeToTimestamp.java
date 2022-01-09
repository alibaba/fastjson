package com.alibaba.fastjson.serializer.issue4002;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

/**
 *
 * @author freeman
 * @see <a href="https://github.com/alibaba/fastjson/issues/4002">issue_4002</a>
 */
public class TestLocalDateTimeToTimestamp {

    @Getter
    @Setter
    @Accessors(chain = true)
    public static class DateTimeGroup {
        private LocalDateTime jdk8DateTime;
        private org.joda.time.LocalDateTime jodaDateTime;
    }

    @Test
    public void testIssue4002() {
        DateTimeGroup raw = new DateTimeGroup()
                .setJdk8DateTime(LocalDateTime.now())
                .setJodaDateTime(org.joda.time.LocalDateTime.now());
        String dateFormat = JSON.toJSONString(raw);
        String timestampFormat = JSON.toJSONString(raw, SerializerFeature.WriteDateUseTimestamp);
        DateTimeGroup parsedObj = JSON.parseObject(timestampFormat, DateTimeGroup.class);

        assertFalse(Pattern.matches(".*\\d{13,}.*", dateFormat));
        assertTrue(Pattern.matches(".*\\d+-\\d+-\\d+T\\d+:\\d+:\\d+.*", dateFormat));
        assertTrue(Pattern.matches(".*\\d{13,}.*", timestampFormat));
        assertFalse(Pattern.matches(".*\\d+-\\d+-\\d+T\\d+:\\d+:\\d+.*", timestampFormat));
        assertEquals(raw.getJodaDateTime(), parsedObj.getJodaDateTime());
        assertEquals(raw.getJdk8DateTime(), parsedObj.getJdk8DateTime());
    }
}
