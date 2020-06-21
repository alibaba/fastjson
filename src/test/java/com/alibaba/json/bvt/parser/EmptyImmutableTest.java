package com.alibaba.json.bvt.parser;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

import java.io.Serializable;
import java.util.*;

public class EmptyImmutableTest extends TestCase {
    public void test_0() throws Exception {
        VO vo = JSON.parseObject("{\"values\":[],\"map\":{}}", VO.class);
    }

    public static class VO {
        private List values = new EmptyList();
        private Map map = new EmptyMap();

        public List getValues() {
            return values;
        }

        public Map getMap() {
            return map;
        }
    }

    private static class EmptyMap extends HashMap {
        public void putAll(Map m) {
            throw new UnsupportedOperationException();
        }
    }

    private static class EmptyList<E>
            extends AbstractList<E>
            implements RandomAccess, Serializable {
        private static final long serialVersionUID = 8842843931221139166L;

        public Iterator<E> iterator() {
            throw new UnsupportedOperationException();
        }
        public ListIterator<E> listIterator() {
            throw new UnsupportedOperationException();
        }

        public boolean addAll(Collection<? extends E> c) {
            throw new UnsupportedOperationException();
        }

        public int size() {return 0;}
        public boolean isEmpty() {return true;}

        public boolean contains(Object obj) {return false;}
        public boolean containsAll(Collection<?> c) { return c.isEmpty(); }

        public Object[] toArray() { return new Object[0]; }

        public <T> T[] toArray(T[] a) {
            if (a.length > 0)
                a[0] = null;
            return a;
        }

        public E get(int index) {
            throw new IndexOutOfBoundsException("Index: "+index);
        }

        public boolean equals(Object o) {
            return (o instanceof List) && ((List<?>)o).isEmpty();
        }

        public int hashCode() { return 1; }

        public void sort(Comparator<? super E> c) {
        }


        // Preserves singleton property
        private Object readResolve() {
            throw new UnsupportedOperationException();
        }
    }
}
