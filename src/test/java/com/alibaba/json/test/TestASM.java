package com.alibaba.json.test;

import java.io.FileOutputStream;
import java.lang.reflect.Method;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.asm.ClassWriter;
import com.alibaba.fastjson.asm.MethodVisitor;
import com.alibaba.fastjson.asm.MethodWriter;
import com.alibaba.fastjson.asm.Opcodes;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.json.test.benchmark.encode.EishayEncode;

public class TestASM extends TestCase implements Opcodes {

    // public void test_0() throws Exception {
    //
    // ClassWriter cw = new ClassWriter(0);
    // cw.visit(V1_1, ACC_PUBLIC, "Example", null, "java/lang/Object", null);
    //
    // MethodVisitor mw = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
    // mw.visitVarInsn(ALOAD, 0);
    // mw.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V");
    // mw.visitInsn(RETURN);
    // mw.visitMaxs(1, 1);
    // mw.visitEnd();
    // mw = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "main", "([Ljava/lang/String;)V", null, null);
    // mw.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
    // mw.visitLdcInsn("Hello world!");
    // mw.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V");
    // mw.visitInsn(RETURN);
    // mw.visitMaxs(2, 2);
    // mw.visitEnd();
    // byte[] code = cw.toByteArray();
    // FileOutputStream fos = new FileOutputStream("Example.class");
    // fos.write(code);
    // fos.close();
    //
    // MyClassLoader loader = new MyClassLoader();
    //
    // Class exampleClass = loader.defineClassF("Example", code, 0, code.length);
    // exampleClass.getMethods()[0].invoke(null, new Object[] { null });
    // }

    public void test_asm() throws Exception {
        String text = JSON.toJSONString(EishayEncode.mediaContent);
        System.out.println(text);
    }
    
    public void test_1() throws Exception {
        ClassWriter cw = new ClassWriter();
        cw.visit(V1_5, ACC_PUBLIC + ACC_SUPER, "DateSerializer", "java/lang/Object", new String[] { "com/alibaba/fastjson/serializer/ObjectSerializer" });

        MethodVisitor mw = new MethodWriter(cw, ACC_PUBLIC, "<init>", "()V", null, null);
        mw.visitVarInsn(ALOAD, 0);
        mw.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V");
        mw.visitInsn(RETURN);
        mw.visitMaxs(1, 1);
        mw.visitEnd();

        mw = new MethodWriter(cw, ACC_PUBLIC, "write", "(Lcom/alibaba/fastjson/serializer/JSONSerializer;Ljava/lang/Object;)V", null, new String[] { "java/io/IOException" });

        mw.visitVarInsn(ALOAD, 1); // serializer
        mw.visitMethodInsn(INVOKEVIRTUAL, "com/alibaba/fastjson/serializer/JSONSerializer", "getWriter", "()Lcom/alibaba/fastjson/serializer/SerializeWriter;");
        mw.visitVarInsn(ASTORE, 3); // out

        mw.visitVarInsn(ALOAD, 2); // obj
        mw.visitTypeInsn(CHECKCAST, getCastType(Entity.class)); // serializer
        mw.visitVarInsn(ASTORE, 4); // obj
        
        mw.visitVarInsn(ALOAD, 3); // out
        mw.visitLdcInsn("{");
        mw.visitMethodInsn(INVOKEVIRTUAL, getCastType(SerializeWriter.class), "writeString", "(Ljava/lang/String;)V");
        
        mw.visitVarInsn(ALOAD, 3); // out
        mw.visitLdcInsn("\"id\":");
        mw.visitMethodInsn(INVOKEVIRTUAL, getCastType(SerializeWriter.class), "write", "(Ljava/lang/String;)V");
        
        mw.visitVarInsn(ALOAD, 3); // out
        mw.visitVarInsn(ALOAD, 4); // entity
        mw.visitMethodInsn(INVOKEVIRTUAL, getCastType(Entity.class), "getId", "()I");
        mw.visitMethodInsn(INVOKEVIRTUAL, getCastType(SerializeWriter.class), "writeInt", "(I)V");
        
        mw.visitVarInsn(ALOAD, 3); // out
        mw.visitLdcInsn("\",name\":");
        mw.visitMethodInsn(INVOKEVIRTUAL, getCastType(SerializeWriter.class), "write", "(Ljava/lang/String;)V");
        
        mw.visitVarInsn(ALOAD, 3); // out
        mw.visitVarInsn(ALOAD, 4); // entity
        mw.visitMethodInsn(INVOKEVIRTUAL, getCastType(Entity.class), "getName", "()Ljava/lang/String;");
        mw.visitMethodInsn(INVOKEVIRTUAL, getCastType(SerializeWriter.class), "writeString", "(Ljava/lang/String;)V");

        mw.visitInsn(RETURN);
        mw.visitMaxs(3, 16);
        mw.visitEnd();

        byte[] code = cw.toByteArray();
        FileOutputStream fos = new FileOutputStream("Example.class");
        fos.write(code);
        fos.close();

        MyClassLoader loader = new MyClassLoader(com.alibaba.fastjson.serializer.ObjectSerializer.class.getClassLoader());

        Class<?> exampleClass = loader.defineClassF("DateSerializer", code, 0, code.length);
        Method[] methods = exampleClass.getMethods();
        Object instance = exampleClass.newInstance();

        SerializeWriter out = new SerializeWriter();
        JSONSerializer serializer = new JSONSerializer(out);
        Entity obj = new Entity();
        methods[0].invoke(instance, serializer, obj);

        System.out.println(out.toString());
    }

