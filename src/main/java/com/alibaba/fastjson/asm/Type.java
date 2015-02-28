/***
 * ASM: a very small and fast Java bytecode manipulation framework
 * Copyright (c) 2000-2007 INRIA, France Telecom
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. Neither the name of the copyright holders nor the names of its
 *    contributors may be used to endorse or promote products derived from
 *    this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.alibaba.fastjson.asm;

/**
 * A Java type. This class can be used to make it easier to manipulate type and method descriptors.
 * 
 * @author Eric Bruneton
 * @author Chris Nokleberg
 */
public class Type {

    /**
     * The sort of the <tt>void</tt> type.
     */
    public static final int  VOID         = 0;

    /**
     * The sort of the <tt>boolean</tt> type.
     */
    public static final int  BOOLEAN      = 1;

    /**
     * The sort of the <tt>char</tt> type.
     */
    public static final int  CHAR         = 2;

    /**
     * The sort of the <tt>byte</tt> type.
     */
    public static final int  BYTE         = 3;

    /**
     * The sort of the <tt>short</tt> type.
     */
    public static final int  SHORT        = 4;

    /**
     * The sort of the <tt>int</tt> type.
     */
    public static final int  INT          = 5;

    /**
     * The sort of the <tt>float</tt> type.
     */
    public static final int  FLOAT        = 6;

    /**
     * The sort of the <tt>long</tt> type.
     */
    public static final int  LONG         = 7;

    /**
     * The sort of the <tt>double</tt> type.
     */
    public static final int  DOUBLE       = 8;

    /**
     * The sort of array reference types.
     */
    public static final int  ARRAY        = 9;

    /**
     * The sort of object reference type.
     */
    public static final int  OBJECT       = 10;

    /**
     * The <tt>void</tt> type.
     */
    public static final Type VOID_TYPE    = new Type(VOID, null, ('V' << 24) | (5 << 16) | (0 << 8) | 0, 1);

    /**
     * The <tt>boolean</tt> type.
     */
    public static final Type BOOLEAN_TYPE = new Type(BOOLEAN, null, ('Z' << 24) | (0 << 16) | (5 << 8) | 1, 1);

    /**
     * The <tt>char</tt> type.
     */
    public static final Type CHAR_TYPE    = new Type(CHAR, null, ('C' << 24) | (0 << 16) | (6 << 8) | 1, 1);

    /**
     * The <tt>byte</tt> type.
     */
    public static final Type BYTE_TYPE    = new Type(BYTE, null, ('B' << 24) | (0 << 16) | (5 << 8) | 1, 1);

    /**
     * The <tt>short</tt> type.
     */
    public static final Type SHORT_TYPE   = new Type(SHORT, null, ('S' << 24) | (0 << 16) | (7 << 8) | 1, 1);

    /**
     * The <tt>int</tt> type.
     */
    public static final Type INT_TYPE     = new Type(INT, null, ('I' << 24) | (0 << 16) | (0 << 8) | 1, 1);

    /**
     * The <tt>float</tt> type.
     */
    public static final Type FLOAT_TYPE   = new Type(FLOAT, null, ('F' << 24) | (2 << 16) | (2 << 8) | 1, 1);

    /**
     * The <tt>long</tt> type.
     */
    public static final Type LONG_TYPE    = new Type(LONG, null, ('J' << 24) | (1 << 16) | (1 << 8) | 2, 1);

    /**
     * The <tt>double</tt> type.
     */
    public static final Type DOUBLE_TYPE  = new Type(DOUBLE, null, ('D' << 24) | (3 << 16) | (3 << 8) | 2, 1);

    // ------------------------------------------------------------------------
    // Fields
    // ------------------------------------------------------------------------

    /**
     * The sort of this Java type.
     */
    private final int        sort;

    /**
     * A buffer containing the internal name of this Java type. This field is only used for reference types.
     */
    private final char[]     buf;

    /**
     * The offset of the internal name of this Java type in {@link #buf buf} or, for primitive types, the size,
     * descriptor and getOpcode offsets for this type (byte 0 contains the size, byte 1 the descriptor, byte 2 the
     * offset for IALOAD or IASTORE, byte 3 the offset for all other instructions).
     */
    private final int        off;

    /**
     * The length of the internal name of this Java type.
     */
    private final int        len;

