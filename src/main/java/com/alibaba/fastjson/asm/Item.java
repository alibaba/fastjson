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
 * A constant pool item. Constant pool items can be created with the 'newXXX' methods in the {@link ClassWriter} class.
 * 
 * @author Eric Bruneton
 */
final class Item {

    /**
     * Index of this item in the constant pool.
     */
    int    index;

    int    type;

    /**
     * Value of this item, for an integer item.
     */
    int    intVal;

    /**
     * Value of this item, for a long item.
     */
    long   longVal;

    /**
     * First part of the value of this item, for items that do not hold a primitive value.
     */
    String strVal1;

    /**
     * Second part of the value of this item, for items that do not hold a primitive value.
     */
    String strVal2;

    /**
     * Third part of the value of this item, for items that do not hold a primitive value.
     */
    String strVal3;

    /**
     * The hash code value of this constant pool item.
     */
    int    hashCode;

    /**
     * Link to another constant pool item, used for collision lists in the constant pool's hash table.
     */
    Item   next;

    /**
     * Constructs an uninitialized {@link Item}.
     */
    Item(){
    }

    /**
     * Constructs a copy of the given item.
     * 
     * @param index index of the item to be constructed.
     * @param i the item that must be copied into the item to be constructed.
     */
    Item(final int index, final Item i){
        this.index = index;
        type = i.type;
        intVal = i.intVal;
        longVal = i.longVal;
        strVal1 = i.strVal1;
        strVal2 = i.strVal2;
        strVal3 = i.strVal3;
        hashCode = i.hashCode;
    }

    /**
     * Sets this item to an item that do not hold a primitive value.
     * 
     * @param type the type of this item.
     * @param strVal1 first part of the value of this item.
     * @param strVal2 second part of the value of this item.
     * @param strVal3 third part of the value of this item.
     */
    void set(final int type, final String strVal1, final String strVal2, final String strVal3) {
        this.type = type;
        this.strVal1 = strVal1;
        this.strVal2 = strVal2;
        this.strVal3 = strVal3;
        switch (type) {
            case 1 /* ClassWriter.UTF8 */:
            case 8 /* ClassWriter.STR */:
            case 7 /* ClassWriter.CLASS */:
            case 13 /* ClassWriter.TYPE_NORMAL */:
                hashCode = 0x7FFFFFFF & (type + strVal1.hashCode());
                return;
            case 12 /* ClassWriter.NAME_TYPE */:
                hashCode = 0x7FFFFFFF & (type + strVal1.hashCode() * strVal2.hashCode());
                return;
                // ClassWriter.FIELD:
                // ClassWriter.METH:
                // ClassWriter.IMETH:
            default:
                hashCode = 0x7FFFFFFF & (type + strVal1.hashCode() * strVal2.hashCode() * strVal3.hashCode());
        }
    }
    
    /**
     * Sets this item to an integer item.
     *
     * @param intVal the value of this item.
     */
    void set(final int intVal) {
        this.type = 3 /* ClassWriter.INT */;
        this.intVal = intVal;
        this.hashCode = 0x7FFFFFFF & (type + intVal);
    }

    /**
     * Indicates if the given item is equal to this one. <i>This method assumes that the two items have the same
     * {@link #type}</i>.
     * 
     * @param i the item to be compared to this one. Both items must have the same {@link #type}.
     * @return <tt>true</tt> if the given item if equal to this one, <tt>false</tt> otherwise.
     */
    boolean isEqualTo(final Item i) {
        switch (type) {
            case 1 /* ClassWriter.UTF8 */:
            case 8 /* ClassWriter.STR */:
            case 7 /* ClassWriter.CLASS */ :
            case 13 /* ClassWriter.TYPE_NORMAL */ :
                return i.strVal1.equals(strVal1);
            case 15 /* ClassWriter.TYPE_MERGED */ :
            case 5 /* ClassWriter.LONG */ :
            case 6 /* ClassWriter.DOUBLE */:
                return i.longVal == longVal;
            case 3 /* ClassWriter.INT */ :
            case 4 /* ClassWriter.FLOAT */:
                return i.intVal == intVal;
            case 12 /* ClassWriter.NAME_TYPE */:
                return i.strVal1.equals(strVal1) && i.strVal2.equals(strVal2);
                // case ClassWriter.FIELD:
                // case ClassWriter.METH:
                // case ClassWriter.IMETH:
            default:
                return i.strVal1.equals(strVal1) && i.strVal2.equals(strVal2) && i.strVal3.equals(strVal3);
        }
    }

}
