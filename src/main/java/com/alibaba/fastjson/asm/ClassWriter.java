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
 * 
 * @author Eric Bruneton
 */
public class ClassWriter {
    /**
     * Minor and major version numbers of the class to be generated.
     */
    int                     version;

    /**
     * Index of the next item to be added in the constant pool.
     */
    int                     index;

    /**
     * The constant pool of this class.
     */
    final ByteVector        pool;

    /**
     * The constant pool's hash table data.
     */
    Item[]                  items;

    /**
     * The threshold of the constant pool's hash table.
     */
    int                     threshold;

    /**
     * A reusable key used to look for items in the {@link #items} hash table.
     */
    final Item              key;

    /**
     * A reusable key used to look for items in the {@link #items} hash table.
     */
    final Item              key2;

    /**
     * A reusable key used to look for items in the {@link #items} hash table.
     */
    final Item              key3;

    /**
     * A type table used to temporarily store internal names that will not necessarily be stored in the constant pool.
     * This type table is used by the control flow and data flow analysis algorithm used to compute stack map frames
     * from scratch. This array associates to each index <tt>i</tt> the Item whose index is <tt>i</tt>. All Item objects
     * stored in this array are also stored in the {@link #items} hash table. These two arrays allow to retrieve an Item
     * from its index or, conversely, to get the index of an Item from its value. Each Item stores an internal name in
     * its {@link Item#strVal1} field.
     */
    Item[]                  typeTable;

    /**
     * The access flags of this class.
     */
    private int             access;

    /**
     * The constant pool item that contains the internal name of this class.
     */
    private int             name;

    /**
     * The internal name of this class.
     */
    String                  thisName;

    /**
     * The constant pool item that contains the internal name of the super class of this class.
     */
    private int             superName;

    /**
     * Number of interfaces implemented or extended by this class or interface.
     */
    private int             interfaceCount;

    /**
     * The interfaces implemented or extended by this class or interface. More precisely, this array contains the
     * indexes of the constant pool items that contain the internal names of these interfaces.
     */
    private int[]           interfaces;

    /**
     * The fields of this class. These fields are stored in a linked list of {@link FieldWriter} objects, linked to each
     * other by their {@link FieldWriter#next} field. This field stores the first element of this list.
     */
    FieldWriter             firstField;

    /**
     * The fields of this class. These fields are stored in a linked list of {@link FieldWriter} objects, linked to each
     * other by their {@link FieldWriter#next} field. This field stores the last element of this list.
     */
    FieldWriter             lastField;

    /**
     * The methods of this class. These methods are stored in a linked list of {@link MethodWriter} objects, linked to
     * each other by their {@link MethodWriter#next} field. This field stores the first element of this list.
     */
    MethodWriter            firstMethod;

    /**
     * The methods of this class. These methods are stored in a linked list of {@link MethodWriter} objects, linked to
     * each other by their {@link MethodWriter#next} field. This field stores the last element of this list.
     */
    MethodWriter            lastMethod;

    // ------------------------------------------------------------------------
    // Constructor
    // ------------------------------------------------------------------------

    public ClassWriter(){
        this(0);
    }

    private ClassWriter(final int flags){
        index = 1;
        pool = new ByteVector();
        items = new Item[256];
        threshold = (int) (0.75d * items.length);
        key = new Item();
        key2 = new Item();
        key3 = new Item();
    }

    // ------------------------------------------------------------------------
    // Implementation of the ClassVisitor interface
    // ------------------------------------------------------------------------

    public void visit(final int version, final int access, final String name, final String superName, final String[] interfaces) {
        this.version = version;
        this.access = access;
        this.name = newClassItem(name).index;
        thisName = name;
        this.superName = superName == null ? 0 : newClassItem(superName).index;
        if (interfaces != null && interfaces.length > 0) {
            interfaceCount = interfaces.length;
            this.interfaces = new int[interfaceCount];
            for (int i = 0; i < interfaceCount; ++i) {
                this.interfaces[i] = newClassItem(interfaces[i]).index;
            }
        }
    }

