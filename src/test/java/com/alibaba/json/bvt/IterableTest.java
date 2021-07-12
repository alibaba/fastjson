package com.alibaba.json.bvt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.Linq;
import junit.framework.TestCase;
import org.apache.commons.lang.NotImplementedException;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * @author TimAndy
 */
public class IterableTest extends TestCase {
    public void testIterable() {
        Person person = new Person();
        person.setId("123");
        person.setName("Hello World!");
        IEnumerable<Person> persons = Linq.repeat(person, 5);

        //write iterable as array
        String json = JSON.toJSONString(persons, SerializerFeature.WriteIterableAsArray);
        List<Person> result = JSON.parseObject(json, new TypeReference<List<Person>>() {
        });
        assertEquals(5, result.size());
        assertSame(Person.class, result.get(0).getClass());
        assertEquals("123", result.get(4).getId());
        assertEquals("Hello World!", result.get(4).getName());

        //write iterable as bean
        Iterable<Person> persons2 = new Repeat<Person>(person, 5);
        String json2 = JSON.toJSONString(persons2);
        Repeat<Person> result2 = JSON.parseObject(json2, new TypeReference<Repeat<Person>>() {
        });
        assertEquals(5, result2.getCount());
        assertEquals("123", result2.getItem().getId());
        assertEquals("Hello World!", result2.getItem().getName());
    }

    static class Person {
        private String id;
        private String name;

        public String getId() {
            return this.id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    static class Repeat<T> implements Iterable<T> {
        private T item;
        private int count;

        Repeat() {
        }

        Repeat(T item, int count) {
            this.item = item;
            this.count = count;
        }

        public T getItem() {
            return this.item;
        }

        public void setItem(T item) {
            this.item = item;
        }

        public int getCount() {
            return this.count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        @Override
        public Iterator<T> iterator() {
            return new Iterator<T>() {
                private int index;

                @Override
                public boolean hasNext() {
                    return this.index < Repeat.this.count;
                }

                @Override
                public T next() {
                    if (this.index < Repeat.this.count) {
                        this.index++;
                        return Repeat.this.item;
                    }
                    throw new NoSuchElementException();
                }

                @Override
                public void remove() {
                    throw new NotImplementedException();
                }
            };
        }
    }
}
