package com.alibaba.fastjson.asm;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by wenshao on 05/08/2017.
 */
public class ClassReader {
    public  final byte[] b;
    private final int[] items;
    private final String[] strings;
    private final int maxStringLength;
    public  final int header;
    private boolean readAnnotations;

    public ClassReader(InputStream is, boolean readAnnotations) throws IOException {
        this.readAnnotations = readAnnotations;

        {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            for (; ; ) {
                int len = is.read(buf);
                if (len == -1) {
                    break;
                }

                if (len > 0) {
                    out.write(buf, 0, len);
                }
            }
            is.close();
            this.b = out.toByteArray();
        }

        // parses the constant pool
        items = new int[readUnsignedShort(8)];
        int n = items.length;
        strings = new String[n];
        int max = 0;
        int index = 10;
        for (int i = 1; i < n; ++i) {
            items[i] = index + 1;
            int size;
            switch (b[index]) {
                case 9: // FIELD:
                case 10: // METH:
                case 11: //IMETH:
                case 3: //INT:
                case 4: //FLOAT:
                case 18: //INVOKEDYN:
                case 12: //NAME_TYPE:
                    size = 5;
                    break;
                case 5: //LONG:
                case 6: //DOUBLE:
                    size = 9;
                    ++i;
                    break;
                case 15: //MHANDLE:
                    size = 4;
                    break;
                case 1: //UTF8:
                    size = 3 + readUnsignedShort(index + 1);
                    if (size > max) {
                        max = size;
                    }
                    break;
                // case HamConstants.CLASS:
                // case HamConstants.STR:
                default:
                    size = 3;
                    break;
            }
            index += size;
        }
        maxStringLength = max;
        // the class header information starts just after the constant pool
        header = index;
    }

    public void accept(final TypeCollector classVisitor) {
        char[] c = new char[maxStringLength]; // buffer used to read strings
        int i, j; // loop variables
        int u, v; // indexes in b
        int anns = 0;

        //read annotations
        if (readAnnotations) {
            u = getAttributes();
            for (i = readUnsignedShort(u); i > 0; --i) {
                String attrName = readUTF8(u + 2, c);
                if ("RuntimeVisibleAnnotations".equals(attrName)) {
                    anns = u + 8;
                    break;
                }
                u += 6 + readInt(u + 4);
            }
        }

        // visits the header
        u = header;
        v = items[readUnsignedShort(u + 4)];
        int len = readUnsignedShort(u + 6);
        u += 8;
        for (i = 0; i < len; ++i) {
            u += 2;
        }
        v = u;
        i = readUnsignedShort(v);
        v += 2;
        for (; i > 0; --i) {
            j = readUnsignedShort(v + 6);
            v += 8;
            for (; j > 0; --j) {
                v += 6 + readInt(v + 2);
            }
        }
        i = readUnsignedShort(v);
        v += 2;
        for (; i > 0; --i) {
            j = readUnsignedShort(v + 6);
            v += 8;
            for (; j > 0; --j) {
                v += 6 + readInt(v + 2);
            }
        }

        i = readUnsignedShort(v);
        v += 2;
        for (; i > 0; --i) {
            v += 6 + readInt(v + 2);
        }

        if (anns != 0) {
            for (i = readUnsignedShort(anns), v = anns + 2; i > 0; --i) {
                String name = readUTF8(v, c);
                classVisitor.visitAnnotation(name);
            }
        }

        // visits the fields
        i = readUnsignedShort(u);
        u += 2;
        for (; i > 0; --i) {
            j = readUnsignedShort(u + 6);
            u += 8;
            for (; j > 0; --j) {
                u += 6 + readInt(u + 2);
            }
        }

        // visits the methods
        i = readUnsignedShort(u);
        u += 2;
        for (; i > 0; --i) {
            // inlined in original ASM source, now a method call
            u = readMethod(classVisitor, c, u);
        }
    }

    private int getAttributes() {
        // skips the header
        int u = header + 8 + readUnsignedShort(header + 6) * 2;
        // skips fields and methods
        for (int i = readUnsignedShort(u); i > 0; --i) {
            for (int j = readUnsignedShort(u + 8); j > 0; --j) {
                u += 6 + readInt(u + 12);
            }
            u += 8;
        }
        u += 2;
        for (int i = readUnsignedShort(u); i > 0; --i) {
            for (int j = readUnsignedShort(u + 8); j > 0; --j) {
                u += 6 + readInt(u + 12);
            }
            u += 8;
        }
        // the attribute_info structure starts just after the methods
        return u + 2;
    }

