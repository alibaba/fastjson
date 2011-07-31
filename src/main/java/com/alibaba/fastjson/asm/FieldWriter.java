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
 * An {@link FieldVisitor} that generates Java fields in bytecode form.
 * 
 * @author Eric Bruneton
 */
final class FieldWriter implements FieldVisitor {

    /**
     * Next field writer (see {@link ClassWriter#firstField firstField}).
     */
    FieldWriter       next;

    /**
     * Access flags of this field.
     */
    private final int access;

    /**
     * The index of the constant pool item that contains the name of this method.
     */
    private final int name;

    /**
     * The index of the constant pool item that contains the descriptor of this field.
     */
    private final int desc;

    // ------------------------------------------------------------------------
    // Constructor
    // ------------------------------------------------------------------------

    /**
     * Constructs a new {@link FieldWriter}.
     * 
     * @param cw the class writer to which this field must be added.
     * @param access the field's access flags (see {@link Opcodes}).
     * @param name the field's name.
     * @param desc the field's descriptor (see {@link Type}).
     * @param signature the field's signature. May be <tt>null</tt>.
     * @param value the field's constant value. May be <tt>null</tt>.
     */
    FieldWriter(final ClassWriter cw, final int access, final String name, final String desc){
        if (cw.firstField == null) {
            cw.firstField = this;
        } else {
            cw.lastField.next = this;
        }
        cw.lastField = this;
        this.access = access;
        this.name = cw.newUTF8(name);
        this.desc = cw.newUTF8(desc);
    }

    // ------------------------------------------------------------------------
    // Implementation of the FieldVisitor interface
    // ------------------------------------------------------------------------

    public void visitEnd() {
    }

    // ------------------------------------------------------------------------
    // Utility methods
    // ------------------------------------------------------------------------

    /**
     * Returns the size of this field.
     * 
     * @return the size of this field.
     */
    int getSize() {
        return 8;
    }

    /**
     * Puts the content of this field into the given byte vector.
     * 
     * @param out where the content of this field must be put.
     */
    void put(final ByteVector out) {
        int mask = Opcodes.ACC_DEPRECATED | ClassWriter.ACC_SYNTHETIC_ATTRIBUTE | ((access & ClassWriter.ACC_SYNTHETIC_ATTRIBUTE) / (ClassWriter.ACC_SYNTHETIC_ATTRIBUTE / Opcodes.ACC_SYNTHETIC));
        out.putShort(access & ~mask).putShort(name).putShort(desc);
        int attributeCount = 0;
        out.putShort(attributeCount);
    }
}
