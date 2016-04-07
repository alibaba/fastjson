package com.alibaba.fastjson.serializer;

import static com.alibaba.fastjson.util.ASMUtils.getDesc;
import static com.alibaba.fastjson.util.ASMUtils.getType;

import java.io.File;
import java.io.FileOutputStream;
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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.asm.ClassWriter;
import com.alibaba.fastjson.asm.FieldWriter;
import com.alibaba.fastjson.asm.Label;
import com.alibaba.fastjson.asm.MethodVisitor;
import com.alibaba.fastjson.asm.MethodWriter;
import com.alibaba.fastjson.asm.Opcodes;
import com.alibaba.fastjson.util.ASMClassLoader;
import com.alibaba.fastjson.util.ASMUtils;
import com.alibaba.fastjson.util.FieldInfo;
import com.alibaba.fastjson.util.TypeUtils;

public class ASMSerializerFactory implements Opcodes {

    protected final ASMClassLoader classLoader = new ASMClassLoader();

    private final AtomicLong seed = new AtomicLong();

    static class Context {

        private final String className;
        private final int beanSerializeFeatures;

        public Context(String className, int beanSerializeFeatures){
            this.className = className;
            this.beanSerializeFeatures = beanSerializeFeatures;
        }

        private Map<String, Integer> variants       = new HashMap<String, Integer>();

        static final int             serializer     = 1;
        static final int             obj            = 2;
        static final int             paramFieldName = 3;
        static final int             paramFieldType = 4;
        static final int             features       = 5;
        static int                   fieldName      = 6;
        static int                   original       = 7;
        static int                   processValue   = 8;
        private int                  variantIndex   = 9;

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
        
        JSONType jsonType = clazz.getAnnotation(JSONType.class);
        
        List<FieldInfo> unsortedGetters = TypeUtils.computeGetters(clazz, jsonType, aliasMap, false);
        List<FieldInfo> getters = TypeUtils.computeGetters(clazz, jsonType, aliasMap, true);
        
        boolean nativeSorted = true;
        for (int i = 0, size = unsortedGetters.size(); i < size; ++i) {
            if (!unsortedGetters.get(i).equals(getters.get(i))) {
                nativeSorted = false;
                break;
            }
        }
        
        if (getters.size() > 256) {
            return null;
        }
        
        for (FieldInfo getter : getters) {
            if (!ASMUtils.checkName(getter.getMember().getName())) {
                return null;
            }
        }

        String className = "ASMSerializer_" + seed.incrementAndGet() + "_" + clazz.getSimpleName();
        String classNameType = "com/alibaba/fastjson/serializer/" + className;
        String classNameFull = "com.alibaba.fastjson.serializer." + className;
        int beanSerializeFeatures = TypeUtils.getSerializeFeatures(clazz);

        ClassWriter cw = new ClassWriter();
        cw.visit(V1_5 //
                 , ACC_PUBLIC + ACC_SUPER //
                 , classNameType //
                 , "com/alibaba/fastjson/serializer/ASMJavaBeanSerializer" //
                 , new String[] { "com/alibaba/fastjson/serializer/ObjectSerializer" } //
        );

        for (FieldInfo fieldInfo : getters) {
            if (fieldInfo.fieldClass.isPrimitive() //
                || fieldInfo.fieldClass.isEnum() //
                || fieldInfo.fieldClass == String.class) {
                continue;
            }
            
            FieldWriter fw = new FieldWriter(cw //
                                             , ACC_PUBLIC //
                                             , fieldInfo.name + "_asm_fieldType" //
                                             , "Ljava/lang/reflect/Type;");
            fw.visitEnd();
        }

        MethodVisitor mw = new MethodWriter(cw, ACC_PUBLIC, "<init>", "()V", null, null);
        mw.visitVarInsn(ALOAD, 0);
        mw.visitLdcInsn(com.alibaba.fastjson.asm.Type.getType(getDesc(clazz)));
        mw.visitMethodInsn(INVOKESPECIAL, "com/alibaba/fastjson/serializer/ASMJavaBeanSerializer", "<init>", "(Ljava/lang/Class;)V");

        // init _asm_fieldType
        for (FieldInfo fieldInfo : getters) {
            if (fieldInfo.fieldClass.isPrimitive() //
                || fieldInfo.fieldClass.isEnum() //
                || fieldInfo.fieldClass == String.class) {
                continue;
            }
            
            mw.visitVarInsn(ALOAD, 0);

            mw.visitLdcInsn(com.alibaba.fastjson.asm.Type.getType(getDesc(fieldInfo.declaringClass)));

            if (fieldInfo.method != null) {
                mw.visitLdcInsn(fieldInfo.method.getName());
                mw.visitMethodInsn(INVOKESTATIC, "com/alibaba/fastjson/util/ASMUtils", "getMethodType",
                                   "(Ljava/lang/Class;Ljava/lang/String;)Ljava/lang/reflect/Type;");

            } else {
                mw.visitLdcInsn(fieldInfo.field.getName());
                mw.visitMethodInsn(INVOKESTATIC, "com/alibaba/fastjson/util/ASMUtils", "getFieldType",
                                   "(Ljava/lang/Class;Ljava/lang/String;)Ljava/lang/reflect/Type;");
            }

            mw.visitFieldInsn(PUTFIELD, classNameType, fieldInfo.name + "_asm_fieldType", "Ljava/lang/reflect/Type;");
        }

        mw.visitInsn(RETURN);
        mw.visitMaxs(4, 4);
        mw.visitEnd();

        {
            Context context = new Context(classNameType, beanSerializeFeatures);

            mw = new MethodWriter(cw, ACC_PUBLIC,
                                "write",
                                "(Lcom/alibaba/fastjson/serializer/JSONSerializer;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/reflect/Type;I)V",
                                null, new String[] { "java/io/IOException" });

            mw.visitVarInsn(ALOAD, Context.serializer); // serializer
            mw.visitMethodInsn(INVOKEVIRTUAL, "com/alibaba/fastjson/serializer/JSONSerializer", "getWriter",
                               "()Lcom/alibaba/fastjson/serializer/SerializeWriter;");
            mw.visitVarInsn(ASTORE, context.var("out"));

            if (!nativeSorted) {
                if (jsonType == null || jsonType.alphabetic()) {
                    Label _else = new Label();
    
                    mw.visitVarInsn(ALOAD, context.var("out"));
                    mw.visitMethodInsn(INVOKEVIRTUAL, "com/alibaba/fastjson/serializer/SerializeWriter", "isSortField",
                                       "()Z");
    
                    mw.visitJumpInsn(IFNE, _else);
                    mw.visitVarInsn(ALOAD, 0);
                    mw.visitVarInsn(ALOAD, 1);
                    mw.visitVarInsn(ALOAD, 2);
                    mw.visitVarInsn(ALOAD, 3);
                    mw.visitVarInsn(ALOAD, 4);
                    mw.visitVarInsn(ILOAD, 5);
                    mw.visitMethodInsn(INVOKEVIRTUAL, classNameType, "writeUnsorted",
                                       "(Lcom/alibaba/fastjson/serializer/JSONSerializer;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/reflect/Type;I)V");
                    mw.visitInsn(RETURN);
    
                    mw.visitLabel(_else);
                }
            }

            mw.visitVarInsn(ALOAD, Context.obj); // obj
            mw.visitTypeInsn(CHECKCAST, getType(clazz)); // serializer
            mw.visitVarInsn(ASTORE, context.var("entity")); // obj
            generateWriteMethod(clazz, mw, getters, context);
            mw.visitInsn(RETURN);
            mw.visitMaxs(6, context.variantIndex + 1);
            mw.visitEnd();
        }

