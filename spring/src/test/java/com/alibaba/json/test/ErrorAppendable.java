package com.alibaba.json.test;

import java.io.IOException;

public final class ErrorAppendable implements Appendable {

    public Appendable append(CharSequence csq, int start, int end) throws IOException {
        throw new IOException("");
    }

    public Appendable append(char c) throws IOException {
        throw new IOException("");
    }

    public Appendable append(CharSequence csq) throws IOException {
        throw new IOException("");
    }
}
