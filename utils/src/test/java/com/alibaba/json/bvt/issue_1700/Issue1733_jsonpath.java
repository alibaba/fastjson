package com.alibaba.json.bvt.issue_1700;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONPath;
import junit.framework.TestCase;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Issue1733_jsonpath extends TestCase {
    public void test_for_issue() throws Exception {
        Order order = new Order();
        order.books.add(new Book(10, "动漫"));
        order.books.add(new Book(50, "科幻"));
        order.books.add(new Book(60, "历史"));

        String path = "$.books[price>20 && category = '科幻']";
        List result = (List) JSONPath.eval(order, path);
        assertSame(1, result.size());
        assertSame(order.books.get(1), result.get(0));
    }

    public void test_for_issue_or() throws Exception {
        Order order = new Order();
        order.books.add(new Book(10, "动漫"));
        order.books.add(new Book(50, "科幻"));
        order.books.add(new Book(60, "历史"));

        String path = "$.books[price>20||category = '科幻']";
        List result = (List) JSONPath.eval(order, path);
        assertEquals(2, result.size());
        assertSame(order.books.get(1), result.get(0));
        assertSame(order.books.get(2), result.get(1));
    }

    public void test_for_issue_or_1() throws Exception {
        Order order = new Order();
        order.books.add(new Book(10, "动漫"));
        order.books.add(new Book(50, "科幻"));
        order.books.add(new Book(60, "历史"));

        String path = "$.books[category = '动漫' ||category = '科幻']";
        List result = (List) JSONPath.eval(order, path);
        assertEquals(2, result.size());
        assertSame(order.books.get(0), result.get(0));
        assertSame(order.books.get(1), result.get(1));
    }

    public static class Order {
        public List<Book> books = new ArrayList<Book>();

    }

    public static class Book {
        public BigDecimal price;
        public String category;

        public Book() {

        }

        public Book(int price, String category) {
            this(new BigDecimal(price), category);
        }

        public Book(BigDecimal price, String category) {
            this.price = price;
            this.category = category;
        }
    }
}
