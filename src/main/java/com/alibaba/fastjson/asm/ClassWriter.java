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
     * Flag to automatically compute the maximum stack size and the maximum number of local variables of methods. If
     * this flag is set, then the arguments of the {@link MethodVisitor#visitMaxs visitMaxs} method of the
     * {@link MethodVisitor} returned by the {@link #visitMethod visitMethod} method will be ignored, and computed
     * automatically from the signature and the bytecode of each method.
     * 
     * @see #ClassWriter(int)
     */
    public static final int COMPUTE_MAXS            = 1;

    /**
     * Flag to automatically compute the stack map frames of methods from scratch. If this flag is set, then the calls
     * to the MethodVisitor#visitFrame method are ignored, and the stack map frames are recomputed from the
     * methods bytecode. The arguments of the MethodVisitor#visitMaxs method are also ignored and
     * recomputed from the bytecode. In other words, computeFrames implies computeMaxs.
     * 
     * @see #ClassWriter(int)
     */
    public static final int COMPUTE_FRAMES          = 2;

    /**
     * Pseudo access flag to distinguish between the synthetic attribute and the synthetic access flag.
     */
    static final int        ACC_SYNTHETIC_ATTRIBUTE = 0x40000;

    /**
     * The type of instructions without any argument.
     */
    static final int        NOARG_INSN              = 0;

    /**
     * The type of instructions with an signed byte argument.
     */
    static final int        SBYTE_INSN              = 1;

    /**
     * The type of instructions with an signed short argument.
     */
    static final int        SHORT_INSN              = 2;

    /**
     * The type of instructions with a local variable index argument.
     */
    static final int        VAR_INSN                = 3;

    /**
     * The type of instructions with an implicit local variable index argument.
     */
    static final int        IMPLVAR_INSN            = 4;

    /**
     * The type of instructions with a type descriptor argument.
     */
    static final int        TYPE_INSN               = 5;

    /**
     * The type of field and method invocations instructions.
     */
    static final int        FIELDORMETH_INSN        = 6;

    /**
     * The type of the INVOKEINTERFACE/INVOKEDYNAMIC instruction.
     */
    static final int        ITFDYNMETH_INSN         = 7;

    /**
     * The type of instructions with a 2 bytes bytecode offset label.
     */
    static final int        LABEL_INSN              = 8;

    /**
     * The type of instructions with a 4 bytes bytecode offset label.
     */
    static final int        LABELW_INSN             = 9;

    /**
     * The type of the LDC instruction.
     */
    static final int        LDC_INSN                = 10;

    /**
     * The type of the LDC_W and LDC2_W instructions.
     */
    static final int        LDCW_INSN               = 11;

    /**
     * The type of the IINC instruction.
     */
    static final int        IINC_INSN               = 12;

    /**
     * The type of the TABLESWITCH instruction.
     */
    static final int        TABL_INSN               = 13;

    /**
     * The type of the LOOKUPSWITCH instruction.
     */
    static final int        LOOK_INSN               = 14;

    /**
     * The type of the MULTIANEWARRAY instruction.
     */
    static final int        MANA_INSN               = 15;

    /**
     * The type of the WIDE instruction.
     */
    static final int        WIDE_INSN               = 16;

    /**
     * The instruction types of all JVM opcodes.
     */
    static final byte[]     TYPE;

    /**
     * The type of CONSTANT_Class constant pool items.
     */
    static final int        CLASS                   = 7;

    /**
     * The type of CONSTANT_Fieldref constant pool items.
     */
    static final int        FIELD                   = 9;

    /**
     * The type of CONSTANT_Methodref constant pool items.
     */
    static final int        METH                    = 10;

    /**
     * The type of CONSTANT_InterfaceMethodref constant pool items.
     */
    static final int        IMETH                   = 11;

    /**
     * The type of CONSTANT_String constant pool items.
     */
    static final int        STR                     = 8;

    /**
     * The type of CONSTANT_Integer constant pool items.
     */
    static final int        INT                     = 3;

    /**
     * The type of CONSTANT_Float constant pool items.
     */
    static final int        FLOAT                   = 4;

    /**
     * The type of CONSTANT_Long constant pool items.
     */
    static final int        LONG                    = 5;

    /**
     * The type of CONSTANT_Double constant pool items.
     */
    static final int        DOUBLE                  = 6;

    /**
     * The type of CONSTANT_NameAndType constant pool items.
     */
    static final int        NAME_TYPE               = 12;

    /**
     * The type of CONSTANT_Utf8 constant pool items.
     */
    static final int        UTF8                    = 1;

    /**
     * Normal type Item stored in the ClassWriter {@link ClassWriter#typeTable}, instead of the constant pool, in order
     * to avoid clashes with normal constant pool items in the ClassWriter constant pool's hash table.
     */
    static final int        TYPE_NORMAL             = 13;

    /**
     * Uninitialized type Item stored in the ClassWriter {@link ClassWriter#typeTable}, instead of the constant pool, in
     * order to avoid clashes with normal constant pool items in the ClassWriter constant pool's hash table.
     */
    static final int        TYPE_UNINIT             = 14;

    /**
     * Merged type Item stored in the ClassWriter {@link ClassWriter#typeTable}, instead of the constant pool, in order
     * to avoid clashes with normal constant pool items in the ClassWriter constant pool's hash table.
     */
    static final int        TYPE_MERGED             = 15;

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
    // Static initializer
    // ------------------------------------------------------------------------

    /**
     * Computes the instruction types of JVM opcodes.
     */
    static {
        int i;
        byte[] b = new byte[220];
        String s = "AAAAAAAAAAAAAAAABCKLLDDDDDEEEEEEEEEEEEEEEEEEEEAAAAAAAADD" + "DDDEEEEEEEEEEEEEEEEEEEEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" + "AAAAAAAAAAAAAAAAAMAAAAAAAAAAAAAAAAAAAAIIIIIIIIIIIIIIIIDNOAA" + "AAAAGGGGGGGHHFBFAAFFAAQPIIJJIIIIIIIIIIIIIIIIII";
        for (i = 0; i < b.length; ++i) {
            b[i] = (byte) (s.charAt(i) - 'A');
        }
        TYPE = b;

        // code to generate the above string
        //
        // // SBYTE_INSN instructions
        // b[Constants.NEWARRAY] = SBYTE_INSN;
        // b[Constants.BIPUSH] = SBYTE_INSN;
        //
        // // SHORT_INSN instructions
        // b[Constants.SIPUSH] = SHORT_INSN;
        //
        // // (IMPL)VAR_INSN instructions
        // b[Constants.RET] = VAR_INSN;
        // for (i = Constants.ILOAD; i <= Constants.ALOAD; ++i) {
        // b[i] = VAR_INSN;
        // }
        // for (i = Constants.ISTORE; i <= Constants.ASTORE; ++i) {
        // b[i] = VAR_INSN;
        // }
        // for (i = 26; i <= 45; ++i) { // ILOAD_0 to ALOAD_3
        // b[i] = IMPLVAR_INSN;
        // }
        // for (i = 59; i <= 78; ++i) { // ISTORE_0 to ASTORE_3
        // b[i] = IMPLVAR_INSN;
        // }
        //
        // // TYPE_INSN instructions
        // b[Constants.NEW] = TYPE_INSN;
        // b[Constants.ANEWARRAY] = TYPE_INSN;
        // b[Constants.CHECKCAST] = TYPE_INSN;
        // b[Constants.INSTANCEOF] = TYPE_INSN;
        //
        // // (Set)FIELDORMETH_INSN instructions
        // for (i = Constants.GETSTATIC; i <= Constants.INVOKESTATIC; ++i) {
        // b[i] = FIELDORMETH_INSN;
        // }
        // b[Constants.INVOKEINTERFACE] = ITFDYNMETH_INSN;
        // b[Constants.INVOKEDYNAMIC] = ITFDYNMETH_INSN;
        //
        // // LABEL(W)_INSN instructions
        // for (i = Constants.IFEQ; i <= Constants.JSR; ++i) {
        // b[i] = LABEL_INSN;
        // }
        // b[Constants.IFNULL] = LABEL_INSN;
        // b[Constants.IFNONNULL] = LABEL_INSN;
        // b[200] = LABELW_INSN; // GOTO_W
        // b[201] = LABELW_INSN; // JSR_W
        // // temporary opcodes used internally by ASM - see Label and
        // MethodWriter
        // for (i = 202; i < 220; ++i) {
        // b[i] = LABEL_INSN;
        // }
        //
        // // LDC(_W) instructions
        // b[Constants.LDC] = LDC_INSN;
        // b[19] = LDCW_INSN; // LDC_W
        // b[20] = LDCW_INSN; // LDC2_W
        //
        // // special instructions
        // b[Constants.IINC] = IINC_INSN;
        // b[Constants.TABLESWITCH] = TABL_INSN;
        // b[Constants.LOOKUPSWITCH] = LOOK_INSN;
        // b[Constants.MULTIANEWARRAY] = MANA_INSN;
        // b[196] = WIDE_INSN; // WIDE
        //
        // for (i = 0; i < b.length; ++i) {
        // System.err.print((char)('A' + b[i]));
        // }
        // System.err.println();
    }

    // ------------------------------------------------------------------------
    // Constructor
    // ------------------------------------------------------------------------

    public ClassWriter(){
        this(0);
    }

    /**
     * Constructs a new {@link ClassWriter} object.
     * 
     * @param flags option flags that can be used to modify the default behavior of this class. See
     * {@link #COMPUTE_MAXS}, {@link #COMPUTE_FRAMES}.
     */
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
        this.name = newClass(name);
        thisName = name;
        this.superName = superName == null ? 0 : newClass(superName);
        if (interfaces != null && interfaces.length > 0) {
            interfaceCount = interfaces.length;
            this.interfaces = new int[interfaceCount];
            for (int i = 0; i < interfaceCount; ++i) {
                this.interfaces[i] = newClass(interfaces[i]);
            }
        }
    }

    public FieldVisitor visitField(final int access, final String name, final String desc) {
        return new FieldWriter(this, access, name, desc);
    }

    public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature, final String[] exceptions) {
        return new MethodWriter(this, access, name, desc, signature, exceptions);
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
        int mask = Opcodes.ACC_DEPRECATED | ClassWriter.ACC_SYNTHETIC_ATTRIBUTE | ((access & ClassWriter.ACC_SYNTHETIC_ATTRIBUTE) / (ClassWriter.ACC_SYNTHETIC_ATTRIBUTE / Opcodes.ACC_SYNTHETIC));
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
            return newInteger(val);
        } else if (cst instanceof String) {
            return newString((String) cst);
        } else if (cst instanceof Type) {
            Type t = (Type) cst;
            return newClassItem(t.getSort() == Type.OBJECT ? t.getInternalName() : t.getDescriptor());
        } else {
            throw new IllegalArgumentException("value " + cst);
        }
    }
    
    Item newInteger(final int value) {
        key.set(value);
        Item result = get(key);
        if (result == null) {
            pool.putByte(INT).putInt(value);
            result = new Item(index++, key);
            put(result);
        }
        return result;
    }

    public int newUTF8(final String value) {
        key.set(UTF8, value, null, null);
        Item result = get(key);
        if (result == null) {
            pool.putByte(UTF8).putUTF8(value);
            result = new Item(index++, key);
            put(result);
        }
        return result.index;
    }

    Item newClassItem(final String value) {
        key2.set(CLASS, value, null, null);
        Item result = get(key2);
        if (result == null) {
            pool.put12(CLASS, newUTF8(value));
            result = new Item(index++, key2);
            put(result);
        }
        return result;
    }

    public int newClass(final String value) {
        return newClassItem(value).index;
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
        key3.set(FIELD, owner, name, desc);
        Item result = get(key3);
        if (result == null) {
            put122(FIELD, newClass(owner), newNameType(name, desc));
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
        int type = itf ? IMETH : METH;
        key3.set(type, owner, name, desc);
        Item result = get(key3);
        if (result == null) {
            put122(type, newClass(owner), newNameType(name, desc));
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
        key2.set(STR, value, null, null);
        Item result = get(key2);
        if (result == null) {
            pool.put12(STR, newUTF8(value));
            result = new Item(index++, key2);
            put(result);
        }
        return result;
    }

    public int newNameType(final String name, final String desc) {
        return newNameTypeItem(name, desc).index;
    }

    /**
     * Adds a name and type to the constant pool of the class being build. Does nothing if the constant pool already
     * contains a similar item.
     * 
     * @param name a name.
     * @param desc a type descriptor.
     * @return a new or already existing name and type item.
     */
    Item newNameTypeItem(final String name, final String desc) {
        key2.set(NAME_TYPE, name, desc, null);
        Item result = get(key2);
        if (result == null) {
            put122(NAME_TYPE, newUTF8(name), newUTF8(desc));
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

    /**
     * Puts one byte and two shorts into the constant pool.
     * 
     * @param b a byte.
     * @param s1 a short.
     * @param s2 another short.
     */
    private void put122(final int b, final int s1, final int s2) {
        pool.put12(b, s1).putShort(s2);
    }
}
