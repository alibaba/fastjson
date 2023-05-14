package com.alibaba.json.test.benchmark.jdk10;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.openjdk.jmh.annotations.*;

import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Warmup(iterations = 10)
@Measurement(iterations = 10)
@State(Scope.Benchmark)
public class StringBenchmark {
    private ObjectMapper mapper = new ObjectMapper();

    static private String s = "{\"compress\":true,\"queryParams\":\"^^$$Z29e389d72fca43e2591dfa6d5bd182039|null{$_$}H4sIAAAAAAAAAK1YW2/kthX+K4GeGsCdkpRIit6XztrehVHf4ssmabwYUCRlay2Jii72ThcD9L3IY5/7NwL076To3+ghJc1obDd2g754R0fnfvnO0X4J0loW5sHWd2cSfjWHRZUHu1+CKpdtauviclkZ96xy2TQnwBrsBsoWM5lniUzkrK2lNjOVZ6ZsZ4XVJp81tquVmZ1NNHwgwU5Q9tLfHp4fHB1cXCzCBQKqrXVWSrBJVjtBJW/Mf7PYSptIO4OflS2dNb0EjZmaFaaVE7L7lZvPs7dX35PZ2aiw1w2qbkxparC3E2jTKCD8+5//+OXnv/7y89/+9dPfgVrI+s60QN94/P7g5OB8fjR1loOzuVUyd+//crvYOwmA0jWmnjdNdlO+nLLmVtZGz6raNOC1bDNbzipXgtk3namXvho+bbUptalPa/jjiU511uyN8X4wdQOyJNht6864MJcFkNes4NMcYlagcRnspjJvgCkrm1bm+RbjhDbXsmptfWFyo1qjnxO7tDc3uTmSTXsBznfNs6qhD25l8wLXYOzXmUZX3tW22Id6Z/maq6qtMk0zdbNxbHP9qWvagbhauXrn2f2YWhexzu4zl7s9aFuoURghjDFkXGoNVWnWfM1g/FB7qUHNvOdyRNSX3v9cOfsXXVXZuj1tb019bpQBQ+XNxL/h/fH+ny+fUveeo9mysXmmfZ9MXp+CJ42Rh9VTGvgH/26yKX0XbNztu6KfNqDkWXm3HVJpjL6qwKSJhhdrXSDeh2XW8q19KEf5MrWQKZdzyMp9psz8UUbrMSeDOHDeWKubR3yrnYHTHJv21uonFTmfvoUSjm1yArgj8+23zh0Yw8KWazVJt+yrxjEhKKYR9aH1/TCECjwn9mGcLuvG8MIDnFNQmtZBZ++qNi7UPqLg4Pjs8nuHOtso+AT+Nog3gZlm2bSmeKQp6ZqshOQMZI8fweCR63fnTwvvBsR5d356vAAMPDn9FrhSYHg7iWTVk3zllC3TrC48wlx4y8EuBnJtoNvKmy36l8G3wciDrBxkWt3lo13IVwlWVo6cZLlPU9tmrjoYIURFGP2xh/KFLHVtM73gMzaL3NxV1YBmwLwhZoPmorXVAJ5Jl+XaR95zPBXrJDwcX56eXez/6Q/hDM2iGf/qd/Pe4hs+Q2++y6QtsjfHh18dz7/7OnCTM2Zo0+WPuyNrTszD2lrfE00/oucGSg3tdlVvsKk2P3amaV15jsH7EYxAJmtNX7QAVocDsczWWbs87wU2jLZyE994JC98FoEbBrdeVgCXdf/cZgUIyQIMBAyHJIxCRDndp/OIUBLtM8b3Ax8PDEi6J+t6+d6N2wQcwZ+ivwCC3R++BLZrXeQBpyxGUZykIkU6otgQJhHmJuGGKaKE72DYUO1p1/pG8VJKxBgzQilSIokREiqkRnIghlhizUFKybodcMaZdj8pY0hggpkICWTornPE0DUMobEgkQCq6mBX2rUlkL7P8hxmaHxsYJWY4fePnSxbSKpv56y8BzdtvfxmTUUzNLS5ucxci2FKCKYopIww7nwsKgnLfLQEC1yPRe/PiOlybKo8Wxfu4zCYz6T0Fcnxou9r21XDsF8ez4+OFu+uzk8OL6/OD/xVcO7vgos2U3drH+7M0ou9heiC0rZDay5aQMM87eoya7samsCD8mjoCGDl1Dnn/Hy54h+f2aQwlWXjN5rH2sHr32M/VQCnV9P1/AAVe28v7Z4D9Bsz2VwwW9vMfpkeW52l2UTBpBAu0eYzmB8TDWeX3aABmTmMVZAR/+gkHBjmn+AhwiGK48jluyuqvVtZlgYmt+zy3B137hxxFR16zat+MTcAfwsqohALwkkUxQuXgRcL/ljKgWdza6vKoe//cf4Bmsc0BdNhW+DF1pgtXA78n18BjR1XdAWjdpQ5vPohQEkqEeHRdIiiGBFsYoZZ8HHlJfTYfZNbdnotuQtgM1KPXkxPkO3LYt4sS/UOgGDTOMMxbtpmegsPpP3L0+FCgfOxsC7HE4fOrMNsY97BDQx1+JBtzqvqyasjc+/6xi/SIrEDWKTA8z5LW3fSTNvZpwD2jD/yhxtkcl88t3L+B3jcQrzfsAJfvahf2skjQoyo3BfrcOjfZxd9402HKMKGCa0ETQXFPCJxiBKWmEQgSrR03f953cVfrjfjfh3sXruBvw52roeR9ySXWE+DsfeEcfCvg9VE22nyCU7H3wQgq+21A9xlWy+Hrwr3Zdh3455Ut2Z9fq0/WJsLWDHS4XIf0PD1scDe2ZAwxEOaxlQrHicxD8OYuuQgFPEY9ZHJ8QNvEIqwkJxRHEnqJhHJWAoFCZSRjDVnvBcqbbksbLe2BNlOaSQjUJ/QiKE0lGmCBEpJmJAoHfK6uRUHOYMNDwkSTMRE0NgIzHTMqFBCplrK0MuN/dDvh8VLkNhHoWgoqBCJ5lQwnKAYmh8bhRiKhUkjr9hPB3xvLF7C5r49KMSCJNEJ55wJoyjChuqUEshnEm9Uuly+SiOPUxaGmmMEmU1Dg8NYY6plCpFgGbG1xtepEylXYcowVxEMNEk5YCmmKiEplTxkxKsrTGFfl0KVJGFKpJQmkgJhFlOkFYp0rLDiSGGvzs+sz+GrdNKQR6FEjEgqYsHgq1kxCI3jEBqGhhOdLomvUskg9bA1qIpTIUisE0Y4xcIgRnWc6nCj8pUuCuhHiohROAk5izBhhhoRaxFHsUx6iHDAnzXmxbosUN86sMsQVpowSsFNFScixZwqHQpMBY62dL7k5aDTyEglRnGcpqkAuCdIawQCKgUeaWKvc8T0VzZQ4sYjTqTGGkeMhYIDoEgGSyjVSvZ4ASdvvkGLNKWJMsAEk4B1xJOESQyTLHAM6dN9tpouKYZjaBBLQgwLSRIGKJjoiEEfRNAEiGqBOdOyh9fhw3P476nVfwDaiC3ccBQAAA==\",\"structures\":\"H4sIAAAAAAAAAI2QwW7DIBBE/4WzD+AYY/IHOUTpvbKsNawbJAMpxpVQ1X+vjZWzObK8nZ2ZX6K8m0ywj6AxDIxcPwloHXBZtkdFfB4r2THW1pxTJceOUqkuHEFswwsDpsUGwhc6lT4g5TVw3iXr10MkIMzvn2UdrYnva31FTEQ7CN52tOnGSU5UN5xh3QJlAkeBraqV3G3t4M1N/hyuyPcKLpqYSthX8NYseIoOlBxu9yinun1xd1u0TOZsBU2XFVYRjbP5wZDuGJ9elyhbtEUOst29hVO2//sHOuYJjmECAAA=\",\"submitParams\":\"^^$$Z262e889742b9875f5107901ea64c33b46|null{$_$}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\",\"validateParams\":\"^^$$5da126afd2e7ebcf402eecea37f8e6df{$_$}H4sIAAAAAAAAAIVRTWsCMRT8LzlLcNePVW9ZG4ogItpaepJn9qmL2WRJIq0VoffSY8/9G4X+HUv/Rt9WSr2U3ibzXmaGeXu2dFDgnXWbMRDyg6LUrLdnpYawtK642pVYvZUG70e0ynpM2YKDzhewAB4cZMiVztEEXtgMNfd26xTy8ZnCLGY1Zk6/bwYTOZTT6bwxrxNrXZYbIM/4UGMlrPAvxwB2AZYTLK2p3LIdKeaKFxjgjK6QxnueXt/GfPwjeNImqRUadORXYxl6RcTn++vx7fH49vTx/EJsAW6DgfjfxJdyJCdieB42obDaKtDV/GE9748YMVuPTnifr8z/lfk1OMx46dBTagi5NbysTsAvIMCMlrPvi5yqQ8z6a1SbPjXMekvQHmsMjXK7MkyDy82KTLrtSLRF0ql32qLRkEIK0WzFUookitMo7cq006wnddGKREd22y12OHwB3aDYjQACAAA=\"}";
    static private DO d_o = JSON.parseObject(s, DO.class);
    static private Object o;
    static private int i;
    static private HashMap<String, String> methodMap = new HashMap<String, String>();

