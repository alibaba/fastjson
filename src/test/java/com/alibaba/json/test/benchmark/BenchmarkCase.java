package com.alibaba.json.test.benchmark;

import com.alibaba.json.test.codec.Codec;

public abstract class BenchmarkCase {

    private final String name;

    public BenchmarkCase(String name){
        super();
        this.name = name;
    }

    public String getName() {
        return name;
    }
    
    public void init(Codec codec) throws Exception {
        
    }
    
    public abstract void execute(Codec codec) throws Exception;
}
