package com.alibaba.json.bvt.writeClassName;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WriteClassNameTest_List3 extends TestCase {

    public void test_list() throws Exception {
        Model model = new Model();
        List tables = new ArrayList();
        tables.add(new ExtTable(1001));
        tables.add(new Table());
        model.setTables(tables);

        String json = JSON.toJSONString(model);
        assertEquals("{\"tables\":[{\"@type\":\"com.alibaba.json.bvt.writeClassName.WriteClassNameTest_List3$ExtTable\",\"id\":1001},{}]}", json);

        Model model2 = JSON.parseObject(json, Model.class);
        assertEquals(ExtTable.class, model2.getTables().iterator().next().getClass());
    }

    public static class Model {
        @JSONField(serialzeFeatures = SerializerFeature.WriteClassName)
        private List<? extends Table> tables;

        public List<? extends Table> getTables() {
            return tables;
        }

        public void setTables(List<? extends Table> tables) {
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
