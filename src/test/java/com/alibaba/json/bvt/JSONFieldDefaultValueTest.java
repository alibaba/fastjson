package com.alibaba.json.bvt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import junit.framework.TestCase;

public class JSONFieldDefaultValueTest extends TestCase {
	public void test_default_value() throws Exception {
		Model m = new Model();
		String s = JSON.toJSONString(m);
		System.out.println(s);
		Model m2 = JSON.parseObject(s, Model.class);
		assertEquals("string", m2.getString());
		assertEquals(false, m2.getaBoolean());
		assertEquals(true, m2.getaBoolean2().booleanValue());
		assertEquals(0, m2.getAnInt());
		assertEquals(888, m2.getInteger().intValue());
		assertEquals(0, m2.getaShort());
		assertEquals(88, m2.getaShort2().shortValue());
		assertEquals('\u0000', m2.getaChar());
		assertEquals('J', m2.getCharacter().charValue());
		assertEquals(0, m2.getaByte());
		assertEquals(8, m2.getaByte2().byteValue());
		assertEquals(0, m2.getaLong());
		assertEquals(8888, m2.getaLong2().longValue());
		assertEquals("0.0", "" + m2.getaFloat());
		assertEquals("8.8", "" + m2.getaFloat2());
		assertEquals("0.0", "" + m2.getaDouble());
		assertEquals("88.88", "" + m2.getaDouble2());
	}

	public void test_not_null() throws Exception {
		Model m = new Model("test", true, 888, (short)88, 'J', (byte)8, 8888L, 8.8F, 88.88, false, 999, (short)99, 'C', (byte)9, 9999L, 9.9F, 99.99);
		String s = JSON.toJSONString(m);
		System.out.println(s);
		Model m2 = JSON.parseObject(s, Model.class);
		assertEquals("test", m2.getString());
		assertEquals(true, m2.getaBoolean());
		assertEquals(false, m2.getaBoolean2().booleanValue());
		assertEquals(888, m2.getAnInt());
		assertEquals(999, m2.getInteger().intValue());
		assertEquals(88, m2.getaShort());
		assertEquals(99, m2.getaShort2().shortValue());
		assertEquals('J', m2.getaChar());
		assertEquals('C', m2.getCharacter().charValue());
		assertEquals(8, m2.getaByte());
		assertEquals(9, m2.getaByte2().byteValue());
		assertEquals(8888, m2.getaLong());
		assertEquals(9999, m2.getaLong2().longValue());
		assertEquals("8.8", "" + m2.getaFloat());
		assertEquals("9.9", "" + m2.getaFloat2());
		assertEquals("88.88", "" + m2.getaDouble());
		assertEquals("99.99", "" + m2.getaDouble2());
	}

	public static class Model {
		@JSONField(defaultValue = "string")
		private String string;

		@JSONField(defaultValue = "true") //shouldn't work
		private boolean aBoolean;
		@JSONField(defaultValue = "888") //shouldn't work
		private int anInt;
		@JSONField(defaultValue = "88") //shouldn't work
		private short aShort;
		@JSONField(defaultValue = "J") //shouldn't work
		private char aChar;
		@JSONField(defaultValue = "8") //shouldn't work
		private byte aByte;
		@JSONField(defaultValue = "8888") //shouldn't work
		private long aLong;
		@JSONField(defaultValue = "8.8") //shouldn't work
		private float aFloat;
		@JSONField(defaultValue = "88.88") //shouldn't work
		private double aDouble;

		@JSONField(defaultValue = "true")
		private Boolean aBoolean2;
		@JSONField(defaultValue = "888")
		private Integer integer;
		@JSONField(defaultValue = "88")
		private Short aShort2;
		@JSONField(defaultValue = "J")
		private Character character;
		@JSONField(defaultValue = "8")
		private Byte aByte2;
		@JSONField(defaultValue = "8888")
		private Long aLong2;
		@JSONField(defaultValue = "8.8")
		private Float aFloat2;
		@JSONField(defaultValue = "88.88")
		private Double aDouble2;

		public Model(String string, boolean aBoolean, int anInt, short aShort, char aChar,
					 byte aByte, long aLong, float aFloat, double aDouble,
					 Boolean aBoolean2, Integer integer, Short aShort2, Character character,
					 Byte aByte2, Long aLong2, Float aFloat2, Double aDouble2) {
			this.string = string;
			this.aBoolean = aBoolean;
			this.anInt = anInt;
			this.aShort = aShort;
			this.aChar = aChar;
			this.aByte = aByte;
			this.aLong = aLong;
			this.aFloat = aFloat;
			this.aDouble = aDouble;
			this.aBoolean2 = aBoolean2;
			this.integer = integer;
			this.aShort2 = aShort2;
			this.character = character;
			this.aByte2 = aByte2;
			this.aLong2 = aLong2;
			this.aFloat2 = aFloat2;
			this.aDouble2 = aDouble2;
		}

		public Model() {
		}

		public String getString() {
			return string;
		}

		public void setString(String string) {
			this.string = string;
		}

		public boolean getaBoolean() {
			return aBoolean;
		}

		public void setaBoolean(boolean aBoolean) {
			this.aBoolean = aBoolean;
		}

		public int getAnInt() {
			return anInt;
		}

		public void setAnInt(int anInt) {
			this.anInt = anInt;
		}

		public short getaShort() {
			return aShort;
		}

		public void setaShort(short aShort) {
			this.aShort = aShort;
		}

		public char getaChar() {
			return aChar;
		}

		public void setaChar(char aChar) {
			this.aChar = aChar;
		}

		public byte getaByte() {
			return aByte;
		}

		public void setaByte(byte aByte) {
			this.aByte = aByte;
		}

		public long getaLong() {
			return aLong;
		}

		public void setaLong(long aLong) {
			this.aLong = aLong;
		}

		public float getaFloat() {
			return aFloat;
		}

		public void setaFloat(float aFloat) {
			this.aFloat = aFloat;
		}

		public double getaDouble() {
			return aDouble;
		}

		public void setaDouble(double aDouble) {
			this.aDouble = aDouble;
		}

		public Boolean getaBoolean2() {
			return aBoolean2;
		}

		public void setaBoolean2(Boolean aBoolean2) {
			this.aBoolean2 = aBoolean2;
		}

		public Integer getInteger() {
			return integer;
		}

		public void setInteger(Integer integer) {
			this.integer = integer;
		}

		public Short getaShort2() {
			return aShort2;
		}

		public void setaShort2(Short aShort2) {
			this.aShort2 = aShort2;
		}

		public Character getCharacter() {
			return character;
		}

		public void setCharacter(Character character) {
			this.character = character;
		}

		public Byte getaByte2() {
			return aByte2;
		}

		public void setaByte2(Byte aByte2) {
			this.aByte2 = aByte2;
		}

		public Long getaLong2() {
			return aLong2;
		}

		public void setaLong2(Long aLong2) {
			this.aLong2 = aLong2;
		}

		public Float getaFloat2() {
			return aFloat2;
		}

		public void setaFloat2(Float aFloat2) {
			this.aFloat2 = aFloat2;
		}

		public Double getaDouble2() {
			return aDouble2;
		}

		public void setaDouble2(Double aDouble2) {
			this.aDouble2 = aDouble2;
		}

	}
}
