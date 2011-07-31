package com.alibaba.json.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

import com.alibaba.fastjson.parser.SymbolTable;

public class SymbolTableDupTest extends TestCase {

    private HashMap<Integer, Integer>      map          = new HashMap<Integer, Integer>();
    private Set<Integer>                   dupHashCodes = new HashSet<Integer>();
    private HashMap<Integer, List<String>> dupList      = new HashMap<Integer, List<String>>();

    private final int                      VALUE        = 114788;

    public void test_0() throws Exception {
        int len = 3;
        char[] chars = new char[len];
        tryBit(chars, len);
        tryBit2(chars, len);
        // tryBit3(chars, len);
        // for (Map.Entry<Integer, List<String>> entry : dupList.entrySet()) {
        // System.out.println(entry.getKey() + " : " + entry.getValue());
        // }

    }

    private void tryBit(char[] chars, int i) {
        char startChar = 'A';
        char endChar = 'z';

        for (char j = startChar; j <= endChar; j++) {
            chars[i - 1] = j;

            if (i > 1) {
                tryBit(chars, i - 1);
            } else {
                test(chars);
            }
        }
    }

    private void tryBit2(char[] chars, int i) {
        char startChar = 'A';
        char endChar = 'z';

        for (char j = startChar; j <= endChar; j++) {
            chars[i - 1] = j;

            if (i > 1) {
                tryBit2(chars, i - 1);
            } else {
                test2(chars);
            }
        }
    }

    private void tryBit3(char[] chars, int i) {
        char startChar = 'A';
        char endChar = 'z';

        for (char j = startChar; j <= endChar; j++) {
            chars[i - 1] = j;

            if (i > 1) {
                tryBit3(chars, i - 1);
            } else {
                test3(chars);
            }
        }
    }

    private void test3(char[] chars) {
        int hash = SymbolTable.hash(chars, 0, chars.length);
        if (hash == VALUE) {
            System.out.println(new String(chars));
        }
    }

    private void test2(char[] chars) {
        int hash = SymbolTable.hash(chars, 0, chars.length);
        if (dupHashCodes.contains(hash)) {
            List<String> list = dupList.get(hash);
            if (list == null) {
                list = new ArrayList<String>();
                dupList.put(hash, list);
            }
            list.add(new String(chars));
        }
    }

    private void test(char[] chars) {
        int hash = SymbolTable.hash(chars, 0, chars.length);
        Integer count = map.get(hash);
        if (count != null) {
            dupHashCodes.add(hash);
            map.put(hash, count.intValue() + 1);
        } else {
            map.put(hash, 1);
        }
    }

}
