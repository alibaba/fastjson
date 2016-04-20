package com.alibaba.fastjson.serializer;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;

import com.alibaba.fastjson.JSONException;

public class SerialWriterStringEncoder {

    private final CharsetEncoder encoder;

    public SerialWriterStringEncoder(Charset cs){
        this.encoder = cs.newEncoder().onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE);
    }

    public byte[] encode(char[] chars, int off, int len) {
        if (len == 0) {
            return new byte[0];
        }

        encoder.reset();

        // We need to perform double, not float, arithmetic; otherwise
        // we lose low order bits when len is larger than 2**24.
        int bytesLength = (int) (len * (double) encoder.maxBytesPerChar());
        byte[] bytes = getBytes(bytesLength);

        ByteBuffer byteBuf = ByteBuffer.wrap(bytes);

        CharBuffer charBuf = CharBuffer.wrap(chars, off, len);
        try {
            CoderResult cr = encoder.encode(charBuf, byteBuf, true);
            if (!cr.isUnderflow()) {
                cr.throwException();
            }
            cr = encoder.flush(byteBuf);
            if (!cr.isUnderflow()) {
                cr.throwException();
            }
        } catch (CharacterCodingException x) {
            // Substitution is always enabled,
            // so this shouldn't happen
            throw new JSONException(x.getMessage(), x);
        }

        int position = byteBuf.position();
        byte[] copy = new byte[position];
        System.arraycopy(bytes, 0, copy, 0, position);
        return copy;
    }

    private final static ThreadLocal<byte[]> bytesBufLocal = new ThreadLocal<byte[]>();

    public static byte[] getBytes(int length) {
        byte[] bytes = bytesBufLocal.get();

        if (bytes == null) {
            bytes = new byte[1024 * 8];
            bytesBufLocal.set(bytes);
        }
        
        return bytes.length < length //
            ? new byte[length] //
            : bytes;
    }

}
