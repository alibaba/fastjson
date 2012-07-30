package com.alibaba.fastjson.serializer;

import static com.alibaba.fastjson.util.ASMUtils.getDesc;
import static com.alibaba.fastjson.util.ASMUtils.getType;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.asm.ClassWriter;
import com.alibaba.fastjson.asm.FieldVisitor;
import com.alibaba.fastjson.asm.Label;
import com.alibaba.fastjson.asm.MethodVisitor;
import com.alibaba.fastjson.asm.Opcodes;
import com.alibaba.fastjson.util.ASMClassLoader;
import com.alibaba.fastjson.util.ASMUtils;
import com.alibaba.fastjson.util.FieldInfo;
import com.alibaba.fastjson.util.TypeUtils;

public class ASMSerializerFactory implements Opcodes {

    private ASMClassLoader classLoader = new ASMClassLoader();

    public ObjectSerializer createJavaBeanSerializer(Class<?> clazz) throws Exception {
        return createJavaBeanSerializer(clazz, (Map<String, String>) null);
    }

    private final AtomicLong seed = new AtomicLong();

    public String getGenClassName(Class<?> clazz) {
        return "Serializer_" + seed.incrementAndGet();
    }

    static class Context {

        private final String className;

        public Context(String className){
            this.className = className;
        }

        private int                  variantIndex = 8;

        private Map<String, Integer> variants     = new HashMap<String, Integer>();

        public int serializer() {
            return 1;
        }

        public String getClassName() {
            return className;
        }

        public int obj() {
            return 2;
        }

        public int paramFieldName() {
            return 3;
        }

        public int paramFieldType() {
            return 4;
        }

        public int fieldName() {
            return 5;
        }

        public int original() {
            return 6;
        }

        public int processValue() {
            return 7;
        }

        public int getVariantCount() {
            return variantIndex;
        }

        public int var(String name) {
            Integer i = variants.get(name);
            if (i == null) {
                variants.put(name, variantIndex++);
            }
            i = variants.get(name);
            return i.intValue();
        }

        public int var(String name, int increment) {
            Integer i = variants.get(name);
            if (i == null) {
                variants.put(name, variantIndex);
                variantIndex += increment;
            }
            i = variants.get(name);
            return i.intValue();
        }
    }

    public ObjectSerializer createJavaBeanSerializer(Class<?> clazz, Map<String, String> aliasMap) throws Exception {
        if (clazz.isPrimitive()) {
            throw new JSONException("unsupportd class " + clazz.getName());
        }

        List<FieldInfo> getters = TypeUtils.computeGetters(clazz, aliasMap, false);

        String className = getGenClassName(clazz);

        ClassWriter cw = new ClassWriter();
        cw.visit(V1_5, ACC_PUBLIC + ACC_SUPER, className, "java/lang/Object",
                 new String[] { "com/alibaba/fastjson/serializer/ObjectSerializer" });

        {
            FieldVisitor fw = cw.visitField(ACC_PRIVATE, "nature", getDesc(JavaBeanSerializer.class));
            fw.visitEnd();
        }

        for (FieldInfo fieldInfo : getters) {
            {
                FieldVisitor fw = cw.visitField(ACC_PUBLIC, fieldInfo.getName() + "_asm_fieldPrefix",
                                                "Ljava/lang/reflect/Type;");
                fw.visitEnd();
            }

            FieldVisitor fw = cw.visitField(ACC_PUBLIC, fieldInfo.getName() + "_asm_fieldType",
                                            "Ljava/lang/reflect/Type;");
            fw.visitEnd();
        }

        MethodVisitor mw = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
        mw.visitVarInsn(ALOAD, 0);
        mw.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V");

        // mw.visitFieldInsn(PUTFIELD, context.getClassName(), fieldInfo.getName() + "_asm_prefix__", "[C");

        for (FieldInfo fieldInfo : getters) {
            mw.visitVarInsn(ALOAD, 0);

            mw.visitLdcInsn(com.alibaba.fastjson.asm.Type.getType(getDesc(fieldInfo.getDeclaringClass())));

            if (fieldInfo.getMethod() != null) {
                mw.visitLdcInsn(fieldInfo.getMethod().getName());
                mw.visitMethodInsn(INVOKESTATIC, getType(ASMUtils.class), "getMethodType",
                                   "(Ljava/lang/Class;Ljava/lang/String;)Ljava/lang/reflect/Type;");

            } else {
                mw.visitLdcInsn(fieldInfo.getField().getName());
                mw.visitMethodInsn(INVOKESTATIC, getType(ASMUtils.class), "getFieldType",
                                   "(Ljava/lang/Class;Ljava/lang/String;)Ljava/lang/reflect/Type;");
            }

            mw.visitFieldInsn(PUTFIELD, className, fieldInfo.getName() + "_asm_fieldType", "Ljava/lang/reflect/Type;");
        }

        mw.visitInsn(RETURN);
        mw.visitMaxs(4, 4);
        mw.visitEnd();

        {
            Context context = new Context(className);

            mw = cw.visitMethod(ACC_PUBLIC,
                                "write",
                                "(Lcom/alibaba/fastjson/serializer/JSONSerializer;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/reflect/Type;)V",
                                null, new String[] { "java/io/IOException" });

            mw.visitVarInsn(ALOAD, context.serializer()); // serializer
            mw.visitMethodInsn(INVOKEVIRTUAL, getType(JSONSerializer.class), "getWriter",
                               "()" + getDesc(SerializeWriter.class));
            mw.visitVarInsn(ASTORE, context.var("out"));

            Label _else = new Label();

            mw.visitVarInsn(ALOAD, context.var("out"));
            mw.visitFieldInsn(GETSTATIC, getType(SerializerFeature.class), "SortField",
                              "L" + getType(SerializerFeature.class) + ";");
            mw.visitMethodInsn(INVOKEVIRTUAL, getType(SerializeWriter.class), "isEnabled",
                               "(" + "L" + getType(SerializerFeature.class) + ";" + ")Z");

            mw.visitJumpInsn(IFEQ, _else);
            mw.visitVarInsn(ALOAD, 0);
            mw.visitVarInsn(ALOAD, 1);
            mw.visitVarInsn(ALOAD, 2);
            mw.visitVarInsn(ALOAD, 3);
            mw.visitVarInsn(ALOAD, context.paramFieldType());
            mw.visitMethodInsn(INVOKEVIRTUAL, className, "write1",
                               "(Lcom/alibaba/fastjson/serializer/JSONSerializer;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/reflect/Type;)V");
            mw.visitInsn(RETURN);

            mw.visitLabel(_else);
            mw.visitVarInsn(ALOAD, context.obj()); // obj
            mw.visitTypeInsn(CHECKCAST, getType(clazz)); // serializer
            mw.visitVarInsn(ASTORE, context.var("entity")); // obj

            generateWriteMethod(clazz, mw, getters, context);

            mw.visitInsn(RETURN);
            mw.visitMaxs(5, context.getVariantCount() + 1);
            mw.visitEnd();
        }

        {
            List<FieldInfo> sortedGetters = TypeUtils.computeGetters(clazz, aliasMap, true);

            // sortField support
            Context context = new Context(className);

            mw = cw.visitMethod(ACC_PUBLIC,
                                "write1",
                                "(Lcom/alibaba/fastjson/serializer/JSONSerializer;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/reflect/Type;)V",
                                null, new String[] { "java/io/IOException" });

            mw.visitVarInsn(ALOAD, context.serializer()); // serializer
            mw.visitMethodInsn(INVOKEVIRTUAL, getType(JSONSerializer.class), "getWriter",
                               "()" + getDesc(SerializeWriter.class));
            mw.visitVarInsn(ASTORE, context.var("out"));

            mw.visitVarInsn(ALOAD, context.obj()); // obj
            mw.visitTypeInsn(CHECKCAST, getType(clazz)); // serializer
            mw.visitVarInsn(ASTORE, context.var("entity")); // obj

            generateWriteMethod(clazz, mw, sortedGetters, context);

            mw.visitInsn(RETURN);
            mw.visitMaxs(5, context.getVariantCount() + 1);
            mw.visitEnd();
        }

        byte[] code = cw.toByteArray();
        //
        // org.apache.commons.io.IOUtils.write(code, new java.io.FileOutputStream(
        // "/usr/alibaba/workspace-3.7/fastjson-asm/target/classes/"
        // + className + ".class"));

        Class<?> exampleClass = classLoader.defineClassPublic(className, code, 0, code.length);
        Object instance = exampleClass.newInstance();

        return (ObjectSerializer) instance;
    }

