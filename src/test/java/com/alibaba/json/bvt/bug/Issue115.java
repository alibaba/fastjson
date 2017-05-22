package com.alibaba.json.bvt.bug;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;

public class Issue115 extends TestCase {

    public void test_for_issue_115() throws Exception {
        Player2 player = new Player2();

        Card2 card = new Card2();
        card.cardId = "hello";
        player.cards.put(1, card);
        player.cardGroup.put(1, card);

        String json = JSON.toJSONString(player);

        System.out.println("json:" + json);

        Player2 player2 = JSON.parseObject(json, Player2.class);
    }

    static class Player2 {

        public Map cards     = new HashMap();
        public Map cardGroup = new HashMap();
    }

    static class Card2 {

        public String cardId;
    }
}
