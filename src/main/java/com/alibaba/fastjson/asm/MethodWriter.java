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
 * @author Eric Bruneton
 * @author Eugene Kuleshov
 */
public class MethodWriter implements MethodVisitor {

    /**
     * Next method writer (see {@link ClassWriter#firstMethod firstMethod}).
     */
    MethodWriter       next;

    /**
     * The class writer to which this method must be added.
     */
    final ClassWriter  cw;

    /**
     * Access flags of this method.
     */
    private int        access;

    /**
     * The index of the constant pool item that contains the name of this method.
     */
    private final int  name;

    /**
     * The index of the constant pool item that contains the descriptor of this method.
     */
    private final int  desc;

    /**
     * Number of exceptions that can be thrown by this method.
     */
    int                exceptionCount;

    /**
     * The exceptions that can be thrown by this method. More precisely, this array contains the indexes of the constant
     * pool items that contain the internal names of these exception classes.
     */
    int[]              exceptions;

    /**
     * The bytecode of this method.
     */
    private ByteVector code                                    = new ByteVector();

    /**
     * Maximum stack size of this method.
     */
    private int        maxStack;

    /**
     * Maximum number of local variables for this method.
     */
    private int        maxLocals;

    // ------------------------------------------------------------------------

    /*
     * Fields for the control flow graph analysis algorithm (used to compute the maximum stack size). A control flow
     * graph contains one node per "basic block", and one edge per "jump" from one basic block to another. Each node
     * (i.e., each basic block) is represented by the Label object that corresponds to the first instruction of this
     * basic block. Each node also stores the list of its successors in the graph, as a linked list of Edge objects.
     */

    // ------------------------------------------------------------------------
    // Constructor
    // ------------------------------------------------------------------------

    public MethodWriter(final ClassWriter cw, final int access, final String name, final String desc, final String signature, final String[] exceptions){
        if (cw.firstMethod == null) {
            cw.firstMethod = this;
        } else {
            cw.lastMethod.next = this;
        }
        cw.lastMethod = this;
        this.cw = cw;
        this.access = access;
        this.name = cw.newUTF8(name);
        this.desc = cw.newUTF8(desc);

        if (exceptions != null && exceptions.length > 0) {
            exceptionCount = exceptions.length;
            this.exceptions = new int[exceptionCount];
            for (int i = 0; i < exceptionCount; ++i) {
                this.exceptions[i] = cw.newClassItem(exceptions[i]).index;
            }
        }
    }

    // ------------------------------------------------------------------------
    // Implementation of the MethodVisitor interface
    // ------------------------------------------------------------------------

    public void visitInsn(final int opcode) {
        // adds the instruction to the bytecode of the method
        code.putByte(opcode);
        // update currentBlock
        // Label currentBlock = this.currentBlock;
    }

    public void visitIntInsn(final int opcode, final int operand) {
        // Label currentBlock = this.currentBlock;
        // adds the instruction to the bytecode of the method
        // if (opcode == Opcodes.SIPUSH) {
        // code.put12(opcode, operand);
        // } else { // BIPUSH or NEWARRAY
        code.put11(opcode, operand);
        // }
    }

    public void visitVarInsn(final int opcode, final int var) {
        // Label currentBlock = this.currentBlock;
        // adds the instruction to the bytecode of the method
        if (var < 4 && opcode != Opcodes.RET) {
            int opt;
            if (opcode < Opcodes.ISTORE) {
                /* ILOAD_0 */
                opt = 26 + ((opcode - Opcodes.ILOAD) << 2) + var;
            } else {
                /* ISTORE_0 */
                opt = 59 + ((opcode - Opcodes.ISTORE) << 2) + var;
            }
            code.putByte(opt);
        } else if (var >= 256) {
            code.putByte(196 /* WIDE */).put12(opcode, var);
        } else {
            code.put11(opcode, var);
        }
    }

    public void visitTypeInsn(final int opcode, final String type) {
        Item i = cw.newClassItem(type);
        // Label currentBlock = this.currentBlock;
        // adds the instruction to the bytecode of the method
        code.put12(opcode, i.index);
    }

    public void visitFieldInsn(final int opcode, final String owner, final String name, final String desc) {
        Item i = cw.newFieldItem(owner, name, desc);
        // Label currentBlock = this.currentBlock;
        // adds the instruction to the bytecode of the method
        code.put12(opcode, i.index);
    }

