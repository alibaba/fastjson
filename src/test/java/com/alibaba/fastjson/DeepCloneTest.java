package com.alibaba.fastjson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class DeepCloneTest {

    private static final SerializerFeature[] features={SerializerFeature.DisableCircularReferenceDetect};

    @Test
    public void fastJsonTest() {
        List<User> list=new ArrayList<User>();
        User user=new User();
        user.setId(1);
        user.setName("test");
        user.setBirthday(new Date());
        list.add(user);
        Map<Integer, User> map=new HashMap<Integer, User>();
        map.put(user.getId(), user);
        Object[] arr=new Object[]{1, "test", list, map, User.class};
        try {
            System.out.println("--------------obj arr------------------");
            Object[] rs=(Object[])JSON.deepClone(arr, features);
            for(int i=0; i < rs.length; i++) {
                Object obj=rs[i];
                System.out.println(obj.getClass().getName() + "--->" + obj);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        System.out.println("--------------user arr------------------");
        User[] arr2=new User[]{user};
        try {
            Object[] rs=(Object[])JSON.deepClone(arr2, features);
            for(int i=0; i < rs.length; i++) {
                Object obj=rs[i];
                System.out.println(obj.getClass().getName() + "--->" + obj);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        System.out.println("--------------map------------------");
        try {
            Map<Integer, User> obj=(Map<Integer, User>)JSON.deepClone(map, features);
            System.out.println(obj.getClass().getName() + "--->" + obj);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public class User implements Serializable {

        private static final long serialVersionUID=-1360495988946413478L;

        private Integer id;

        private String name;

        private Date birthday;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id=id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name=name;
        }

        public Date getBirthday() {
            return birthday;
        }

        public void setBirthday(Date birthday) {
            this.birthday=birthday;
        }

        @Override
        public String toString() {
            return "User [id=" + id + ", name=" + name + ", birthday=" + birthday + "]";
        }

    }
}
