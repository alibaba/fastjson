package com.alibaba.json.bvt.issue_1300;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * Created by kimmking on 02/07/2017.
 */
public class Issue1306 extends TestCase {

    public void test_for_issue() {
        Goods goods = new Goods();
        goods.setProperties(Arrays.asList(new Goods.Property()));
        TT tt = new TT(goods);
        String json = JSON.toJSONString(tt);
        assertEquals("{\"goodsList\":[{\"properties\":[{}]}]}", json);
        TT n = JSON.parseObject(json, TT.class);
        assertNotNull(n);
        assertNotNull(n.getGoodsList());
        assertNotNull(n.getGoodsList().get(0));
        assertNotNull(n.getGoodsList().get(0).getProperties());
    }

    public static abstract class IdEntity<ID extends Serializable> implements Cloneable, Serializable{

        private static final long serialVersionUID = 4877536176216854937L;

        public IdEntity() {}

        public abstract ID getId();
        public abstract void setId(ID id);
    }

    public static class LongEntity extends IdEntity<Long> {

        private static final long serialVersionUID = -2740365657805589848L;

        private Long id;

        @Override
        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }
    }

    public static class Goods extends LongEntity{
        private static final long serialVersionUID = -5751106975913625097L;
        private List<Property> properties;

        public List<Property> getProperties() {
            return properties;
        }

        public void setProperties(List<Property> properties) {
            this.properties = properties;
        }

        public static class Property extends LongEntity{
            private static final long serialVersionUID = 7941148286688199390L;
        }
    }

    public static class TT extends LongEntity {
        private static final long serialVersionUID = 2988415809510669142L;

        public TT(){}
        public TT(Goods goods){
            goodsList = Arrays.asList(goods);
        }


        private List<Goods> goodsList;

        public List<Goods> getGoodsList() {
            return goodsList;
        }

        public void setGoodsList(List<Goods> goodsList) {
            this.goodsList = goodsList;
        }
    }
}