    // ------------------------------------------------------------------------
    // Other public methods
    // ------------------------------------------------------------------------

    /**
     * Returns the bytecode of the class that was build with this class writer.
     * 
     * @return the bytecode of the class that was build with this class writer.
     */
    public byte[] toByteArray() {
        // computes the real size of the bytecode of this class
        int size = 24 + 2 * interfaceCount;
        int nbFields = 0;
        FieldWriter fb = firstField;
        while (fb != null) {
            ++nbFields;
            size += fb.getSize();
            fb = fb.next;
        }
        int nbMethods = 0;
        MethodWriter mb = firstMethod;
        while (mb != null) {
            ++nbMethods;
            size += mb.getSize();
            mb = mb.next;
        }
        int attributeCount = 0;
        size += pool.length;
        // allocates a byte vector of this size, in order to avoid unnecessary
        // arraycopy operations in the ByteVector.enlarge() method
        ByteVector out = new ByteVector(size);
        out.putInt(0xCAFEBABE).putInt(version);
        out.putShort(index).putByteArray(pool.data, 0, pool.length);
        int mask = 393216; // Opcodes.ACC_DEPRECATED | ClassWriter.ACC_SYNTHETIC_ATTRIBUTE | ((access & ClassWriter.ACC_SYNTHETIC_ATTRIBUTE) / (ClassWriter.ACC_SYNTHETIC_ATTRIBUTE / Opcodes.ACC_SYNTHETIC));
        out.putShort(access & ~mask).putShort(name).putShort(superName);
        out.putShort(interfaceCount);
        for (int i = 0; i < interfaceCount; ++i) {
            out.putShort(interfaces[i]);
        }
        out.putShort(nbFields);
        fb = firstField;
        while (fb != null) {
            fb.put(out);
            fb = fb.next;
        }
        out.putShort(nbMethods);
        mb = firstMethod;
        while (mb != null) {
            mb.put(out);
            mb = mb.next;
        }
        out.putShort(attributeCount);
        return out.data;
    }

    // ------------------------------------------------------------------------
    // Utility methods: constant pool management
    // ------------------------------------------------------------------------

    /**
     * Adds a number or string constant to the constant pool of the class being build. Does nothing if the constant pool
     * already contains a similar item.
     * 
     * @param cst the value of the constant to be added to the constant pool. This parameter must be an {@link Integer},
     * a {@link Float}, a {@link Long}, a {@link Double}, a {@link String} or a {@link Type}.
     * @return a new or already existing constant item with the given value.
     */
    Item newConstItem(final Object cst) {
        if (cst instanceof Integer) {
            int val = ((Integer) cst).intValue();
            // return newInteger(val);
            key.set(val);
            Item result = get(key);
            if (result == null) {
                pool.putByte(3 /* INT */ ).putInt(val);
                result = new Item(index++, key);
                put(result);
            }
            return result;
        } else if (cst instanceof String) {
            return newString((String) cst);
        } else if (cst instanceof Type) {
            Type t = (Type) cst;
            return newClassItem(t.sort == 10 /*Type.OBJECT*/ ? t.getInternalName() : t.getDescriptor());
        } else {
            throw new IllegalArgumentException("value " + cst);
        }
    }

    public int newUTF8(final String value) {
        key.set(1 /* UTF8 */, value, null, null);
        Item result = get(key);
        if (result == null) {
            pool.putByte(1 /* UTF8 */).putUTF8(value);
            result = new Item(index++, key);
            put(result);
        }
        return result.index;
    }

    public Item newClassItem(final String value) {
        key2.set(7 /* CLASS */, value, null, null);
        Item result = get(key2);
        if (result == null) {
            pool.put12(7 /* CLASS */, newUTF8(value));
            result = new Item(index++, key2);
            put(result);
        }
        return result;
    }

