package com.alibaba.fastjson.deserializer.issue3656;

import java.util.ArrayList;
import java.util.List;

public class MainFastJSON {
    public static class Type {
        public List<String> enums = new ArrayList<>();
        public String typeBuilderName = "";
        public String type = "";
    }

    public static class Metadata {
        public String serviceType;
        public List<Type> types = new ArrayList<>();
    }


}