    private void generateWriteMethod(Class<?> clazz, MethodVisitor mw, List<FieldInfo> getters, Context context)
                                                                                                                throws Exception {
        Label end = new Label();

        int size = getters.size();

        {
            // 格式化输出不走asm 优化
            Label endFormat_ = new Label();
            Label notNull_ = new Label();
            mw.visitVarInsn(ALOAD, context.var("out"));
            mw.visitFieldInsn(GETSTATIC, getType(SerializerFeature.class), "PrettyFormat",
                              "L" + getType(SerializerFeature.class) + ";");
            mw.visitMethodInsn(INVOKEVIRTUAL, getType(SerializeWriter.class), "isEnabled",
                               "(" + "L" + getType(SerializerFeature.class) + ";" + ")Z");
            mw.visitJumpInsn(IFEQ, endFormat_);

            mw.visitVarInsn(ALOAD, 0);
            mw.visitFieldInsn(GETFIELD, context.getClassName(), "nature", getDesc(JavaBeanSerializer.class));
            mw.visitJumpInsn(IFNONNULL, notNull_);

            initNature(clazz, mw, context);

            // /////
            mw.visitLabel(notNull_);

            mw.visitVarInsn(ALOAD, 0);
            mw.visitFieldInsn(GETFIELD, context.getClassName(), "nature", getDesc(JavaBeanSerializer.class));
            mw.visitVarInsn(ALOAD, 1);
            mw.visitVarInsn(ALOAD, 2);
            mw.visitVarInsn(ALOAD, 3);
            mw.visitVarInsn(ALOAD, 4);
            mw.visitMethodInsn(INVOKEVIRTUAL, getType(JavaBeanSerializer.class), "write",
                               "(Lcom/alibaba/fastjson/serializer/JSONSerializer;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/reflect/Type;)V");
            mw.visitInsn(RETURN);

            mw.visitLabel(endFormat_);
        }

        {
            // if (serializer.containsReference(object)) {

            Label endRef_ = new Label();
            Label notNull_ = new Label();

            mw.visitVarInsn(ALOAD, context.serializer());
            mw.visitVarInsn(ALOAD, context.obj());
            mw.visitMethodInsn(INVOKEVIRTUAL, getType(JSONSerializer.class), "containsReference",
                               "(Ljava/lang/Object;)Z");
            mw.visitJumpInsn(IFEQ, endRef_);

            mw.visitVarInsn(ALOAD, 0);
            mw.visitFieldInsn(GETFIELD, context.getClassName(), "nature", getDesc(JavaBeanSerializer.class));
            mw.visitJumpInsn(IFNONNULL, notNull_);

            initNature(clazz, mw, context);

            // /////
            mw.visitLabel(notNull_);
            mw.visitVarInsn(ALOAD, 0);
            mw.visitFieldInsn(GETFIELD, context.getClassName(), "nature", getDesc(JavaBeanSerializer.class));
            mw.visitVarInsn(ALOAD, 1);
            mw.visitVarInsn(ALOAD, 2);
            mw.visitMethodInsn(INVOKEVIRTUAL, getType(JavaBeanSerializer.class), "writeReference",
                               "(Lcom/alibaba/fastjson/serializer/JSONSerializer;Ljava/lang/Object;)V");

            mw.visitInsn(RETURN);

            mw.visitLabel(endRef_);
        }

        {
            mw.visitVarInsn(ALOAD, context.serializer());
            mw.visitMethodInsn(INVOKEVIRTUAL, getType(JSONSerializer.class), "getContext",
                               "()Lcom/alibaba/fastjson/serializer/SerialContext;");
            mw.visitVarInsn(ASTORE, context.var("parent"));

            mw.visitVarInsn(ALOAD, context.serializer());
            mw.visitVarInsn(ALOAD, context.var("parent"));
            mw.visitVarInsn(ALOAD, context.obj());
            mw.visitVarInsn(ALOAD, context.paramFieldName());
            mw.visitMethodInsn(INVOKEVIRTUAL, getType(JSONSerializer.class), "setContext",
                               "(Lcom/alibaba/fastjson/serializer/SerialContext;Ljava/lang/Object;Ljava/lang/Object;)V");
        }

        // SEPERATO
        {
            Label end_ = new Label();
            Label else_ = new Label();
            Label writeClass_ = new Label();

//            mw.visitVarInsn(ALOAD, context.var("out"));
//            mw.visitFieldInsn(GETSTATIC, getType(SerializerFeature.class), "WriteClassName",
//                              "L" + getType(SerializerFeature.class) + ";");
//            mw.visitMethodInsn(INVOKEVIRTUAL, getType(SerializeWriter.class), "isEnabled",
//                               "(" + "L" + getType(SerializerFeature.class) + ";" + ")Z");
//            mw.visitJumpInsn(IFEQ, else_);
            
            mw.visitVarInsn(ALOAD, context.serializer());
            mw.visitVarInsn(ALOAD, context.paramFieldType());
            mw.visitVarInsn(ALOAD, context.obj());
            mw.visitMethodInsn(INVOKEVIRTUAL, getType(JSONSerializer.class), "isWriteClassName",
                               "(Ljava/lang/reflect/Type;Ljava/lang/Object;)Z");
            mw.visitJumpInsn(IFEQ, else_);

            // mw.visitVarInsn(ALOAD, context.paramFieldType());
            // mw.visitJumpInsn(IFNULL, writeClass_);

            // IFNULL
            mw.visitVarInsn(ALOAD, context.paramFieldType());
            mw.visitVarInsn(ALOAD, context.obj());
            mw.visitMethodInsn(INVOKEVIRTUAL, getType(Object.class), "getClass", "()Ljava/lang/Class;");
            mw.visitJumpInsn(IF_ACMPEQ, else_);

            mw.visitLabel(writeClass_);
            mw.visitVarInsn(ALOAD, context.var("out"));
            mw.visitLdcInsn("{\"@type\":\"" + clazz.getName() + "\"");
            mw.visitMethodInsn(INVOKEVIRTUAL, getType(SerializeWriter.class), "write", "(Ljava/lang/String;)V");
            mw.visitVarInsn(BIPUSH, ',');
            mw.visitJumpInsn(GOTO, end_);

            mw.visitLabel(else_);
            mw.visitVarInsn(BIPUSH, '{');

            mw.visitLabel(end_);
        }

        mw.visitVarInsn(ISTORE, context.var("seperator"));

        for (int i = 0; i < size; ++i) {
            FieldInfo property = getters.get(i);
            Class<?> propertyClass = property.getFieldClass();

            mw.visitLdcInsn(property.getName());
            mw.visitVarInsn(ASTORE, context.fieldName());

            if (propertyClass == byte.class) {
                _byte(clazz, mw, property, context);
            } else if (propertyClass == short.class) {
                _short(clazz, mw, property, context);
            } else if (propertyClass == int.class) {
                _int(clazz, mw, property, context);
            } else if (propertyClass == long.class) {
                _long(clazz, mw, property, context);
            } else if (propertyClass == float.class) {
                _float(clazz, mw, property, context);
            } else if (propertyClass == double.class) {
                _double(clazz, mw, property, context);
            } else if (propertyClass == boolean.class) {
                _boolean(clazz, mw, property, context);
            } else if (propertyClass == char.class) {
                _char(clazz, mw, property, context);
            } else if (propertyClass == String.class) {
                _string(clazz, mw, property, context);
            } else if (propertyClass == BigDecimal.class) {
                _decimal(clazz, mw, property, context);
            } else if (List.class.isAssignableFrom(propertyClass)) {
                _list(clazz, mw, property, context);
                // _object(clazz, mw, property, context);
            } else if (propertyClass.isEnum()) {
                _enum(clazz, mw, property, context);
            } else {
                _object(clazz, mw, property, context);
            }
        }

        Label _if = new Label();
        Label _else = new Label();
        Label _end_if = new Label();

        mw.visitLabel(_if);

        // if (seperator == '{')
        mw.visitVarInsn(ILOAD, context.var("seperator"));
        mw.visitIntInsn(BIPUSH, '{');
        mw.visitJumpInsn(IF_ICMPNE, _else);

        mw.visitVarInsn(ALOAD, context.var("out"));
        mw.visitLdcInsn("{}");
        mw.visitMethodInsn(INVOKEVIRTUAL, getType(SerializeWriter.class), "write", "(Ljava/lang/String;)V");

        mw.visitJumpInsn(GOTO, _end_if);

        mw.visitLabel(_else);

        mw.visitVarInsn(ALOAD, context.var("out"));
        mw.visitVarInsn(BIPUSH, '}');
        mw.visitMethodInsn(INVOKEVIRTUAL, getType(SerializeWriter.class), "write", "(C)V");

        mw.visitLabel(_end_if);
        mw.visitLabel(end);

        mw.visitVarInsn(ALOAD, context.serializer());
        mw.visitVarInsn(ALOAD, context.var("parent"));
        mw.visitMethodInsn(INVOKEVIRTUAL, getType(JSONSerializer.class), "setContext",
                           "(Lcom/alibaba/fastjson/serializer/SerialContext;)V");

    }

