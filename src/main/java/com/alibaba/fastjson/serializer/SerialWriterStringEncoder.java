package com.alibaba.fastjson.serializer;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.util.ThreadLocalCache;

public class SerialWriterStringEncoder {

	private final CharsetEncoder encoder;

	public SerialWriterStringEncoder(Charset cs) {
		this(cs.newEncoder().onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE));
	}
	
	public SerialWriterStringEncoder(CharsetEncoder encoder) {
	    this.encoder = encoder;
	}

	public byte[] encode(char[] chars, int off, int len) {
		if (len == 0) {
			return new byte[0];
		}

		encoder.reset();

		int bytesLength = scale(len, encoder.maxBytesPerChar());

		byte[] bytes = ThreadLocalCache.getBytes(bytesLength);

		return encode(chars, off, len, bytes);
	}

	public CharsetEncoder getEncoder() {
		return encoder;
	}

	public byte[] encode(char[] chars, int off, int len, byte[] bytes) {
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

		int bytesLength = byteBuf.position();
		byte[] copy = new byte[bytesLength];
		System.arraycopy(bytes, 0, copy, 0, bytesLength);
		return copy;
	}

	private static int scale(int len, float expansionFactor) {
		// We need to perform double, not float, arithmetic; otherwise
		// we lose low order bits when len is larger than 2**24.
		return (int) (len * (double) expansionFactor);
	}

}
