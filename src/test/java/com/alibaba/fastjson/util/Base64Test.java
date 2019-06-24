package com.alibaba.fastjson.util;

import org.junit.Assert;
import org.junit.Test;

public class Base64Test {

  @Test
  public void decodeFastChar() {
    Assert.assertArrayEquals(
        new byte[] {},
        Base64.decodeFast(new char[0], 1, 0)
    );
    Assert.assertArrayEquals(
        new byte[] {126, -118, 1, 106},
        Base64.decodeFast(new char[] {'[', 'f', 'o', 'o', 'B', 'a', 'r', ']'}, 0, 8)
    );

    char[] chars = new char[] {
        'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a',
        'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a',
        'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a',
        'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a',
        'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a',
        'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a',
        'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a',
        'a', 'a', 'a', 'a', 'a', 'a', '\r', 'a', 'a', 'a'
    };

    byte[] expected = new byte[] {
        105, -90, -102, 105, -90, -102, 105, -90, -102, 105,
        -90, -102, 105, -90, -102, 105, -90, -102, 105, -90,
        -102, 105, -90, -102, 105, -90, -102, 105, -90, -102,
        105, -90, -102, 105, -90, -102, 105, -90, -102, 105,
        -90, -102, 105, -90, -102, 105, -90, -102, 105, -90,
        -102, 105, -90, -102, -1, -1, -1
    };

    Assert.assertArrayEquals(expected, Base64.decodeFast(chars, 1, 78));
  }

  @Test
  public void decodeFastString() {
    Assert.assertArrayEquals(
        new byte[] {},
        Base64.decodeFast("", 1, 0)
    );
    Assert.assertArrayEquals(
        new byte[] {126, -118, 1, 106},
        Base64.decodeFast("[fooBar]", 0, 8)
    );

    String input = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
        "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\raa";

    byte[] expected = new byte[] {
        105, -90, -102, 105, -90, -102, 105, -90, -102, 105,
        -90, -102, 105, -90, -102, 105, -90, -102, 105, -90,
        -102, 105, -90, -102, 105, -90, -102, 105, -90, -102,
        105, -90, -102, 105, -90, -102, 105, -90, -102, 105,
        -90, -102, 105, -90, -102, 105, -90, -102, 105, -90,
        -102, 105, -90, -102, -1, -1, -1
    };

    Assert.assertArrayEquals(expected, Base64.decodeFast(input, 1, 78));
  }

  @Test
  public void decodeFastString2() {
    Assert.assertArrayEquals(
        new byte[] {},
        Base64.decodeFast("")
    );
    Assert.assertArrayEquals(
        new byte[] {126, -118, 1, 106},
        Base64.decodeFast("[fooBar]")
    );

    String input = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
        "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\raa";

    byte[] expected = new byte[] {
        105, -90, -102, 105, -90, -102, 105, -90, -102, 105,
        -90, -102, 105, -90, -102, 105, -90, -102, 105, -90,
        -102, 105, -90, -102, 105, -90, -102, 105, -90, -102,
        105, -90, -102, 105, -90, -102, 105, -90, -102, 105,
        -90, -102, 105, -90, -102, 105, -90, -102, 105, -90,
        -102, 105, -90, -102, 105, -90, -102
    };

    Assert.assertArrayEquals(expected, Base64.decodeFast(input));
  }
}
