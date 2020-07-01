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
 * Defines the JVM opcodes, access flags and array type codes. This interface does not define all the JVM opcodes
 * because some opcodes are automatically handled. For example, the xLOAD and xSTORE opcodes are automatically replaced
 * by xLOAD_n and xSTORE_n opcodes when possible. The xLOAD_n and xSTORE_n opcodes are therefore not defined in this
 * interface. Likewise for LDC, automatically replaced by LDC_W or LDC2_W when necessary, WIDE, GOTO_W and JSR_W.
 * 
 * @author Eric Bruneton
 * @author Eugene Kuleshov
 */
public interface Opcodes {
    int T_INT = 10;

    // versions

    // int V1_1 = 3 << 16 | 45;
    // int V1_2 = 0 << 16 | 46;
    // int V1_3 = 0 << 16 | 47;
    // int V1_4 = 0 << 16 | 48;
    int    V1_5                = 0 << 16 | 49;
    // int V1_6 = 0 << 16 | 50;
    // int V1_7 = 0 << 16 | 51;

    // access flags

    int    ACC_PUBLIC          = 0x0001;                 // class, field, method
    int    ACC_SUPER           = 0x0020;                 // class

    // opcodes // visit method (- = idem)

    int    ACONST_NULL         = 1;                      // -
    int    ICONST_0            = 3;                      // -
    int    ICONST_1            = 4;                      // -
    int    LCONST_0            = 9;                      // -
    int    LCONST_1            = 10;                     // -
    int    FCONST_0            = 11;                     // -
    int    DCONST_0            = 14;                     // -
    int    BIPUSH              = 16;                     // visitIntInsn
                                                          // int SIPUSH = 17; // -
//    int    LDC                 = 18;                     // visitLdcInsn
    // int LDC_W = 19; // -
    // int LDC2_W = 20; // -
    int    ILOAD               = 21;                     // visitVarInsn
    int    LLOAD               = 22;                     // -
    int    FLOAD               = 23;                     // -
    int    DLOAD               = 24;                     // -
    int    ALOAD               = 25;                     // -
  
    int    ISTORE              = 54;                     // visitVarInsn
    int    LSTORE              = 55;                     // -
    int    FSTORE              = 56;                     // -
    int    DSTORE              = 57;                     // -
    int    ASTORE              = 58;                     // -
    int    IASTORE             = 79; // visitInsn
    
    int    POP                 = 87;                     // -
//    int    POP2                = 88;                     // -
    int    DUP                 = 89;                     // -
                                                         
    int    IADD                = 96;                     // -

//    int    ISUB                = 100;                    // -
                                                  
    int    IAND                = 126;                    // -
                                                          // int LAND = 127; // -
    int    IOR                 = 128;                    // -
                                                          // int LOR = 129; // -
                                                          // int IXOR = 130; // -
                                                          // int LXOR = 131; // -
    // int    IINC                = 132;                    // visitIincInsn
                                                       
    int    LCMP                = 148;                    // -
    int    FCMPL               = 149;                    // -
    int    DCMPL               = 151;                    // -
    int    IFEQ                = 153;                    // visitJumpInsn
    int    IFNE                = 154;                    // -
    int    IFLE                = 158;                    // -
    int    IF_ICMPEQ           = 159;                    // -
    int    IF_ICMPNE           = 160;                    // -
    int    IF_ICMPLT           = 161;                    // -
    int    IF_ICMPGE           = 162;                    // -
    int    IF_ICMPGT           = 163;                    // -
    int    IF_ACMPEQ           = 165;                    // -
    int    IF_ACMPNE           = 166;                    // -
    int    GOTO                = 167;                    // -
    int    RET                 = 169;                    // visitVarInsn
    int    ARETURN             = 176;                    // -
    int    RETURN              = 177;                    // -
    int    GETSTATIC           = 178;                    // visitFieldInsn
    int    GETFIELD            = 180;                    // -
    int    PUTFIELD            = 181;                    // -
    int    INVOKEVIRTUAL       = 182;                    // visitMethodInsn
    int    INVOKESPECIAL       = 183;                    // -
    int    INVOKESTATIC        = 184;                    // -
    int    INVOKEINTERFACE     = 185;                    // -
    // int INVOKEDYNAMIC = 186; // -
    int    NEW                 = 187;                    // visitTypeInsn
    int    NEWARRAY            = 188;                    // visitIntInsn
                                                          // int ANEWARRAY = 189; // visitTypeInsn
                                                          // int ARRAYLENGTH = 190; // visitInsn
                                                          // int ATHROW = 191; // -
    int    CHECKCAST           = 192;                    // visitTypeInsn
    int    INSTANCEOF          = 193;
    
    int    IFNULL              = 198;                    // visitJumpInsn
    int    IFNONNULL           = 199;                    // -
    int    GOTO_W              = 200;                    // visitJumpInsn
    // int JSR_W = 201; // -
}
