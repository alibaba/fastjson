package com.alibaba.json.bvt.issue_1300;


import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.jaxrs.FastJsonFeature;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.server.JSONP;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.Assert;
import org.junit.Test;

import javax.ws.rs.*;
import javax.ws.rs.core.Application;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import java.util.Date;

public class Issue1392 extends JerseyTest {

    static class Book {

        private int bookId;
        private String bookName;
        private String publisher;
        private String isbn;
        private Date publishTime;
        private Object hello;

        public int getBookId() {
            return bookId;
        }

        public void setBookId(int bookId) {
            this.bookId = bookId;
        }

        public String getBookName() {
            return bookName;
        }

        public void setBookName(String bookName) {
            this.bookName = bookName;
        }

        public String getPublisher() {
            return publisher;
        }

        public void setPublisher(String publisher) {
            this.publisher = publisher;
        }

        public String getIsbn() {
            return isbn;
        }

        public void setIsbn(String isbn) {
            this.isbn = isbn;
        }

        public Date getPublishTime() {
            return publishTime;
        }

        public void setPublishTime(Date publishTime) {
            this.publishTime = publishTime;
        }

        public Object getHello() {
            return hello;
        }

        public void setHello(Object hello) {
            this.hello = hello;
        }
    }

    @Provider
    static class FastJsonResolver implements ContextResolver<FastJsonConfig> {

        public FastJsonConfig getContext(Class<?> type) {

            FastJsonConfig fastJsonConfig = new FastJsonConfig();

            fastJsonConfig.setSerializerFeatures(
                    SerializerFeature.WriteMapNullValue,
                    SerializerFeature.BrowserSecure);

            return fastJsonConfig;
        }
    }


    @Path("book1392")
    public static class BookRestFul {

        @GET
        @Path("{id}")
        @Produces({"application/javascript", "application/json"})
        @Consumes({"application/javascript", "application/json"})
        @JSONP(queryParam = "callback")
        public Book getBookById(@PathParam("id") Long id) {

            Book book = new Book();
            book.setBookId(2);
            book.setBookName("Python源码剖析");
            book.setPublisher("电子工业出版社");
            book.setPublishTime(new Date());
            book.setIsbn("911122");

            return book;
        }
    }

    @Override
    protected void configureClient(ClientConfig config) {
        config.register(FastJsonFeature.class);
    }

    @Override
    protected Application configure() {
        enable(TestProperties.LOG_TRAFFIC);
        enable(TestProperties.DUMP_ENTITY);

        ResourceConfig config = new ResourceConfig();

        config.register(FastJsonResolver.class);

        config.register(FastJsonFeature.class);

        config.packages("com.alibaba.json.bvt.issue_1300");
        return config;
    }

    @Test
    public void test() {

        final String reponse = target("book1392").path("123").request().accept("application/javascript").get(String.class);
        System.out.println(reponse);
        Assert.assertTrue(reponse.indexOf("Python源码剖析") > 0);
        Assert.assertTrue(reponse.indexOf("电子工业出版社") > 0);
    }

}