    private int readMethod(TypeCollector classVisitor, char[] c, int u) {
        int v;
        int w;
        int j;
        String attrName;
        int k;
        int access = readUnsignedShort(u);
        String name = readUTF8(u + 2, c);
        String desc = readUTF8(u + 4, c);
        v = 0;
        w = 0;

        // looks for Code and Exceptions attributes
        j = readUnsignedShort(u + 6);
        u += 8;
        for (; j > 0; --j) {
            attrName = readUTF8(u, c);
            int attrSize = readInt(u + 2);
            u += 6;
            // tests are sorted in decreasing frequency order
            // (based on frequencies observed on typical classes)
            if (attrName.equals("Code")) {
                v = u;
            }
            u += attrSize;
        }
        // reads declared exceptions
        if (w == 0) {
        } else {
            w += 2;
            for (j = 0; j < readUnsignedShort(w); ++j) {
                w += 2;
            }
        }

        // visits the method's code, if any
        MethodCollector mv = classVisitor.visitMethod(access, name, desc);

        if (mv != null && v != 0) {
            int codeLength = readInt(v + 4);
            v += 8;

            int codeStart = v;
            int codeEnd = v + codeLength;
            v = codeEnd;

            j = readUnsignedShort(v);
            v += 2;
            for (; j > 0; --j) {
                v += 8;
            }
            // parses the local variable, line number tables, and code
            // attributes
            int varTable = 0;
            int varTypeTable = 0;
            j = readUnsignedShort(v);
            v += 2;
            for (; j > 0; --j) {
                attrName = readUTF8(v, c);
                if (attrName.equals("LocalVariableTable")) {
                    varTable = v + 6;
                } else if (attrName.equals("LocalVariableTypeTable")) {
                    varTypeTable = v + 6;
                }
                v += 6 + readInt(v + 2);
            }

            v = codeStart;
            // visits the local variable tables
            if (varTable != 0) {
                if (varTypeTable != 0) {
                    k = readUnsignedShort(varTypeTable) * 3;
                    w = varTypeTable + 2;
                    int[] typeTable = new int[k];
                    while (k > 0) {
                        typeTable[--k] = w + 6; // signature
                        typeTable[--k] = readUnsignedShort(w + 8); // index
                        typeTable[--k] = readUnsignedShort(w); // start
                        w += 10;
                    }
                }
                k = readUnsignedShort(varTable);
                w = varTable + 2;
                for (; k > 0; --k) {
                    int index = readUnsignedShort(w + 8);
                    mv.visitLocalVariable(readUTF8(w + 4, c), index);
                    w += 10;
                }
            }
        }
        return u;
    }

    private int readUnsignedShort(final int index) {
        byte[] b = this.b;
        return ((b[index] & 0xFF) << 8) | (b[index + 1] & 0xFF);
    }

    private int readInt(final int index) {
        byte[] b = this.b;
        return ((b[index] & 0xFF) << 24) | ((b[index + 1] & 0xFF) << 16)
                | ((b[index + 2] & 0xFF) << 8) | (b[index + 3] & 0xFF);
    }

    private String readUTF8(int index, final char[] buf) {
        int item = readUnsignedShort(index);
        String s = strings[item];
        if (s != null) {
            return s;
        }
        index = items[item];
        return strings[item] = readUTF(index + 2, readUnsignedShort(index), buf);
    }

    private String readUTF(int index, final int utfLen, final char[] buf) {
        int endIndex = index + utfLen;
        byte[] b = this.b;
        int strLen = 0;
        int c;
        int st = 0;
        char cc = 0;
        while (index < endIndex) {
            c = b[index++];
            switch (st) {
                case 0:
                    c = c & 0xFF;
                    if (c < 0x80) {  // 0xxxxxxx
                        buf[strLen++] = (char) c;
                    } else if (c < 0xE0 && c > 0xBF) {  // 110x xxxx 10xx xxxx
                        cc = (char) (c & 0x1F);
                        st = 1;
                    } else {  // 1110 xxxx 10xx xxxx 10xx xxxx
                        cc = (char) (c & 0x0F);
                        st = 2;
                    }
                    break;

                case 1:  // byte 2 of 2-byte char or byte 3 of 3-byte char
                    buf[strLen++] = (char) ((cc << 6) | (c & 0x3F));
                    st = 0;
                    break;

                case 2:  // byte 2 of 3-byte char
                    cc = (char) ((cc << 6) | (c & 0x3F));
                    st = 1;
                    break;
            }
        }
        return new String(buf, 0, strLen);
    }
}