        if (!nativeSorted) {
            // sortField support
            Context context = new Context(classNameType, beanSerializeFeatures);

            mw = new MethodWriter(cw, ACC_PUBLIC,
                                "writeUnsorted",
                                "(Lcom/alibaba/fastjson/serializer/JSONSerializer;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/reflect/Type;I)V",
                                null, new String[] { "java/io/IOException" });

            mw.visitVarInsn(ALOAD, Context.serializer); // serializer
            mw.visitMethodInsn(INVOKEVIRTUAL, "com/alibaba/fastjson/serializer/JSONSerializer", "getWriter",
                               "()Lcom/alibaba/fastjson/serializer/SerializeWriter;");
            mw.visitVarInsn(ASTORE, context.var("out"));

            mw.visitVarInsn(ALOAD, Context.obj); // obj
            mw.visitTypeInsn(CHECKCAST, getType(clazz)); // serializer
            mw.visitVarInsn(ASTORE, context.var("entity")); // obj

            generateWriteMethod(clazz, mw, unsortedGetters, context);

            mw.visitInsn(RETURN);
            mw.visitMaxs(6, context.variantIndex + 1);
            mw.visitEnd();
        }

        // writeAsArray
        {
            Context context = new Context(classNameType, beanSerializeFeatures);

            mw = new MethodWriter(cw, ACC_PUBLIC,
                                "writeAsArray",
                                "(Lcom/alibaba/fastjson/serializer/JSONSerializer;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/reflect/Type;)V",
                                null, new String[] { "java/io/IOException" });

            mw.visitVarInsn(ALOAD, Context.serializer); // serializer
            mw.visitMethodInsn(INVOKEVIRTUAL, "com/alibaba/fastjson/serializer/JSONSerializer", "getWriter",
                               "()Lcom/alibaba/fastjson/serializer/SerializeWriter;");
            mw.visitVarInsn(ASTORE, context.var("out"));

            mw.visitVarInsn(ALOAD, Context.obj); // obj
            mw.visitTypeInsn(CHECKCAST, getType(clazz)); // serializer
            mw.visitVarInsn(ASTORE, context.var("entity")); // obj
            generateWriteAsArray(clazz, mw, getters, context);
            mw.visitInsn(RETURN);
            mw.visitMaxs(6, context.variantIndex + 1);
            mw.visitEnd();
        }

        byte[] code = cw.toByteArray();

        if(JSON.DUMP_CLASS != null){
            FileOutputStream fos=null;
            try {
                fos=new FileOutputStream(JSON.DUMP_CLASS+ File.separator
                        + classNameType + ".class");
                fos.write(code);
            }catch (Exception ex){
                System.err.println("FASTJSON dump class:"+classNameType+"失败:"+ex.getMessage());
            }finally {
                if(fos!=null){
                    fos.close();
                }
            }
        }

        Class<?> exampleClass = classLoader.defineClassPublic(classNameFull, code, 0, code.length);
        Object instance = exampleClass.newInstance();

        return (ObjectSerializer) instance;
    }

    private void generateWriteAsArray(Class<?> clazz, MethodVisitor mw, List<FieldInfo> getters, Context context)
                                                                                                                 throws Exception {

        mw.visitVarInsn(ALOAD, context.var("out"));
        mw.visitVarInsn(BIPUSH, '[');
        mw.visitMethodInsn(INVOKEVIRTUAL, "com/alibaba/fastjson/serializer/SerializeWriter", "write", "(I)V");

        int size = getters.size();

        if (size == 0) {
            mw.visitVarInsn(ALOAD, context.var("out"));
            mw.visitVarInsn(BIPUSH, ']');
            mw.visitMethodInsn(INVOKEVIRTUAL, "com/alibaba/fastjson/serializer/SerializeWriter", "write", "(I)V");
            return;
        }

        for (int i = 0; i < size; ++i) {
            final char seperator = (i == size - 1) ? ']' : ',';

            FieldInfo property = getters.get(i);
            Class<?> propertyClass = property.fieldClass;

            mw.visitLdcInsn(property.name);
            mw.visitVarInsn(ASTORE, Context.fieldName);

            if (propertyClass == byte.class //
                || propertyClass == short.class //
                || propertyClass == int.class) {

                mw.visitVarInsn(ALOAD, context.var("out"));
                _get(mw, context, property);
                mw.visitVarInsn(BIPUSH, seperator);
                mw.visitMethodInsn(INVOKEVIRTUAL, "com/alibaba/fastjson/serializer/SerializeWriter", "writeIntAndChar", "(IC)V");

            } else if (propertyClass == long.class) {
                mw.visitVarInsn(ALOAD, context.var("out"));
                _get(mw, context, property);
                mw.visitVarInsn(BIPUSH, seperator);
                mw.visitMethodInsn(INVOKEVIRTUAL, "com/alibaba/fastjson/serializer/SerializeWriter", "writeLongAndChar", "(JC)V");

            } else if (propertyClass == float.class) {
                mw.visitVarInsn(ALOAD, context.var("out"));
                _get(mw, context, property);
                mw.visitVarInsn(BIPUSH, seperator);
                mw.visitMethodInsn(INVOKEVIRTUAL, "com/alibaba/fastjson/serializer/SerializeWriter", "writeFloatAndChar", "(FC)V");

            } else if (propertyClass == double.class) {
                mw.visitVarInsn(ALOAD, context.var("out"));
                _get(mw, context, property);
                mw.visitVarInsn(BIPUSH, seperator);
                mw.visitMethodInsn(INVOKEVIRTUAL, "com/alibaba/fastjson/serializer/SerializeWriter", "writeDoubleAndChar", "(DC)V");

            } else if (propertyClass == boolean.class) {
                mw.visitVarInsn(ALOAD, context.var("out"));
                _get(mw, context, property);
                mw.visitVarInsn(BIPUSH, seperator);
                mw.visitMethodInsn(INVOKEVIRTUAL, "com/alibaba/fastjson/serializer/SerializeWriter", "writeBooleanAndChar", "(ZC)V");
            } else if (propertyClass == char.class) {
                mw.visitVarInsn(ALOAD, context.var("out"));
                _get(mw, context, property);
                mw.visitVarInsn(BIPUSH, seperator);
                mw.visitMethodInsn(INVOKEVIRTUAL, "com/alibaba/fastjson/serializer/SerializeWriter", "writeCharacterAndChar", "(CC)V");

            } else if (propertyClass == String.class) {
                mw.visitVarInsn(ALOAD, context.var("out"));
                _get(mw, context, property);
                mw.visitVarInsn(BIPUSH, seperator);
                mw.visitMethodInsn(INVOKEVIRTUAL, "com/alibaba/fastjson/serializer/SerializeWriter", "writeString",
                                   "(Ljava/lang/String;C)V");
            } else if (propertyClass.isEnum()) {
                mw.visitVarInsn(ALOAD, context.var("out"));
                _get(mw, context, property);
                mw.visitVarInsn(BIPUSH, seperator);
                mw.visitMethodInsn(INVOKEVIRTUAL, "com/alibaba/fastjson/serializer/SerializeWriter", "writeEnum", "(Ljava/lang/Enum;C)V");
            } else {
                String format = property.getFormat();

                mw.visitVarInsn(ALOAD, Context.serializer);
                _get(mw, context, property);
                if (format != null) {
                    mw.visitLdcInsn(format);
                    mw.visitMethodInsn(INVOKEVIRTUAL, "com/alibaba/fastjson/serializer/JSONSerializer", "writeWithFormat",
                                       "(Ljava/lang/Object;Ljava/lang/String;)V");
                } else {
                    mw.visitVarInsn(ALOAD, Context.fieldName);
                    if (property.fieldType instanceof Class<?> //
                        && ((Class<?>) property.fieldType).isPrimitive()) {
                        mw.visitMethodInsn(INVOKEVIRTUAL, "com/alibaba/fastjson/serializer/JSONSerializer", "writeWithFieldName",
                                           "(Ljava/lang/Object;Ljava/lang/Object;)V");
                    } else {
                        mw.visitVarInsn(ALOAD, 0);
                        mw.visitFieldInsn(GETFIELD, context.className, property.name + "_asm_fieldType",
                                          "Ljava/lang/reflect/Type;");
                        mw.visitLdcInsn(property.serialzeFeatures);

                        mw.visitMethodInsn(INVOKEVIRTUAL, "com/alibaba/fastjson/serializer/JSONSerializer", "writeWithFieldName",
                                           "(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/reflect/Type;I)V");
                    }
                }

                mw.visitVarInsn(ALOAD, context.var("out"));
                mw.visitVarInsn(BIPUSH, seperator);
                mw.visitMethodInsn(INVOKEVIRTUAL, "com/alibaba/fastjson/serializer/SerializeWriter", "write", "(I)V");
            }
        }
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
            mw.visitMethodInsn(INVOKEVIRTUAL, "com/alibaba/fastjson/serializer/SerializeWriter", "isPrettyFormat",
                               "()Z");
            mw.visitJumpInsn(IFEQ, endFormat_);

            mw.visitVarInsn(ALOAD, 0);
            mw.visitFieldInsn(GETFIELD, context.className, "nature", "Lcom/alibaba/fastjson/serializer/JavaBeanSerializer;");
            mw.visitJumpInsn(IFNONNULL, notNull_);

            // /////
            mw.visitLabel(notNull_);

            mw.visitVarInsn(ALOAD, 0);
            mw.visitFieldInsn(GETFIELD, context.className, "nature", "Lcom/alibaba/fastjson/serializer/JavaBeanSerializer;");
            mw.visitVarInsn(ALOAD, 1);
            mw.visitVarInsn(ALOAD, 2);
            mw.visitVarInsn(ALOAD, 3);
            mw.visitVarInsn(ALOAD, 4);
            mw.visitVarInsn(ILOAD, 5);
            mw.visitMethodInsn(INVOKEVIRTUAL, "com/alibaba/fastjson/serializer/JavaBeanSerializer", "write",
                               "(Lcom/alibaba/fastjson/serializer/JSONSerializer;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/reflect/Type;I)V");
            mw.visitInsn(RETURN);

            mw.visitLabel(endFormat_);
        }