    private void initNature(Class<?> clazz, MethodVisitor mw, Context context) {
        mw.visitVarInsn(ALOAD, 0);
        mw.visitTypeInsn(NEW, getType(JavaBeanSerializer.class));
        mw.visitInsn(DUP);
        mw.visitLdcInsn(com.alibaba.fastjson.asm.Type.getType(getDesc(clazz)));
        mw.visitMethodInsn(INVOKESPECIAL, getType(JavaBeanSerializer.class), "<init>", "(" + getDesc(Class.class)
                                                                                       + ")V");
        mw.visitFieldInsn(PUTFIELD, context.getClassName(), "nature", getDesc(JavaBeanSerializer.class));
    }

    private void _object(Class<?> clazz, MethodVisitor mw, FieldInfo property, Context context) {
        Label _end = new Label();

        _nameApply(mw, property, context, _end);
        _get(mw, context, property);
        mw.visitVarInsn(ASTORE, context.var("object"));

        _filters(mw, property, context, _end);

        _writeObject(mw, property, context, _end);

        mw.visitLabel(_end);
    }

    private void _enum(Class<?> clazz, MethodVisitor mw, FieldInfo property, Context context) {
        boolean writeEnumUsingToString = false;
        JSONField annotation = property.getAnnotation(JSONField.class);
        if (annotation != null) {
            for (SerializerFeature feature : annotation.serialzeFeatures()) {
                if (feature == SerializerFeature.WriteEnumUsingToString) {
                    writeEnumUsingToString = true;
                }
            }
        }

        Label _not_null = new Label();
        Label _end_if = new Label();
        Label _end = new Label();

        _nameApply(mw, property, context, _end);
        _get(mw, context, property);
        mw.visitTypeInsn(CHECKCAST, getType(Enum.class)); // cast
        mw.visitVarInsn(ASTORE, context.var("enum"));

        _filters(mw, property, context, _end);

        mw.visitVarInsn(ALOAD, context.var("enum"));
        mw.visitJumpInsn(IFNONNULL, _not_null);
        _if_write_null(mw, property, context);
        mw.visitJumpInsn(GOTO, _end_if);

        mw.visitLabel(_not_null);
        mw.visitVarInsn(ALOAD, context.var("out"));
        mw.visitVarInsn(ILOAD, context.var("seperator"));
        mw.visitVarInsn(ALOAD, context.fieldName());
        mw.visitVarInsn(ALOAD, context.var("enum"));

        if (writeEnumUsingToString) {
            mw.visitMethodInsn(INVOKEVIRTUAL, getType(Object.class), "toString", "()Ljava/lang/String;");
            mw.visitMethodInsn(INVOKEVIRTUAL, getType(SerializeWriter.class), "writeFieldValue",
                               "(CLjava/lang/String;Ljava/lang/String;)V");
        } else {
            mw.visitMethodInsn(INVOKEVIRTUAL, getType(SerializeWriter.class), "writeFieldValue",
                               "(CLjava/lang/String;L" + getType(Enum.class) + ";)V");
        }

        _seperator(mw, context);

        mw.visitLabel(_end_if);

        mw.visitLabel(_end);
    }

    private void _long(Class<?> clazz, MethodVisitor mw, FieldInfo property, Context context) {
        Label _end = new Label();

        _nameApply(mw, property, context, _end);
        _get(mw, context, property);
        mw.visitVarInsn(LSTORE, context.var("long", 2));

        _filters(mw, property, context, _end);

        mw.visitVarInsn(ALOAD, context.var("out"));
        mw.visitVarInsn(ILOAD, context.var("seperator"));
        mw.visitVarInsn(ALOAD, context.fieldName());
        mw.visitVarInsn(LLOAD, context.var("long", 2));
        mw.visitMethodInsn(INVOKEVIRTUAL, getType(SerializeWriter.class), "writeFieldValue", "(CLjava/lang/String;J)V");

        _seperator(mw, context);

        mw.visitLabel(_end);
    }

    private void _float(Class<?> clazz, MethodVisitor mw, FieldInfo property, Context context) {
        Label _end = new Label();

        _nameApply(mw, property, context, _end);
        _get(mw, context, property);
        mw.visitVarInsn(FSTORE, context.var("float"));

        _filters(mw, property, context, _end);

        mw.visitVarInsn(ALOAD, context.var("out"));
        mw.visitVarInsn(ILOAD, context.var("seperator"));
        mw.visitVarInsn(ALOAD, context.fieldName());
        mw.visitVarInsn(FLOAD, context.var("float"));
        mw.visitMethodInsn(INVOKEVIRTUAL, getType(SerializeWriter.class), "writeFieldValue", "(CLjava/lang/String;F)V");

        _seperator(mw, context);

        mw.visitLabel(_end);
    }

    private void _double(Class<?> clazz, MethodVisitor mw, FieldInfo property, Context context) {
        Label _end = new Label();

        _nameApply(mw, property, context, _end);
        _get(mw, context, property);
        mw.visitVarInsn(DSTORE, context.var("double", 2));

        _filters(mw, property, context, _end);

        mw.visitVarInsn(ALOAD, context.var("out"));
        mw.visitVarInsn(ILOAD, context.var("seperator"));
        mw.visitVarInsn(ALOAD, context.fieldName());
        mw.visitVarInsn(DLOAD, context.var("double", 2));
        mw.visitMethodInsn(INVOKEVIRTUAL, getType(SerializeWriter.class), "writeFieldValue", "(CLjava/lang/String;D)V");

        _seperator(mw, context);

        mw.visitLabel(_end);
    }

