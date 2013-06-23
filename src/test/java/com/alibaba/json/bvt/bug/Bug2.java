package com.alibaba.json.bvt.bug;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;

public class Bug2 extends TestCase {

    public void test_0() throws Exception {
        Entity entity = new Entity();

        entity.setArticles(Collections.singletonList(new Article()));

        String jsonString = JSON.toJSONString(entity);

        System.out.println(jsonString);

        Entity entity2 = JSON.parseObject(jsonString, Entity.class);
        Assert.assertEquals(entity.getArticles().size(), entity2.getArticles().size());
    }

    public static class Entity {

        private List<HashMap<String, String>> list     = new ArrayList<HashMap<String, String>>();
        private List<Article>                 articles = null;

        public List<HashMap<String, String>> getList() {
            return list;
        }

        public void setList(List<HashMap<String, String>> list) {
            this.list = list;
        }

        public List<Article> getArticles() {
            return articles;
        }

        public void setArticles(List<Article> articles) {
            this.articles = articles;
        }

    }

    public static class Article {

        private long id;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

    }
}