    public void visitMethodInsn(final int opcode, final String owner, final String name, final String desc) {
        boolean itf = opcode == Opcodes.INVOKEINTERFACE;
        Item i = cw.newMethodItem(owner, name, desc, itf);
        int argSize = i.intVal;
        // Label currentBlock = this.currentBlock;
        // adds the instruction to the bytecode of the method
        if (itf) {
            if (argSize == 0) {
                argSize = Type.getArgumentsAndReturnSizes(desc);
                i.intVal = argSize;
            }
            code.put12(Opcodes.INVOKEINTERFACE, i.index).put11(argSize >> 2, 0);
        } else {
            code.put12(opcode, i.index);
        }
    }

    public void visitJumpInsn(final int opcode, final Label label) {
        // Label currentBlock = this.currentBlock;
        // adds the instruction to the bytecode of the method
        if ((label.status & 2 /* Label.RESOLVED */ ) != 0 && label.position - code.length < Short.MIN_VALUE) {
            throw new UnsupportedOperationException();
        } else {
            /*
             * case of a backward jump with an offset >= -32768, or of a forward jump with, of course, an unknown
             * offset. In these cases we store the offset in 2 bytes (which will be increased in resizeInstructions, if
             * needed).
             */
            code.putByte(opcode);
            // Currently, GOTO_W is the only supported wide reference
            label.put(this, code, code.length - 1, opcode == Opcodes.GOTO_W);
        }
    }

    public void visitLabel(final Label label) {
        // resolves previous forward references to label, if any
        label.resolve(this, code.length, code.data);
    }

    public void visitLdcInsn(final Object cst) {
        Item i = cw.newConstItem(cst);
        // Label currentBlock = this.currentBlock;
        // adds the instruction to the bytecode of the method
        int index = i.index;
        if (i.type == 5 /* ClassWriter.LONG */ || i.type == 6 /* ClassWriter.DOUBLE */) {
            code.put12(20 /* LDC2_W */, index);
        } else if (index >= 256) {
            code.put12(19 /* LDC_W */, index);
        } else {
            code.put11(18 /*Opcodes.LDC*/, index);
        }
    }

    public void visitIincInsn(final int var, final int increment) {
        // adds the instruction to the bytecode of the method
//        if ((var > 255) || (increment > 127) || (increment < -128)) {
//            code.putByte(196 /* WIDE */).put12(Opcodes.IINC, var).putShort(increment);
//        } else {
            code.putByte(132 /* Opcodes.IINC*/ ).put11(var, increment);
//        }
    }

    public void visitMaxs(final int maxStack, final int maxLocals) {
        this.maxStack = maxStack;
        this.maxLocals = maxLocals;
    }

    public void visitEnd() {
    }

    // ------------------------------------------------------------------------
    // Utility methods: control flow analysis algorithm
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // Utility methods: stack map frames
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // Utility methods: dump bytecode array
    // ------------------------------------------------------------------------

    /**
     * Returns the size of the bytecode of this method.
     * 
     * @return the size of the bytecode of this method.
     */
    final int getSize() {
        int size = 8;
        if (code.length > 0) {
            cw.newUTF8("Code");
            size += 18 + code.length + 8 * 0;
        }
        if (exceptionCount > 0) {
            cw.newUTF8("Exceptions");
            size += 8 + 2 * exceptionCount;
        }
        return size;
    }

    /**
     * Puts the bytecode of this method in the given byte vector.
     * 
     * @param out the byte vector into which the bytecode of this method must be copied.
     */
    final void put(final ByteVector out) {
        final int mask = 393216; //Opcodes.ACC_DEPRECATED | ClassWriter.ACC_SYNTHETIC_ATTRIBUTE | ((access & ClassWriter.ACC_SYNTHETIC_ATTRIBUTE) / (ClassWriter.ACC_SYNTHETIC_ATTRIBUTE / Opcodes.ACC_SYNTHETIC));
        out.putShort(access & ~mask).putShort(name).putShort(desc);
        int attributeCount = 0;
        if (code.length > 0) {
            ++attributeCount;
        }
        if (exceptionCount > 0) {
            ++attributeCount;
        }

        out.putShort(attributeCount);
        if (code.length > 0) {
            int size = 12 + code.length + 8 * 0; // handlerCount
            out.putShort(cw.newUTF8("Code")).putInt(size);
            out.putShort(maxStack).putShort(maxLocals);
            out.putInt(code.length).putByteArray(code.data, 0, code.length);
            out.putShort(0); // handlerCount
            attributeCount = 0;
            out.putShort(attributeCount);
        }
        if (exceptionCount > 0) {
            out.putShort(cw.newUTF8("Exceptions")).putInt(2 * exceptionCount + 2);
            out.putShort(exceptionCount);
            for (int i = 0; i < exceptionCount; ++i) {
                out.putShort(exceptions[i]);
            }
        }

    }

}