    private void _char(Class<?> clazz, MethodVisitor mw, FieldInfo property, Context context) {
        Label _end = new Label();

        _nameApply(mw, property, context, _end);
        _get(mw, context, property);
        mw.visitVarInsn(ISTORE, context.var("char"));

        _filters(mw, property, context, _end);

        mw.visitVarInsn(ALOAD, context.var("out"));
        mw.visitVarInsn(ILOAD, context.var("seperator"));
        mw.visitVarInsn(ALOAD, context.fieldName());
        mw.visitVarInsn(ILOAD, context.var("char"));

        mw.visitMethodInsn(INVOKEVIRTUAL, getType(SerializeWriter.class), "writeFieldValue", "(CLjava/lang/String;C)V");

        _seperator(mw, context);

        mw.visitLabel(_end);
    }

    private void _boolean(Class<?> clazz, MethodVisitor mw, FieldInfo property, Context context) {
        Label _end = new Label();

        _nameApply(mw, property, context, _end);
        _get(mw, context, property);
        mw.visitVarInsn(ISTORE, context.var("boolean"));

        _filters(mw, property, context, _end);

        mw.visitVarInsn(ALOAD, context.var("out"));
        mw.visitVarInsn(ILOAD, context.var("seperator"));
        mw.visitVarInsn(ALOAD, context.fieldName());
        mw.visitVarInsn(ILOAD, context.var("boolean"));

        mw.visitMethodInsn(INVOKEVIRTUAL, getType(SerializeWriter.class), "writeFieldValue", "(CLjava/lang/String;Z)V");

        _seperator(mw, context);

        mw.visitLabel(_end);
    }

    private void _get(MethodVisitor mw, Context context, FieldInfo property) {
        Method method = property.getMethod();
        if (method != null) {
            mw.visitVarInsn(ALOAD, context.var("entity"));
            mw.visitMethodInsn(INVOKEVIRTUAL, getType(method.getDeclaringClass()), method.getName(), getDesc(method));
        } else {
            mw.visitVarInsn(ALOAD, context.var("entity"));
            mw.visitFieldInsn(GETFIELD, getType(property.getDeclaringClass()), property.getName(),
                              getDesc(property.getFieldClass()));
        }
    }

    private void _byte(Class<?> clazz, MethodVisitor mw, FieldInfo property, Context context) {
        Label _end = new Label();

        _nameApply(mw, property, context, _end);
        _get(mw, context, property);
        mw.visitVarInsn(ISTORE, context.var("byte"));

        _filters(mw, property, context, _end);

        mw.visitVarInsn(ALOAD, context.var("out"));
        mw.visitVarInsn(ILOAD, context.var("seperator"));
        mw.visitVarInsn(ALOAD, context.fieldName());
        mw.visitVarInsn(ILOAD, context.var("byte"));

        mw.visitMethodInsn(INVOKEVIRTUAL, getType(SerializeWriter.class), "writeFieldValue", "(CLjava/lang/String;I)V");

        _seperator(mw, context);

        mw.visitLabel(_end);
    }

    private void _short(Class<?> clazz, MethodVisitor mw, FieldInfo property, Context context) {
        Label _end = new Label();

        _nameApply(mw, property, context, _end);
        _get(mw, context, property);
        mw.visitVarInsn(ISTORE, context.var("short"));

        _filters(mw, property, context, _end);

        mw.visitVarInsn(ALOAD, context.var("out"));
        mw.visitVarInsn(ILOAD, context.var("seperator"));
        mw.visitVarInsn(ALOAD, context.fieldName());
        mw.visitVarInsn(ILOAD, context.var("short"));

        mw.visitMethodInsn(INVOKEVIRTUAL, getType(SerializeWriter.class), "writeFieldValue", "(CLjava/lang/String;I)V");

        _seperator(mw, context);

        mw.visitLabel(_end);
    }

    private void _int(Class<?> clazz, MethodVisitor mw, FieldInfo property, Context context) {
        Label _end = new Label();

        _nameApply(mw, property, context, _end);
        _get(mw, context, property);
        mw.visitVarInsn(ISTORE, context.var("int"));

        _filters(mw, property, context, _end);

        mw.visitVarInsn(ALOAD, context.var("out"));
        mw.visitVarInsn(ILOAD, context.var("seperator"));
        mw.visitVarInsn(ALOAD, context.fieldName());
        mw.visitVarInsn(ILOAD, context.var("int"));

        mw.visitMethodInsn(INVOKEVIRTUAL, getType(SerializeWriter.class), "writeFieldValue", "(CLjava/lang/String;I)V");

        _seperator(mw, context);

        mw.visitLabel(_end);
    }

    private void _decimal(Class<?> clazz, MethodVisitor mw, FieldInfo property, Context context) {
        Label _end = new Label();

        _nameApply(mw, property, context, _end);
        _get(mw, context, property);
        mw.visitVarInsn(ASTORE, context.var("decimal"));

        _filters(mw, property, context, _end);

        Label _if = new Label();
        Label _else = new Label();
        Label _end_if = new Label();

        mw.visitLabel(_if);

        // if (decimalValue == null) {
        mw.visitVarInsn(ALOAD, context.var("decimal"));
        mw.visitJumpInsn(IFNONNULL, _else);
        _if_write_null(mw, property, context);
        mw.visitJumpInsn(GOTO, _end_if);

        mw.visitLabel(_else); // else { out.writeFieldValue(seperator, fieldName, fieldValue)

        mw.visitVarInsn(ALOAD, context.var("out"));
        mw.visitVarInsn(ILOAD, context.var("seperator"));
        mw.visitVarInsn(ALOAD, context.fieldName());
        mw.visitVarInsn(ALOAD, context.var("decimal"));
        mw.visitMethodInsn(INVOKEVIRTUAL, getType(SerializeWriter.class), "writeFieldValue",
                           "(CLjava/lang/String;Ljava/math/BigDecimal;)V");

        _seperator(mw, context);
        mw.visitJumpInsn(GOTO, _end_if);

        mw.visitLabel(_end_if);

        mw.visitLabel(_end);
    }

    private void _string(Class<?> clazz, MethodVisitor mw, FieldInfo property, Context context) {
        Label _end = new Label();

        _nameApply(mw, property, context, _end);
        _get(mw, context, property);
        mw.visitVarInsn(ASTORE, context.var("string"));

        _filters(mw, property, context, _end);

        Label _else = new Label();
        Label _end_if = new Label();

        // if (value == null) {
        mw.visitVarInsn(ALOAD, context.var("string"));
        mw.visitJumpInsn(IFNONNULL, _else);

        _if_write_null(mw, property, context);

        mw.visitJumpInsn(GOTO, _end_if);

        mw.visitLabel(_else); // else { out.writeFieldValue(seperator, fieldName, fieldValue)
        mw.visitVarInsn(ALOAD, context.var("out"));
        mw.visitVarInsn(ILOAD, context.var("seperator"));
        mw.visitVarInsn(ALOAD, context.fieldName());
        mw.visitVarInsn(ALOAD, context.var("string"));
        mw.visitMethodInsn(INVOKEVIRTUAL, getType(SerializeWriter.class), "writeFieldValue",
                           "(CLjava/lang/String;Ljava/lang/String;)V");

        _seperator(mw, context);

        mw.visitLabel(_end_if);

        mw.visitLabel(_end);
    }

