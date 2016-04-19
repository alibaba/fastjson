package com.alibaba.json.bvt.bug;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.Assert;
import org.junit.Test;

import com.alibaba.fastjson.JSON;

public class FastJsonSerializeIterableTest {
    @Test
    public void testWithIterable() {
        class Person {
            private String name;
            public Person(String s) {
                this.name = s;
            }
            public String getName() {
                return name;
            }
        }
        final Person s1 = new Person("fast");
        final Person s2 = new Person("fast");
        Iterable<Person> iterable = new Iterable<Person>() {
            @Override
            public Iterator<Person> iterator() {
                return new Iterator<Person>() {
                    int cursor = 0;
                    @Override
                    public boolean hasNext() {
                        return cursor < 2;
                    }

                    @Override
                    public Person next() {
                        int val = cursor++;
                        switch (val) {
                            case 0:
                                return s1;
                            case 1:
                                return s2;
                            default:
                                throw new NoSuchElementException();
                        }
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
        List<Person> list = new ArrayList<Person>();
        for (Person p : iterable) {
            list.add(p);
        }
        
        Assert.assertEquals("[{\"name\":\"fast\"},{\"name\":\"fast\"}]", JSON.toJSONString(list));
        Assert.assertEquals("[{\"name\":\"fast\"},{\"name\":\"fast\"}]", JSON.toJSONString(iterable));
        Assert.assertEquals("[{\"name\":\"fast\"},{\"name\":\"fast\"}]", JSON.toJSONString(list.iterator()));
    }
}