    // ------------------------------------------------------------------------
    // Constructors
    // ------------------------------------------------------------------------

    /**
     * Constructs a reference type.
     * 
     * @param sort the sort of the reference type to be constructed.
     * @param buf a buffer containing the descriptor of the previous type.
     * @param off the offset of this descriptor in the previous buffer.
     * @param len the length of this descriptor.
     */
    private Type(final int sort, final char[] buf, final int off, final int len){
        this.sort = sort;
        this.buf = buf;
        this.off = off;
        this.len = len;
    }

    /**
     * Returns the Java type corresponding to the given type descriptor.
     * 
     * @param typeDescriptor a type descriptor.
     * @return the Java type corresponding to the given type descriptor.
     */
    public static Type getType(final String typeDescriptor) {
        return getType(typeDescriptor.toCharArray(), 0);
    }

    public static int getArgumentsAndReturnSizes(final String desc) {
        int n = 1;
        int c = 1;
        while (true) {
            char car = desc.charAt(c++);
            if (car == ')') {
                car = desc.charAt(c);
                return n << 2 | (car == 'V' ? 0 : (car == 'D' || car == 'J' ? 2 : 1));
            } else if (car == 'L') {
                while (desc.charAt(c++) != ';') {
                }
                n += 1;
//            } else if (car == '[') {
//                while ((car = desc.charAt(c)) == '[') {
//                    ++c;
//                }
//                if (car == 'D' || car == 'J') {
//                    n -= 1;
//                }
            } else if (car == 'D' || car == 'J') {
                n += 2;
            } else {
                n += 1;
            }
        }
    }

    /**
     * Returns the Java type corresponding to the given type descriptor.
     * 
     * @param buf a buffer containing a type descriptor.
     * @param off the offset of this descriptor in the previous buffer.
     * @return the Java type corresponding to the given type descriptor.
     */
    private static Type getType(final char[] buf, final int off) {
        int len;
        switch (buf[off]) {
            case 'V':
                return VOID_TYPE;
            case 'Z':
                return BOOLEAN_TYPE;
            case 'C':
                return CHAR_TYPE;
            case 'B':
                return BYTE_TYPE;
            case 'S':
                return SHORT_TYPE;
            case 'I':
                return INT_TYPE;
            case 'F':
                return FLOAT_TYPE;
            case 'J':
                return LONG_TYPE;
            case 'D':
                return DOUBLE_TYPE;
            case '[':
                len = 1;
                while (buf[off + len] == '[') {
                    ++len;
                }
                if (buf[off + len] == 'L') {
                    ++len;
                    while (buf[off + len] != ';') {
                        ++len;
                    }
                }
                return new Type(ARRAY, buf, off, len + 1);
                // case 'L':
            default:
                len = 1;
                while (buf[off + len] != ';') {
                    ++len;
                }
                return new Type(OBJECT, buf, off + 1, len - 1);
        }
    }

    // ------------------------------------------------------------------------
    // Accessors
    // ------------------------------------------------------------------------

    /**
     * Returns the sort of this Java type.
     * 
     * @return {@link #VOID VOID}, {@link #BOOLEAN BOOLEAN}, {@link #CHAR CHAR}, {@link #BYTE BYTE}, {@link #SHORT
     * SHORT}, {@link #INT INT}, {@link #FLOAT FLOAT}, {@link #LONG LONG}, {@link #DOUBLE DOUBLE}, {@link #ARRAY ARRAY}
     * or {@link #OBJECT OBJECT}.
     */
    public int getSort() {
        return sort;
    }

    /**
     * Returns the internal name of the class corresponding to this object or array type. The internal name of a class
     * is its fully qualified name (as returned by Class.getName(), where '.' are replaced by '/'. This method should
     * only be used for an object or array type.
     * 
     * @return the internal name of the class corresponding to this object type.
     */
    public String getInternalName() {
        return new String(buf, off, len);
    }

    // ------------------------------------------------------------------------
    // Conversion to type descriptors
    // ------------------------------------------------------------------------

    /**
     * Returns the descriptor corresponding to this Java type.
     * 
     * @return the descriptor corresponding to this Java type.
     */
    String getDescriptor() {
        return new String(this.buf, off, len);
    }
}