    @Setup
    public void prepare() {
        methodMap.put("org.openjdk.jmh.annotations.org.openjdk.jmh.annotations.StringBenchmark.prepare(aaa,bbb,com.alibaba.fastjson.JSON)", "");
    }

    @Benchmark
    @Fork(value = 3, jvmArgs = "-XX:+CompactStrings")
    public void testParseJsonComp() {
        o = JSON.parseObject(s, DO.class);
    }

    @Benchmark
    @Fork(value = 3, jvmArgs = "-XX:-CompactStrings")
    public void testParseJson() {
        o = JSON.parseObject(s, DO.class);
    }

    //    @Benchmark
    @Fork(value = 3, jvmArgs = "-XX:+CompactStrings")
    public void testToJsonComp() {
        o = JSON.toJSON(d_o);
    }

    //    @Benchmark
    @Fork(value = 3, jvmArgs = "-XX:-CompactStrings")
    public void testToJson() {
        o = JSON.toJSON(d_o);
    }

//    //    @Benchmark
//    @Fork(value = 3, jvmArgs = "-XX:+CompactStrings")
//    public void testJoinComp() {
//        o = IntStream.range(0, 10000).mapToObj(String::valueOf).collect(Collectors.joining());
//    }
//
//    //    @Benchmark
//    @Fork(value = 3, jvmArgs = "-XX:-CompactStrings")
//    public void testJoin() {
//        o = IntStream.range(0, 10000).mapToObj(String::valueOf).collect(Collectors.joining());
//    }