        {
            // if (serializer.containsReference(object)) {

            Label endRef_ = new Label();
            Label notNull_ = new Label();

            mw.visitVarInsn(ALOAD, 0);
            mw.visitFieldInsn(GETFIELD, context.className, "nature", "Lcom/alibaba/fastjson/serializer/JavaBeanSerializer;");
            mw.visitJumpInsn(IFNONNULL, notNull_);

            // /////
            mw.visitLabel(notNull_);
            mw.visitVarInsn(ALOAD, 0);
            mw.visitFieldInsn(GETFIELD, context.className, "nature", "Lcom/alibaba/fastjson/serializer/JavaBeanSerializer;");
            mw.visitVarInsn(ALOAD, Context.serializer);
            mw.visitVarInsn(ALOAD, Context.obj);
            mw.visitVarInsn(ILOAD, Context.features);
            mw.visitMethodInsn(INVOKEVIRTUAL, "com/alibaba/fastjson/serializer/JavaBeanSerializer", "writeReference",
                               "(Lcom/alibaba/fastjson/serializer/JSONSerializer;Ljava/lang/Object;I)Z");
            
            mw.visitJumpInsn(IFEQ, endRef_);

            mw.visitInsn(RETURN);

            mw.visitLabel(endRef_);
        }

        {
            Label endWriteAsArray_ = new Label();

            mw.visitVarInsn(ALOAD, 0);
            mw.visitFieldInsn(GETFIELD, context.className, "nature", "Lcom/alibaba/fastjson/serializer/JavaBeanSerializer;");
            mw.visitVarInsn(ALOAD, Context.serializer);
            mw.visitMethodInsn(INVOKEVIRTUAL, "com/alibaba/fastjson/serializer/JavaBeanSerializer", "isWriteAsArray",
                               "(Lcom/alibaba/fastjson/serializer/JSONSerializer;)Z");
            mw.visitJumpInsn(IFEQ, endWriteAsArray_);

            // /////
            mw.visitVarInsn(ALOAD, 0); // this
            mw.visitVarInsn(ALOAD, 1); // serializer
            mw.visitVarInsn(ALOAD, 2); // obj
            mw.visitVarInsn(ALOAD, 3); // fieldObj
            mw.visitVarInsn(ALOAD, 4); // fieldType
            mw.visitMethodInsn(INVOKEVIRTUAL, context.className, "writeAsArray",
                               "(Lcom/alibaba/fastjson/serializer/JSONSerializer;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/reflect/Type;)V");

            mw.visitInsn(RETURN);

            mw.visitLabel(endWriteAsArray_);
        }

        {
            mw.visitVarInsn(ALOAD, Context.serializer);
            mw.visitMethodInsn(INVOKEVIRTUAL, "com/alibaba/fastjson/serializer/JSONSerializer", "getContext",
                               "()Lcom/alibaba/fastjson/serializer/SerialContext;");
            mw.visitVarInsn(ASTORE, context.var("parent"));

            mw.visitVarInsn(ALOAD, Context.serializer);
            mw.visitVarInsn(ALOAD, context.var("parent"));
            mw.visitVarInsn(ALOAD, Context.obj);
            mw.visitVarInsn(ALOAD, Context.paramFieldName);
            mw.visitLdcInsn(context.beanSerializeFeatures);
            mw.visitMethodInsn(INVOKEVIRTUAL, "com/alibaba/fastjson/serializer/JSONSerializer", "setContext",
                               "(Lcom/alibaba/fastjson/serializer/SerialContext;Ljava/lang/Object;Ljava/lang/Object;I)V");
        }

        // SEPERATO
        {
            Label end_ = new Label();
            Label else_ = new Label();
            Label writeClass_ = new Label();

            mw.visitVarInsn(ALOAD, Context.serializer);
            mw.visitVarInsn(ALOAD, Context.paramFieldType);
            mw.visitVarInsn(ALOAD, Context.obj);
            mw.visitMethodInsn(INVOKEVIRTUAL, "com/alibaba/fastjson/serializer/JSONSerializer", "isWriteClassName",
                               "(Ljava/lang/reflect/Type;Ljava/lang/Object;)Z");
            mw.visitJumpInsn(IFEQ, else_);

            // IFNULL
            mw.visitVarInsn(ALOAD, Context.paramFieldType);
            mw.visitVarInsn(ALOAD, Context.obj);
            mw.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Object", "getClass", "()Ljava/lang/Class;");
            mw.visitJumpInsn(IF_ACMPEQ, else_);

            mw.visitLabel(writeClass_);
            mw.visitVarInsn(ALOAD, context.var("out"));
            mw.visitLdcInsn("{\"" + JSON.DEFAULT_TYPE_KEY + "\":\"" + clazz.getName() + "\"");
            mw.visitMethodInsn(INVOKEVIRTUAL, "com/alibaba/fastjson/serializer/SerializeWriter", "write", "(Ljava/lang/String;)V");
            mw.visitVarInsn(BIPUSH, ',');
            mw.visitJumpInsn(GOTO, end_);

            mw.visitLabel(else_);
            mw.visitVarInsn(BIPUSH, '{');

            mw.visitLabel(end_);
        }

        mw.visitVarInsn(ISTORE, context.var("seperator"));

        _before(mw, context);
        
        mw.visitVarInsn(ALOAD, context.var("out"));
        mw.visitMethodInsn(INVOKEVIRTUAL, "com/alibaba/fastjson/serializer/SerializeWriter", "isNotWriteDefaultValue",
                           "()Z");
        mw.visitVarInsn(ISTORE, context.var("notWriteDefaultValue"));

