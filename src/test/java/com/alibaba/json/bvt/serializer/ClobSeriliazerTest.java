package com.alibaba.json.bvt.serializer;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.sql.Clob;
import java.sql.SQLException;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class ClobSeriliazerTest extends TestCase {
	public void test_clob() throws Exception {
		Assert.assertEquals("\"abcdefg中国\"",
				JSON.toJSONString(new MockClob("abcdefg中国")));
	}

	public void test_clob_null() throws Exception {
		Assert.assertEquals("{\"value\":null}", JSON.toJSONString(new VO(),
				SerializerFeature.WriteMapNullValue));
	}

	public void test_clob_error() throws Exception {
		Exception error = null;
		try {
			JSON.toJSONString(new MockClob(new SQLException()));
		} catch (Exception ex) {
			error = ex;
		}
		Assert.assertNotNull(error);
	}

	@SuppressWarnings("unused")
	private static class VO {
		private Clob value;

		public Clob getValue() {
			return value;
		}

		public void setValue(Clob value) {
			this.value = value;
		}
	}

	public static class MockClob implements Clob {
		private final String text;

		private SQLException error;

		public MockClob(String text) {
			this.text = text;
		}

		public MockClob(SQLException error) {
			this.text = null;
			this.error = error;
		}

		public SQLException getError() {
			return error;
		}

		public void setError(SQLException error) {
			this.error = error;
		}

		public long length() throws SQLException {
			// TODO Auto-generated method stub
			return 0;
		}

		public String getSubString(long pos, int length) throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}

		public Reader getCharacterStream() throws SQLException {
			if (error != null) {
				throw error;
			}
			return new StringReader(text);
		}

		public InputStream getAsciiStream() throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}

		public long position(String searchstr, long start) throws SQLException {
			// TODO Auto-generated method stub
			return 0;
		}

		public long position(Clob searchstr, long start) throws SQLException {
			// TODO Auto-generated method stub
			return 0;
		}

		public int setString(long pos, String str) throws SQLException {
			// TODO Auto-generated method stub
			return 0;
		}

		public int setString(long pos, String str, int offset, int len)
				throws SQLException {
			// TODO Auto-generated method stub
			return 0;
		}

		public OutputStream setAsciiStream(long pos) throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}

		public Writer setCharacterStream(long pos) throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}

		public void truncate(long len) throws SQLException {
			// TODO Auto-generated method stub

		}

		public void free() throws SQLException {
			// TODO Auto-generated method stub

		}

		public Reader getCharacterStream(long pos, long length)
				throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}

	}
}
