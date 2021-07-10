package com.alibaba.fastjson.deserializer.issue3762.beans;

import com.alibaba.fastjson.annotation.JSONField;

public class Demo{
    public abstract static class Info<T> {
        private Long id;

        public Long getId() {
            return id;
        }

        @SuppressWarnings("unchecked")
        public T setId(Long id) {
            this.id = id;
            return (T) this;
        }

        @Override
        public String toString() {
            return "Info{" +
                    "id=" + id +
                    '}';
        }
    }

    public static class SubInfo extends Info<SubInfo> {
        private String name;

        public String getName() {
            return name;
        }

        public SubInfo setName(String name) {
            this.name = name;
            return this;
        }

        @Override
        public String toString() {
            return "SubInfo{" +
                    "id=" + super.id +
                    ", name='" + name + '\'' +
                    '}';
        }

        @Override
        public boolean equals( Object o ) {
            if ( this == o ) return true;
            if ( o == null || getClass( ) != o.getClass( ) ) return false;
            return this.toString().equals( o.toString() );
        }
    }

    public static class A {
        private String id;

        public String getId() {
            return id;
        }

        public A setId(String id) {
            this.id = id;
            return this;
        }

        @Override
        public String toString() {
            return "A{" +
                    "id='" + id + '\'' +
                    '}';
        }
    }

    public static class B extends A {

        @Override
        public A setId(String id) {
            super.id = id;
            return this;
        }

        @Override
        public String toString() {
            return "B{" +
                    "id='" + super.id + '\'' +
                    '}';
        }

        @Override
        public boolean equals( Object obj ) {
            return this.toString().equals( obj.toString() );
        }
    }
}
