package com.alibaba.json.bvt.issue_1341;

import java.util.Date;

public class Book {

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
