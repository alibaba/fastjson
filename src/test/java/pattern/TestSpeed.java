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
            list.add(new Result<Boolean>(null));
        }
        return list;
    }
    @Test
    public void speedTest1() {
        long all = 0;
        for (int i = 0; i < 10; i++) {
            long fastStart = System.currentTimeMillis();
            parser1();
            long fastEnd = System.currentTimeMillis();
            all += fastEnd - fastStart;
        }
        System.out.println("平均时间:"+(all / 10));
    }
    private void parser1() {
        for (Result<Boolean> booleanResult : getResultList1()) {
            JSON.toJSONString(
                    booleanResult,
                    SerializerFeature.PrettyFormat,
                    SerializerFeature.WriteNullBooleanAsFalse);
        }
    }
    private  List<Result<Boolean>> getResultList1() {
        List<Result<Boolean>> list = new ArrayList<Result<Boolean>>();
        for (int i = 0; i < 1000000; i++) {
            list.add(new Result<Boolean>(null));
        }
        return list;
    }
}
