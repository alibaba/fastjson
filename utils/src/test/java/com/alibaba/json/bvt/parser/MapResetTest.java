package com.alibaba.json.bvt.parser;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;

public class MapResetTest extends TestCase {

    public void test_0() throws Exception {
        Book book = new Book();

//         book.setMetadata(new MetaData());
        String text = JSON.toJSONString(book);

        System.out.println(text);

        Book book2 = JSON.parseObject(text, Book.class);
        System.out.println(JSON.toJSONString(book2));
    }

    public static class Book {

        private int      id;
        private int      pageCountNum;

        private MetaData metadata;

         public int getPageCountNum() {
         return pageCountNum;
         }
        
        public void setPageCountNum(int pageCountNum) {
            this.pageCountNum = pageCountNum;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public MetaData getMetadata() {
            return metadata;
        }

        public void setMetadata(MetaData metadata) {
            this.metadata = metadata;
        }

        

        
    }

    public static class MetaData {

    }
}
