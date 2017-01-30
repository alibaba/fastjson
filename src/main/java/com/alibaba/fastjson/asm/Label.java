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
 */
public class Label {

    /**
     * Indicates if the position of this label is known.
     */
    static final int RESOLVED = 2;

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

    private int[]    srcAndRefPositions;

    int              inputStackTop;

    int              outputStackMax;

    Label            successor;

    Label            next;

    // ------------------------------------------------------------------------
    // Constructor
    // ------------------------------------------------------------------------

    /**
     * Constructs a new label.
     */
    public Label(){
    }

    void put(final MethodWriter owner, final ByteVector out, final int source) {
        if ((status & RESOLVED) == 0) {
            addReference(source, out.length);
            out.putShort(-1);
        } else {
            out.putShort(position - source);
        }
    }

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
