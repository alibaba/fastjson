package com.alibaba.json.test.benchmark;

import com.alibaba.json.test.benchmark.encode.*;
import com.alibaba.json.test.benchmark.decode.*;
import com.alibaba.json.test.codec.*;

public class BenchmarkMain {
	public static void main(String[] args) throws Exception {
	     // 注意，byte[]在jackson中是使用base64编码的，不正确的。

        BenchmarkExecutor executor = new BenchmarkExecutor();
        executor.setExecuteCount(10);
//        executor.getCodecList().add(new FastjsonSCodec());
        executor.getCodecList().add(new FastjsonCodec());
//        executor.getCodecList().add(new FastjsonBeanToArrayCodec());
//        executor.getCodecList().add(new GsonCodec());
//        executor.getCodecList().add(new Jackson2Codec());
        //
        // executor.getCodecList().add(new SimpleJsonCodec());
        // executor.getCodecList().add(new JsonLibCodec());
        // executor.getCodecList().add(new JsonSmartCodec());

        executor.setLoopCount(1000 * 1000);

//        executor.getCaseList().add(new CartObjectParse());
//        executor.getCaseList().add(new TradeParse());
//        executor.getCaseList().add(new XueluDecode());
//         executor.getCaseList().add(new EishayEncode());
         executor.getCaseList().add(new EishayDecode());
//         executor.getCaseList().add(new GetHomePageResponseDecode());
//         executor.getCaseList().add(new EishayTreeDecode());
//         executor.getCaseList().add(new Eishay3Encode());
//         executor.getCaseList().add(new EishayEncodeManual());
//         executor.getCaseList().add(new IntArray1000Decode());
        // executor.getCaseList().add(new StringArray1000Decode());
        // executor.getCaseList().add(new Map1000StringDecode());
        // executor.getCaseList().add(new Entity100StringDecode());
//        executor.getCaseList().add(new Entity100IntDecode());


        executor.execute();
	}
}