    private void _list(Class<?> clazz, MethodVisitor mw, FieldInfo property, Context context) {
        Type propertyType = property.getFieldType();

        Type elementType;
        if (propertyType instanceof Class) {
            elementType = Object.class;
        } else {
            elementType = ((ParameterizedType) propertyType).getActualTypeArguments()[0];
        }

        Class<?> elementClass = null;
        if (elementType instanceof Class<?>) {
            elementClass = (Class<?>) elementType;
        }

        Label _end = new Label();

        Label _if = new Label();
        Label _else = new Label();
        Label _end_if = new Label();

        mw.visitLabel(_if);

        _nameApply(mw, property, context, _end);
        _get(mw, context, property);
        mw.visitTypeInsn(CHECKCAST, getType(List.class)); // cast
        mw.visitVarInsn(ASTORE, context.var("list"));

        _filters(mw, property, context, _end);

        mw.visitVarInsn(ALOAD, context.var("list"));
        mw.visitJumpInsn(IFNONNULL, _else);
        _if_write_null(mw, property, context);
        mw.visitJumpInsn(GOTO, _end_if);

        mw.visitLabel(_else); // else {

        mw.visitVarInsn(ALOAD, context.var("out"));
        mw.visitVarInsn(ILOAD, context.var("seperator"));
        mw.visitMethodInsn(INVOKEVIRTUAL, getType(SerializeWriter.class), "write", "(C)V");

        mw.visitVarInsn(ALOAD, context.var("out"));
        mw.visitVarInsn(ALOAD, context.fieldName());
        mw.visitMethodInsn(INVOKEVIRTUAL, getType(SerializeWriter.class), "writeFieldName", "(Ljava/lang/String;)V");

        //
        mw.visitVarInsn(ALOAD, context.var("list"));
        mw.visitMethodInsn(INVOKEINTERFACE, getType(List.class), "size", "()I");
        mw.visitVarInsn(ISTORE, context.var("int"));

        Label _if_3 = new Label();
        Label _else_3 = new Label();
        Label _end_if_3 = new Label();

        mw.visitLabel(_if_3);

        mw.visitVarInsn(ILOAD, context.var("int"));
        mw.visitInsn(ICONST_0);
        mw.visitJumpInsn(IF_ICMPNE, _else_3);

        mw.visitVarInsn(ALOAD, context.var("out"));
        mw.visitLdcInsn("[]");
        mw.visitMethodInsn(INVOKEVIRTUAL, getType(SerializeWriter.class), "write", "(Ljava/lang/String;)V");

        mw.visitJumpInsn(GOTO, _end_if_3);

        mw.visitLabel(_else_3);

        {
            mw.visitVarInsn(ALOAD, context.serializer());
            mw.visitVarInsn(ALOAD, context.var("list"));
            mw.visitVarInsn(ALOAD, context.fieldName());
            mw.visitMethodInsn(INVOKEVIRTUAL, getType(JSONSerializer.class), "setContext",
                               "(Ljava/lang/Object;Ljava/lang/Object;)V");
        }

        {
            mw.visitVarInsn(ALOAD, context.var("out"));
            mw.visitVarInsn(BIPUSH, '[');
            mw.visitMethodInsn(INVOKEVIRTUAL, getType(SerializeWriter.class), "write", "(C)V");

            // list_serializer = null
            mw.visitInsn(ACONST_NULL);
            mw.visitTypeInsn(CHECKCAST, getType(ObjectSerializer.class)); // cast to string
            mw.visitVarInsn(ASTORE, context.var("list_ser"));

            Label _for = new Label();
            Label _end_for = new Label();

            mw.visitInsn(ICONST_0);
            mw.visitVarInsn(ISTORE, context.var("i"));

            // for (; i < list.size() -1; ++i) {
            mw.visitLabel(_for);
            mw.visitVarInsn(ILOAD, context.var("i"));

            mw.visitVarInsn(ILOAD, context.var("int"));
            mw.visitInsn(ICONST_1);
            mw.visitInsn(ISUB);

            mw.visitJumpInsn(IF_ICMPGE, _end_for); // i < list.size - 1

            if (elementType == String.class) {
                // out.write((String)list.get(i));
                mw.visitVarInsn(ALOAD, context.var("out"));
                mw.visitVarInsn(ALOAD, context.var("list"));
                mw.visitVarInsn(ILOAD, context.var("i"));
                mw.visitMethodInsn(INVOKEINTERFACE, getType(List.class), "get", "(I)Ljava/lang/Object;");
                mw.visitTypeInsn(CHECKCAST, getType(String.class)); // cast to string
                mw.visitVarInsn(BIPUSH, ',');
                mw.visitMethodInsn(INVOKEVIRTUAL, getType(SerializeWriter.class), "writeString",
                                   "(Ljava/lang/String;C)V");
            } else {
                mw.visitVarInsn(ALOAD, context.serializer());
                mw.visitVarInsn(ALOAD, context.var("list"));
                mw.visitVarInsn(ILOAD, context.var("i"));
                mw.visitMethodInsn(INVOKEINTERFACE, getType(List.class), "get", "(I)Ljava/lang/Object;");
                mw.visitVarInsn(ILOAD, context.var("i"));
                mw.visitMethodInsn(INVOKESTATIC, getType(Integer.class), "valueOf", "(I)Ljava/lang/Integer;");

                if (elementClass != null && Modifier.isPublic(elementClass.getModifiers())) {
                    mw.visitLdcInsn(com.alibaba.fastjson.asm.Type.getType(getDesc((Class<?>) elementType)));
                    mw.visitMethodInsn(INVOKEVIRTUAL, getType(JSONSerializer.class), "writeWithFieldName",
                                       "(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/reflect/Type;)V");
                } else {
                    mw.visitMethodInsn(INVOKEVIRTUAL, getType(JSONSerializer.class), "writeWithFieldName",
                                       "(Ljava/lang/Object;Ljava/lang/Object;)V");
                }

                mw.visitVarInsn(ALOAD, context.var("out"));
                mw.visitVarInsn(BIPUSH, ',');
                mw.visitMethodInsn(INVOKEVIRTUAL, getType(SerializeWriter.class), "write", "(C)V");
            }

            mw.visitIincInsn(context.var("i"), 1);
            mw.visitJumpInsn(GOTO, _for);

            mw.visitLabel(_end_for);

            if (elementType == String.class) {
                // out.write((String)list.get(size - 1));
                mw.visitVarInsn(ALOAD, context.var("out"));
                mw.visitVarInsn(ALOAD, context.var("list"));
                mw.visitVarInsn(ILOAD, context.var("int"));
                mw.visitInsn(ICONST_1);
                mw.visitInsn(ISUB);
                mw.visitMethodInsn(INVOKEINTERFACE, getType(List.class), "get", "(I)Ljava/lang/Object;");
                mw.visitTypeInsn(CHECKCAST, getType(String.class)); // cast to string
                mw.visitVarInsn(BIPUSH, ']');
                mw.visitMethodInsn(INVOKEVIRTUAL, getType(SerializeWriter.class), "writeString",
                                   "(Ljava/lang/String;C)V");
            } else {
                mw.visitVarInsn(ALOAD, context.serializer());
                mw.visitVarInsn(ALOAD, context.var("list"));
                mw.visitVarInsn(ILOAD, context.var("i"));
                mw.visitMethodInsn(INVOKEINTERFACE, getType(List.class), "get", "(I)Ljava/lang/Object;");
                mw.visitVarInsn(ILOAD, context.var("i"));
                mw.visitMethodInsn(INVOKESTATIC, getType(Integer.class), "valueOf", "(I)Ljava/lang/Integer;");

                if (elementClass != null && Modifier.isPublic(elementClass.getModifiers())) {
                    mw.visitLdcInsn(com.alibaba.fastjson.asm.Type.getType(getDesc((Class<?>) elementType)));
                    mw.visitMethodInsn(INVOKEVIRTUAL, getType(JSONSerializer.class), "writeWithFieldName",
                                       "(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/reflect/Type;)V");
                } else {
                    mw.visitMethodInsn(INVOKEVIRTUAL, getType(JSONSerializer.class), "writeWithFieldName",
                                       "(Ljava/lang/Object;Ljava/lang/Object;)V");
                }

                mw.visitVarInsn(ALOAD, context.var("out"));
                mw.visitVarInsn(BIPUSH, ']');
                mw.visitMethodInsn(INVOKEVIRTUAL, getType(SerializeWriter.class), "write", "(C)V");
            }
        }

        {
            mw.visitVarInsn(ALOAD, context.serializer());
            mw.visitMethodInsn(INVOKEVIRTUAL, getType(JSONSerializer.class), "popContext", "()V");
        }

        mw.visitLabel(_end_if_3);

        _seperator(mw, context);

        mw.visitLabel(_end_if);

        mw.visitLabel(_end);
    }

