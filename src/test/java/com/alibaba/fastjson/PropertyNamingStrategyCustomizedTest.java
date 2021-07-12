package com.alibaba.fastjson;

import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.spi.IPropertyNamingStrategy;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @project fastjson
 * @desc:
 * @date 2021-06-03 11:36
 */
public class PropertyNamingStrategyCustomizedTest {

    public static class Person {

        //ID
        public int personId;

        //名称
        public String personName;

        // 邮箱
        public String personEmail;

        // 手机号
        public String personPhone;

        public int getPersonId() {
            return personId;
        }

        public void setPersonId(int personId) {
            this.personId = personId;
        }

        public String getPersonName() {
            return personName;
        }

        public void setPersonName(String personName) {
            this.personName = personName;
        }

        public String getPersonEmail() {
            return personEmail;
        }

        public void setPersonEmail(String personEmail) {
            this.personEmail = personEmail;
        }

        public String getPersonPhone() {
            return personPhone;
        }

        public void setPersonPhone(String personPhone) {
            this.personPhone = personPhone;
        }
    }
    
    public static class PropertyNamingStrategyTmp implements IPropertyNamingStrategy {

        @Override
        public String translate(String propertyName) {
            return propertyName+"Test:"+Thread.currentThread().getId();
        }
    }
    
    public static class TestTask implements Runnable{

        @Override
        public void run() {
            for(int i = 0 ; i< 1000 ;i ++){
                PropertyNamingStrategy.Customized.register(new PropertyNamingStrategyTmp());
                Person person = new Person();
                person.personId = 21;
                person.personName="小奋斗教程";
                person.personEmail="1732482792@qq.com";
                person.personPhone="156983444xx";
                SerializeConfig config = new SerializeConfig();
                config.propertyNamingStrategy = PropertyNamingStrategy.Customized;
                String json = JSON.toJSONString(person,config);
                System.out.println("反序列 person json -> ");
                System.out.println(json);
                PropertyNamingStrategy.Customized.unRegister();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            
        }
    }
    
    @Test
    public void test() throws InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(3);
        pool.execute(new TestTask());
        pool.execute(new TestTask());
        pool.execute(new TestTask());
        Thread.sleep(10*10000);

    }
}
