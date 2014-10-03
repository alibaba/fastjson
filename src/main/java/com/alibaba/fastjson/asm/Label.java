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
 * A label represents a position in the bytecode of a method. Labels are used for jump, goto, and switch instructions,
 * and for try catch blocks. A label designates the <i>instruction</i> that is just after. Note however that there can
 * be other elements between a label and the instruction it designates (such as other labels, stack map frames, line
 * numbers, etc.).
 * 
 * @author Eric Bruneton
 */
public class Label {

    /**
     * Indicates if the position of this label is known.
     */
    static final int RESOLVED = 2;

    /**
     * Field used to associate user information to a label. Warning: this field is used by the ASM tree package. In
     * order to use it with the ASM tree package you must override the
     *com.alibaba.fastjson.asm.tree.MethodNode#getLabelNode method.
     */
    public Object    info;

    int              status;

    /**
     * The line number corresponding to this label, if known.
     */
    int              line;

    /**
     * The position of this label in the code, if known.
     */
    int              position;

    /**
     * Number of forward references to this label, times two.
     */
    private int      referenceCount;

    /**
     * Informations about forward references. Each forward reference is described by two consecutive integers in this
     * array: the first one is the position of the first byte of the bytecode instruction that contains the forward
     * reference, while the second is the position of the first byte of the forward reference itself. In fact the sign
     * of the first integer indicates if this reference uses 2 or 4 bytes, and its absolute value gives the position of
     * the bytecode instruction. This array is also used as a bitset to store the subroutines to which a basic block
     * belongs. This information is needed in MethodWriter#visitMaxs, after all forward references have been
     * resolved. Hence the same array can be used for both purposes without problems.
     */
    private int[]    srcAndRefPositions;

    // ------------------------------------------------------------------------

    /*
     * Fields for the control flow and data flow graph analysis algorithms (used to compute the maximum stack size or
     * the stack map frames). A control flow graph contains one node per "basic block", and one edge per "jump" from one
     * basic block to another. Each node (i.e., each basic block) is represented by the Label object that corresponds to
     * the first instruction of this basic block. Each node also stores the list of its successors in the graph, as a
     * linked list of Edge objects. The control flow analysis algorithms used to compute the maximum stack size or the
     * stack map frames are similar and use two steps. The first step, during the visit of each instruction, builds
     * information about the state of the local variables and the operand stack at the end of each basic block, called
     * the "output frame", <i>relatively</i> to the frame state at the beginning of the basic block, which is called the
     * "input frame", and which is <i>unknown</i> during this step. The second step, in link MethodWriter#visitMaxs,
     * is a fix point algorithm that computes information about the input frame of each basic block, from the input
     * state of the first basic block (known from the method signature), and by the using the previously computed
     * relative output frames. The algorithm used to compute the maximum stack size only computes the relative output
     * and absolute input stack heights, while the algorithm used to compute stack map frames computes relative output
     * frames and absolute input frames.
     */

    /**
     * Start of the output stack relatively to the input stack. The exact semantics of this field depends on the
     * algorithm that is used. When only the maximum stack size is computed, this field is the number of elements in the
     * input stack. When the stack map frames are completely computed, this field is the offset of the first output
     * stack element relatively to the top of the input stack. This offset is always negative or null. A null offset
     * means that the output stack must be appended to the input stack. A -n offset means that the first n output stack
     * elements must replace the top n input stack elements, and that the other elements must be appended to the input
     * stack.
     */
    int              inputStackTop;

    /**
     * Maximum height reached by the output stack, relatively to the top of the input stack. This maximum is always
     * positive or null.
     */
    int              outputStackMax;

    /**
     * The successor of this label, in the order they are visited. This linked list does not include labels used for
     * debug info only. If ClassWriter#COMPUTE_FRAMES option is used then, in addition, it does not contain
     * successive labels that denote the same bytecode position (in this case only the first label appears in this
     * list).
     */
    Label            successor;

    /**
     * The next basic block in the basic block stack. This stack is used in the main loop of the fix point algorithm
     * used in the second step of the control flow analysis algorithms. It is also used in {@link #visitSubroutine} to
     * avoid using a recursive method.
     * 
     * @see MethodWriter#visitMaxs
     */
    Label            next;

    // ------------------------------------------------------------------------
    // Constructor
    // ------------------------------------------------------------------------

    /**
     * Constructs a new label.
     */
    public Label(){
    }

    // ------------------------------------------------------------------------
    // Methods to compute offsets and to manage forward references
    // ------------------------------------------------------------------------

    /**
     * Puts a reference to this label in the bytecode of a method. If the position of the label is known, the offset is
     * computed and written directly. Otherwise, a null offset is written and a new forward reference is declared for
     * this label.
     * 
     * @param owner the code writer that calls this method.
     * @param out the bytecode of the method.
     * @param source the position of first byte of the bytecode instruction that contains this label.
     * @param wideOffset <tt>true</tt> if the reference must be stored in 4 bytes, or <tt>false</tt> if it must be
     * stored with 2 bytes.
     * @throws IllegalArgumentException if this label has not been created by the given code writer.
     */
    void put(final MethodWriter owner, final ByteVector out, final int source) {
        if ((status & RESOLVED) == 0) {
            addReference(source, out.length);
            out.putShort(-1);
        } else {
            out.putShort(position - source);
        }
    }

    /**
     * Adds a forward reference to this label. This method must be called only for a true forward reference, i.e. only
     * if this label is not resolved yet. For backward references, the offset of the reference can be, and must be,
     * computed and stored directly.
     * 
     * @param sourcePosition the position of the referencing instruction. This position will be used to compute the
     * offset of this forward reference.
     * @param referencePosition the position where the offset for this forward reference must be stored.
     */
    private void addReference(final int sourcePosition, final int referencePosition) {
        if (srcAndRefPositions == null) {
            srcAndRefPositions = new int[6];
        }
        if (referenceCount >= srcAndRefPositions.length) {
            int[] a = new int[srcAndRefPositions.length + 6];
            System.arraycopy(srcAndRefPositions, 0, a, 0, srcAndRefPositions.length);
            srcAndRefPositions = a;
        }
        srcAndRefPositions[referenceCount++] = sourcePosition;
        srcAndRefPositions[referenceCount++] = referencePosition;
    }

    /**
     * Resolves all forward references to this label. This method must be called when this label is added to the
     * bytecode of the method, i.e. when its position becomes known. This method fills in the blanks that where left in
     * the bytecode by each forward reference previously added to this label.
     * 
     * @param owner the code writer that calls this method.
     * @param position the position of this label in the bytecode.
     * @param data the bytecode of the method.
     * @return <tt>true</tt> if a blank that was left for this label was to small to store the offset. In such a case
     * the corresponding jump instruction is replaced with a pseudo instruction (using unused opcodes) using an unsigned
     * two bytes offset. These pseudo instructions will need to be replaced with true instructions with wider offsets (4
     * bytes instead of 2). This is done in {@link MethodWriter#resizeInstructions}.
     * @throws IllegalArgumentException if this label has already been resolved, or if it has not been created by the
     * given code writer.
     */
    void resolve(final MethodWriter owner, final int position, final byte[] data) {
        this.status |= RESOLVED;
        this.position = position;
        int i = 0;
        while (i < referenceCount) {
            int source = srcAndRefPositions[i++];
            int reference = srcAndRefPositions[i++];
            int offset = position - source;
            data[reference++] = (byte) (offset >>> 8);
            data[reference] = (byte) offset;

        }
    }

}
