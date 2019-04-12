package com.alibaba.json.test.benchmark;

import com.alibaba.json.test.benchmark.encode.*;
import com.alibaba.json.test.benchmark.decode.*;
import com.alibaba.json.test.codec.*;

public class BenchmarkMain {
    public static void main(String[] args) throws Exception {
         // 注意，byte[]在jackson中是使用base64编码的，不正确的。

        BenchmarkExecutor executor = new BenchmarkExecutor();
        executor.setExecuteCount(10);
//        executor.getCodecList().add(new FastjsonManualCodec());
        executor.getCodecList().add(new FastjsonCodec());
//        executor.getCodecList().add(new FastjsonBeanToArrayCodec());
//        executor.getCodecList().add(new FastjsonGenCodec());
//        executor.getCodecList().add(new FastjsonBeanToArrayCodec());
//        executor.getCodecList().add(new JacksonCodec());
//        executor.getCodecList().add(new Jackson2Codec());
//        executor.getCodecList().add(new Jackson2AfterBurnCodec());
        //
        // executor.getCodecList().add(new SimpleJsonCodec());
        // executor.getCodecList().add(new JsonLibCodec());
        // executor.getCodecList().add(new JsonSmartCodec());

        executor.setLoopCount(1000 * 1000 * 1);

//        executor.getCaseList().add(new TradeObjectParse());
//        executor.getCaseList().add(new EishayDecodeBytes());
//        executor.getCaseList().add(new EishayEncodeOutputStream());
//        executor.getCaseList().add(new EishayEncodeToBytes());
         executor.getCaseList().add(new EishayDecode()); // 1069
                                                         //JDK8_162 1094
                                                         //JDK9_01  1214
                                                         //JDK9_04  1252
                                                         //JDK10    1088
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