    String getCastType(Class<?> returnType) {
        if (returnType.isPrimitive()) {
            return getWrapperType(returnType);
        } else {
            return getAsmTypeAsString(returnType, false);
        }
    }

    private String getWrapperType(Class<?> type) {
        if (Integer.TYPE.equals(type)) {
            return Integer.class.getCanonicalName().replaceAll("\\.", "/");
        } else if (Boolean.TYPE.equals(type)) {
            return Boolean.class.getCanonicalName().replaceAll("\\.", "/");
        } else if (Character.TYPE.equals(type)) {
            return Character.class.getCanonicalName().replaceAll("\\.", "/");
        } else if (Byte.TYPE.equals(type)) {
            return Byte.class.getCanonicalName().replaceAll("\\.", "/");
        } else if (Short.TYPE.equals(type)) {
            return Short.class.getCanonicalName().replaceAll("\\.", "/");
        } else if (Float.TYPE.equals(type)) {
            return Float.class.getCanonicalName().replaceAll("\\.", "/");
        } else if (Long.TYPE.equals(type)) {
            return Long.class.getCanonicalName().replaceAll("\\.", "/");
        } else if (Double.TYPE.equals(type)) {
            return Double.class.getCanonicalName().replaceAll("\\.", "/");
        } else if (Void.TYPE.equals(type)) {
            return Void.class.getCanonicalName().replaceAll("\\.", "/");
        }

        throw new IllegalStateException("Type: " + type.getCanonicalName() + " is not a primitive type");
    }

    public String getAsmTypeAsString(Class<?> parameterType, boolean wrap) {
        if (parameterType.isArray()) {
            if (parameterType.getComponentType().isPrimitive()) {
                Class<?> componentType = parameterType.getComponentType();
                return "[" + getPrimitiveLetter(componentType);
            } else {
                return "[" + getAsmTypeAsString(parameterType.getComponentType(), true);
            }
        } else {
            if (!parameterType.isPrimitive()) {
                String clsName = parameterType.getCanonicalName();

                if (parameterType.isMemberClass()) {
                    int lastDot = clsName.lastIndexOf(".");
                    clsName = clsName.substring(0, lastDot) + "$" + clsName.substring(lastDot + 1);
                }
                if (wrap) {
                    return "L" + clsName.replaceAll("\\.", "/") + ";";
                } else {
                    return clsName.replaceAll("\\.", "/");
                }
            } else {
                return getPrimitiveLetter(parameterType);
            }
        }
    }

    private String getPrimitiveLetter(Class<?> type) {
        if (Integer.TYPE.equals(type)) {
            return "I";
        } else if (Void.TYPE.equals(type)) {
            return "V";
        } else if (Boolean.TYPE.equals(type)) {
            return "Z";
        } else if (Character.TYPE.equals(type)) {
            return "C";
        } else if (Byte.TYPE.equals(type)) {
            return "B";
        } else if (Short.TYPE.equals(type)) {
            return "S";
        } else if (Float.TYPE.equals(type)) {
            return "F";
        } else if (Long.TYPE.equals(type)) {
            return "J";
        } else if (Double.TYPE.equals(type)) {
            return "D";
        }

        throw new IllegalStateException("Type: " + type.getCanonicalName() + " is not a primitive type");
    }

    public static class Entity {

        private int    id;
        private String name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }

    public static class MyClassLoader extends ClassLoader {

        public MyClassLoader(ClassLoader parent){
            super(parent);
        }

        public Class<?> defineClassF(String name, byte[] b, int off, int len) throws ClassFormatError {
            return defineClass(name, b, off, len, null);
        }
    }

    public static class Foo {

        public void execute() {
            System.out.println("Hello World");
        }
    }

}
