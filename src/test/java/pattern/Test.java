package pattern;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.ArrayList;
import java.util.List;

/**
 * Created By maxiaoyao Date: 2017/9/26 Time: 下午1:56
 */
public class Test {
    public static void main(String[] args) {
        Result<List> listResult = new Result<List>(new ArrayList());
        Result<Boolean> booleanResult = new Result<Boolean>(null);
        Result<Integer> integerResult = new Result<Integer>(null);
        System.out.println(JSON.toJSONString(
                listResult,
                SerializerFeature.PrettyFormat,
                SerializerFeature.WriteNullListAsEmpty
        ));
        System.out.println(JSON.toJSONString(
                booleanResult,
                SerializerFeature.PrettyFormat,
                SerializerFeature.WriteNullListAsEmpty
        ));
        System.out.println(JSON.toJSONString(
                integerResult,
                SerializerFeature.PrettyFormat,
                SerializerFeature.WriteNullListAsEmpty
        ));
    }
}
