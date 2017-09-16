package com.alibaba.json.bvt.writeClassName;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import junit.framework.TestCase;

import java.util.*;

public class WriteClassNameTest_Set5 extends TestCase {

    public void test_list() throws Exception {
        Model model = new Model();
        LinkedHashSet tables = new LinkedHashSet();
        tables.add(new ExtTable(1001));
        tables.add(new Table());
        model.setTables(tables);

        String json = JSON.toJSONString(model);
        assertEquals("{\"tables\":[{\"@type\":\"com.alibaba.json.bvt.writeClassName.WriteClassNameTest_Set5$ExtTable\",\"id\":1001},{}]}", json);

        Model model2 = JSON.parseObject(json, Model.class);
        assertEquals(ExtTable.class, model2.getTables().iterator().next().getClass());

        JSONObject jsonObject = JSON.parseObject(json, Feature.IgnoreAutoType);
        assertEquals("{\"tables\":[{\"id\":1001},{}]}", jsonObject.toJSONString());
    }

    public static class Model {
        @JSONField(serialzeFeatures = SerializerFeature.WriteClassName)
        private LinkedHashSet<? extends Table> tables;

        public LinkedHashSet<? extends Table> getTables() {
            return tables;
        }

        public void setTables(LinkedHashSet<? extends Table> tables) {
            this.tables = tables;
        }
    }

    public static class Table {

    }

    public static class ExtTable extends Table {
        public int id;

        public ExtTable() {

        }

        public ExtTable(int id) {
            this.id = id;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ExtTable extTable = (ExtTable) o;

            return id == extTable.id;
        }

        @Override
        public int hashCode() {
            return id;
        }
    }
}
