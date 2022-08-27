package com.wheelchair.parser;

import com.diffblue.deeptestutils.Reflector;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Method;

import static org.junit.Assert.*;
import com.diffblue.deeptestutils.Reflector;


public class JSONScannerTest {

    @Test
    public void checkDate1() throws Throwable {
        // Arrange
        char y0 = '2';
        char y1 = '0';
        char y2 = '2';
        char y3 = '0';
        char M0 = '0';
        char M1 = '2';
        int d0 = 48;
        int d1 = 52;

        // Act
        Class<?> c = Reflector.forName("com.alibaba.fastjson.parser.JSONScanner");
        Method m = c.getDeclaredMethod("checkDate", Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("int"), Reflector.forName("int"));
        m.setAccessible(true);
        boolean retval = (Boolean)m.invoke(null, y0, y1, y2, y3, M0, M1, d0, d1);

        // Assert result
        Assert.assertEquals(true, retval);
    }
    @Test
    public void checkDate2() throws Throwable {
        // Arrange
        char y0 = '2';
        char y1 = '0';
        char y2 = '2';
        char y3 = '0';
        char M0 = '0';
        char M1 = '2';
        int d0 = 48;
        int d1 = 48;

        // Act
        Class<?> c = Reflector.forName("com.alibaba.fastjson.parser.JSONScanner");
        Method m = c.getDeclaredMethod("checkDate", Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("int"), Reflector.forName("int"));
        m.setAccessible(true);
        boolean retval = (Boolean)m.invoke(null, y0, y1, y2, y3, M0, M1, d0, d1);

        // Assert result
        Assert.assertEquals(false, retval);
    }
    @Test
    public void checkDate3() throws Throwable {
        // Arrange
        char y0 = '2';
        char y1 = '0';
        char y2 = '2';
        char y3 = '0';
        char M0 = '0';
        char M1 = '2';
        int d0 = 48;
        int d1 = 97;

        // Act
        Class<?> c = Reflector.forName("com.alibaba.fastjson.parser.JSONScanner");
        Method m = c.getDeclaredMethod("checkDate", Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("int"), Reflector.forName("int"));
        m.setAccessible(true);
        boolean retval = (Boolean)m.invoke(null, y0, y1, y2, y3, M0, M1, d0, d1);

        // Assert result
        Assert.assertEquals(false, retval);
    }
    @Test
    public void checkDate4() throws Throwable {
        // Arrange
        char y0 = '2';
        char y1 = '0';
        char y2 = '2';
        char y3 = '0';
        char M0 = '0';
        char M1 = '2';
        int d0 = 49;
        int d1 = 52;

        // Act
        Class<?> c = Reflector.forName("com.alibaba.fastjson.parser.JSONScanner");
        Method m = c.getDeclaredMethod("checkDate", Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("int"), Reflector.forName("int"));
        m.setAccessible(true);
        boolean retval = (Boolean)m.invoke(null, y0, y1, y2, y3, M0, M1, d0, d1);

        // Assert result
        Assert.assertEquals(true, retval);
    }
    @Test
    public void checkDate5() throws Throwable {
        // Arrange
        char y0 = '2';
        char y1 = '0';
        char y2 = '2';
        char y3 = '0';
        char M0 = '0';
        char M1 = '2';
        int d0 = 49;
        int d1 = 21;

        // Act
        Class<?> c = Reflector.forName("com.alibaba.fastjson.parser.JSONScanner");
        Method m = c.getDeclaredMethod("checkDate", Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("int"), Reflector.forName("int"));
        m.setAccessible(true);
        boolean retval = (Boolean)m.invoke(null, y0, y1, y2, y3, M0, M1, d0, d1);

        // Assert result
        Assert.assertEquals(false, retval);
    }
    @Test
    public void checkDate6() throws Throwable {
        // Arrange
        char y0 = '2';
        char y1 = '0';
        char y2 = '2';
        char y3 = '0';
        char M0 = '0';
        char M1 = '2';
        int d0 = 49;
        int d1 = 97;

        // Act
        Class<?> c = Reflector.forName("com.alibaba.fastjson.parser.JSONScanner");
        Method m = c.getDeclaredMethod("checkDate", Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("int"), Reflector.forName("int"));
        m.setAccessible(true);
        boolean retval = (Boolean)m.invoke(null, y0, y1, y2, y3, M0, M1, d0, d1);

        // Assert result
        Assert.assertEquals(false, retval);
    }
    @Test
    public void checkDate7() throws Throwable {
        // Arrange
        char y0 = '2';
        char y1 = '0';
        char y2 = '2';
        char y3 = '0';
        char M0 = '0';
        char M1 = '2';
        int d0 = 51;
        int d1 = 49;

        // Act
        Class<?> c = Reflector.forName("com.alibaba.fastjson.parser.JSONScanner");
        Method m = c.getDeclaredMethod("checkDate", Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("int"), Reflector.forName("int"));
        m.setAccessible(true);
        boolean retval = (Boolean)m.invoke(null, y0, y1, y2, y3, M0, M1, d0, d1);

        // Assert result
        Assert.assertEquals(true, retval);
    }
    @Test
    public void checkDate8() throws Throwable {
        // Arrange
        char y0 = '2';
        char y1 = '0';
        char y2 = '2';
        char y3 = '0';
        char M0 = '0';
        char M1 = '2';
        int d0 = 51;
        int d1 = 21;

        // Act
        Class<?> c = Reflector.forName("com.alibaba.fastjson.parser.JSONScanner");
        Method m = c.getDeclaredMethod("checkDate", Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("int"), Reflector.forName("int"));
        m.setAccessible(true);
        boolean retval = (Boolean)m.invoke(null, y0, y1, y2, y3, M0, M1, d0, d1);

        // Assert result
        Assert.assertEquals(false, retval);
    }
    @Test
    public void checkDate9() throws Throwable {
        // Arrange
        char y0 = '2';
        char y1 = '0';
        char y2 = '2';
        char y3 = '0';
        char M0 = '0';
        char M1 = '2';
        int d0 = 51;
        int d1 = 52;

        // Act
        Class<?> c = Reflector.forName("com.alibaba.fastjson.parser.JSONScanner");
        Method m = c.getDeclaredMethod("checkDate", Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("int"), Reflector.forName("int"));
        m.setAccessible(true);
        boolean retval = (Boolean)m.invoke(null, y0, y1, y2, y3, M0, M1, d0, d1);

        // Assert result
        Assert.assertEquals(false, retval);
    }
    @Test
    public void checkDate10() throws Throwable {
        // Arrange
        char y0 = '2';
        char y1 = '0';
        char y2 = '2';
        char y3 = '0';
        char M0 = '0';
        char M1 = '2';
        int d0 = 21;
        int d1 = 52;

        // Act
        Class<?> c = Reflector.forName("com.alibaba.fastjson.parser.JSONScanner");
        Method m = c.getDeclaredMethod("checkDate", Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("int"), Reflector.forName("int"));
        m.setAccessible(true);
        boolean retval = (Boolean)m.invoke(null, y0, y1, y2, y3, M0, M1, d0, d1);

        // Assert result
        Assert.assertEquals(false, retval);
    }
    @Test
    public void checkDate11() throws Throwable {
        // Arrange
        char y0 = '2';
        char y1 = '0';
        char y2 = '2';
        char y3 = '0';
        char M0 = '0';
        char M1 = '2';
        int d0 = 52;
        int d1 = 52;

        // Act
        Class<?> c = Reflector.forName("com.alibaba.fastjson.parser.JSONScanner");
        Method m = c.getDeclaredMethod("checkDate", Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("int"), Reflector.forName("int"));
        m.setAccessible(true);
        boolean retval = (Boolean)m.invoke(null, y0, y1, y2, y3, M0, M1, d0, d1);

        // Assert result
        Assert.assertEquals(false, retval);
    }
    @Test
    public void checkDate12() throws Throwable {
        // Arrange
        char y0 = '2';
        char y1 = '0';
        char y2 = '2';
        char y3 = '0';
        char M0 = '0';
        char M1 = '0';
        int d0 = 48;
        int d1 = 52;

        // Act
        Class<?> c = Reflector.forName("com.alibaba.fastjson.parser.JSONScanner");
        Method m = c.getDeclaredMethod("checkDate", Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("int"), Reflector.forName("int"));
        m.setAccessible(true);
        boolean retval = (Boolean)m.invoke(null, y0, y1, y2, y3, M0, M1, d0, d1);

        // Assert result
        Assert.assertEquals(false, retval);
    }
    @Test
    public void checkDate13() throws Throwable {
        // Arrange
        char y0 = '2';
        char y1 = '0';
        char y2 = '2';
        char y3 = '0';
        char M0 = '0';
        char M1 = 'a';
        int d0 = 48;
        int d1 = 52;

        // Act
        Class<?> c = Reflector.forName("com.alibaba.fastjson.parser.JSONScanner");
        Method m = c.getDeclaredMethod("checkDate", Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("int"), Reflector.forName("int"));
        m.setAccessible(true);
        boolean retval = (Boolean)m.invoke(null, y0, y1, y2, y3, M0, M1, d0, d1);

        // Assert result
        Assert.assertEquals(false, retval);
    }
    @Test
    public void checkDate14() throws Throwable {
        // Arrange
        char y0 = '2';
        char y1 = '0';
        char y2 = '2';
        char y3 = '0';
        char M0 = '1';
        char M1 = '0';
        int d0 = 48;
        int d1 = 52;

        // Act
        Class<?> c = Reflector.forName("com.alibaba.fastjson.parser.JSONScanner");
        Method m = c.getDeclaredMethod("checkDate", Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("int"), Reflector.forName("int"));
        m.setAccessible(true);
        boolean retval = (Boolean)m.invoke(null, y0, y1, y2, y3, M0, M1, d0, d1);

        // Assert result
        Assert.assertEquals(true, retval);
    }
    @Test
    public void checkDate15() throws Throwable {
        // Arrange
        char y0 = '2';
        char y1 = '0';
        char y2 = '2';
        char y3 = '0';
        char M0 = '1';
        char M1 = '!';
        int d0 = 48;
        int d1 = 52;

        // Act
        Class<?> c = Reflector.forName("com.alibaba.fastjson.parser.JSONScanner");
        Method m = c.getDeclaredMethod("checkDate", Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("int"), Reflector.forName("int"));
        m.setAccessible(true);
        boolean retval = (Boolean)m.invoke(null, y0, y1, y2, y3, M0, M1, d0, d1);

        // Assert result
        Assert.assertEquals(false, retval);
    }
    @Test
    public void checkDate16() throws Throwable {
        // Arrange
        char y0 = '2';
        char y1 = '0';
        char y2 = '2';
        char y3 = '0';
        char M0 = '1';
        char M1 = '3';
        int d0 = 48;
        int d1 = 52;

        // Act
        Class<?> c = Reflector.forName("com.alibaba.fastjson.parser.JSONScanner");
        Method m = c.getDeclaredMethod("checkDate", Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("int"), Reflector.forName("int"));
        m.setAccessible(true);
        boolean retval = (Boolean)m.invoke(null, y0, y1, y2, y3, M0, M1, d0, d1);

        // Assert result
        Assert.assertEquals(false, retval);
    }
    @Test
    public void checkDate17() throws Throwable {
        // Arrange
        char y0 = '2';
        char y1 = '0';
        char y2 = '2';
        char y3 = '0';
        char M0 = '!';
        char M1 = '2';
        int d0 = 48;
        int d1 = 52;

        // Act
        Class<?> c = Reflector.forName("com.alibaba.fastjson.parser.JSONScanner");
        Method m = c.getDeclaredMethod("checkDate", Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("int"), Reflector.forName("int"));
        m.setAccessible(true);
        boolean retval = (Boolean)m.invoke(null, y0, y1, y2, y3, M0, M1, d0, d1);

        // Assert result
        Assert.assertEquals(false, retval);
    }
    @Test
    public void checkDate18() throws Throwable {
        // Arrange
        char y0 = '2';
        char y1 = '0';
        char y2 = '2';
        char y3 = '0';
        char M0 = '2';
        char M1 = '2';
        int d0 = 48;
        int d1 = 52;

        // Act
        Class<?> c = Reflector.forName("com.alibaba.fastjson.parser.JSONScanner");
        Method m = c.getDeclaredMethod("checkDate", Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("int"), Reflector.forName("int"));
        m.setAccessible(true);
        boolean retval = (Boolean)m.invoke(null, y0, y1, y2, y3, M0, M1, d0, d1);

        // Assert result
        Assert.assertEquals(false, retval);
    }
    @Test
    public void checkDate19() throws Throwable {
        // Arrange
        char y0 = '2';
        char y1 = '0';
        char y2 = '2';
        char y3 = '!';
        char M0 = '0';
        char M1 = '2';
        int d0 = 48;
        int d1 = 52;

        // Act
        Class<?> c = Reflector.forName("com.alibaba.fastjson.parser.JSONScanner");
        Method m = c.getDeclaredMethod("checkDate", Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("int"), Reflector.forName("int"));
        m.setAccessible(true);
        boolean retval = (Boolean)m.invoke(null, y0, y1, y2, y3, M0, M1, d0, d1);

        // Assert result
        Assert.assertEquals(false, retval);
    }
    @Test
    public void checkDate20() throws Throwable {
        // Arrange
        char y0 = '2';
        char y1 = '0';
        char y2 = '2';
        char y3 = 'a';
        char M0 = '0';
        char M1 = '2';
        int d0 = 48;
        int d1 = 52;

        // Act
        Class<?> c = Reflector.forName("com.alibaba.fastjson.parser.JSONScanner");
        Method m = c.getDeclaredMethod("checkDate", Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("int"), Reflector.forName("int"));
        m.setAccessible(true);
        boolean retval = (Boolean)m.invoke(null, y0, y1, y2, y3, M0, M1, d0, d1);

        // Assert result
        Assert.assertEquals(false, retval);
    }
    @Test
    public void checkDate21() throws Throwable {
        // Arrange
        char y0 = '2';
        char y1 = '0';
        char y2 = '!';
        char y3 = '0';
        char M0 = '0';
        char M1 = '2';
        int d0 = 48;
        int d1 = 52;

        // Act
        Class<?> c = Reflector.forName("com.alibaba.fastjson.parser.JSONScanner");
        Method m = c.getDeclaredMethod("checkDate", Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("int"), Reflector.forName("int"));
        m.setAccessible(true);
        boolean retval = (Boolean)m.invoke(null, y0, y1, y2, y3, M0, M1, d0, d1);

        // Assert result
        Assert.assertEquals(false, retval);
    }
    @Test
    public void checkDate22() throws Throwable {
        // Arrange
        char y0 = '2';
        char y1 = '0';
        char y2 = 'a';
        char y3 = '0';
        char M0 = '0';
        char M1 = '2';
        int d0 = 48;
        int d1 = 52;

        // Act
        Class<?> c = Reflector.forName("com.alibaba.fastjson.parser.JSONScanner");
        Method m = c.getDeclaredMethod("checkDate", Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("int"), Reflector.forName("int"));
        m.setAccessible(true);
        boolean retval = (Boolean)m.invoke(null, y0, y1, y2, y3, M0, M1, d0, d1);

        // Assert result
        Assert.assertEquals(false, retval);
    }
    @Test
    public void checkDate23() throws Throwable {
        // Arrange
        char y0 = '2';
        char y1 = '!';
        char y2 = '2';
        char y3 = '0';
        char M0 = '0';
        char M1 = '2';
        int d0 = 48;
        int d1 = 52;

        // Act
        Class<?> c = Reflector.forName("com.alibaba.fastjson.parser.JSONScanner");
        Method m = c.getDeclaredMethod("checkDate", Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("int"), Reflector.forName("int"));
        m.setAccessible(true);
        boolean retval = (Boolean)m.invoke(null, y0, y1, y2, y3, M0, M1, d0, d1);

        // Assert result
        Assert.assertEquals(false, retval);
    }
    @Test
    public void checkDate24() throws Throwable {
        // Arrange
        char y0 = '2';
        char y1 = 'a';
        char y2 = '2';
        char y3 = '0';
        char M0 = '0';
        char M1 = '2';
        int d0 = 48;
        int d1 = 52;

        // Act
        Class<?> c = Reflector.forName("com.alibaba.fastjson.parser.JSONScanner");
        Method m = c.getDeclaredMethod("checkDate", Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("int"), Reflector.forName("int"));
        m.setAccessible(true);
        boolean retval = (Boolean)m.invoke(null, y0, y1, y2, y3, M0, M1, d0, d1);

        // Assert result
        Assert.assertEquals(false, retval);
    }
    @Test
    public void checkDate25() throws Throwable {
        // Arrange
        char y0 = '!';
        char y1 = '0';
        char y2 = '2';
        char y3 = '0';
        char M0 = '0';
        char M1 = '2';
        int d0 = 48;
        int d1 = 52;

        // Act
        Class<?> c = Reflector.forName("com.alibaba.fastjson.parser.JSONScanner");
        Method m = c.getDeclaredMethod("checkDate", Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("int"), Reflector.forName("int"));
        m.setAccessible(true);
        boolean retval = (Boolean)m.invoke(null, y0, y1, y2, y3, M0, M1, d0, d1);

        // Assert result
        Assert.assertEquals(false, retval);
    }
    @Test
    public void checkDate26() throws Throwable {
        // Arrange
        char y0 = 'a';
        char y1 = '0';
        char y2 = '2';
        char y3 = '0';
        char M0 = '0';
        char M1 = '2';
        int d0 = 48;
        int d1 = 52;

        // Act
        Class<?> c = Reflector.forName("com.alibaba.fastjson.parser.JSONScanner");
        Method m = c.getDeclaredMethod("checkDate", Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("char"), Reflector.forName("int"), Reflector.forName("int"));
        m.setAccessible(true);
        boolean retval = (Boolean)m.invoke(null, y0, y1, y2, y3, M0, M1, d0, d1);

        // Assert result
        Assert.assertEquals(false, retval);
    }
}