    private void _filters(MethodVisitor mw, FieldInfo property, Context context, Label _end) {
        if (property.getField() != null) {
            if (Modifier.isTransient(property.getField().getModifiers())) {
                mw.visitVarInsn(ALOAD, context.var("out"));
                mw.visitFieldInsn(GETSTATIC, getType(SerializerFeature.class), "SkipTransientField",
                                  "L" + getType(SerializerFeature.class) + ";");
                mw.visitMethodInsn(INVOKEVIRTUAL, getType(SerializeWriter.class), "isEnabled",
                                   "(" + "L" + getType(SerializerFeature.class) + ";" + ")Z");

                // if true
                mw.visitJumpInsn(IFNE, _end);
            }
        }

        _apply(mw, property, context);
        mw.visitJumpInsn(IFEQ, _end);

        _processKey(mw, property, context);

        Label _else_processKey = new Label();
        _processValue(mw, property, context);

        mw.visitVarInsn(ALOAD, context.original());
        mw.visitVarInsn(ALOAD, context.processValue());
        mw.visitJumpInsn(IF_ACMPEQ, _else_processKey);
        _writeObject(mw, property, context, _end);
        mw.visitJumpInsn(GOTO, _end);

        mw.visitLabel(_else_processKey);
    }
    
    private void _nameApply(MethodVisitor mw, FieldInfo property, Context context, Label _end) {
        mw.visitVarInsn(ALOAD, context.serializer());
        mw.visitVarInsn(ALOAD, context.obj());
        mw.visitVarInsn(ALOAD, context.fieldName());
        mw.visitMethodInsn(INVOKESTATIC, getType(FilterUtils.class), "applyName",
                           "(Lcom/alibaba/fastjson/serializer/JSONSerializer;Ljava/lang/Object;Ljava/lang/String;)Z");
        mw.visitJumpInsn(IFEQ, _end);
    }

    private void _writeObject(MethodVisitor mw, FieldInfo fieldInfo, Context context, Label _end) {
        String format = null;
        JSONField annotation = fieldInfo.getAnnotation(JSONField.class);

        if (annotation != null) {
            format = annotation.format();

            if (format.trim().length() == 0) {
                format = null;
            }
        }

        Label _not_null = new Label();

        mw.visitVarInsn(ALOAD, context.processValue());
        mw.visitJumpInsn(IFNONNULL, _not_null); // if (obj == null)
        _if_write_null(mw, fieldInfo, context);
        mw.visitJumpInsn(GOTO, _end);

        mw.visitLabel(_not_null);
        // writeFieldNullNumber
        mw.visitVarInsn(ALOAD, context.var("out"));
        mw.visitVarInsn(ILOAD, context.var("seperator"));
        mw.visitMethodInsn(INVOKEVIRTUAL, getType(SerializeWriter.class), "write", "(C)V");

        // out.writeFieldName("fieldName")
        mw.visitVarInsn(ALOAD, context.var("out"));
        mw.visitVarInsn(ALOAD, context.fieldName());
        mw.visitMethodInsn(INVOKEVIRTUAL, getType(SerializeWriter.class), "writeFieldName", "(Ljava/lang/String;)V");

        // serializer.write(obj)
        mw.visitVarInsn(ALOAD, context.serializer()); // serializer
        mw.visitVarInsn(ALOAD, context.processValue());
        if (format != null) {
            mw.visitLdcInsn(format);
            mw.visitMethodInsn(INVOKEVIRTUAL, getType(JSONSerializer.class), "writeWithFormat",
                               "(Ljava/lang/Object;Ljava/lang/String;)V");
        } else {
            mw.visitVarInsn(ALOAD, context.fieldName());
            if (fieldInfo.getFieldType() instanceof Class<?> && ((Class<?>) fieldInfo.getFieldType()).isPrimitive()) {
                mw.visitMethodInsn(INVOKEVIRTUAL, getType(JSONSerializer.class), "writeWithFieldName",
                                   "(Ljava/lang/Object;Ljava/lang/Object;)V");
            } else {
                // fieldInfo.getName() + "_asm_fieldType"

                mw.visitVarInsn(ALOAD, 0);
                mw.visitFieldInsn(GETFIELD, context.getClassName(), fieldInfo.getName() + "_asm_fieldType",
                                  "Ljava/lang/reflect/Type;");

                mw.visitMethodInsn(INVOKEVIRTUAL, getType(JSONSerializer.class), "writeWithFieldName",
                                   "(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/reflect/Type;)V");
            }
        }

        _seperator(mw, context);
    }

    private void _apply(MethodVisitor mw, FieldInfo property, Context context) {
        Class<?> propertyClass = property.getFieldClass();

        mw.visitVarInsn(ALOAD, context.serializer());
        mw.visitVarInsn(ALOAD, context.obj());
        mw.visitVarInsn(ALOAD, context.fieldName());

        if (propertyClass == byte.class) {
            mw.visitVarInsn(ILOAD, context.var("byte"));
            mw.visitMethodInsn(INVOKESTATIC, getType(FilterUtils.class), "apply",
                               "(Lcom/alibaba/fastjson/serializer/JSONSerializer;Ljava/lang/Object;Ljava/lang/String;B)Z");
        } else if (propertyClass == short.class) {
            mw.visitVarInsn(ILOAD, context.var("short"));
            mw.visitMethodInsn(INVOKESTATIC, getType(FilterUtils.class), "apply",
                               "(Lcom/alibaba/fastjson/serializer/JSONSerializer;Ljava/lang/Object;Ljava/lang/String;S)Z");
        } else if (propertyClass == int.class) {
            mw.visitVarInsn(ILOAD, context.var("int"));
            mw.visitMethodInsn(INVOKESTATIC, getType(FilterUtils.class), "apply",
                               "(Lcom/alibaba/fastjson/serializer/JSONSerializer;Ljava/lang/Object;Ljava/lang/String;I)Z");
        } else if (propertyClass == char.class) {
            mw.visitVarInsn(ILOAD, context.var("char"));
            mw.visitMethodInsn(INVOKESTATIC, getType(FilterUtils.class), "apply",
                               "(Lcom/alibaba/fastjson/serializer/JSONSerializer;Ljava/lang/Object;Ljava/lang/String;C)Z");
        } else if (propertyClass == long.class) {
            mw.visitVarInsn(LLOAD, context.var("long", 2));
            mw.visitMethodInsn(INVOKESTATIC, getType(FilterUtils.class), "apply",
                               "(Lcom/alibaba/fastjson/serializer/JSONSerializer;Ljava/lang/Object;Ljava/lang/String;J)Z");
        } else if (propertyClass == float.class) {
            mw.visitVarInsn(FLOAD, context.var("float"));
            mw.visitMethodInsn(INVOKESTATIC, getType(FilterUtils.class), "apply",
                               "(Lcom/alibaba/fastjson/serializer/JSONSerializer;Ljava/lang/Object;Ljava/lang/String;F)Z");
        } else if (propertyClass == double.class) {
            mw.visitVarInsn(DLOAD, context.var("double", 2));
            mw.visitMethodInsn(INVOKESTATIC, getType(FilterUtils.class), "apply",
                               "(Lcom/alibaba/fastjson/serializer/JSONSerializer;Ljava/lang/Object;Ljava/lang/String;D)Z");
        } else if (propertyClass == boolean.class) {
            mw.visitVarInsn(ILOAD, context.var("boolean"));
            mw.visitMethodInsn(INVOKESTATIC, getType(FilterUtils.class), "apply",
                               "(Lcom/alibaba/fastjson/serializer/JSONSerializer;Ljava/lang/Object;Ljava/lang/String;B)Z");
        } else if (propertyClass == BigDecimal.class) {
            mw.visitVarInsn(ALOAD, context.var("decimal"));
            mw.visitMethodInsn(INVOKESTATIC, getType(FilterUtils.class), "apply",
                               "(Lcom/alibaba/fastjson/serializer/JSONSerializer;Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Object;)Z");
        } else if (propertyClass == String.class) {
            mw.visitVarInsn(ALOAD, context.var("string"));
            mw.visitMethodInsn(INVOKESTATIC, getType(FilterUtils.class), "apply",
                               "(Lcom/alibaba/fastjson/serializer/JSONSerializer;Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Object;)Z");
        } else if (propertyClass.isEnum()) {
            mw.visitVarInsn(ALOAD, context.var("enum"));
            mw.visitMethodInsn(INVOKESTATIC, getType(FilterUtils.class), "apply",
                               "(Lcom/alibaba/fastjson/serializer/JSONSerializer;Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Object;)Z");
        } else if (List.class.isAssignableFrom(propertyClass)) {
            mw.visitVarInsn(ALOAD, context.var("list"));
            mw.visitMethodInsn(INVOKESTATIC, getType(FilterUtils.class), "apply",
                               "(Lcom/alibaba/fastjson/serializer/JSONSerializer;Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Object;)Z");
        } else {
            mw.visitVarInsn(ALOAD, context.var("object"));
            mw.visitMethodInsn(INVOKESTATIC, getType(FilterUtils.class), "apply",
                               "(Lcom/alibaba/fastjson/serializer/JSONSerializer;Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Object;)Z");
        }
    }

