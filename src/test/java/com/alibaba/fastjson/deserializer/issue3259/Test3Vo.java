package com.alibaba.fastjson.deserializer.issue3259;

import java.util.List;

public class Test3Vo {
    private List<Test4Vo> test4VoList;

    public List<Test4Vo> getTest4VoList() {
        return test4VoList;
    }

    public void setTest4VoList(List<Test4Vo> test4VoList) {
        this.test4VoList = test4VoList;
    }
}