    //    @Benchmark
    @Fork(value = 3, jvmArgs = "-XX:+CompactStrings")
    public void testFindMethodComp() {
        o = testFindMethod0();
    }

    //    @Benchmark
    @Fork(value = 3, jvmArgs = "-XX:-CompactStrings")
    public void testFindMethod() {
        o = testFindMethod0();
    }


    private Object testFindMethod0() {
        StringBuilder sb = new StringBuilder();
        sb.append("org.openjdk.jmh.annotations.org.openjdk.jmh.annotations.StringBenchmark")
                .append(".").append("prepare").append("(")
                .append("aaa").append(",").append("bbb").append(",")
                .append("com.alibaba.fastjson.JSON").append(")");

        return methodMap.get(sb.toString());
    }

    static class DO {
        boolean compress;
        String queryParams, structures, submitParams, validateParams;

        public boolean isCompress() {
            return compress;
        }

        public void setCompress(boolean compress) {
            this.compress = compress;
        }

        public String getQueryParams() {
            return queryParams;
        }

        public void setQueryParams(String queryParams) {
            this.queryParams = queryParams;
        }

        public String getStructures() {
            return structures;
        }

        public void setStructures(String structures) {
            this.structures = structures;
        }

        public String getSubmitParams() {
            return submitParams;
        }

        public void setSubmitParams(String submitParams) {
            this.submitParams = submitParams;
        }

        public String getValidateParams() {
            return validateParams;
        }

        public void setValidateParams(String validateParams) {
            this.validateParams = validateParams;
        }
    }
}
