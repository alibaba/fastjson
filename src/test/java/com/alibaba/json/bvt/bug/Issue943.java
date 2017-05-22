package com.alibaba.json.bvt.bug;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import junit.framework.TestCase;

import java.util.List;

/**
 * Created by wenshao on 09/12/2016.
 */
public class Issue943 extends TestCase {
    public void test_for_issue() throws Exception {
        String text = "{\n" +
                "\t\"symbols\":[\n" +
                "\t    {\"id\":1,\"type\":\"SCATTER\"},\n" +
                "\t    {\"id\":2,\"type\":\"BONUS\"}\n" +
                "\t]\n" +
                "}";

        JSONObject root = JSON.parseObject(text);

        JSONArray symbols = root.getJSONArray("symbols");
        assertNotNull(symbols);
        assertEquals(2, symbols.size());
        assertEquals(1, symbols.getJSONObject(0).get("id"));
        assertEquals("SCATTER", symbols.getJSONObject(0).get("type"));

        assertEquals(2, symbols.getJSONObject(1).get("id"));
        assertEquals("BONUS", symbols.getJSONObject(1).get("type"));

        SlotConfig slotConfig = JSON.parseObject(text, SlotConfig.class);

        assertNotNull(slotConfig);
        assertEquals(2, slotConfig.symbols.size());
        assertEquals(1, slotConfig.symbols.get(0).getId());
        assertEquals(SymbolType.SCATTER, slotConfig.symbols.get(0).getType());

        assertEquals(2, slotConfig.symbols.get(1).getId());
        assertEquals(SymbolType.BONUS, slotConfig.symbols.get(1).getType());

    }

    private static class SlotConfig {
        private List<Symbol> symbols;

        public List<Symbol> getSymbols() {
            return symbols;
        }

        public void setSymbols(List<Symbol> symbols) {
            this.symbols = symbols;
        }
    }

    private static class Symbol {
        private int id;

        private SymbolType type;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public SymbolType getType() {
            return type;
        }

        public void setType(SymbolType type) {
            this.type = type;
        }
    }

    enum SymbolType {
        NORMAL, WILD, SCATTER, BONUS
    }
}
