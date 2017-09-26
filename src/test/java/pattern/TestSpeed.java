package pattern;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created By maxiaoyao Date: 2017/9/26 Time: 下午6:22
 */
public class TestSpeed {
    @Test
    public void speedTest() {
        long all = 0;
        for (int i = 0; i < 10; i++) {
            long fastStart = System.currentTimeMillis();
            parser();
            long fastEnd = System.currentTimeMillis();
            all += fastEnd - fastStart;
        }
        System.out.println("平均时间:"+(all / 10));
    }

    private void parser() {
        for (Result<Boolean> booleanResult : getResultList()) {
            JSON.toJSONString(
                    booleanResult,
                    SerializerFeature.PrettyFormat,
                    SerializerFeature.WriteNullBooleanAsFalse);
        }
    }

    private  List<Result<Boolean>> getResultList() {
        List<Result<Boolean>> list = new ArrayList<Result<Boolean>>();
        for (int i = 0; i < 500000; i++) {
            list.add(new Result<Boolean>(true));
        }
        return list;
    }
}