        for (int i = 0; i < size; ++i) {
            FieldInfo property = getters.get(i);
            Class<?> propertyClass = property.fieldClass;

            mw.visitLdcInsn(property.name);
            mw.visitVarInsn(ASTORE, Context.fieldName);

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

        _after(mw, context);
        
        Label _else = new Label();
        Label _end_if = new Label();

        mw.visitVarInsn(ILOAD, context.var("seperator"));
        mw.visitIntInsn(BIPUSH, '{');
        mw.visitJumpInsn(IF_ICMPNE, _else);

        mw.visitVarInsn(ALOAD, context.var("out"));
        mw.visitVarInsn(BIPUSH, '{');
        mw.visitMethodInsn(INVOKEVIRTUAL, "com/alibaba/fastjson/serializer/SerializeWriter", "write", "(I)V");

        mw.visitLabel(_else);

        mw.visitVarInsn(ALOAD, context.var("out"));
        mw.visitVarInsn(BIPUSH, '}');
        mw.visitMethodInsn(INVOKEVIRTUAL, "com/alibaba/fastjson/serializer/SerializeWriter", "write", "(I)V");

        mw.visitLabel(_end_if);
        mw.visitLabel(end);

        mw.visitVarInsn(ALOAD, Context.serializer);
        mw.visitVarInsn(ALOAD, context.var("parent"));
        mw.visitMethodInsn(INVOKEVIRTUAL, "com/alibaba/fastjson/serializer/JSONSerializer", "setContext",
                           "(Lcom/alibaba/fastjson/serializer/SerialContext;)V");

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
        JSONField annotation = property.getAnnotation();
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
        mw.visitTypeInsn(CHECKCAST, "java/lang/Enum"); // cast
        mw.visitVarInsn(ASTORE, context.var("enum"));

        _filters(mw, property, context, _end);

        mw.visitVarInsn(ALOAD, context.var("enum"));
        mw.visitJumpInsn(IFNONNULL, _not_null);
        _if_write_null(mw, property, context);
        mw.visitJumpInsn(GOTO, _end_if);

        mw.visitLabel(_not_null);
        mw.visitVarInsn(ALOAD, context.var("out"));
        mw.visitVarInsn(ILOAD, context.var("seperator"));
        mw.visitVarInsn(ALOAD, Context.fieldName);
        mw.visitVarInsn(ALOAD, context.var("enum"));

        if (writeEnumUsingToString) {
            mw.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Object", "toString", "()Ljava/lang/String;");
            mw.visitMethodInsn(INVOKEVIRTUAL, "com/alibaba/fastjson/serializer/SerializeWriter", "writeFieldValue",
                               "(CLjava/lang/String;Ljava/lang/String;)V");
        } else {
            mw.visitMethodInsn(INVOKEVIRTUAL, "com/alibaba/fastjson/serializer/SerializeWriter", "writeFieldValue",
                               "(CLjava/lang/String;Ljava/lang/Enum;)V");
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
        mw.visitVarInsn(ALOAD, Context.fieldName);
        mw.visitVarInsn(LLOAD, context.var("long", 2));
        mw.visitMethodInsn(INVOKEVIRTUAL, "com/alibaba/fastjson/serializer/SerializeWriter", "writeFieldValue", "(CLjava/lang/String;J)V");

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
        mw.visitVarInsn(ALOAD, Context.fieldName);
        mw.visitVarInsn(FLOAD, context.var("float"));
        mw.visitMethodInsn(INVOKEVIRTUAL, "com/alibaba/fastjson/serializer/SerializeWriter", "writeFieldValue", "(CLjava/lang/String;F)V");

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
        mw.visitVarInsn(ALOAD, Context.fieldName);
        mw.visitVarInsn(DLOAD, context.var("double", 2));
        mw.visitMethodInsn(INVOKEVIRTUAL, "com/alibaba/fastjson/serializer/SerializeWriter", "writeFieldValue", "(CLjava/lang/String;D)V");

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
        mw.visitVarInsn(ALOAD, Context.fieldName);
        mw.visitVarInsn(ILOAD, context.var("char"));

        mw.visitMethodInsn(INVOKEVIRTUAL, "com/alibaba/fastjson/serializer/SerializeWriter", "writeFieldValue", "(CLjava/lang/String;C)V");

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
        mw.visitVarInsn(ALOAD, Context.fieldName);
        mw.visitVarInsn(ILOAD, context.var("boolean"));

        mw.visitMethodInsn(INVOKEVIRTUAL, "com/alibaba/fastjson/serializer/SerializeWriter", "writeFieldValue", "(CLjava/lang/String;Z)V");

        _seperator(mw, context);

        mw.visitLabel(_end);
    }

    private void _get(MethodVisitor mw, Context context, FieldInfo property) {
        Method method = property.method;
        if (method != null) {
            mw.visitVarInsn(ALOAD, context.var("entity"));
            mw.visitMethodInsn(INVOKEVIRTUAL, getType(method.getDeclaringClass()), method.getName(), getDesc(method));
        } else {
            mw.visitVarInsn(ALOAD, context.var("entity"));
            mw.visitFieldInsn(GETFIELD, getType(property.declaringClass), property.field.getName(),
                              getDesc(property.fieldClass));
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
        mw.visitVarInsn(ALOAD, Context.fieldName);
        mw.visitVarInsn(ILOAD, context.var("byte"));

        mw.visitMethodInsn(INVOKEVIRTUAL, "com/alibaba/fastjson/serializer/SerializeWriter", "writeFieldValue", "(CLjava/lang/String;I)V");

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
        mw.visitVarInsn(ALOAD, Context.fieldName);
        mw.visitVarInsn(ILOAD, context.var("short"));

        mw.visitMethodInsn(INVOKEVIRTUAL, "com/alibaba/fastjson/serializer/SerializeWriter", "writeFieldValue", "(CLjava/lang/String;I)V");

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
        mw.visitVarInsn(ALOAD, Context.fieldName);
        mw.visitVarInsn(ILOAD, context.var("int"));

        mw.visitMethodInsn(INVOKEVIRTUAL, "com/alibaba/fastjson/serializer/SerializeWriter", "writeFieldValue", "(CLjava/lang/String;I)V");

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
        mw.visitVarInsn(ALOAD, Context.fieldName);
        mw.visitVarInsn(ALOAD, context.var("decimal"));
        mw.visitMethodInsn(INVOKEVIRTUAL, "com/alibaba/fastjson/serializer/SerializeWriter", "writeFieldValue",
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
        mw.visitVarInsn(ALOAD, Context.fieldName);
        mw.visitVarInsn(ALOAD, context.var("string"));
        mw.visitMethodInsn(INVOKEVIRTUAL, "com/alibaba/fastjson/serializer/SerializeWriter", "writeFieldValue",
                           "(CLjava/lang/String;Ljava/lang/String;)V");

        _seperator(mw, context);

        mw.visitLabel(_end_if);

        mw.visitLabel(_end);
    }

    private void _list(Class<?> clazz, MethodVisitor mw, FieldInfo property, Context context) {
        Type propertyType = property.fieldType;

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
        mw.visitTypeInsn(CHECKCAST, "java/util/List"); // cast
        mw.visitVarInsn(ASTORE, context.var("list"));

        _filters(mw, property, context, _end);

        mw.visitVarInsn(ALOAD, context.var("list"));
        mw.visitJumpInsn(IFNONNULL, _else);
        _if_write_null(mw, property, context);
        mw.visitJumpInsn(GOTO, _end_if);

        mw.visitLabel(_else); // else {

        mw.visitVarInsn(ALOAD, context.var("out"));
        mw.visitVarInsn(ILOAD, context.var("seperator"));
        mw.visitMethodInsn(INVOKEVIRTUAL, "com/alibaba/fastjson/serializer/SerializeWriter", "write", "(I)V");

        mw.visitVarInsn(ALOAD, context.var("out"));
        mw.visitVarInsn(ALOAD, Context.fieldName);
        mw.visitInsn(ICONST_0);
        mw.visitMethodInsn(INVOKEVIRTUAL, "com/alibaba/fastjson/serializer/SerializeWriter", "writeFieldName", "(Ljava/lang/String;Z)V");

        //
        mw.visitVarInsn(ALOAD, context.var("list"));
        mw.visitMethodInsn(INVOKEINTERFACE, "java/util/List", "size", "()I");
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
        mw.visitMethodInsn(INVOKEVIRTUAL, "com/alibaba/fastjson/serializer/SerializeWriter", "write", "(Ljava/lang/String;)V");

        mw.visitJumpInsn(GOTO, _end_if_3);

        mw.visitLabel(_else_3);

        {
            mw.visitVarInsn(ALOAD, Context.serializer);
            mw.visitVarInsn(ALOAD, context.var("list"));
            mw.visitVarInsn(ALOAD, Context.fieldName);
            mw.visitMethodInsn(INVOKEVIRTUAL, "com/alibaba/fastjson/serializer/JSONSerializer", "setContext",
                               "(Ljava/lang/Object;Ljava/lang/Object;)V");
        }

        {
            mw.visitVarInsn(ALOAD, context.var("out"));
            mw.visitVarInsn(BIPUSH, '[');
            mw.visitMethodInsn(INVOKEVIRTUAL, "com/alibaba/fastjson/serializer/SerializeWriter", "write", "(I)V");

            // list_serializer = null
            mw.visitInsn(ACONST_NULL);
            mw.visitTypeInsn(CHECKCAST, "com/alibaba/fastjson/serializer/ObjectSerializer"); // cast to string
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
                mw.visitMethodInsn(INVOKEINTERFACE, "java/util/List", "get", "(I)Ljava/lang/Object;");
                mw.visitTypeInsn(CHECKCAST, "java/lang/String"); // cast to string
                mw.visitVarInsn(BIPUSH, ',');
                mw.visitMethodInsn(INVOKEVIRTUAL, "com/alibaba/fastjson/serializer/SerializeWriter", "writeString",
                                   "(Ljava/lang/String;C)V");
            } else {
                mw.visitVarInsn(ALOAD, Context.serializer);
                mw.visitVarInsn(ALOAD, context.var("list"));
                mw.visitVarInsn(ILOAD, context.var("i"));
                mw.visitMethodInsn(INVOKEINTERFACE, "java/util/List", "get", "(I)Ljava/lang/Object;");
                mw.visitVarInsn(ILOAD, context.var("i"));
                mw.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;");

                if (elementClass != null && Modifier.isPublic(elementClass.getModifiers())) {
                    mw.visitLdcInsn(com.alibaba.fastjson.asm.Type.getType(getDesc((Class<?>) elementType)));
                    mw.visitLdcInsn(property.serialzeFeatures);
                    mw.visitMethodInsn(INVOKEVIRTUAL, "com/alibaba/fastjson/serializer/JSONSerializer", "writeWithFieldName",
                                       "(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/reflect/Type;I)V");
                } else {
                    mw.visitMethodInsn(INVOKEVIRTUAL, "com/alibaba/fastjson/serializer/JSONSerializer", "writeWithFieldName",
                                       "(Ljava/lang/Object;Ljava/lang/Object;)V");
                }

                mw.visitVarInsn(ALOAD, context.var("out"));
                mw.visitVarInsn(BIPUSH, ',');
                mw.visitMethodInsn(INVOKEVIRTUAL, "com/alibaba/fastjson/serializer/SerializeWriter", "write", "(I)V");
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
                mw.visitMethodInsn(INVOKEINTERFACE, "java/util/List", "get", "(I)Ljava/lang/Object;");
                mw.visitTypeInsn(CHECKCAST, "java/lang/String"); // cast to string
                mw.visitVarInsn(BIPUSH, ']');
                mw.visitMethodInsn(INVOKEVIRTUAL, "com/alibaba/fastjson/serializer/SerializeWriter", "writeString",
                                   "(Ljava/lang/String;C)V");
            } else {
                mw.visitVarInsn(ALOAD, Context.serializer);
                mw.visitVarInsn(ALOAD, context.var("list"));
                mw.visitVarInsn(ILOAD, context.var("i"));
                mw.visitMethodInsn(INVOKEINTERFACE, "java/util/List", "get", "(I)Ljava/lang/Object;");
                mw.visitVarInsn(ILOAD, context.var("i"));
                mw.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;");

                if (elementClass != null && Modifier.isPublic(elementClass.getModifiers())) {
                    mw.visitLdcInsn(com.alibaba.fastjson.asm.Type.getType(getDesc((Class<?>) elementType)));
                    mw.visitLdcInsn(property.serialzeFeatures);
                    mw.visitMethodInsn(INVOKEVIRTUAL, "com/alibaba/fastjson/serializer/JSONSerializer", "writeWithFieldName",
                                       "(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/reflect/Type;I)V");
                } else {
                    mw.visitMethodInsn(INVOKEVIRTUAL, "com/alibaba/fastjson/serializer/JSONSerializer", "writeWithFieldName",
                                       "(Ljava/lang/Object;Ljava/lang/Object;)V");
                }

                mw.visitVarInsn(ALOAD, context.var("out"));
                mw.visitVarInsn(BIPUSH, ']');
                mw.visitMethodInsn(INVOKEVIRTUAL, "com/alibaba/fastjson/serializer/SerializeWriter", "write", "(I)V");
            }
        }

        {
            mw.visitVarInsn(ALOAD, Context.serializer);
            mw.visitMethodInsn(INVOKEVIRTUAL, "com/alibaba/fastjson/serializer/JSONSerializer", "popContext", "()V");
        }

        mw.visitLabel(_end_if_3);

        _seperator(mw, context);

        mw.visitLabel(_end_if);

        mw.visitLabel(_end);
    }

    private void _filters(MethodVisitor mw, FieldInfo property, Context context, Label _end) {
        if (property.field != null) {
            if (Modifier.isTransient(property.field.getModifiers())) {
                mw.visitVarInsn(ALOAD, context.var("out"));
                mw.visitMethodInsn(INVOKEVIRTUAL, "com/alibaba/fastjson/serializer/SerializeWriter", "isSkipTransientField",
                                   "()Z");

                // if true
                mw.visitJumpInsn(IFNE, _end);
            }
        }
        
        _notWriteDefault(mw, property, context, _end);
        
        _apply(mw, property, context);
        mw.visitJumpInsn(IFEQ, _end);

        _processKey(mw, property, context);

        Label _else_processKey = new Label();
        _processValue(mw, property, context);

        mw.visitVarInsn(ALOAD, Context.original);
        mw.visitVarInsn(ALOAD, Context.processValue);
        mw.visitJumpInsn(IF_ACMPEQ, _else_processKey);
        _writeObject(mw, property, context, _end);
        mw.visitJumpInsn(GOTO, _end);

        mw.visitLabel(_else_processKey);
    }

    private void _nameApply(MethodVisitor mw, FieldInfo property, Context context, Label _end) {
        mw.visitVarInsn(ALOAD, Context.serializer);
        mw.visitVarInsn(ALOAD, Context.obj);
        mw.visitVarInsn(ALOAD, Context.fieldName);
        mw.visitMethodInsn(INVOKESTATIC, "com/alibaba/fastjson/serializer/FilterUtils", "applyName",
                           "(Lcom/alibaba/fastjson/serializer/JSONSerializer;Ljava/lang/Object;Ljava/lang/String;)Z");
        mw.visitJumpInsn(IFEQ, _end);
        
        _labelApply(mw, property, context, _end);
        
        if (property.field == null) {
            mw.visitVarInsn(ALOAD, context.var("out"));
            mw.visitMethodInsn(INVOKEVIRTUAL, "com/alibaba/fastjson/serializer/SerializeWriter", "isIgnoreNonFieldGetter",
                               "()Z");

            // if true
            mw.visitJumpInsn(IFNE, _end);
        }
    }
    
    private void _labelApply(MethodVisitor mw, FieldInfo property, Context context, Label _end) {
        mw.visitVarInsn(ALOAD, Context.serializer);
        mw.visitLdcInsn(property.label);
        
        mw.visitMethodInsn(INVOKESTATIC, "com/alibaba/fastjson/serializer/FilterUtils", "applyLabel",
                "(Lcom/alibaba/fastjson/serializer/JSONSerializer;Ljava/lang/String;)Z");
        mw.visitJumpInsn(IFEQ, _end);
    }

    private void _writeObject(MethodVisitor mw, FieldInfo fieldInfo, Context context, Label _end) {
        String format = fieldInfo.getFormat();

        Label _not_null = new Label();

        mw.visitVarInsn(ALOAD, Context.processValue);
        mw.visitJumpInsn(IFNONNULL, _not_null); // if (obj == null)
        _if_write_null(mw, fieldInfo, context);
        mw.visitJumpInsn(GOTO, _end);

        mw.visitLabel(_not_null);
        // writeFieldNullNumber
        mw.visitVarInsn(ALOAD, context.var("out"));
        mw.visitVarInsn(ILOAD, context.var("seperator"));
        mw.visitMethodInsn(INVOKEVIRTUAL, "com/alibaba/fastjson/serializer/SerializeWriter", "write", "(I)V");

        // out.writeFieldName("fieldName")
        mw.visitVarInsn(ALOAD, context.var("out"));
        mw.visitVarInsn(ALOAD, Context.fieldName);
        mw.visitInsn(ICONST_0);
        mw.visitMethodInsn(INVOKEVIRTUAL, "com/alibaba/fastjson/serializer/SerializeWriter", "writeFieldName", "(Ljava/lang/String;Z)V");

        // serializer.write(obj)
        mw.visitVarInsn(ALOAD, Context.serializer);
        mw.visitVarInsn(ALOAD, Context.processValue);
        if (format != null) {
            mw.visitLdcInsn(format);
            mw.visitMethodInsn(INVOKEVIRTUAL, "com/alibaba/fastjson/serializer/JSONSerializer", "writeWithFormat",
                               "(Ljava/lang/Object;Ljava/lang/String;)V");
        } else {
            mw.visitVarInsn(ALOAD, Context.fieldName);
            if (fieldInfo.fieldType instanceof Class<?> //
                && ((Class<?>) fieldInfo.fieldType).isPrimitive()) {
                mw.visitMethodInsn(INVOKEVIRTUAL, "com/alibaba/fastjson/serializer/JSONSerializer", "writeWithFieldName",
                                   "(Ljava/lang/Object;Ljava/lang/Object;)V");
            } else {
                if (fieldInfo.fieldClass == String.class) {
                    mw.visitLdcInsn(com.alibaba.fastjson.asm.Type.getType(getDesc(String.class)));
                } else {
                    mw.visitVarInsn(ALOAD, 0);
                    mw.visitFieldInsn(GETFIELD, context.className, fieldInfo.name + "_asm_fieldType",
                                      "Ljava/lang/reflect/Type;");
                }
                mw.visitLdcInsn(fieldInfo.serialzeFeatures);

                mw.visitMethodInsn(INVOKEVIRTUAL, "com/alibaba/fastjson/serializer/JSONSerializer", "writeWithFieldName",
                                   "(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/reflect/Type;I)V");
            }
        }

        _seperator(mw, context);
    }

    private void _before(MethodVisitor mw, Context context) {
        mw.visitVarInsn(ALOAD, Context.serializer);
        mw.visitVarInsn(ALOAD, Context.obj);
        mw.visitVarInsn(ILOAD, context.var("seperator"));
        mw.visitMethodInsn(INVOKESTATIC, "com/alibaba/fastjson/serializer/FilterUtils", "writeBefore",
                           "(Lcom/alibaba/fastjson/serializer/JSONSerializer;Ljava/lang/Object;C)C");
        mw.visitVarInsn(ISTORE, context.var("seperator"));
    }

    private void _after(MethodVisitor mw, Context context) {
        mw.visitVarInsn(ALOAD, Context.serializer);
        mw.visitVarInsn(ALOAD, Context.obj);
        mw.visitVarInsn(ILOAD, context.var("seperator"));
        mw.visitMethodInsn(INVOKESTATIC, "com/alibaba/fastjson/serializer/FilterUtils", "writeAfter",
                           "(Lcom/alibaba/fastjson/serializer/JSONSerializer;Ljava/lang/Object;C)C");
        mw.visitVarInsn(ISTORE, context.var("seperator"));
    }
    
    private void _notWriteDefault(MethodVisitor mw, FieldInfo property, Context context, Label _end) {
        Label elseLabel = new Label();
        
        mw.visitVarInsn(ILOAD, context.var("notWriteDefaultValue"));
        mw.visitJumpInsn(IFEQ, elseLabel);
        
        Class<?> propertyClass = property.fieldClass;
        if (propertyClass == boolean.class) {
            mw.visitVarInsn(ILOAD, context.var("boolean"));
            mw.visitJumpInsn(IFEQ, _end);
        } else if (propertyClass == byte.class) {
            mw.visitVarInsn(ILOAD, context.var("byte"));
            mw.visitJumpInsn(IFEQ, _end);
        } else if (propertyClass == short.class) {
            mw.visitVarInsn(ILOAD, context.var("short"));
            mw.visitJumpInsn(IFEQ, _end);
        } else if (propertyClass == int.class) {
            mw.visitVarInsn(ILOAD, context.var("int"));
            mw.visitJumpInsn(IFEQ, _end);
        } else if (propertyClass == long.class) {
            mw.visitVarInsn(LLOAD, context.var("long"));
            mw.visitInsn(LCONST_0);
            mw.visitInsn(LCMP);
            mw.visitJumpInsn(IFEQ, _end);
        } else if (propertyClass == float.class) {
            mw.visitVarInsn(FLOAD, context.var("float"));
            mw.visitInsn(FCONST_0);
            mw.visitInsn(FCMPL);
            mw.visitJumpInsn(IFEQ, _end);
        } else if (propertyClass == double.class) {
            mw.visitVarInsn(DLOAD, context.var("double"));
            mw.visitInsn(DCONST_0);
            mw.visitInsn(DCMPL);
            mw.visitJumpInsn(IFEQ, _end);
        }
        
        mw.visitLabel(elseLabel);
    }

    private void _apply(MethodVisitor mw, FieldInfo property, Context context) {
        Class<?> propertyClass = property.fieldClass;

        mw.visitVarInsn(ALOAD, Context.serializer);
        mw.visitVarInsn(ALOAD, Context.obj);
        mw.visitVarInsn(ALOAD, Context.fieldName);

        if (propertyClass == byte.class) {
            mw.visitVarInsn(ILOAD, context.var("byte"));
            mw.visitMethodInsn(INVOKESTATIC, "com/alibaba/fastjson/serializer/FilterUtils", "apply",
                               "(Lcom/alibaba/fastjson/serializer/JSONSerializer;Ljava/lang/Object;Ljava/lang/String;B)Z");
        } else if (propertyClass == short.class) {
            mw.visitVarInsn(ILOAD, context.var("short"));
            mw.visitMethodInsn(INVOKESTATIC, "com/alibaba/fastjson/serializer/FilterUtils", "apply",
                               "(Lcom/alibaba/fastjson/serializer/JSONSerializer;Ljava/lang/Object;Ljava/lang/String;S)Z");
        } else if (propertyClass == int.class) {
            mw.visitVarInsn(ILOAD, context.var("int"));
            mw.visitMethodInsn(INVOKESTATIC, "com/alibaba/fastjson/serializer/FilterUtils", "apply",
                               "(Lcom/alibaba/fastjson/serializer/JSONSerializer;Ljava/lang/Object;Ljava/lang/String;I)Z");
        } else if (propertyClass == char.class) {
            mw.visitVarInsn(ILOAD, context.var("char"));
            mw.visitMethodInsn(INVOKESTATIC, "com/alibaba/fastjson/serializer/FilterUtils", "apply",
                               "(Lcom/alibaba/fastjson/serializer/JSONSerializer;Ljava/lang/Object;Ljava/lang/String;C)Z");
        } else if (propertyClass == long.class) {
            mw.visitVarInsn(LLOAD, context.var("long", 2));
            mw.visitMethodInsn(INVOKESTATIC, "com/alibaba/fastjson/serializer/FilterUtils", "apply",
                               "(Lcom/alibaba/fastjson/serializer/JSONSerializer;Ljava/lang/Object;Ljava/lang/String;J)Z");
        } else if (propertyClass == float.class) {
            mw.visitVarInsn(FLOAD, context.var("float"));
            mw.visitMethodInsn(INVOKESTATIC, "com/alibaba/fastjson/serializer/FilterUtils", "apply",
                               "(Lcom/alibaba/fastjson/serializer/JSONSerializer;Ljava/lang/Object;Ljava/lang/String;F)Z");
        } else if (propertyClass == double.class) {
            mw.visitVarInsn(DLOAD, context.var("double", 2));
            mw.visitMethodInsn(INVOKESTATIC, "com/alibaba/fastjson/serializer/FilterUtils", "apply",
                               "(Lcom/alibaba/fastjson/serializer/JSONSerializer;Ljava/lang/Object;Ljava/lang/String;D)Z");
        } else if (propertyClass == boolean.class) {
            mw.visitVarInsn(ILOAD, context.var("boolean"));
            mw.visitMethodInsn(INVOKESTATIC, "com/alibaba/fastjson/serializer/FilterUtils", "apply",
                               "(Lcom/alibaba/fastjson/serializer/JSONSerializer;Ljava/lang/Object;Ljava/lang/String;B)Z");
        } else if (propertyClass == BigDecimal.class) {
            mw.visitVarInsn(ALOAD, context.var("decimal"));
            mw.visitMethodInsn(INVOKESTATIC, "com/alibaba/fastjson/serializer/FilterUtils", "apply",
                               "(Lcom/alibaba/fastjson/serializer/JSONSerializer;Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Object;)Z");
        } else if (propertyClass == String.class) {
            mw.visitVarInsn(ALOAD, context.var("string"));
            mw.visitMethodInsn(INVOKESTATIC, "com/alibaba/fastjson/serializer/FilterUtils", "apply",
                               "(Lcom/alibaba/fastjson/serializer/JSONSerializer;Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Object;)Z");
        } else if (propertyClass.isEnum()) {
            mw.visitVarInsn(ALOAD, context.var("enum"));
            mw.visitMethodInsn(INVOKESTATIC, "com/alibaba/fastjson/serializer/FilterUtils", "apply",
                               "(Lcom/alibaba/fastjson/serializer/JSONSerializer;Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Object;)Z");
        } else if (List.class.isAssignableFrom(propertyClass)) {
            mw.visitVarInsn(ALOAD, context.var("list"));
            mw.visitMethodInsn(INVOKESTATIC, "com/alibaba/fastjson/serializer/FilterUtils", "apply",
                               "(Lcom/alibaba/fastjson/serializer/JSONSerializer;Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Object;)Z");
        } else {
            mw.visitVarInsn(ALOAD, context.var("object"));
            mw.visitMethodInsn(INVOKESTATIC, "com/alibaba/fastjson/serializer/FilterUtils", "apply",
                               "(Lcom/alibaba/fastjson/serializer/JSONSerializer;Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Object;)Z");
        }
    }

    private void _processValue(MethodVisitor mw, FieldInfo property, Context context) {
        Class<?> propertyClass = property.fieldClass;

        mw.visitVarInsn(ALOAD, Context.serializer);
        mw.visitVarInsn(ALOAD, Context.obj);
        mw.visitVarInsn(ALOAD, Context.fieldName);

        String valueDesc = "Ljava/lang/Object;";
        if (propertyClass == byte.class) {
            mw.visitVarInsn(ILOAD, context.var("byte"));
            valueDesc = "B";
            mw.visitInsn(ACONST_NULL);
            mw.visitVarInsn(ASTORE, Context.original);
        } else if (propertyClass == short.class) {
            mw.visitVarInsn(ILOAD, context.var("short"));
            valueDesc = "S";
            mw.visitInsn(ACONST_NULL);
            mw.visitVarInsn(ASTORE, Context.original);
        } else if (propertyClass == int.class) {
            mw.visitVarInsn(ILOAD, context.var("int"));
            valueDesc = "I";
            mw.visitInsn(ACONST_NULL);
            mw.visitVarInsn(ASTORE, Context.original);
        } else if (propertyClass == char.class) {
            mw.visitVarInsn(ILOAD, context.var("char"));
            mw.visitMethodInsn(INVOKESTATIC, "java/lang/Character", "valueOf", "(C)Ljava/lang/Character;");
            mw.visitVarInsn(ASTORE, Context.original);
            mw.visitVarInsn(ALOAD, Context.original);
        } else if (propertyClass == long.class) {
            mw.visitVarInsn(LLOAD, context.var("long", 2));
            valueDesc = "J";
            mw.visitInsn(ACONST_NULL);
            mw.visitVarInsn(ASTORE, Context.original);
        } else if (propertyClass == float.class) {
            mw.visitVarInsn(FLOAD, context.var("float"));
            valueDesc = "F";
            mw.visitInsn(ACONST_NULL);
            mw.visitVarInsn(ASTORE, Context.original);
        } else if (propertyClass == double.class) {
            mw.visitVarInsn(DLOAD, context.var("double", 2));
            valueDesc = "D";
            mw.visitInsn(ACONST_NULL);
            mw.visitVarInsn(ASTORE, Context.original);
        } else if (propertyClass == boolean.class) {
            mw.visitVarInsn(ILOAD, context.var("boolean"));
            valueDesc = "Z";
            mw.visitInsn(ACONST_NULL);
            mw.visitVarInsn(ASTORE, Context.original);
        } else if (propertyClass == BigDecimal.class) {
            mw.visitVarInsn(ALOAD, context.var("decimal"));
            mw.visitVarInsn(ASTORE, Context.original);
            mw.visitVarInsn(ALOAD, Context.original);
        } else if (propertyClass == String.class) {
            mw.visitVarInsn(ALOAD, context.var("string"));
            mw.visitVarInsn(ASTORE, Context.original);
            mw.visitVarInsn(ALOAD, Context.original);
        } else if (propertyClass.isEnum()) {
            mw.visitVarInsn(ALOAD, context.var("enum"));
            mw.visitVarInsn(ASTORE, Context.original);
            mw.visitVarInsn(ALOAD, Context.original);
        } else if (List.class.isAssignableFrom(propertyClass)) {
            mw.visitVarInsn(ALOAD, context.var("list"));
            mw.visitVarInsn(ASTORE, Context.original);
            mw.visitVarInsn(ALOAD, Context.original);
        } else {
            mw.visitVarInsn(ALOAD, context.var("object"));
            mw.visitVarInsn(ASTORE, Context.original);
            mw.visitVarInsn(ALOAD, Context.original);
        }

        mw.visitMethodInsn(INVOKESTATIC,
                           "com/alibaba/fastjson/serializer/FilterUtils",
                           "processValue",
                           "(Lcom/alibaba/fastjson/serializer/JSONSerializer;Ljava/lang/Object;Ljava/lang/String;" + valueDesc + ")Ljava/lang/Object;");

        mw.visitVarInsn(ASTORE, Context.processValue);
    }

    private void _processKey(MethodVisitor mw, FieldInfo property, Context context) {
        Class<?> propertyClass = property.fieldClass;

        mw.visitVarInsn(ALOAD, Context.serializer);
        mw.visitVarInsn(ALOAD, Context.obj);
        mw.visitVarInsn(ALOAD, Context.fieldName);

        if (propertyClass == byte.class) {
            mw.visitVarInsn(ILOAD, context.var("byte"));
            mw.visitMethodInsn(INVOKESTATIC, "com/alibaba/fastjson/serializer/FilterUtils", "processKey",
                               "(Lcom/alibaba/fastjson/serializer/JSONSerializer;Ljava/lang/Object;Ljava/lang/String;B)Ljava/lang/String;");
        } else if (propertyClass == short.class) {
            mw.visitVarInsn(ILOAD, context.var("short"));
            mw.visitMethodInsn(INVOKESTATIC, "com/alibaba/fastjson/serializer/FilterUtils", "processKey",
                               "(Lcom/alibaba/fastjson/serializer/JSONSerializer;Ljava/lang/Object;Ljava/lang/String;S)Ljava/lang/String;");
        } else if (propertyClass == int.class) {
            mw.visitVarInsn(ILOAD, context.var("int"));
            mw.visitMethodInsn(INVOKESTATIC, "com/alibaba/fastjson/serializer/FilterUtils", "processKey",
                               "(Lcom/alibaba/fastjson/serializer/JSONSerializer;Ljava/lang/Object;Ljava/lang/String;I)Ljava/lang/String;");
        } else if (propertyClass == char.class) {
            mw.visitVarInsn(ILOAD, context.var("char"));
            mw.visitMethodInsn(INVOKESTATIC, "com/alibaba/fastjson/serializer/FilterUtils", "processKey",
                               "(Lcom/alibaba/fastjson/serializer/JSONSerializer;Ljava/lang/Object;Ljava/lang/String;C)Ljava/lang/String;");
        } else if (propertyClass == long.class) {
            mw.visitVarInsn(LLOAD, context.var("long", 2));
            mw.visitMethodInsn(INVOKESTATIC, "com/alibaba/fastjson/serializer/FilterUtils", "processKey",
                               "(Lcom/alibaba/fastjson/serializer/JSONSerializer;Ljava/lang/Object;Ljava/lang/String;J)Ljava/lang/String;");
        } else if (propertyClass == float.class) {
            mw.visitVarInsn(FLOAD, context.var("float"));
            mw.visitMethodInsn(INVOKESTATIC, "com/alibaba/fastjson/serializer/FilterUtils", "processKey",
                               "(Lcom/alibaba/fastjson/serializer/JSONSerializer;Ljava/lang/Object;Ljava/lang/String;F)Ljava/lang/String;");
        } else if (propertyClass == double.class) {
            mw.visitVarInsn(DLOAD, context.var("double", 2));
            mw.visitMethodInsn(INVOKESTATIC, "com/alibaba/fastjson/serializer/FilterUtils", "processKey",
                               "(Lcom/alibaba/fastjson/serializer/JSONSerializer;Ljava/lang/Object;Ljava/lang/String;D)Ljava/lang/String;");
        } else if (propertyClass == boolean.class) {
            mw.visitVarInsn(ILOAD, context.var("boolean"));
            mw.visitMethodInsn(INVOKESTATIC, "com/alibaba/fastjson/serializer/FilterUtils", "processKey",
                               "(Lcom/alibaba/fastjson/serializer/JSONSerializer;Ljava/lang/Object;Ljava/lang/String;Z)Ljava/lang/String;");
        } else if (propertyClass == BigDecimal.class) {
            mw.visitVarInsn(ALOAD, context.var("decimal"));
            mw.visitMethodInsn(INVOKESTATIC,
                               "com/alibaba/fastjson/serializer/FilterUtils",
                               "processKey",
                               "(Lcom/alibaba/fastjson/serializer/JSONSerializer;Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/String;");
        } else if (propertyClass == String.class) {
            mw.visitVarInsn(ALOAD, context.var("string"));
            mw.visitMethodInsn(INVOKESTATIC,
                               "com/alibaba/fastjson/serializer/FilterUtils",
                               "processKey",
                               "(Lcom/alibaba/fastjson/serializer/JSONSerializer;Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/String;");
        } else if (propertyClass.isEnum()) {
            mw.visitVarInsn(ALOAD, context.var("enum"));
            mw.visitMethodInsn(INVOKESTATIC,
                               "com/alibaba/fastjson/serializer/FilterUtils",
                               "processKey",
                               "(Lcom/alibaba/fastjson/serializer/JSONSerializer;Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/String;");
        } else if (List.class.isAssignableFrom(propertyClass)) {

            mw.visitVarInsn(ALOAD, context.var("list"));
            mw.visitMethodInsn(INVOKESTATIC,
                               "com/alibaba/fastjson/serializer/FilterUtils",
                               "processKey",
                               "(Lcom/alibaba/fastjson/serializer/JSONSerializer;Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/String;");
        } else {
            mw.visitVarInsn(ALOAD, context.var("object"));
            mw.visitMethodInsn(INVOKESTATIC,
                               "com/alibaba/fastjson/serializer/FilterUtils",
                               "processKey",
                               "(Lcom/alibaba/fastjson/serializer/JSONSerializer;Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/String;");
        }

        mw.visitVarInsn(ASTORE, Context.fieldName);
    }

    private void _if_write_null(MethodVisitor mw, FieldInfo fieldInfo, Context context) {
        Class<?> propertyClass = fieldInfo.fieldClass;

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
        JSONField annotation = fieldInfo.getAnnotation();
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
            mw.visitMethodInsn(INVOKEVIRTUAL, "com/alibaba/fastjson/serializer/SerializeWriter", "isWriteMapNullValue",
                               "()Z");
            mw.visitJumpInsn(IFEQ, _else);
        }

        mw.visitLabel(_write_null);
        // out.writeFieldNull(seperator, 'fieldName')
        mw.visitVarInsn(ALOAD, context.var("out"));
        mw.visitVarInsn(ILOAD, context.var("seperator"));
        mw.visitVarInsn(ALOAD, Context.fieldName);

        if (propertyClass == String.class || propertyClass == Character.class) {
            if (writeNullStringAsEmpty) {
                mw.visitLdcInsn("");
                mw.visitMethodInsn(INVOKEVIRTUAL, "com/alibaba/fastjson/serializer/SerializeWriter", "writeFieldValue",
                                   "(CLjava/lang/String;Ljava/lang/String;)V");
            } else {
                mw.visitMethodInsn(INVOKEVIRTUAL, "com/alibaba/fastjson/serializer/SerializeWriter", "writeFieldNullString",
                                   "(CLjava/lang/String;)V");
            }
        } else if (Number.class.isAssignableFrom(propertyClass)) {
            if (writeNullNumberAsZero) {
                mw.visitInsn(ICONST_0);
                mw.visitMethodInsn(INVOKEVIRTUAL, "com/alibaba/fastjson/serializer/SerializeWriter", "writeFieldValue",
                                   "(CLjava/lang/String;I)V");
            } else {
                mw.visitMethodInsn(INVOKEVIRTUAL, "com/alibaba/fastjson/serializer/SerializeWriter", "writeFieldNullNumber",
                                   "(CLjava/lang/String;)V");
            }
        } else if (propertyClass == Boolean.class) {
            if (writeNullBooleanAsFalse) {
                mw.visitInsn(ICONST_0);
                mw.visitMethodInsn(INVOKEVIRTUAL, "com/alibaba/fastjson/serializer/SerializeWriter", "writeFieldValue",
                                   "(CLjava/lang/String;Z)V");
            } else {
                mw.visitMethodInsn(INVOKEVIRTUAL, "com/alibaba/fastjson/serializer/SerializeWriter", "writeFieldNullBoolean",
                                   "(CLjava/lang/String;)V");
            }
        } else if (Collection.class.isAssignableFrom(propertyClass) || propertyClass.isArray()) {
            if (writeNullListAsEmpty) {
                mw.visitMethodInsn(INVOKEVIRTUAL, "com/alibaba/fastjson/serializer/SerializeWriter", "writeFieldEmptyList",
                                   "(CLjava/lang/String;)V");
            } else {
                mw.visitMethodInsn(INVOKEVIRTUAL, "com/alibaba/fastjson/serializer/SerializeWriter", "writeFieldNullList",
                                   "(CLjava/lang/String;)V");
            }
        } else {
            mw.visitMethodInsn(INVOKEVIRTUAL, "com/alibaba/fastjson/serializer/SerializeWriter", "writeFieldNull",
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