    private void _processValue(MethodVisitor mw, FieldInfo property, Context context) {
        Class<?> propertyClass = property.getFieldClass();

        mw.visitVarInsn(ALOAD, context.serializer());
        mw.visitVarInsn(ALOAD, context.obj());
        mw.visitVarInsn(ALOAD, context.fieldName());

        if (propertyClass == byte.class) {
            mw.visitVarInsn(ILOAD, context.var("byte"));
            mw.visitMethodInsn(INVOKESTATIC, getType(Byte.class), "valueOf", "(B)Ljava/lang/Byte;");
        } else if (propertyClass == short.class) {
            mw.visitVarInsn(ILOAD, context.var("short"));
            mw.visitMethodInsn(INVOKESTATIC, getType(Short.class), "valueOf", "(S)Ljava/lang/Short;");
        } else if (propertyClass == int.class) {
            mw.visitVarInsn(ILOAD, context.var("int"));
            mw.visitMethodInsn(INVOKESTATIC, getType(Integer.class), "valueOf", "(I)Ljava/lang/Integer;");
        } else if (propertyClass == char.class) {
            mw.visitVarInsn(ILOAD, context.var("char"));
            mw.visitMethodInsn(INVOKESTATIC, getType(Character.class), "valueOf", "(C)Ljava/lang/Character;");
        } else if (propertyClass == long.class) {
            mw.visitVarInsn(LLOAD, context.var("long", 2));
            mw.visitMethodInsn(INVOKESTATIC, getType(Long.class), "valueOf", "(J)Ljava/lang/Long;");
        } else if (propertyClass == float.class) {
            mw.visitVarInsn(FLOAD, context.var("float"));
            mw.visitMethodInsn(INVOKESTATIC, getType(Float.class), "valueOf", "(F)Ljava/lang/Float;");
        } else if (propertyClass == double.class) {
            mw.visitVarInsn(DLOAD, context.var("double", 2));
            mw.visitMethodInsn(INVOKESTATIC, getType(Double.class), "valueOf", "(D)Ljava/lang/Double;");
        } else if (propertyClass == boolean.class) {
            mw.visitVarInsn(ILOAD, context.var("boolean"));
            mw.visitMethodInsn(INVOKESTATIC, getType(Boolean.class), "valueOf", "(Z)Ljava/lang/Boolean;");
        } else if (propertyClass == BigDecimal.class) {
            mw.visitVarInsn(ALOAD, context.var("decimal"));
        } else if (propertyClass == String.class) {
            mw.visitVarInsn(ALOAD, context.var("string"));
        } else if (propertyClass.isEnum()) {
            mw.visitVarInsn(ALOAD, context.var("enum"));
        } else if (List.class.isAssignableFrom(propertyClass)) {
            mw.visitVarInsn(ALOAD, context.var("list"));
        } else {
            mw.visitVarInsn(ALOAD, context.var("object"));
        }

        mw.visitVarInsn(ASTORE, context.original());
        mw.visitVarInsn(ALOAD, context.original());

        mw.visitMethodInsn(INVOKESTATIC,
                           getType(FilterUtils.class),
                           "processValue",
                           "(Lcom/alibaba/fastjson/serializer/JSONSerializer;Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/Object;");

        mw.visitVarInsn(ASTORE, context.processValue());
    }

