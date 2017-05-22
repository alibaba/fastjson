/*
 * Copyright 1999-2017 Alibaba Group.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.json.test;

/**
 * <pre>
 * 当有比较多的字符不能在某个串中出现， 通过一个查表算法确定。比如识别
 * ［a,b,c,d］不能在一个string中出现。
 * 由于消耗比较多的内存， 最好使用单一实例。 初始化过程并非线程安全。 最
 * 好一次完成初始化的过程。
 * 使用方法：
 * DetectProhibitChar p2 = new DetectProhibitChar();
 * p2.addProhibitChar(&quot;我们是中国人＊＊＃W￥％＆＊（￥％
 * ＆＊AAAAAAAAAAAAAAAAAAAAAAA&quot;);
 * for (int i = 0; i &lt; 65536; i++) {
 * if (p2.isProhibitChar((char) i)) {
 * System.out.print((char) i);
 * }
 * }
 * </pre>
 * 
 * @author sdh5724
 */
public class DetectProhibitChar {

    byte[] masks = new byte[1024 * 8];

    public DetectProhibitChar(){

    }

    public DetectProhibitChar(char prohibits[]){
        addProhibitChar(prohibits);
    }

    /**
     * 增加一个跳越字符
     * 
     * @param c
     */
    public void addProhibitChar(char c) {
        int pos = c >> 3;
        masks[pos] = (byte) ((masks[pos] & 0xFF) | (1 << (c % 8)));
    }

    /**
     * 增加一个string里的所有字符
     * 
     * @param str
     */
    public void addProhibitChar(String str) {
        if (str != null) {
            char cs[] = str.toCharArray();
            for (char c : cs) {
                addProhibitChar(c);
            }
        }
    }

    public void addProhibitChar(char prohibits[]) {
        if (prohibits != null) {
            for (char c : prohibits) {
                addProhibitChar(c);
            }
        }
    }

    public void removeProhibitChar(char c) {
        int pos = c >> 3;
        masks[pos] = (byte) ((masks[pos] & 0xFF) & (~(1 << (c % 8))));
    }

    public boolean isProhibitChar(char c) {
        int pos = c >> 3;
        int i = (masks[pos] & 0xFF) & (1 << (c % 8));
        return (i != 0);
    }

    public boolean hasProhibitChar(char cs[]) {
        if (cs != null) {
            for (char c : cs) {
                if (isProhibitChar(c)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean hasProhibitChar(String str) {
        if (str != null) {
            return hasProhibitChar(str.toCharArray());
        }
        return false;
    }

}