    /**
     * Adds a field reference to the constant pool of the class being build. Does nothing if the constant pool already
     * contains a similar item.
     * 
     * @param owner the internal name of the field's owner class.
     * @param name the field's name.
     * @param desc the field's descriptor.
     * @return a new or already existing field reference item.
     */
    Item newFieldItem(final String owner, final String name, final String desc) {
        key3.set(9 /* FIELD */, owner, name, desc);
        Item result = get(key3);
        if (result == null) {
            // put122(9 /* FIELD */, newClassItem(owner).index, newNameTypeItem(name, desc).index);
            int s1 = newClassItem(owner).index, s2 = newNameTypeItem(name, desc).index;
            pool.put12(9 /* FIELD */, s1).putShort(s2);
            result = new Item(index++, key3);
            put(result);
        }
        return result;
    }

    /**
     * Adds a method reference to the constant pool of the class being build. Does nothing if the constant pool already
     * contains a similar item.
     * 
     * @param owner the internal name of the method's owner class.
     * @param name the method's name.
     * @param desc the method's descriptor.
     * @param itf <tt>true</tt> if <tt>owner</tt> is an interface.
     * @return a new or already existing method reference item.
     */
    Item newMethodItem(final String owner, final String name, final String desc, final boolean itf) {
        int type = itf ? 11 /* IMETH */ : 10 /* METH */;
        key3.set(type, owner, name, desc);
        Item result = get(key3);
        if (result == null) {
            // put122(type, newClassItem(owner).index, newNameTypeItem(name, desc).index);
            int s1 = newClassItem(owner).index, s2 = newNameTypeItem(name, desc).index;
            pool.put12(type, s1).putShort(s2);
            result = new Item(index++, key3);
            put(result);
        }
        return result;
    }

    /**
     * Adds a string to the constant pool of the class being build. Does nothing if the constant pool already contains a
     * similar item.
     * 
     * @param value the String value.
     * @return a new or already existing string item.
     */
    private Item newString(final String value) {
        key2.set(8 /* STR */, value, null, null);
        Item result = get(key2);
        if (result == null) {
            pool.put12(8 /*STR*/, newUTF8(value));
            result = new Item(index++, key2);
            put(result);
        }
        return result;
    }

    /**
     * Adds a name and type to the constant pool of the class being build. Does nothing if the constant pool already
     * contains a similar item.
     * 
     * @param name a name.
     * @param desc a type descriptor.
     * @return a new or already existing name and type item.
     */
    public Item newNameTypeItem(final String name, final String desc) {
        key2.set(12 /* NAME_TYPE */, name, desc, null);
        Item result = get(key2);
        if (result == null) {
            //put122(12 /* NAME_TYPE */, newUTF8(name), newUTF8(desc));
            int s1 = newUTF8(name), s2 = newUTF8(desc);
            pool.put12(12 /* NAME_TYPE */, s1).putShort(s2);
            result = new Item(index++, key2);
            put(result);
        }
        return result;
    }

    /**
     * Returns the constant pool's hash table item which is equal to the given item.
     * 
     * @param key a constant pool item.
     * @return the constant pool's hash table item which is equal to the given item, or <tt>null</tt> if there is no
     * such item.
     */
    private Item get(final Item key) {
        Item i = items[key.hashCode % items.length];
        while (i != null && (i.type != key.type || !key.isEqualTo(i))) {
            i = i.next;
        }
        return i;
    }

    /**
     * Puts the given item in the constant pool's hash table. The hash table <i>must</i> not already contains this item.
     * 
     * @param i the item to be added to the constant pool's hash table.
     */
    private void put(final Item i) {
        if (index > threshold) {
            int ll = items.length;
            int nl = ll * 2 + 1;
            Item[] newItems = new Item[nl];
            for (int l = ll - 1; l >= 0; --l) {
                Item j = items[l];
                while (j != null) {
                    int index = j.hashCode % newItems.length;
                    Item k = j.next;
                    j.next = newItems[index];
                    newItems[index] = j;
                    j = k;
                }
            }
            items = newItems;
            threshold = (int) (nl * 0.75);
        }
        int index = i.hashCode % items.length;
        i.next = items[index];
        items[index] = i;
    }

   
//    private void put122(final int b, final int s1, final int s2) {
//        pool.put12(b, s1).putShort(s2);
//    }
}