    private void _processKey(MethodVisitor mw, FieldInfo property, Context context) {
        Class<?> propertyClass = property.getFieldClass();

        mw.visitVarInsn(ALOAD, context.serializer());
        mw.visitVarInsn(ALOAD, context.obj());
        mw.visitVarInsn(ALOAD, context.fieldName());

        if (propertyClass == byte.class) {
            mw.visitVarInsn(ILOAD, context.var("byte"));
            mw.visitMethodInsn(INVOKESTATIC, getType(FilterUtils.class), "processKey",
                               "(Lcom/alibaba/fastjson/serializer/JSONSerializer;Ljava/lang/Object;Ljava/lang/String;B)Ljava/lang/String;");
        } else if (propertyClass == short.class) {
            mw.visitVarInsn(ILOAD, context.var("short"));
            mw.visitMethodInsn(INVOKESTATIC, getType(FilterUtils.class), "processKey",
                               "(Lcom/alibaba/fastjson/serializer/JSONSerializer;Ljava/lang/Object;Ljava/lang/String;S)Ljava/lang/String;");
        } else if (propertyClass == int.class) {
            mw.visitVarInsn(ILOAD, context.var("int"));
            mw.visitMethodInsn(INVOKESTATIC, getType(FilterUtils.class), "processKey",
                               "(Lcom/alibaba/fastjson/serializer/JSONSerializer;Ljava/lang/Object;Ljava/lang/String;I)Ljava/lang/String;");
        } else if (propertyClass == char.class) {
            mw.visitVarInsn(ILOAD, context.var("char"));
            mw.visitMethodInsn(INVOKESTATIC, getType(FilterUtils.class), "processKey",
                               "(Lcom/alibaba/fastjson/serializer/JSONSerializer;Ljava/lang/Object;Ljava/lang/String;C)Ljava/lang/String;");
        } else if (propertyClass == long.class) {
            mw.visitVarInsn(LLOAD, context.var("long", 2));
            mw.visitMethodInsn(INVOKESTATIC, getType(FilterUtils.class), "processKey",
                               "(Lcom/alibaba/fastjson/serializer/JSONSerializer;Ljava/lang/Object;Ljava/lang/String;J)Ljava/lang/String;");
        } else if (propertyClass == float.class) {
            mw.visitVarInsn(FLOAD, context.var("float"));
            mw.visitMethodInsn(INVOKESTATIC, getType(FilterUtils.class), "processKey",
                               "(Lcom/alibaba/fastjson/serializer/JSONSerializer;Ljava/lang/Object;Ljava/lang/String;F)Ljava/lang/String;");
        } else if (propertyClass == double.class) {
            mw.visitVarInsn(DLOAD, context.var("double", 2));
            mw.visitMethodInsn(INVOKESTATIC, getType(FilterUtils.class), "processKey",
                               "(Lcom/alibaba/fastjson/serializer/JSONSerializer;Ljava/lang/Object;Ljava/lang/String;D)Ljava/lang/String;");
        } else if (propertyClass == boolean.class) {
            mw.visitVarInsn(ILOAD, context.var("boolean"));
            mw.visitMethodInsn(INVOKESTATIC, getType(FilterUtils.class), "processKey",
                               "(Lcom/alibaba/fastjson/serializer/JSONSerializer;Ljava/lang/Object;Ljava/lang/String;Z)Ljava/lang/String;");
        } else if (propertyClass == BigDecimal.class) {
            mw.visitVarInsn(ALOAD, context.var("decimal"));
            mw.visitMethodInsn(INVOKESTATIC,
                               getType(FilterUtils.class),
                               "processKey",
                               "(Lcom/alibaba/fastjson/serializer/JSONSerializer;Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/String;");
        } else if (propertyClass == String.class) {
            mw.visitVarInsn(ALOAD, context.var("string"));
            mw.visitMethodInsn(INVOKESTATIC,
                               getType(FilterUtils.class),
                               "processKey",
                               "(Lcom/alibaba/fastjson/serializer/JSONSerializer;Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/String;");
        } else if (propertyClass.isEnum()) {
            mw.visitVarInsn(ALOAD, context.var("enum"));
            mw.visitMethodInsn(INVOKESTATIC,
                               getType(FilterUtils.class),
                               "processKey",
                               "(Lcom/alibaba/fastjson/serializer/JSONSerializer;Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/String;");
        } else if (List.class.isAssignableFrom(propertyClass)) {

            mw.visitVarInsn(ALOAD, context.var("list"));
            mw.visitMethodInsn(INVOKESTATIC,
                               getType(FilterUtils.class),
                               "processKey",
                               "(Lcom/alibaba/fastjson/serializer/JSONSerializer;Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/String;");
        } else {
            mw.visitVarInsn(ALOAD, context.var("object"));
            mw.visitMethodInsn(INVOKESTATIC,
                               getType(FilterUtils.class),
                               "processKey",
                               "(Lcom/alibaba/fastjson/serializer/JSONSerializer;Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/String;");
        }

        mw.visitVarInsn(ASTORE, context.fieldName());
    }

    private void _if_write_null(MethodVisitor mw, FieldInfo fieldInfo, Context context) {
        Class<?> propertyClass = fieldInfo.getFieldClass();

        Label _if = new Label();
        Label _else = new Label();
        Label _write_null = new Label();
        Label _end_if = new Label();

        mw.visitLabel(_if);

        // out.isEnabled(Serializer.WriteMapNullValue)
        boolean writeNull = false;
        boolean writeNullNumberAsZero = false;
        boolean writeNullStringAsEmpty = false;
        boolean writeNullBooleanAsFalse = false;
        boolean writeNullListAsEmpty = false;
        JSONField annotation = fieldInfo.getAnnotation(JSONField.class);
        if (annotation != null) {
            for (SerializerFeature feature : annotation.serialzeFeatures()) {
                if (feature == SerializerFeature.WriteMapNullValue) {
                    writeNull = true;
                } else if (feature == SerializerFeature.WriteNullNumberAsZero) {
                    writeNullNumberAsZero = true;
                } else if (feature == SerializerFeature.WriteNullStringAsEmpty) {
                    writeNullStringAsEmpty = true;
                } else if (feature == SerializerFeature.WriteNullBooleanAsFalse) {
                    writeNullBooleanAsFalse = true;
                } else if (feature == SerializerFeature.WriteNullListAsEmpty) {
                    writeNullListAsEmpty = true;
                }
            }
        }

        if (!writeNull) {
            mw.visitVarInsn(ALOAD, context.var("out"));
            mw.visitFieldInsn(GETSTATIC, getType(SerializerFeature.class), "WriteMapNullValue",
                              "L" + getType(SerializerFeature.class) + ";");
            mw.visitMethodInsn(INVOKEVIRTUAL, getType(SerializeWriter.class), "isEnabled",
                               "(" + "L" + getType(SerializerFeature.class) + ";" + ")Z");
            mw.visitJumpInsn(IFEQ, _else);
        }

        mw.visitLabel(_write_null);
        // out.writeFieldNull(seperator, 'fieldName')
        mw.visitVarInsn(ALOAD, context.var("out"));
        mw.visitVarInsn(ILOAD, context.var("seperator"));
        mw.visitVarInsn(ALOAD, context.fieldName());

        if (propertyClass == String.class || propertyClass == Character.class) {
            if (writeNullStringAsEmpty) {
                mw.visitLdcInsn("");
                mw.visitMethodInsn(INVOKEVIRTUAL, getType(SerializeWriter.class), "writeFieldValue",
                                   "(CLjava/lang/String;Ljava/lang/String;)V");
            } else {
                mw.visitMethodInsn(INVOKEVIRTUAL, getType(SerializeWriter.class), "writeFieldNullString",
                                   "(CLjava/lang/String;)V");
            }
        } else if (Number.class.isAssignableFrom(propertyClass)) {
            if (writeNullNumberAsZero) {
                mw.visitInsn(ICONST_0);
                mw.visitMethodInsn(INVOKEVIRTUAL, getType(SerializeWriter.class), "writeFieldValue",
                                   "(CLjava/lang/String;I)V");
            } else {
                mw.visitMethodInsn(INVOKEVIRTUAL, getType(SerializeWriter.class), "writeFieldNullNumber",
                                   "(CLjava/lang/String;)V");
            }
        } else if (propertyClass == Boolean.class) {
            if (writeNullBooleanAsFalse) {
                mw.visitInsn(ICONST_0);
                mw.visitMethodInsn(INVOKEVIRTUAL, getType(SerializeWriter.class), "writeFieldValue",
                                   "(CLjava/lang/String;Z)V");
            } else {
                mw.visitMethodInsn(INVOKEVIRTUAL, getType(SerializeWriter.class), "writeFieldNullBoolean",
                                   "(CLjava/lang/String;)V");
            }
        } else if (Collection.class.isAssignableFrom(propertyClass) || propertyClass.isArray()) {
            if (writeNullListAsEmpty) {
                mw.visitMethodInsn(INVOKEVIRTUAL, getType(SerializeWriter.class), "writeFieldEmptyList",
                                   "(CLjava/lang/String;)V");
            } else {
                mw.visitMethodInsn(INVOKEVIRTUAL, getType(SerializeWriter.class), "writeFieldNullList",
                                   "(CLjava/lang/String;)V");
            }
        } else {
            mw.visitMethodInsn(INVOKEVIRTUAL, getType(SerializeWriter.class), "writeFieldNull",
                               "(CLjava/lang/String;)V");
        }

        // seperator = ',';
        _seperator(mw, context);

        mw.visitJumpInsn(GOTO, _end_if);

        mw.visitLabel(_else);

        mw.visitLabel(_end_if);
    }

    private void _seperator(MethodVisitor mw, Context context) {
        mw.visitVarInsn(BIPUSH, ',');
        mw.visitVarInsn(ISTORE, context.var("seperator"));
    }

}
