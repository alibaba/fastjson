package com.alibaba.json.bvt.issue_1341;


import com.alibaba.fastjson.support.jaxrs.FastJsonProvider;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.server.JSONP;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.Assert;
import org.junit.Test;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import java.util.Date;

public class TestIssue1341 extends JerseyTest {

    @Path("book")
    public static class BookRestFul {

        @GET
        @Path("{id}")
        @Produces({"application/javascript", "application/json"})
        @JSONP(queryParam = "callback")
        public Book getBookById(@PathParam("id") Long id) {

            Book book = new Book();
            book.setBookId(0);
            book.setBookName("Python源码剖析");
            book.setPublisher("电子工业出版社");
            book.setPublishTime(new Date());
            book.setIsbn("911122");

            return book;
        }

        @GET
        @Path("/2/{id}")
        @Produces({"application/javascript", "application/json"})
        public Book getBookById2(@PathParam("id") Long id) {

            Book book = new Book();
            book.setBookId(2);
            book.setBookName("Python源码剖析2");
            book.setPublisher("电子工业出版社2");
            book.setPublishTime(new Date());
            book.setIsbn("911122");

            return book;
        }
    }

    @Override
    protected void configureClient(ClientConfig config) {
        config.register(new FastJsonFeature()).register(FastJsonProvider.class);
    }

    @Override
    protected Application configure() {
        enable(TestProperties.LOG_TRAFFIC);
        enable(TestProperties.DUMP_ENTITY);

        ResourceConfig config = new ResourceConfig();
        config.packages("com.alibaba.json.bvt.issue_1341");
        return config;
    }

    @Test
    public void test() {

        final String reponse = target("book").path("123").request().accept("application/javascript").get(String.class);

        Assert.assertTrue(reponse.indexOf("callback") > -1);
        Assert.assertTrue(reponse.indexOf("Python源码剖析") > 0);
        Assert.assertTrue(reponse.indexOf("电子工业出版社") > 0);
    }

    @Test
    public void test2() {

        final String reponse = target("book").path("/2/123").request().accept("application/javascript").get(String.class);

        Assert.assertTrue(reponse.indexOf("Python源码剖析2") > 0);
        Assert.assertTrue(reponse.indexOf("电子工业出版社2") > 0);
    }
}
