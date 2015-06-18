package com.alibaba.json.test.benchmark;

import com.alibaba.json.test.benchmark.decode.EishayDecode;
import com.alibaba.json.test.codec.FastjsonCodec;
import com.alibaba.json.test.codec.FastjsonGenCodec;

public class BenchmarkMain {
	public static void main(String[] args) throws Exception {
	     // 注意，byte[]在jackson中是使用base64编码的，不正确的。

        BenchmarkExecutor executor = new BenchmarkExecutor();
        executor.setExecuteCount(5);
        executor.getCodecList().add(new FastjsonCodec());
//        executor.getCodecList().add(new FastjsonGenCodec());
//        executor.getCodecList().add(new FastjsonBeanToArrayCodec());
//        executor.getCodecList().add(new JacksonCodec());
//        executor.getCodecList().add(new Jackson2Codec());
        //
        // executor.getCodecList().add(new SimpleJsonCodec());
        // executor.getCodecList().add(new JsonLibCodec());
        // executor.getCodecList().add(new JsonSmartCodec());

        executor.setLoopCount(1000 * 1000 * 1);

//        executor.getCaseList().add(new EishayDecodeBytes());
//        executor.getCaseList().add(new EishayDecode2Bytes());
         executor.getCaseList().add(new EishayDecode());
//         executor.getCaseList().add(new EishayDecodeByClassName());
//         executor.getCaseList().add(new EishayTreeDecode());
//         executor.getCaseList().add(new EishayEncode());
//         executor.getCaseList().add(new EishayEncodeManual());
        // executor.getCaseList().add(new IntArray1000Decode());
        // executor.getCaseList().add(new StringArray1000Decode());
        // executor.getCaseList().add(new Map1000StringDecode());
        // executor.getCaseList().add(new Entity100StringDecode());

        // executor.getCaseList().add(new ListBoolean1000Encode());
        // executor.getCaseList().add(new ArrayBoolean1000Encode());
        // executor.getCaseList().add(new IntArray1000Decode());
        // executor.getCaseList().add(new StringArray1000Decode());
        // executor.getCaseList().add(new GroupEncode());
        // executor.getCaseList().add(new CategoryEncode());
        // executor.getCaseList().add(new GroupEncode());
        // executor.getCaseList().add(new Map1Decode());
        // executor.getCaseList().add(new Entity100IntDecode());
        // executor.getCaseList().add(new Entity100StringDecode());
        // executor.getCaseList().add(new Entity100IntEncode());
        // executor.getCaseList().add(new ArrayByte1000Encode());
        // executor.getCaseList().add(new ArrayInt1000Encode());
        // executor.getCaseList().add(new ArrayLong1000Encode());
        // executor.getCaseList().add(new ArrayString1000Encode());
        // executor.getCaseList().add(new ArrayEmptyList1000Encode());
        // executor.getCaseList().add(new ArrayEmptyMap1000Encode());
        // executor.getCaseList().add(new ArrayObjectEmptyMap1000Encode());
        // executor.getCaseList().add(new Map1000Encode());

        executor.execute();
	}
}
