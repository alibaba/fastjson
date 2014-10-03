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
public interface MethodVisitor {

    // -------------------------------------------------------------------------
    // Annotations and non standard attributes
    // -------------------------------------------------------------------------

    // -------------------------------------------------------------------------
    // Normal instructions
    // -------------------------------------------------------------------------

    /**
     * Visits a zero operand instruction.
     * 
     * @param opcode the opcode of the instruction to be visited. This opcode is either NOP, ACONST_NULL, ICONST_M1,
     * ICONST_0, ICONST_1, ICONST_2, ICONST_3, ICONST_4, ICONST_5, LCONST_0, LCONST_1, FCONST_0, FCONST_1, FCONST_2,
     * DCONST_0, DCONST_1, IALOAD, LALOAD, FALOAD, DALOAD, AALOAD, BALOAD, CALOAD, SALOAD, IASTORE, LASTORE, FASTORE,
     * DASTORE, AASTORE, BASTORE, CASTORE, SASTORE, POP, POP2, DUP, DUP_X1, DUP_X2, DUP2, DUP2_X1, DUP2_X2, SWAP, IADD,
     * LADD, FADD, DADD, ISUB, LSUB, FSUB, DSUB, IMUL, LMUL, FMUL, DMUL, IDIV, LDIV, FDIV, DDIV, IREM, LREM, FREM, DREM,
     * INEG, LNEG, FNEG, DNEG, ISHL, LSHL, ISHR, LSHR, IUSHR, LUSHR, IAND, LAND, IOR, LOR, IXOR, LXOR, I2L, I2F, I2D,
     * L2I, L2F, L2D, F2I, F2L, F2D, D2I, D2L, D2F, I2B, I2C, I2S, LCMP, FCMPL, FCMPG, DCMPL, DCMPG, IRETURN, LRETURN,
     * FRETURN, DRETURN, ARETURN, RETURN, ARRAYLENGTH, ATHROW, MONITORENTER, or MONITOREXIT.
     */
    void visitInsn(int opcode);

    /**
     * Visits an instruction with a single int operand.
     * 
     * @param opcode the opcode of the instruction to be visited. This opcode is either BIPUSH, SIPUSH or NEWARRAY.
     * @param operand the operand of the instruction to be visited.<br>
     * When opcode is BIPUSH, operand value should be between Byte.MIN_VALUE and Byte.MAX_VALUE.<br>
     * When opcode is SIPUSH, operand value should be between Short.MIN_VALUE and Short.MAX_VALUE.<br>
     * When opcode is NEWARRAY, operand value should be one of {@link Opcodes#T_BOOLEAN}, {@link Opcodes#T_CHAR},
     * {@link Opcodes#T_FLOAT}, {@link Opcodes#T_DOUBLE}, {@link Opcodes#T_BYTE}, {@link Opcodes#T_SHORT},
     * {@link Opcodes#T_INT} or {@link Opcodes#T_LONG}.
     */
    void visitIntInsn(int opcode, int operand);

    /**
     * Visits a local variable instruction. A local variable instruction is an instruction that loads or stores the
     * value of a local variable.
     * 
     * @param opcode the opcode of the local variable instruction to be visited. This opcode is either ILOAD, LLOAD,
     * FLOAD, DLOAD, ALOAD, ISTORE, LSTORE, FSTORE, DSTORE, ASTORE or RET.
     * @param var the operand of the instruction to be visited. This operand is the index of a local variable.
     */
    void visitVarInsn(int opcode, int var);

    /**
     * Visits a type instruction. A type instruction is an instruction that takes the internal name of a class as
     * parameter.
     * 
     * @param opcode the opcode of the type instruction to be visited. This opcode is either NEW, ANEWARRAY, CHECKCAST
     * or INSTANCEOF.
     * @param type the operand of the instruction to be visited. This operand must be the internal name of an object or
     * array class (see {@link Type#getInternalName() getInternalName}).
     */
    void visitTypeInsn(int opcode, String type);

    /**
     * Visits a field instruction. A field instruction is an instruction that loads or stores the value of a field of an
     * object.
     * 
     * @param opcode the opcode of the type instruction to be visited. This opcode is either GETSTATIC, PUTSTATIC,
     * GETFIELD or PUTFIELD.
     * @param owner the internal name of the field's owner class (see {@link Type#getInternalName() getInternalName}).
     * @param name the field's name.
     * @param desc the field's descriptor (see {@link Type Type}).
     */
    void visitFieldInsn(int opcode, String owner, String name, String desc);

    /**
     * Visits a method instruction. A method instruction is an instruction that invokes a method.
     * 
     * @param opcode the opcode of the type instruction to be visited. This opcode is either INVOKEVIRTUAL,
     * INVOKESPECIAL, INVOKESTATIC, INVOKEINTERFACE or INVOKEDYNAMIC.
     * @param owner the internal name of the method's owner class (see {@link Type#getInternalName() getInternalName})
     * or {@link com.alibaba.fastjson.asm.Opcodes#INVOKEDYNAMIC_OWNER}.
     * @param name the method's name.
     * @param desc the method's descriptor (see {@link Type Type}).
     */
    void visitMethodInsn(int opcode, String owner, String name, String desc);

    /**
     * Visits a jump instruction. A jump instruction is an instruction that may jump to another instruction.
     * 
     * @param opcode the opcode of the type instruction to be visited. This opcode is either IFEQ, IFNE, IFLT, IFGE,
     * IFGT, IFLE, IF_ICMPEQ, IF_ICMPNE, IF_ICMPLT, IF_ICMPGE, IF_ICMPGT, IF_ICMPLE, IF_ACMPEQ, IF_ACMPNE, GOTO, JSR,
     * IFNULL or IFNONNULL.
     * @param label the operand of the instruction to be visited. This operand is a label that designates the
     * instruction to which the jump instruction may jump.
     */
    void visitJumpInsn(int opcode, Label label);

    /**
     * Visits a label. A label designates the instruction that will be visited just after it.
     * 
     * @param label a {@link Label Label} object.
     */
    void visitLabel(Label label);

    // -------------------------------------------------------------------------
    // Special instructions
    // -------------------------------------------------------------------------

    /**
     * Visits a LDC instruction.
     * 
     * @param cst the constant to be loaded on the stack. This parameter must be a non null {@link Integer}, a
     * {@link Float}, a {@link Long}, a {@link Double} a {@link String} (or a {@link Type} for <tt>.class</tt>
     * constants, for classes whose version is 49.0 or more).
     */
    void visitLdcInsn(Object cst);

    /**
     * Visits an IINC instruction.
     * 
     * @param var index of the local variable to be incremented.
     * @param increment amount to increment the local variable by.
     */
    void visitIincInsn(int var, int increment);

    // -------------------------------------------------------------------------
    // Exceptions table entries, debug information, max stack and max locals
    // -------------------------------------------------------------------------

    /**
     * Visits the maximum stack size and the maximum number of local variables of the method.
     * 
     * @param maxStack maximum stack size of the method.
     * @param maxLocals maximum number of local variables for the method.
     */
    void visitMaxs(int maxStack, int maxLocals);

    /**
     * Visits the end of the method. This method, which is the last one to be called, is used to inform the visitor that
     * all the annotations and attributes of the method have been visited.
     */
    void visitEnd();
}
