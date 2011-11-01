package com.alibaba.json.bvt.bug;

import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

import com.alibaba.json.bvt.bug.Bug_for_Johnny.EnumType;

public class Bug_for_Johnny1 extends TestCase {

    public static class MyObject {

        private String              stringType;
        private byte                byteType;
        private short               shortType;
        private int                 intType;
        private long                longType;
        private char                charType;
        private float               floatType;
        private double              doubleType;
        private boolean             boolType;
        private List<String>        ListType;
        private Map<String, String> mapType;
        private Set<String>         setType;
        private EnumType            enumType;

        // private Map<String, MyObject> map2;

        public Set<String> getSetType() {
            return setType;
        }

        public void setSetType(Set<String> setType) {
            this.setType = setType;
        }

        /**
         * @return the stringType
         */
        public String getStringType() {
            return stringType;
        }

        public EnumType getEnumType() {
            return enumType;
        }

        public void setEnumType(EnumType enumType) {
            this.enumType = enumType;
        }

        public List<String> getListType() {
            return ListType;
        }

        public void setListType(List<String> listType) {
            ListType = listType;
        }

        public Map<String, String> getMapType() {
            return mapType;
        }

        public void setMapType(Map<String, String> mapType) {
            this.mapType = mapType;
        }

        /**
         * @param stringType the stringType to set
         */
        public void setStringType(String stringType) {
            this.stringType = stringType;
        }

        /**
         * @return the byteType
         */
        public byte getByteType() {
            return byteType;
        }

        /**
         * @param byteType the byteType to set
         */
        public void setByteType(byte byteType) {
            this.byteType = byteType;
        }

        /**
         * @return the shortType
         */
        public short getShortType() {
            return shortType;
        }

        /**
         * @param shortType the shortType to set
         */
        public void setShortType(short shortType) {
            this.shortType = shortType;
        }

        /**
         * @return the intType
         */
        public int getIntType() {
            return intType;
        }

        /**
         * @param intType the intType to set
         */
        public void setIntType(int intType) {
            this.intType = intType;
        }

        /**
         * @return the longType
         */
        public long getLongType() {
            return longType;
        }

        /**
         * @param longType the longType to set
         */
        public void setLongType(long longType) {
            this.longType = longType;
        }

        /**
         * @return the charType
         */
        public char getCharType() {
            return charType;
        }

        /**
         * @param charType the charType to set
         */
        public void setCharType(char charType) {
            this.charType = charType;
        }

        /**
         * @return the floatType
         */
        public float getFloatType() {
            return floatType;
        }

        /**
         * @param floatType the floatType to set
         */
        public void setFloatType(float floatType) {
            this.floatType = floatType;
        }

        /**
         * @return the doubleType
         */
        public double getDoubleType() {
            return doubleType;
        }

        /**
         * @param doubleType the doubleType to set
         */
        public void setDoubleType(double doubleType) {
            this.doubleType = doubleType;
        }

        /**
         * @return the boolType
         */
        public boolean isBoolType() {
            return boolType;
        }

        /**
         * @param boolType the boolType to set
         */
        public void setBoolType(boolean boolType) {
            this.boolType = boolType;
        }

        // public Map<String, MyObject> getMap2() {
        // return map2;
        // }
        //
        // public void setMap2(Map<String, MyObject> map2) {
        // this.map2 = map2;
        // }

        /**
         * Constructs a <code>GroupEntity</code> <br>
         */
        public MyObject(){
        }
    }

}
