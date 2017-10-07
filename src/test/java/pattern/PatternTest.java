package pattern;

import com.google.common.collect.Lists;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import org.junit.Test;

import java.util.List;

/**
 * Created By maxiaoyao Date: 2017/10/3 Time: 下午6:22
 */
public class PatternTest {
    @Test
    public void testPattern(){
        Result<List> listResult = new Result<List>(Lists.newArrayList());
        Result<Boolean> booleanResult = new Result<Boolean>(null);
//        String listJson1 = JSON.toJSONString(
//                listResult
//        );
//        String nullJson1 = JSON.toJSONString(
//                booleanResult
//                , SerializerFeature.WriteNullListAsEmpty
//        );
//        System.out.println(listJson1);
//        System.out.println(nullJson1);
        String listJson = JSON.toJSONString(
                listResult
                , SerializerFeature.PrettyFormat
        );
        String nullJson = JSON.toJSONString(
                booleanResult
                , SerializerFeature.PrettyFormat
                , SerializerFeature.WriteNullListAsEmpty
        );
        System.out.println(listJson);
        System.out.println(nullJson);//期望返回{data:null}由于缓存了上次的数组类型,最终返回了{data:[]}
    }
    @Test
    public void ordinal() {
        System.out.println(Integer.toBinaryString(1 << 0));
        System.out.println(Integer.toBinaryString(1 << 1));
        System.out.println(Integer.toBinaryString(1 << 2));
        System.out.println(Integer.toBinaryString(1 << 3));
        System.out.println(Integer.toBinaryString(3089));
        System.out.println(Integer.toBinaryString(8192));
        System.out.println(Integer.toBinaryString(8192|3089));
        System.out.println(Integer.toBinaryString(11345));
        System.out.println(Integer.toBinaryString(64));
        System.out.println(11345&64);
    }
}
