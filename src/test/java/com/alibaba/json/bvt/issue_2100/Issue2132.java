package com.alibaba.json.bvt.issue_2100;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

public class Issue2132 extends TestCase {
    public void test_for_issue() throws Exception {
        Cpu cpu = new Cpu("intel", 3.3);
        Screen screen = new Screen(16, 9, "samsung");
        Student student = new Student();
        Computer computer = student.assembling(cpu,screen);
        cpu.setName("intell");

        Object[] objectArray = new Object[4];
        objectArray[0] = cpu;
        objectArray[1] = screen;
        objectArray[2] = "2";
        objectArray[3] = "3";
        List<Object> list1 = new ArrayList<Object>();
        list1.add(objectArray);
        list1.add(computer);
        String s = JSON.toJSONString(list1);

        assertEquals("[[{\"name\":\"intell\",\"speed\":3.3},{\"height\":9,\"name\":\"samsung\",\"width\":16},\"2\",\"3\"],{\"cpu\":{\"$ref\":\"$[0][0]\"},\"screen\":{\"$ref\":\"$[0][1]\"}}]", s);
    }

    public static class Cpu {
        private String name;
        private double speed;

        public Cpu(String name, double speed) {
            this.name = name;
            this.speed = speed;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getSpeed() {
            return speed;
        }

        public void setSpeed(double speed) {
            this.speed = speed;
        }
    }

    public static class Screen {
        private int width;
        private int height;
        private String name;

        public Screen(int width, int height, String name) {
            this.width = width;
            this.height = height;
            this.name = name;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class Computer {
        Cpu cpu;
        Screen screen;

        public Computer(Cpu cpu, Screen screen) {
            this.cpu = cpu;
            this.screen = screen;
        }

        public Cpu getCpu() {
            return cpu;
        }

        public void setCpu(Cpu cpu) {
            this.cpu = cpu;
        }

        public Screen getScreen() {
            return screen;
        }

        public void setScreen(Screen screen) {
            this.screen = screen;
        }
    }

    public static class Student {
        private Cpu cpu;
        private Screen screen;

        public Computer assembling(Cpu cpu, Screen screen) {
            this.cpu = cpu;
            this.screen = screen;

            return new Computer(cpu, screen);
        }

        public Cpu getCpu() {
            return cpu;
        }

        public void setCpu(Cpu cpu) {
            this.cpu = cpu;
        }

        public Screen getScreen() {
            return screen;
        }

        public void setScreen(Screen screen) {
            this.screen = screen;
        }
    }
}
