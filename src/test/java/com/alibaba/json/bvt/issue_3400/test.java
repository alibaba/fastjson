package com.alibaba.json.bvt.issue_3400;

import static com.sun.org.apache.xml.internal.utils.XMLCharacterRecognizer.isWhiteSpace;
import static org.mockito.internal.progress.SequenceNumber.next;

/**
 * Description:  <br>
 *
 * @author byw
 * @create 2020/9/20
 */
public class test {
    private char ch;
    public void main(String[] args) {

        skipWhiteSpace();
        aaa aaa = new aaa();
        aaa.skipWhiteSpace();
    }

    void skipWhiteSpace(){

    }

    static class aaa{

        private char ch;
        final void skipWhiteSpace() {
            if (ch > '\r') {
                return;
            }

            while (isWhiteSpace(ch)) {
                next();
            }
        }
    }
}
