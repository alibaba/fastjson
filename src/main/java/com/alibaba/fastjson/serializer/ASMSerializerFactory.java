package com.alibaba.fastjson.serializer;

import static com.alibaba.fastjson.util.ASMUtils.desc;
import static com.alibaba.fastjson.util.ASMUtils.type;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.util.ASMClassLoader;
import com.alibaba.fastjson.util.ASMUtils;
import com.alibaba.fastjson.util.FieldInfo;
import com.alibaba.fastjson.util.TypeUtils;

public class ASMSerializerFactory implements Opcodes {

    protected final ASMClassLoader classLoader             = new ASMClassLoader();

    private final AtomicLong       seed                    = new AtomicLong();

    static final String            JSONSerializer          = type(JSONSerializer.class);
    static final String            SerializeWriter         = type(SerializeWriter.class);
    static final String            JavaBeanSerializer      = type(JavaBeanSerializer.class);
    static final String            JavaBeanSerializer_desc = "L" + type(JavaBeanSerializer.class) + ";";
    static final String            SerialContext_desc      = desc(SerialContext.class);

    static class Context {

        static final int              serializer     = 1;
        static final int              obj            = 2;
        static final int              paramFieldName = 3;
        static final int              paramFieldType = 4;
        static final int              features       = 5;
        static int                    fieldName      = 6;
        static int                    original       = 7;
        static int                    processValue   = 8;

        private final List<FieldInfo> getters;
        private final String          className;
        private final int             beanSerializeFeatures;
        private final boolean         writeDirect;
        private final JSONType        jsonType;
        private final int             beanFeatures;

        private Map<String, Integer>  variants       = new HashMap<String, Integer>();
        private int                   variantIndex   = 9;
        private boolean               nonContext;

        public Context(List<FieldInfo> getters, JSONType jsonType, String className, int beanSerializeFeatures,
                       boolean writeDirect, boolean nonContext){
            this.getters = getters;
            this.jsonType = jsonType;
            this.className = className;
            this.beanSerializeFeatures = beanSerializeFeatures;
            this.writeDirect = writeDirect;
            this.nonContext = nonContext;
            if (this.writeDirect) {
                processValue = 8;
            }
            this.beanFeatures = jsonType != null ? //
                SerializerFeature.of(jsonType.serialzeFeatures()) //
                : 0;
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

        JSONType jsonType = clazz.getAnnotation(JSONType.class);

        List<FieldInfo> unsortedGetters = TypeUtils.computeGetters(clazz, jsonType, aliasMap, false);

        for (FieldInfo fieldInfo : unsortedGetters) {
            if (fieldInfo.field == null //
                && fieldInfo.method != null //
                && fieldInfo.method.getDeclaringClass().isInterface()) {
                return new JavaBeanSerializer(clazz);
            }
        }

        String[] orders = null;

        if (jsonType != null) {
            orders = jsonType.orders();
        }

        List<FieldInfo> getters;
        if (orders != null && orders.length != 0) {
            getters = TypeUtils.computeGetters(clazz, jsonType, aliasMap, true);
        } else {
            getters = new ArrayList<FieldInfo>(unsortedGetters);
            Collections.sort(getters);
        }

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
        String packageName = ASMSerializerFactory.class.getPackage().getName();
        String classNameType = packageName.replace('.', '/') + "/" + className;
        String classNameFull = packageName + "." + className;
        int beanSerializeFeatures = TypeUtils.getSerializeFeatures(clazz);

        ClassWriter cw = new ClassWriter();
        cw.visit(V1_5 //
                 , ACC_PUBLIC + ACC_SUPER //
                 , classNameType //
                 , type(JavaBeanSerializer.class) //
                 , new String[] { type(ObjectSerializer.class) } //
        );

        for (FieldInfo fieldInfo : getters) {
            if (fieldInfo.fieldClass.isPrimitive() //
                || fieldInfo.fieldClass.isEnum() //
                || fieldInfo.fieldClass == String.class) {
                continue;
            }

            new FieldWriter(cw, ACC_PUBLIC, fieldInfo.name + "_asm_fieldType", "Ljava/lang/reflect/Type;") //
                                                                                                           .visitEnd();
            
            if (List.class.isAssignableFrom(fieldInfo.fieldClass)) {
                new FieldWriter(cw, ACC_PUBLIC, fieldInfo.name + "_asm_list_item_ser_",
                                desc(ObjectSerializer.class)) //
                                                              .visitEnd();
            }
            
            new FieldWriter(cw, ACC_PUBLIC, fieldInfo.name + "_asm_ser_", desc(ObjectSerializer.class)) //
                                                                                                        .visitEnd();
        }

        MethodVisitor mw = new MethodWriter(cw, ACC_PUBLIC, "<init>", "()V", null, null);
        mw.visitVarInsn(ALOAD, 0);
        mw.visitLdcInsn(com.alibaba.fastjson.asm.Type.getType(desc(clazz)));
        mw.visitMethodInsn(INVOKESPECIAL, type(JavaBeanSerializer.class), "<init>", "(Ljava/lang/Class;)V");

        // init _asm_fieldType
        for (int i = 0; i < getters.size(); ++i) {
            FieldInfo fieldInfo = getters.get(i);
            if (fieldInfo.fieldClass.isPrimitive() //
                || fieldInfo.fieldClass.isEnum() //
                || fieldInfo.fieldClass == String.class) {
                continue;
            }

            mw.visitVarInsn(ALOAD, 0);

            if (fieldInfo.method != null) {
                mw.visitLdcInsn(com.alibaba.fastjson.asm.Type.getType(desc(fieldInfo.declaringClass)));
                mw.visitLdcInsn(fieldInfo.method.getName());
                mw.visitMethodInsn(INVOKESTATIC, type(ASMUtils.class), "getMethodType",
                                   "(Ljava/lang/Class;Ljava/lang/String;)Ljava/lang/reflect/Type;");

            } else {
                mw.visitVarInsn(ALOAD, 0);
                mw.visitLdcInsn(i);
                mw.visitMethodInsn(INVOKESPECIAL, JavaBeanSerializer, "getFieldType", "(I)Ljava/lang/reflect/Type;");
            }

            mw.visitFieldInsn(PUTFIELD, classNameType, fieldInfo.name + "_asm_fieldType", "Ljava/lang/reflect/Type;");
        }

        mw.visitInsn(RETURN);
        mw.visitMaxs(4, 4);
        mw.visitEnd();
        
        boolean DisableCircularReferenceDetect = false;
        if (jsonType != null) {
            for (SerializerFeature featrues : jsonType.serialzeFeatures()) {
                if (featrues == SerializerFeature.DisableCircularReferenceDetect) {
                    DisableCircularReferenceDetect = true;
                    break;
                }
            }
        }

        // 0 write
        // 1 writeNormal
        // 2 writeNonContext
        for (int i = 0; i < 3; ++i) {
            String methodName;
            boolean nonContext = DisableCircularReferenceDetect;
            boolean writeDirect = false;
            if (i == 0) {
                methodName = "write";
                writeDirect = true;
            } else if (i == 1) {
                methodName = "writeNormal";
            } else {
                writeDirect = true;
                nonContext = true;
                methodName = "writeDirectNonContext";
            }

            Context context = new Context(getters, jsonType, classNameType, beanSerializeFeatures, writeDirect, nonContext);

            mw = new MethodWriter(cw, //
                                  ACC_PUBLIC, //
                                  methodName, //
                                  "(L" + JSONSerializer
                                              + ";Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/reflect/Type;I)V", //
                                  null, //
                                  new String[] { "java/io/IOException" } //
            );

            mw.visitVarInsn(ALOAD, Context.serializer); // serializer
            mw.visitFieldInsn(GETFIELD, JSONSerializer, "out", desc(SerializeWriter.class));
            mw.visitVarInsn(ASTORE, context.var("out"));

            if ((!nativeSorted) // 
                    && !context.writeDirect) {
                
                if (jsonType == null || jsonType.alphabetic()) {
                    Label _else = new Label();

                    mw.visitVarInsn(ALOAD, context.var("out"));
                    mw.visitMethodInsn(INVOKEVIRTUAL, SerializeWriter, "isSortField", "()Z");

                    mw.visitJumpInsn(IFNE, _else);
                    mw.visitVarInsn(ALOAD, 0);
                    mw.visitVarInsn(ALOAD, 1);
                    mw.visitVarInsn(ALOAD, 2);
                    mw.visitVarInsn(ALOAD, 3);
                    mw.visitVarInsn(ALOAD, 4);
                    mw.visitVarInsn(ILOAD, 5);
                    mw.visitMethodInsn(INVOKEVIRTUAL, classNameType,
                                       "writeUnsorted", "(L" + JSONSerializer
                                                        + ";Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/reflect/Type;I)V");
                    mw.visitInsn(RETURN);

                    mw.visitLabel(_else);
                }
            }

            // isWriteDoubleQuoteDirect
            if (context.writeDirect && !nonContext) {
                Label _direct = new Label();
                Label _directElse = new Label();

                mw.visitVarInsn(ALOAD, Context.serializer);
                mw.visitVarInsn(ALOAD, 0);
                mw.visitMethodInsn(INVOKEVIRTUAL, JSONSerializer, "writeDirect", "(" + JavaBeanSerializer_desc + ")Z");
                mw.visitJumpInsn(IFNE, _directElse);
                
                mw.visitVarInsn(ALOAD, 0);
                mw.visitVarInsn(ALOAD, 1);
                mw.visitVarInsn(ALOAD, 2);
                mw.visitVarInsn(ALOAD, 3);
                mw.visitVarInsn(ALOAD, 4);
                mw.visitVarInsn(ILOAD, 5);
                mw.visitMethodInsn(INVOKEVIRTUAL, classNameType,
                                   "writeNormal", "(L" + JSONSerializer
                                             + ";Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/reflect/Type;I)V");
                mw.visitInsn(RETURN);
                
                mw.visitLabel(_directElse);
                mw.visitVarInsn(ALOAD, context.var("out"));
                mw.visitLdcInsn(SerializerFeature.DisableCircularReferenceDetect.mask);
                mw.visitMethodInsn(INVOKEVIRTUAL, SerializeWriter, "isEnabled", "(I)Z");
                mw.visitJumpInsn(IFEQ, _direct);
                
                mw.visitVarInsn(ALOAD, 0);
                mw.visitVarInsn(ALOAD, 1);
                mw.visitVarInsn(ALOAD, 2);
                mw.visitVarInsn(ALOAD, 3);
                mw.visitVarInsn(ALOAD, 4);
                mw.visitVarInsn(ILOAD, 5);
                mw.visitMethodInsn(INVOKEVIRTUAL, classNameType,
                                   "writeDirectNonContext", "(L" + JSONSerializer
                                             + ";Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/reflect/Type;I)V");
                mw.visitInsn(RETURN);

                mw.visitLabel(_direct);
            }

            mw.visitVarInsn(ALOAD, Context.obj); // obj
            mw.visitTypeInsn(CHECKCAST, type(clazz)); // serializer
            mw.visitVarInsn(ASTORE, context.var("entity")); // obj
            generateWriteMethod(clazz, mw, getters, context);
            mw.visitInsn(RETURN);
            mw.visitMaxs(7, context.variantIndex + 2);
            mw.visitEnd();
        }

        if (!nativeSorted) {
            // sortField support
            Context context = new Context(getters, jsonType, classNameType, beanSerializeFeatures, false, DisableCircularReferenceDetect);

            mw = new MethodWriter(cw, ACC_PUBLIC, "writeUnsorted",
                                  "(L" + JSONSerializer + ";Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/reflect/Type;I)V",
                                  null, new String[] { "java/io/IOException" });

            mw.visitVarInsn(ALOAD, Context.serializer); // serializer
            mw.visitFieldInsn(GETFIELD, JSONSerializer, "out", desc(SerializeWriter.class));
            mw.visitVarInsn(ASTORE, context.var("out"));

            mw.visitVarInsn(ALOAD, Context.obj); // obj
            mw.visitTypeInsn(CHECKCAST, type(clazz)); // serializer
            mw.visitVarInsn(ASTORE, context.var("entity")); // obj

            generateWriteMethod(clazz, mw, unsortedGetters, context);

            mw.visitInsn(RETURN);
            mw.visitMaxs(7, context.variantIndex + 2);
            mw.visitEnd();
        }

        
        // 0 writeAsArray
        // 1 writeAsArrayNormal
        // 2 writeAsArrayNonContext
        for (int i = 0; i < 3; ++i) {
            String methodName;
            boolean nonContext = DisableCircularReferenceDetect;
            boolean writeDirect = false;
            if (i == 0) {
                methodName = "writeAsArray";
                writeDirect = true;
            } else if (i == 1) {
                methodName = "writeAsArrayNormal";
            } else {
                writeDirect = true;
                nonContext = true;
                methodName = "writeAsArrayNonContext";
            }
            
            Context context = new Context(getters, jsonType, classNameType, beanSerializeFeatures, writeDirect, nonContext);

            mw = new MethodWriter(cw, ACC_PUBLIC, methodName,
                                  "(L" + JSONSerializer + ";Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/reflect/Type;)V",
                                  null, new String[] { "java/io/IOException" });

            mw.visitVarInsn(ALOAD, Context.serializer); // serializer
            mw.visitFieldInsn(GETFIELD, JSONSerializer, "out", desc(SerializeWriter.class));
            mw.visitVarInsn(ASTORE, context.var("out"));

            mw.visitVarInsn(ALOAD, Context.obj); // obj
            mw.visitTypeInsn(CHECKCAST, type(clazz)); // serializer
            mw.visitVarInsn(ASTORE, context.var("entity")); // obj
            generateWriteAsArray(clazz, mw, getters, context);
            mw.visitInsn(RETURN);
            mw.visitMaxs(7, context.variantIndex + 2);
            mw.visitEnd();
        }

        byte[] code = cw.toByteArray();

        // if(JSON.DUMP_CLASS != null){
        // FileOutputStream fos=null;
        // try {
        // fos=new FileOutputStream(JSON.DUMP_CLASS+ File.separator
        // + classNameType + ".class");
        // fos.write(code);
        // }catch (Exception ex){
        // System.err.println("FASTJSON dump class:"+classNameType+"失败:"+ex.getMessage());
        // }finally {
        // if(fos!=null){
        // fos.close();
        // }
        // }
        // }

        Class<?> exampleClass = classLoader.defineClassPublic(classNameFull, code, 0, code.length);
        Object instance = exampleClass.newInstance();

        return (ObjectSerializer) instance;
    }

    private void generateWriteAsArray(Class<?> clazz, MethodVisitor mw, List<FieldInfo> getters,
                                      Context context) throws Exception {

        mw.visitVarInsn(ALOAD, context.var("out"));
        mw.visitVarInsn(BIPUSH, '[');
        mw.visitMethodInsn(INVOKEVIRTUAL, SerializeWriter, "write", "(I)V");

        int size = getters.size();

        if (size == 0) {
            mw.visitVarInsn(ALOAD, context.var("out"));
            mw.visitVarInsn(BIPUSH, ']');
            mw.visitMethodInsn(INVOKEVIRTUAL, SerializeWriter, "write", "(I)V");
            return;
        }

        for (int i = 0; i < size; ++i) {
            final char seperator = (i == size - 1) ? ']' : ',';

            FieldInfo fieldInfo = getters.get(i);
            Class<?> fieldClass = fieldInfo.fieldClass;

            mw.visitLdcInsn(fieldInfo.name);
            mw.visitVarInsn(ASTORE, Context.fieldName);

            if (fieldClass == byte.class //
                || fieldClass == short.class //
                || fieldClass == int.class) {

                mw.visitVarInsn(ALOAD, context.var("out"));
                mw.visitInsn(DUP);
                _get(mw, context, fieldInfo);
                mw.visitMethodInsn(INVOKEVIRTUAL, SerializeWriter, "writeInt", "(I)V");
                mw.visitVarInsn(BIPUSH, seperator);
                mw.visitMethodInsn(INVOKEVIRTUAL, SerializeWriter, "write", "(I)V");
            } else if (fieldClass == long.class) {
                mw.visitVarInsn(ALOAD, context.var("out"));
                mw.visitInsn(DUP);
                _get(mw, context, fieldInfo);
                mw.visitMethodInsn(INVOKEVIRTUAL, SerializeWriter, "writeLong", "(J)V");
                mw.visitVarInsn(BIPUSH, seperator);
                mw.visitMethodInsn(INVOKEVIRTUAL, SerializeWriter, "write", "(I)V");
            } else if (fieldClass == float.class) {
                mw.visitVarInsn(ALOAD, context.var("out"));
                mw.visitInsn(DUP);
                _get(mw, context, fieldInfo);
                mw.visitInsn(ICONST_1);
                mw.visitMethodInsn(INVOKEVIRTUAL, SerializeWriter, "writeFloat", "(FZ)V");
                mw.visitVarInsn(BIPUSH, seperator);
                mw.visitMethodInsn(INVOKEVIRTUAL, SerializeWriter, "write", "(I)V");
            } else if (fieldClass == double.class) {
                mw.visitVarInsn(ALOAD, context.var("out"));
                mw.visitInsn(DUP);
                _get(mw, context, fieldInfo);
                mw.visitInsn(ICONST_1);
                mw.visitMethodInsn(INVOKEVIRTUAL, SerializeWriter, "writeDouble", "(DZ)V");
                mw.visitVarInsn(BIPUSH, seperator);
                mw.visitMethodInsn(INVOKEVIRTUAL, SerializeWriter, "write", "(I)V");
            } else if (fieldClass == boolean.class) {
                mw.visitVarInsn(ALOAD, context.var("out"));
                mw.visitInsn(DUP);
                _get(mw, context, fieldInfo);
                mw.visitMethodInsn(INVOKEVIRTUAL, SerializeWriter, "write", "(Z)V");
                mw.visitVarInsn(BIPUSH, seperator);
                mw.visitMethodInsn(INVOKEVIRTUAL, SerializeWriter, "write", "(I)V");
            } else if (fieldClass == char.class) {
                mw.visitVarInsn(ALOAD, context.var("out"));
                _get(mw, context, fieldInfo); // Character.toString(value)
                mw.visitMethodInsn(INVOKESTATIC, "java/lang/Character", "toString", "(C)Ljava/lang/String;");
                mw.visitVarInsn(BIPUSH, seperator);
                mw.visitMethodInsn(INVOKEVIRTUAL, SerializeWriter, "writeString", "(Ljava/lang/String;C)V");

            } else if (fieldClass == String.class) {
                mw.visitVarInsn(ALOAD, context.var("out"));
                _get(mw, context, fieldInfo);
                mw.visitVarInsn(BIPUSH, seperator);
                mw.visitMethodInsn(INVOKEVIRTUAL, SerializeWriter, "writeString", "(Ljava/lang/String;C)V");
            } else if (fieldClass.isEnum()) {
                mw.visitVarInsn(ALOAD, context.var("out"));
                mw.visitInsn(DUP);
                _get(mw, context, fieldInfo);
                mw.visitMethodInsn(INVOKEVIRTUAL, SerializeWriter, "writeEnum", "(Ljava/lang/Enum;)V");
                mw.visitVarInsn(BIPUSH, seperator);
                mw.visitMethodInsn(INVOKEVIRTUAL, SerializeWriter, "write", "(I)V");
            } else if (List.class.isAssignableFrom(fieldClass)) {
                Type fieldType = fieldInfo.fieldType;

                Type elementType;
                if (fieldType instanceof Class) {
                    elementType = Object.class;
                } else {
                    elementType = ((ParameterizedType) fieldType).getActualTypeArguments()[0];
                }

                Class<?> elementClass = null;
                if (elementType instanceof Class<?>) {
                    elementClass = (Class<?>) elementType;
                    
                    if (elementClass == Object.class) {
                        elementClass = null;
                    }
                }
                
                _get(mw, context, fieldInfo);
                mw.visitTypeInsn(CHECKCAST, "java/util/List"); // cast
                mw.visitVarInsn(ASTORE, context.var("list"));
                
                Label nullEnd_ = new Label(), nullElse_ = new Label();
                
                mw.visitVarInsn(ALOAD, context.var("list"));
                mw.visitJumpInsn(IFNONNULL, nullElse_);

                mw.visitVarInsn(ALOAD, context.var("out"));
                mw.visitMethodInsn(INVOKEVIRTUAL, SerializeWriter, "writeNull", "()V");
                mw.visitJumpInsn(GOTO, nullEnd_);
                
                mw.visitLabel(nullElse_);
                
                mw.visitVarInsn(ALOAD, context.var("list"));
                mw.visitMethodInsn(INVOKEINTERFACE, "java/util/List", "size", "()I");
                mw.visitVarInsn(ISTORE, context.var("size"));
                
                mw.visitVarInsn(ALOAD, context.var("out"));
                mw.visitVarInsn(BIPUSH, '[');
                mw.visitMethodInsn(INVOKEVIRTUAL, SerializeWriter, "write", "(I)V");

                Label for_ = new Label(), forFirst_ = new Label(), forEnd_ = new Label();

                mw.visitInsn(ICONST_0);
                mw.visitVarInsn(ISTORE, context.var("i"));

                // for (; i < list.size() -1; ++i) {
                mw.visitLabel(for_);
                mw.visitVarInsn(ILOAD, context.var("i"));
                mw.visitVarInsn(ILOAD, context.var("size"));
                mw.visitJumpInsn(IF_ICMPGE, forEnd_); // i < list.size - 1

                mw.visitVarInsn(ILOAD, context.var("i"));
                mw.visitJumpInsn(IFEQ, forFirst_); // i < list.size - 1

                mw.visitVarInsn(ALOAD, context.var("out"));
                mw.visitVarInsn(BIPUSH, ',');
                mw.visitMethodInsn(INVOKEVIRTUAL, SerializeWriter, "write", "(I)V");

                mw.visitLabel(forFirst_);
                
                mw.visitVarInsn(ALOAD, context.var("list"));
                mw.visitVarInsn(ILOAD, context.var("i"));
                mw.visitMethodInsn(INVOKEINTERFACE, "java/util/List", "get", "(I)Ljava/lang/Object;");
                mw.visitVarInsn(ASTORE, context.var("list_item"));
                
                Label forItemNullEnd_ = new Label(), forItemNullElse_ = new Label();
                
                mw.visitVarInsn(ALOAD, context.var("list_item"));
                mw.visitJumpInsn(IFNONNULL, forItemNullElse_);

                mw.visitVarInsn(ALOAD, context.var("out"));
                mw.visitMethodInsn(INVOKEVIRTUAL, SerializeWriter, "writeNull", "()V");
                mw.visitJumpInsn(GOTO, forItemNullEnd_);
                
                mw.visitLabel(forItemNullElse_);
                
                Label forItemClassIfEnd_ = new Label(), forItemClassIfElse_ = new Label();
                if (elementClass != null) {
                    mw.visitVarInsn(ALOAD, context.var("list_item"));
                    mw.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Object", "getClass", "()Ljava/lang/Class;");
                    mw.visitLdcInsn(com.alibaba.fastjson.asm.Type.getType(desc(elementClass)));
                    mw.visitJumpInsn(IF_ACMPNE, forItemClassIfElse_);
                    
                    _getListFieldItemSer(context, mw, fieldInfo, elementClass);
                    mw.visitVarInsn(ALOAD, Context.serializer);
                    mw.visitVarInsn(ALOAD, context.var("list_item")); // object
                    if (context.nonContext) { // fieldName
                        mw.visitInsn(ACONST_NULL);
                    } else {
                        mw.visitVarInsn(ILOAD, context.var("i"));
                        mw.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;");
                    }
                    mw.visitLdcInsn(com.alibaba.fastjson.asm.Type.getType(desc(elementClass))); // fieldType
                    mw.visitLdcInsn(fieldInfo.serialzeFeatures); // features
                    mw.visitMethodInsn(INVOKEINTERFACE, type(ObjectSerializer.class), "write", // 
                                       "(L" + JSONSerializer + ";Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/reflect/Type;I)V");
                    mw.visitJumpInsn(GOTO, forItemClassIfEnd_);
                }

                mw.visitLabel(forItemClassIfElse_);
                mw.visitVarInsn(ALOAD, Context.serializer);
                mw.visitVarInsn(ALOAD, context.var("list_item"));
                if (context.nonContext) {
                    mw.visitInsn(ACONST_NULL);
                } else {
                    mw.visitVarInsn(ILOAD, context.var("i"));
                    mw.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;");
                }
                if (elementClass != null && Modifier.isPublic(elementClass.getModifiers())) {
                    mw.visitLdcInsn(com.alibaba.fastjson.asm.Type.getType(desc((Class<?>) elementType)));
                    mw.visitLdcInsn(fieldInfo.serialzeFeatures);
                    mw.visitMethodInsn(INVOKEVIRTUAL, JSONSerializer, "writeWithFieldName",
                                       "(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/reflect/Type;I)V");
                } else {
                    mw.visitMethodInsn(INVOKEVIRTUAL, JSONSerializer, "writeWithFieldName",
                                       "(Ljava/lang/Object;Ljava/lang/Object;)V");
                }
                mw.visitLabel(forItemClassIfEnd_);
                mw.visitLabel(forItemNullEnd_);

                mw.visitIincInsn(context.var("i"), 1);
                mw.visitJumpInsn(GOTO, for_);

                mw.visitLabel(forEnd_);

                mw.visitVarInsn(ALOAD, context.var("out"));
                mw.visitVarInsn(BIPUSH, ']');
                mw.visitMethodInsn(INVOKEVIRTUAL, SerializeWriter, "write", "(I)V");
                
                mw.visitLabel(nullEnd_);
                
                mw.visitVarInsn(ALOAD, context.var("out"));
                mw.visitVarInsn(BIPUSH, seperator);
                mw.visitMethodInsn(INVOKEVIRTUAL, SerializeWriter, "write", "(I)V");
            } else {
                Label notNullEnd_ = new Label(), notNullElse_ = new Label();
                
                _get(mw, context, fieldInfo);
                mw.visitInsn(DUP);
                mw.visitVarInsn(ASTORE, context.var("field_" + fieldInfo.fieldClass.getName()));
                mw.visitJumpInsn(IFNONNULL, notNullElse_);

                mw.visitVarInsn(ALOAD, context.var("out"));
                mw.visitMethodInsn(INVOKEVIRTUAL, SerializeWriter, "writeNull", "()V");
                mw.visitJumpInsn(GOTO, notNullEnd_);
                
                mw.visitLabel(notNullElse_);
                
                Label classIfEnd_ = new Label(), classIfElse_ = new Label();
                mw.visitVarInsn(ALOAD, context.var("field_" + fieldInfo.fieldClass.getName()));
                mw.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Object", "getClass", "()Ljava/lang/Class;");
                mw.visitLdcInsn(com.alibaba.fastjson.asm.Type.getType(desc(fieldClass)));
                mw.visitJumpInsn(IF_ACMPNE, classIfElse_);
                
                _getFieldSer(context, mw, fieldInfo);
                mw.visitVarInsn(ALOAD, Context.serializer);
                mw.visitVarInsn(ALOAD, context.var("field_" + fieldInfo.fieldClass.getName()));
                mw.visitVarInsn(ALOAD, Context.fieldName);
                mw.visitLdcInsn(com.alibaba.fastjson.asm.Type.getType(desc(fieldClass))); // fieldType
                mw.visitLdcInsn(fieldInfo.serialzeFeatures); // features
                mw.visitMethodInsn(INVOKEINTERFACE, type(ObjectSerializer.class), "write", // 
                                   "(L" + JSONSerializer + ";Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/reflect/Type;I)V");
                mw.visitJumpInsn(GOTO, classIfEnd_);
                
                mw.visitLabel(classIfElse_);
                String format = fieldInfo.getFormat();

                mw.visitVarInsn(ALOAD, Context.serializer);
                mw.visitVarInsn(ALOAD, context.var("field_" + fieldInfo.fieldClass.getName()));
                if (format != null) {
                    mw.visitLdcInsn(format);
                    mw.visitMethodInsn(INVOKEVIRTUAL, JSONSerializer, "writeWithFormat",
                                       "(Ljava/lang/Object;Ljava/lang/String;)V");
                } else {
                    mw.visitVarInsn(ALOAD, Context.fieldName);
                    if (fieldInfo.fieldType instanceof Class<?> //
                        && ((Class<?>) fieldInfo.fieldType).isPrimitive()) {
                        mw.visitMethodInsn(INVOKEVIRTUAL, JSONSerializer, "writeWithFieldName",
                                           "(Ljava/lang/Object;Ljava/lang/Object;)V");
                    } else {
                        mw.visitVarInsn(ALOAD, 0);
                        mw.visitFieldInsn(GETFIELD, context.className, fieldInfo.name + "_asm_fieldType",
                                          "Ljava/lang/reflect/Type;");
                        mw.visitLdcInsn(fieldInfo.serialzeFeatures);

                        mw.visitMethodInsn(INVOKEVIRTUAL, JSONSerializer, "writeWithFieldName",
                                           "(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/reflect/Type;I)V");
                    }
                }
                mw.visitLabel(classIfEnd_);
                mw.visitLabel(notNullEnd_);

                mw.visitVarInsn(ALOAD, context.var("out"));
                mw.visitVarInsn(BIPUSH, seperator);
                mw.visitMethodInsn(INVOKEVIRTUAL, SerializeWriter, "write", "(I)V");
            }
        }
    }

    private void generateWriteMethod(Class<?> clazz, MethodVisitor mw, List<FieldInfo> getters,
                                     Context context) throws Exception {
        // if (serializer.containsReference(object)) {
        Label end = new Label();

        int size = getters.size();

        if (!context.writeDirect) {
            // pretty format not byte code optimized
            Label endSupper_ = new Label();
            Label supper_ = new Label();
            mw.visitVarInsn(ALOAD, context.var("out"));
            mw.visitLdcInsn(SerializerFeature.PrettyFormat.mask);
            mw.visitMethodInsn(INVOKEVIRTUAL, SerializeWriter, "isEnabled", "(I)Z");
            mw.visitJumpInsn(IFNE, supper_);

            boolean hasMethod = false;
            for (FieldInfo getter : getters) {
                if (getter.method != null) {
                    hasMethod = true;
                }
            }

            if (hasMethod) {
                mw.visitVarInsn(ALOAD, context.var("out"));
                mw.visitLdcInsn(SerializerFeature.IgnoreErrorGetter.mask);
                mw.visitMethodInsn(INVOKEVIRTUAL, SerializeWriter, "isEnabled", "(I)Z");
                mw.visitJumpInsn(IFEQ, endSupper_);
            } else {
                mw.visitJumpInsn(GOTO, endSupper_);
            }

            mw.visitLabel(supper_);
            mw.visitVarInsn(ALOAD, 0);
            mw.visitVarInsn(ALOAD, 1);
            mw.visitVarInsn(ALOAD, 2);
            mw.visitVarInsn(ALOAD, 3);
            mw.visitVarInsn(ALOAD, 4);
            mw.visitVarInsn(ILOAD, 5);
            mw.visitMethodInsn(INVOKESPECIAL, JavaBeanSerializer,
                               "write", "(L" + JSONSerializer
                                        + ";Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/reflect/Type;I)V");
            mw.visitInsn(RETURN);

            mw.visitLabel(endSupper_);
        }

        {
            if (!context.nonContext) {
                Label endRef_ = new Label();

                // /////
                mw.visitVarInsn(ALOAD, 0);
                mw.visitVarInsn(ALOAD, Context.serializer);
                mw.visitVarInsn(ALOAD, Context.obj);
                mw.visitVarInsn(ILOAD, Context.features);
                mw.visitMethodInsn(INVOKEVIRTUAL, JavaBeanSerializer, "writeReference",
                                   "(L" + JSONSerializer + ";Ljava/lang/Object;I)Z");

                mw.visitJumpInsn(IFEQ, endRef_);

                mw.visitInsn(RETURN);

                mw.visitLabel(endRef_);
            }
        }

        final String writeAsArrayMethodName;
        
        if (context.writeDirect) {
            if (context.nonContext) {
                writeAsArrayMethodName = "writeAsArrayNonContext";
            } else {
                writeAsArrayMethodName = "writeAsArray";
            }
        } else {
            writeAsArrayMethodName = "writeAsArrayNormal";
        }
        
        if ((context.beanFeatures & SerializerFeature.BeanToArray.mask) == 0) {
            Label endWriteAsArray_ = new Label();

            mw.visitVarInsn(ALOAD, context.var("out"));
            mw.visitLdcInsn(SerializerFeature.BeanToArray.mask);
            mw.visitMethodInsn(INVOKEVIRTUAL, SerializeWriter, "isEnabled", "(I)Z");
            mw.visitJumpInsn(IFEQ, endWriteAsArray_);

            // /////
            mw.visitVarInsn(ALOAD, 0); // this
            mw.visitVarInsn(ALOAD, 1); // serializer
            mw.visitVarInsn(ALOAD, 2); // obj
            mw.visitVarInsn(ALOAD, 3); // fieldObj
            mw.visitVarInsn(ALOAD, 4); // fieldType
            mw.visitMethodInsn(INVOKEVIRTUAL, //
                               context.className, //
                               writeAsArrayMethodName, //
                               "(L" + JSONSerializer + ";Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/reflect/Type;)V");

            mw.visitInsn(RETURN);

            mw.visitLabel(endWriteAsArray_);
        } else {
            mw.visitVarInsn(ALOAD, 0); // this
            mw.visitVarInsn(ALOAD, 1); // serializer
            mw.visitVarInsn(ALOAD, 2); // obj
            mw.visitVarInsn(ALOAD, 3); // fieldObj
            mw.visitVarInsn(ALOAD, 4); // fieldType
            mw.visitMethodInsn(INVOKEVIRTUAL, //
                               context.className, //
                               writeAsArrayMethodName, //
                               "(L" + JSONSerializer + ";Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/reflect/Type;)V");
        }

        if (!context.nonContext) {
            mw.visitVarInsn(ALOAD, Context.serializer);
            mw.visitMethodInsn(INVOKEVIRTUAL, JSONSerializer, "getContext", "()" + SerialContext_desc);
            mw.visitVarInsn(ASTORE, context.var("parent"));

            mw.visitVarInsn(ALOAD, Context.serializer);
            mw.visitVarInsn(ALOAD, context.var("parent"));
            mw.visitVarInsn(ALOAD, Context.obj);
            mw.visitVarInsn(ALOAD, Context.paramFieldName);
            mw.visitLdcInsn(context.beanSerializeFeatures);
            mw.visitMethodInsn(INVOKEVIRTUAL, JSONSerializer, "setContext",
                               "(" + SerialContext_desc + "Ljava/lang/Object;Ljava/lang/Object;I)V");
        }

        // SEPERATO
        if (!context.writeDirect) {
            Label end_ = new Label();
            Label else_ = new Label();
            Label writeClass_ = new Label();

            mw.visitVarInsn(ALOAD, Context.serializer);
            mw.visitVarInsn(ALOAD, Context.paramFieldType);
            mw.visitVarInsn(ALOAD, Context.obj);
            mw.visitMethodInsn(INVOKEVIRTUAL, JSONSerializer, "isWriteClassName",
                               "(Ljava/lang/reflect/Type;Ljava/lang/Object;)Z");
            mw.visitJumpInsn(IFEQ, else_);

            // IFNULL
            mw.visitVarInsn(ALOAD, Context.paramFieldType);
            mw.visitVarInsn(ALOAD, Context.obj);
            mw.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Object", "getClass", "()Ljava/lang/Class;");
            mw.visitJumpInsn(IF_ACMPEQ, else_);

            mw.visitLabel(writeClass_);
            mw.visitVarInsn(ALOAD, context.var("out"));

            String typeName = null;
            if (context.jsonType != null) {
                typeName = context.jsonType.typeName();
            }
            if (typeName == null || typeName.length() == 0) {
                typeName = clazz.getName();
            }
            mw.visitLdcInsn("{\"" + JSON.DEFAULT_TYPE_KEY + "\":\"" + typeName + "\"");
            mw.visitMethodInsn(INVOKEVIRTUAL, SerializeWriter, "write", "(Ljava/lang/String;)V");
            mw.visitVarInsn(BIPUSH, ',');
            mw.visitJumpInsn(GOTO, end_);

            mw.visitLabel(else_);
            mw.visitVarInsn(BIPUSH, '{');

            mw.visitLabel(end_);
        } else {
            mw.visitVarInsn(BIPUSH, '{');
        }

        mw.visitVarInsn(ISTORE, context.var("seperator"));

        if (!context.writeDirect) {
            _before(mw, context);
        }

        mw.visitVarInsn(ALOAD, context.var("out"));
        mw.visitMethodInsn(INVOKEVIRTUAL, SerializeWriter, "isNotWriteDefaultValue", "()Z");
        mw.visitVarInsn(ISTORE, context.var("notWriteDefaultValue"));

        if (!context.writeDirect) {
            mw.visitVarInsn(ALOAD, Context.serializer);
            mw.visitVarInsn(ALOAD, 0);
            mw.visitMethodInsn(INVOKEVIRTUAL, JSONSerializer, "checkValue",
                               "(" + desc(SerializeFilterable.class) + ")Z");
            mw.visitVarInsn(ISTORE, context.var("checkValue"));

            mw.visitVarInsn(ALOAD, Context.serializer);
            mw.visitMethodInsn(INVOKEVIRTUAL, JSONSerializer, "hasNameFilters", "()Z");
            mw.visitVarInsn(ISTORE, context.var("hasNameFilters"));
        }

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

        if (!context.writeDirect) {
            _after(mw, context);
        }

        Label _else = new Label();
        Label _end_if = new Label();

        mw.visitVarInsn(ILOAD, context.var("seperator"));
        mw.visitIntInsn(BIPUSH, '{');
        mw.visitJumpInsn(IF_ICMPNE, _else);

        mw.visitVarInsn(ALOAD, context.var("out"));
        mw.visitVarInsn(BIPUSH, '{');
        mw.visitMethodInsn(INVOKEVIRTUAL, SerializeWriter, "write", "(I)V");

        mw.visitLabel(_else);

        mw.visitVarInsn(ALOAD, context.var("out"));
        mw.visitVarInsn(BIPUSH, '}');
        mw.visitMethodInsn(INVOKEVIRTUAL, SerializeWriter, "write", "(I)V");

        mw.visitLabel(_end_if);
        mw.visitLabel(end);

        if (!context.nonContext) {
            mw.visitVarInsn(ALOAD, Context.serializer);
            mw.visitVarInsn(ALOAD, context.var("parent"));
            mw.visitMethodInsn(INVOKEVIRTUAL, JSONSerializer, "setContext", "(" + SerialContext_desc + ")V");
        }

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
            mw.visitMethodInsn(INVOKEVIRTUAL, SerializeWriter, "writeFieldValue",
                               "(CLjava/lang/String;Ljava/lang/String;)V");
        } else {
            Label useNameEnd_ = new Label(), useNameElse_ = new Label();;
            mw.visitVarInsn(ALOAD, context.var("out"));
            mw.visitLdcInsn(SerializerFeature.WriteEnumUsingName.mask);
            mw.visitMethodInsn(INVOKEVIRTUAL, SerializeWriter, "isEnabled", "(I)Z");
            mw.visitJumpInsn(IFEQ, useNameElse_);
            
            mw.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Enum", "name", "()Ljava/lang/String;");
            mw.visitMethodInsn(INVOKEVIRTUAL, SerializeWriter, "writeFieldValueStringWithDoubleQuote",
                               "(CLjava/lang/String;Ljava/lang/String;)V");
            mw.visitJumpInsn(GOTO, useNameEnd_);
            
            mw.visitLabel(useNameElse_);
            mw.visitMethodInsn(INVOKEVIRTUAL, SerializeWriter, "writeFieldValue",
                    "(CLjava/lang/String;Ljava/lang/Enum;)V");
            
            mw.visitLabel(useNameEnd_);
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
        mw.visitMethodInsn(INVOKEVIRTUAL, SerializeWriter, "writeFieldValue", "(CLjava/lang/String;J)V");

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
        mw.visitMethodInsn(INVOKEVIRTUAL, SerializeWriter, "writeFieldValue", "(CLjava/lang/String;F)V");

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
        mw.visitMethodInsn(INVOKEVIRTUAL, SerializeWriter, "writeFieldValue", "(CLjava/lang/String;D)V");

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

        mw.visitMethodInsn(INVOKEVIRTUAL, SerializeWriter, "writeFieldValue", "(CLjava/lang/String;C)V");

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

        mw.visitMethodInsn(INVOKEVIRTUAL, SerializeWriter, "writeFieldValue", "(CLjava/lang/String;Z)V");

        _seperator(mw, context);

        mw.visitLabel(_end);
    }

    private void _get(MethodVisitor mw, Context context, FieldInfo property) {
        Method method = property.method;
        if (method != null) {
            mw.visitVarInsn(ALOAD, context.var("entity"));
            mw.visitMethodInsn(INVOKEVIRTUAL, type(method.getDeclaringClass()), method.getName(), desc(method));
        } else {
            mw.visitVarInsn(ALOAD, context.var("entity"));
            mw.visitFieldInsn(GETFIELD, type(property.declaringClass), property.field.getName(),
                              desc(property.fieldClass));
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

        mw.visitMethodInsn(INVOKEVIRTUAL, SerializeWriter, "writeFieldValue", "(CLjava/lang/String;I)V");

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

        mw.visitMethodInsn(INVOKEVIRTUAL, SerializeWriter, "writeFieldValue", "(CLjava/lang/String;I)V");

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

        mw.visitMethodInsn(INVOKEVIRTUAL, SerializeWriter, "writeFieldValue", "(CLjava/lang/String;I)V");

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
        mw.visitMethodInsn(INVOKEVIRTUAL, SerializeWriter, "writeFieldValue",
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

        if (context.writeDirect) {
            mw.visitVarInsn(ALOAD, context.var("out"));
            mw.visitVarInsn(ILOAD, context.var("seperator"));
            mw.visitVarInsn(ALOAD, Context.fieldName);
            mw.visitVarInsn(ALOAD, context.var("string"));
            mw.visitMethodInsn(INVOKEVIRTUAL, SerializeWriter, "writeFieldValueStringWithDoubleQuoteCheck",
                               "(CLjava/lang/String;Ljava/lang/String;)V");
        } else {
            mw.visitVarInsn(ALOAD, context.var("out"));
            mw.visitVarInsn(ILOAD, context.var("seperator"));
            mw.visitVarInsn(ALOAD, Context.fieldName);
            mw.visitVarInsn(ALOAD, context.var("string"));
            mw.visitMethodInsn(INVOKEVIRTUAL, SerializeWriter, "writeFieldValue",
                               "(CLjava/lang/String;Ljava/lang/String;)V");
        }
        _seperator(mw, context);

        mw.visitLabel(_end_if);

        mw.visitLabel(_end);
    }

    private void _list(Class<?> clazz, MethodVisitor mw, FieldInfo fieldInfo, Context context) {
        Type propertyType = fieldInfo.fieldType;

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

        Label _end = new Label(), _else = new Label(), _end_if = new Label();

        _nameApply(mw, fieldInfo, context, _end);
        _get(mw, context, fieldInfo);
        mw.visitTypeInsn(CHECKCAST, "java/util/List"); // cast
        mw.visitVarInsn(ASTORE, context.var("list"));

        _filters(mw, fieldInfo, context, _end);

        mw.visitVarInsn(ALOAD, context.var("list"));
        mw.visitJumpInsn(IFNONNULL, _else);
        _if_write_null(mw, fieldInfo, context);
        mw.visitJumpInsn(GOTO, _end_if);

        mw.visitLabel(_else); // else {

        mw.visitVarInsn(ALOAD, context.var("out"));
        mw.visitVarInsn(ILOAD, context.var("seperator"));
        mw.visitMethodInsn(INVOKEVIRTUAL, SerializeWriter, "write", "(I)V");

        if (context.writeDirect) {
            mw.visitVarInsn(ALOAD, context.var("out"));
            mw.visitVarInsn(ALOAD, Context.fieldName);
            mw.visitMethodInsn(INVOKEVIRTUAL, SerializeWriter, "writeFieldNameDirect", "(Ljava/lang/String;)V");
        } else {
            mw.visitVarInsn(ALOAD, context.var("out"));
            mw.visitVarInsn(ALOAD, Context.fieldName);
            mw.visitInsn(ICONST_0);
            mw.visitMethodInsn(INVOKEVIRTUAL, SerializeWriter, "writeFieldName", "(Ljava/lang/String;Z)V");
        }

        //
        mw.visitVarInsn(ALOAD, context.var("list"));
        mw.visitMethodInsn(INVOKEINTERFACE, "java/util/List", "size", "()I");
        mw.visitVarInsn(ISTORE, context.var("size"));

        Label _else_3 = new Label();
        Label _end_if_3 = new Label();

        mw.visitVarInsn(ILOAD, context.var("size"));
        mw.visitInsn(ICONST_0);
        mw.visitJumpInsn(IF_ICMPNE, _else_3);

        mw.visitVarInsn(ALOAD, context.var("out"));
        mw.visitLdcInsn("[]");
        mw.visitMethodInsn(INVOKEVIRTUAL, SerializeWriter, "write", "(Ljava/lang/String;)V");

        mw.visitJumpInsn(GOTO, _end_if_3);

        mw.visitLabel(_else_3);

        if (!context.nonContext) {
            mw.visitVarInsn(ALOAD, Context.serializer);
            mw.visitVarInsn(ALOAD, context.var("list"));
            mw.visitVarInsn(ALOAD, Context.fieldName);
            mw.visitMethodInsn(INVOKEVIRTUAL, JSONSerializer, "setContext", "(Ljava/lang/Object;Ljava/lang/Object;)V");
        }

        if (elementType == String.class // 
                && context.writeDirect) {
            mw.visitVarInsn(ALOAD, context.var("out"));
            mw.visitVarInsn(ALOAD, context.var("list"));
            mw.visitMethodInsn(INVOKEVIRTUAL, SerializeWriter, "write", "(Ljava/util/List;)V");
        } else {
            mw.visitVarInsn(ALOAD, context.var("out"));
            mw.visitVarInsn(BIPUSH, '[');
            mw.visitMethodInsn(INVOKEVIRTUAL, SerializeWriter, "write", "(I)V");

            Label for_ = new Label(), forFirst_ = new Label(), forEnd_ = new Label();

            mw.visitInsn(ICONST_0);
            mw.visitVarInsn(ISTORE, context.var("i"));

            // for (; i < list.size() -1; ++i) {
            mw.visitLabel(for_);
            mw.visitVarInsn(ILOAD, context.var("i"));
            mw.visitVarInsn(ILOAD, context.var("size"));
            mw.visitJumpInsn(IF_ICMPGE, forEnd_); // i < list.size - 1

            mw.visitVarInsn(ILOAD, context.var("i"));
            mw.visitJumpInsn(IFEQ, forFirst_); // i < list.size - 1

            mw.visitVarInsn(ALOAD, context.var("out"));
            mw.visitVarInsn(BIPUSH, ',');
            mw.visitMethodInsn(INVOKEVIRTUAL, SerializeWriter, "write", "(I)V");

            mw.visitLabel(forFirst_);
            
            mw.visitVarInsn(ALOAD, context.var("list"));
            mw.visitVarInsn(ILOAD, context.var("i"));
            mw.visitMethodInsn(INVOKEINTERFACE, "java/util/List", "get", "(I)Ljava/lang/Object;");
            mw.visitVarInsn(ASTORE, context.var("list_item"));
            
            Label forItemNullEnd_ = new Label(), forItemNullElse_ = new Label();
            
            mw.visitVarInsn(ALOAD, context.var("list_item"));
            mw.visitJumpInsn(IFNONNULL, forItemNullElse_);

            mw.visitVarInsn(ALOAD, context.var("out"));
            mw.visitMethodInsn(INVOKEVIRTUAL, SerializeWriter, "writeNull", "()V");
            mw.visitJumpInsn(GOTO, forItemNullEnd_);
            
            mw.visitLabel(forItemNullElse_);

            Label forItemClassIfEnd_ = new Label(), forItemClassIfElse_ = new Label();
            if (elementClass != null) {
                mw.visitVarInsn(ALOAD, context.var("list_item"));
                mw.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Object", "getClass", "()Ljava/lang/Class;");
                mw.visitLdcInsn(com.alibaba.fastjson.asm.Type.getType(desc(elementClass)));
                mw.visitJumpInsn(IF_ACMPNE, forItemClassIfElse_);
                
                _getListFieldItemSer(context, mw, fieldInfo, elementClass);
                mw.visitVarInsn(ALOAD, Context.serializer);
                mw.visitVarInsn(ALOAD, context.var("list_item")); // object
                if (context.nonContext) { // fieldName
                    mw.visitInsn(ACONST_NULL);
                } else {
                    mw.visitVarInsn(ILOAD, context.var("i"));
                    mw.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;");
                }
                mw.visitLdcInsn(com.alibaba.fastjson.asm.Type.getType(desc(elementClass))); // fieldType
                mw.visitLdcInsn(fieldInfo.serialzeFeatures); // features
                mw.visitMethodInsn(INVOKEINTERFACE, type(ObjectSerializer.class), "write", // 
                                   "(L" + JSONSerializer + ";Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/reflect/Type;I)V");
                mw.visitJumpInsn(GOTO, forItemClassIfEnd_);
            }
            
            mw.visitLabel(forItemClassIfElse_);
            
            mw.visitVarInsn(ALOAD, Context.serializer);
            mw.visitVarInsn(ALOAD, context.var("list_item"));
            if (context.nonContext) {
                mw.visitInsn(ACONST_NULL);
            } else {
                mw.visitVarInsn(ILOAD, context.var("i"));
                mw.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;");
            }
            
            if (elementClass != null && Modifier.isPublic(elementClass.getModifiers())) {
                mw.visitLdcInsn(com.alibaba.fastjson.asm.Type.getType(desc((Class<?>) elementType)));
                mw.visitLdcInsn(fieldInfo.serialzeFeatures);
                mw.visitMethodInsn(INVOKEVIRTUAL, JSONSerializer, "writeWithFieldName",
                                   "(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/reflect/Type;I)V");
            } else {
                mw.visitMethodInsn(INVOKEVIRTUAL, JSONSerializer, "writeWithFieldName",
                                   "(Ljava/lang/Object;Ljava/lang/Object;)V");
            }
            
            mw.visitLabel(forItemClassIfEnd_);
            mw.visitLabel(forItemNullEnd_);

            mw.visitIincInsn(context.var("i"), 1);
            mw.visitJumpInsn(GOTO, for_);

            mw.visitLabel(forEnd_);

            mw.visitVarInsn(ALOAD, context.var("out"));
            mw.visitVarInsn(BIPUSH, ']');
            mw.visitMethodInsn(INVOKEVIRTUAL, SerializeWriter, "write", "(I)V");
        }

        {
            mw.visitVarInsn(ALOAD, Context.serializer);
            mw.visitMethodInsn(INVOKEVIRTUAL, JSONSerializer, "popContext", "()V");
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
                mw.visitLdcInsn(SerializerFeature.SkipTransientField.mask);
                mw.visitMethodInsn(INVOKEVIRTUAL, SerializeWriter, "isEnabled", "(I)Z");
                mw.visitJumpInsn(IFNE, _end);
            }
        }

        _notWriteDefault(mw, property, context, _end);

        if (context.writeDirect) {
            return;
        }

        _apply(mw, property, context);
        mw.visitJumpInsn(IFEQ, _end);

        _processKey(mw, property, context);

        _processValue(mw, property, context, _end);
    }

    private void _nameApply(MethodVisitor mw, FieldInfo property, Context context, Label _end) {
        if (!context.writeDirect) {
            mw.visitVarInsn(ALOAD, Context.serializer);
            mw.visitVarInsn(ALOAD, 0);
            mw.visitVarInsn(ALOAD, Context.obj);
            mw.visitVarInsn(ALOAD, Context.fieldName);
            mw.visitMethodInsn(INVOKEVIRTUAL, JSONSerializer, "applyName",
                               "(" + desc(SerializeFilterable.class) + "Ljava/lang/Object;Ljava/lang/String;)Z");
            mw.visitJumpInsn(IFEQ, _end);

            _labelApply(mw, property, context, _end);
        }

        if (property.field == null) {
            mw.visitVarInsn(ALOAD, context.var("out"));
            mw.visitLdcInsn(SerializerFeature.IgnoreNonFieldGetter.mask);
            mw.visitMethodInsn(INVOKEVIRTUAL, SerializeWriter, "isEnabled", "(I)Z");

            // if true
            mw.visitJumpInsn(IFNE, _end);
        }
    }

    private void _labelApply(MethodVisitor mw, FieldInfo property, Context context, Label _end) {
        mw.visitVarInsn(ALOAD, Context.serializer);
        mw.visitVarInsn(ALOAD, 0);
        mw.visitLdcInsn(property.label);
        mw.visitMethodInsn(INVOKEVIRTUAL, JSONSerializer, "applyLabel",
                           "(" + desc(SerializeFilterable.class) + "Ljava/lang/String;)Z");
        mw.visitJumpInsn(IFEQ, _end);
    }

    private void _writeObject(MethodVisitor mw, FieldInfo fieldInfo, Context context, Label _end) {
        String format = fieldInfo.getFormat();
        Class<?> fieldClass = fieldInfo.fieldClass;

        Label notNull_ = new Label();

        // if (obj == null)
        if (context.writeDirect) {
            mw.visitVarInsn(ALOAD, context.var("object"));
        } else {
            mw.visitVarInsn(ALOAD, Context.processValue);
        }
        mw.visitInsn(DUP);
        mw.visitVarInsn(ASTORE, context.var("object"));
        mw.visitJumpInsn(IFNONNULL, notNull_);
        _if_write_null(mw, fieldInfo, context);
        mw.visitJumpInsn(GOTO, _end);

        mw.visitLabel(notNull_);
        
        mw.visitVarInsn(ALOAD, context.var("out"));
        mw.visitVarInsn(ILOAD, context.var("seperator"));
        mw.visitMethodInsn(INVOKEVIRTUAL, SerializeWriter, "write", "(I)V");
        
        // out.writeFieldName("fieldName")
        mw.visitVarInsn(ALOAD, context.var("out"));
        mw.visitVarInsn(ALOAD, Context.fieldName);
        mw.visitInsn(ICONST_0);
        mw.visitMethodInsn(INVOKEVIRTUAL, SerializeWriter, "writeFieldName", "(Ljava/lang/String;Z)V");

        Label classIfEnd_ = new Label(), classIfElse_ = new Label();
        if (!ParserConfig.isPrimitive(fieldClass)) {
            mw.visitVarInsn(ALOAD, context.var("object"));
            mw.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Object", "getClass", "()Ljava/lang/Class;");
            mw.visitLdcInsn(com.alibaba.fastjson.asm.Type.getType(desc(fieldClass)));
            mw.visitJumpInsn(IF_ACMPNE, classIfElse_);
            
            _getFieldSer(context, mw, fieldInfo);
            mw.visitVarInsn(ALOAD, Context.serializer);
            mw.visitVarInsn(ALOAD, context.var("object"));
            mw.visitVarInsn(ALOAD, Context.fieldName);
            mw.visitLdcInsn(com.alibaba.fastjson.asm.Type.getType(desc(fieldClass))); // fieldType
            mw.visitLdcInsn(fieldInfo.serialzeFeatures); // features
            mw.visitMethodInsn(INVOKEINTERFACE, type(ObjectSerializer.class), "write", // 
                               "(L" + JSONSerializer + ";Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/reflect/Type;I)V");
            mw.visitJumpInsn(GOTO, classIfEnd_);
        }

        mw.visitLabel(classIfElse_);
        
        // serializer.write(obj)
        mw.visitVarInsn(ALOAD, Context.serializer);
        if (context.writeDirect) {
            mw.visitVarInsn(ALOAD, context.var("object"));
        } else {
            mw.visitVarInsn(ALOAD, Context.processValue);
        }
        if (format != null) {
            mw.visitLdcInsn(format);
            mw.visitMethodInsn(INVOKEVIRTUAL, JSONSerializer, "writeWithFormat",
                               "(Ljava/lang/Object;Ljava/lang/String;)V");
        } else {
            mw.visitVarInsn(ALOAD, Context.fieldName);
            if (fieldInfo.fieldType instanceof Class<?> //
                && ((Class<?>) fieldInfo.fieldType).isPrimitive()) {
                mw.visitMethodInsn(INVOKEVIRTUAL, JSONSerializer, "writeWithFieldName",
                                   "(Ljava/lang/Object;Ljava/lang/Object;)V");
            } else {
                if (fieldInfo.fieldClass == String.class) {
                    mw.visitLdcInsn(com.alibaba.fastjson.asm.Type.getType(desc(String.class)));
                } else {
                    mw.visitVarInsn(ALOAD, 0);
                    mw.visitFieldInsn(GETFIELD, context.className, fieldInfo.name + "_asm_fieldType",
                                      "Ljava/lang/reflect/Type;");
                }
                mw.visitLdcInsn(fieldInfo.serialzeFeatures);

                mw.visitMethodInsn(INVOKEVIRTUAL, JSONSerializer, "writeWithFieldName",
                                   "(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/reflect/Type;I)V");
            }
        }
        mw.visitLabel(classIfEnd_);

        _seperator(mw, context);
    }

    private void _before(MethodVisitor mw, Context context) {
        mw.visitVarInsn(ALOAD, Context.serializer);
        mw.visitVarInsn(ALOAD, 0);
        mw.visitVarInsn(ALOAD, Context.obj);
        mw.visitVarInsn(ILOAD, context.var("seperator"));
        mw.visitMethodInsn(INVOKEVIRTUAL, JSONSerializer, "writeBefore",
                           "(" + desc(SerializeFilterable.class) + "Ljava/lang/Object;C)C");
        mw.visitVarInsn(ISTORE, context.var("seperator"));
    }

    private void _after(MethodVisitor mw, Context context) {
        mw.visitVarInsn(ALOAD, Context.serializer);
        mw.visitVarInsn(ALOAD, 0);
        mw.visitVarInsn(ALOAD, Context.obj);
        mw.visitVarInsn(ILOAD, context.var("seperator"));
        mw.visitMethodInsn(INVOKEVIRTUAL, JSONSerializer, "writeAfter",
                           "(" + desc(SerializeFilterable.class) + "Ljava/lang/Object;C)C");
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
        mw.visitVarInsn(ALOAD, 0);
        mw.visitVarInsn(ALOAD, Context.obj);
        mw.visitVarInsn(ALOAD, Context.fieldName);

        if (propertyClass == byte.class) {
            mw.visitVarInsn(ILOAD, context.var("byte"));
            mw.visitMethodInsn(INVOKESTATIC, "java/lang/Byte", "valueOf", "(B)Ljava/lang/Byte;");
        } else if (propertyClass == short.class) {
            mw.visitVarInsn(ILOAD, context.var("short"));
            mw.visitMethodInsn(INVOKESTATIC, "java/lang/Short", "valueOf", "(S)Ljava/lang/Short;");
        } else if (propertyClass == int.class) {
            mw.visitVarInsn(ILOAD, context.var("int"));
            mw.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;");
        } else if (propertyClass == char.class) {
            mw.visitVarInsn(ILOAD, context.var("char"));
            mw.visitMethodInsn(INVOKESTATIC, "java/lang/Character", "valueOf", "(C)Ljava/lang/Character;");
        } else if (propertyClass == long.class) {
            mw.visitVarInsn(LLOAD, context.var("long", 2));
            mw.visitMethodInsn(INVOKESTATIC, "java/lang/Long", "valueOf", "(J)Ljava/lang/Long;");
        } else if (propertyClass == float.class) {
            mw.visitVarInsn(FLOAD, context.var("float"));
            mw.visitMethodInsn(INVOKESTATIC, "java/lang/Float", "valueOf", "(F)Ljava/lang/Float;");
        } else if (propertyClass == double.class) {
            mw.visitVarInsn(DLOAD, context.var("double", 2));
            mw.visitMethodInsn(INVOKESTATIC, "java/lang/Double", "valueOf", "(D)Ljava/lang/Double;");
        } else if (propertyClass == boolean.class) {
            mw.visitVarInsn(ILOAD, context.var("boolean"));
            mw.visitMethodInsn(INVOKESTATIC, "java/lang/Boolean", "valueOf", "(Z)Ljava/lang/Boolean;");
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
        mw.visitMethodInsn(INVOKEVIRTUAL, JSONSerializer,
                           "apply", "(" + desc(SerializeFilterable.class)
                                    + "Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Object;)Z");
    }

    private void _processValue(MethodVisitor mw, FieldInfo fieldInfo, Context context, Label _end) {
        Label _else_processKey = new Label();

        Class<?> fieldClass = fieldInfo.fieldClass;

        if (fieldClass.isPrimitive()) {
            Label _end_checkValue = new Label();
            mw.visitVarInsn(ILOAD, context.var("checkValue"));
            mw.visitJumpInsn(IFNE, _end_checkValue);

            mw.visitInsn(ACONST_NULL);
            mw.visitInsn(DUP);
            mw.visitVarInsn(ASTORE, Context.original);
            mw.visitVarInsn(ASTORE, Context.processValue);
            mw.visitJumpInsn(GOTO, _else_processKey);

            mw.visitLabel(_end_checkValue);
        }

        int fieldIndex = -1;
        for (int i = 0, size = context.getters.size(); i < size; ++i) {
            FieldInfo item = context.getters.get(i);
            if (item.name.equals(fieldInfo.name)) {
                fieldIndex = i;
                break;
            }
        }

        mw.visitVarInsn(ALOAD, Context.serializer);
        mw.visitVarInsn(ALOAD, 0);
        mw.visitVarInsn(ALOAD, 0);
        mw.visitLdcInsn(fieldIndex);
        mw.visitMethodInsn(INVOKEVIRTUAL, JavaBeanSerializer, "getBeanContext", "(I)" + desc(BeanContext.class));
        mw.visitVarInsn(ALOAD, Context.obj);
        mw.visitVarInsn(ALOAD, Context.fieldName);

        String valueDesc = "Ljava/lang/Object;";
        if (fieldClass == byte.class) {
            mw.visitVarInsn(ILOAD, context.var("byte"));
            mw.visitMethodInsn(INVOKESTATIC, "java/lang/Byte", "valueOf", "(B)Ljava/lang/Byte;");
            mw.visitInsn(DUP);
            mw.visitVarInsn(ASTORE, Context.original);
        } else if (fieldClass == short.class) {
            mw.visitVarInsn(ILOAD, context.var("short"));
            mw.visitMethodInsn(INVOKESTATIC, "java/lang/Short", "valueOf", "(S)Ljava/lang/Short;");
            mw.visitInsn(DUP);
            mw.visitVarInsn(ASTORE, Context.original);
        } else if (fieldClass == int.class) {
            mw.visitVarInsn(ILOAD, context.var("int"));
            mw.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;");
            mw.visitInsn(DUP);
            mw.visitVarInsn(ASTORE, Context.original);
        } else if (fieldClass == char.class) {
            mw.visitVarInsn(ILOAD, context.var("char"));
            mw.visitMethodInsn(INVOKESTATIC, "java/lang/Character", "valueOf", "(C)Ljava/lang/Character;");
            mw.visitInsn(DUP);
            mw.visitVarInsn(ASTORE, Context.original);
        } else if (fieldClass == long.class) {
            mw.visitVarInsn(LLOAD, context.var("long", 2));
            mw.visitMethodInsn(INVOKESTATIC, "java/lang/Long", "valueOf", "(J)Ljava/lang/Long;");
            mw.visitInsn(DUP);
            mw.visitVarInsn(ASTORE, Context.original);
        } else if (fieldClass == float.class) {
            mw.visitVarInsn(FLOAD, context.var("float"));
            mw.visitMethodInsn(INVOKESTATIC, "java/lang/Float", "valueOf", "(F)Ljava/lang/Float;");
            mw.visitInsn(DUP);
            mw.visitVarInsn(ASTORE, Context.original);
        } else if (fieldClass == double.class) {
            mw.visitVarInsn(DLOAD, context.var("double", 2));
            mw.visitMethodInsn(INVOKESTATIC, "java/lang/Double", "valueOf", "(D)Ljava/lang/Double;");
            mw.visitInsn(DUP);
            mw.visitVarInsn(ASTORE, Context.original);
        } else if (fieldClass == boolean.class) {
            mw.visitVarInsn(ILOAD, context.var("boolean"));
            mw.visitMethodInsn(INVOKESTATIC, "java/lang/Boolean", "valueOf", "(Z)Ljava/lang/Boolean;");
            mw.visitInsn(DUP);
            mw.visitVarInsn(ASTORE, Context.original);
        } else if (fieldClass == BigDecimal.class) {
            mw.visitVarInsn(ALOAD, context.var("decimal"));
            mw.visitVarInsn(ASTORE, Context.original);
            mw.visitVarInsn(ALOAD, Context.original);
        } else if (fieldClass == String.class) {
            mw.visitVarInsn(ALOAD, context.var("string"));
            mw.visitVarInsn(ASTORE, Context.original);
            mw.visitVarInsn(ALOAD, Context.original);
        } else if (fieldClass.isEnum()) {
            mw.visitVarInsn(ALOAD, context.var("enum"));
            mw.visitVarInsn(ASTORE, Context.original);
            mw.visitVarInsn(ALOAD, Context.original);
        } else if (List.class.isAssignableFrom(fieldClass)) {
            mw.visitVarInsn(ALOAD, context.var("list"));
            mw.visitVarInsn(ASTORE, Context.original);
            mw.visitVarInsn(ALOAD, Context.original);
        } else {
            mw.visitVarInsn(ALOAD, context.var("object"));
            mw.visitVarInsn(ASTORE, Context.original);
            mw.visitVarInsn(ALOAD, Context.original);
        }

        mw.visitMethodInsn(INVOKEVIRTUAL, JSONSerializer, "processValue",
                           "(" + desc(SerializeFilterable.class) //
                                                                          + desc(BeanContext.class) //
                                                                          + "Ljava/lang/Object;Ljava/lang/String;" //
                                                                          + valueDesc + ")Ljava/lang/Object;");

        mw.visitVarInsn(ASTORE, Context.processValue);

        mw.visitVarInsn(ALOAD, Context.original);
        mw.visitVarInsn(ALOAD, Context.processValue);
        mw.visitJumpInsn(IF_ACMPEQ, _else_processKey);
        _writeObject(mw, fieldInfo, context, _end);
        mw.visitJumpInsn(GOTO, _end);

        mw.visitLabel(_else_processKey);
    }

    private void _processKey(MethodVisitor mw, FieldInfo property, Context context) {
        Label _else_processKey = new Label();

        mw.visitVarInsn(ILOAD, context.var("hasNameFilters"));
        mw.visitJumpInsn(IFNE, _else_processKey);

        Class<?> propertyClass = property.fieldClass;

        mw.visitVarInsn(ALOAD, Context.serializer);
        mw.visitVarInsn(ALOAD, 0);
        mw.visitVarInsn(ALOAD, Context.obj);
        mw.visitVarInsn(ALOAD, Context.fieldName);

        if (propertyClass == byte.class) {
            mw.visitVarInsn(ILOAD, context.var("byte"));
            mw.visitMethodInsn(INVOKESTATIC, "java/lang/Byte", "valueOf", "(B)Ljava/lang/Byte;");
        } else if (propertyClass == short.class) {
            mw.visitVarInsn(ILOAD, context.var("short"));
            mw.visitMethodInsn(INVOKESTATIC, "java/lang/Short", "valueOf", "(S)Ljava/lang/Short;");
        } else if (propertyClass == int.class) {
            mw.visitVarInsn(ILOAD, context.var("int"));
            mw.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;");
        } else if (propertyClass == char.class) {
            mw.visitVarInsn(ILOAD, context.var("char"));
            mw.visitMethodInsn(INVOKESTATIC, "java/lang/Character", "valueOf", "(C)Ljava/lang/Character;");
        } else if (propertyClass == long.class) {
            mw.visitVarInsn(LLOAD, context.var("long", 2));
            mw.visitMethodInsn(INVOKESTATIC, "java/lang/Long", "valueOf", "(J)Ljava/lang/Long;");
        } else if (propertyClass == float.class) {
            mw.visitVarInsn(FLOAD, context.var("float"));
            mw.visitMethodInsn(INVOKESTATIC, "java/lang/Float", "valueOf", "(F)Ljava/lang/Float;");
        } else if (propertyClass == double.class) {
            mw.visitVarInsn(DLOAD, context.var("double", 2));
            mw.visitMethodInsn(INVOKESTATIC, "java/lang/Double", "valueOf", "(D)Ljava/lang/Double;");
        } else if (propertyClass == boolean.class) {
            mw.visitVarInsn(ILOAD, context.var("boolean"));
            mw.visitMethodInsn(INVOKESTATIC, "java/lang/Boolean", "valueOf", "(Z)Ljava/lang/Boolean;");
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

        mw.visitMethodInsn(INVOKEVIRTUAL, JSONSerializer,
                           "processKey", "(" + desc(SerializeFilterable.class)
                                         + "Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/String;");

        mw.visitVarInsn(ASTORE, Context.fieldName);

        mw.visitLabel(_else_processKey);
    }

    private void _if_write_null(MethodVisitor mw, FieldInfo fieldInfo, Context context) {
        Class<?> propertyClass = fieldInfo.fieldClass;

        Label _if = new Label();
        Label _else = new Label();
        Label _write_null = new Label();
        Label _end_if = new Label();

        mw.visitLabel(_if);

        JSONField annotation = fieldInfo.getAnnotation();
        int features = 0;
        if (annotation != null) {
            features = SerializerFeature.of(annotation.serialzeFeatures());
            ;
        }

        if ((features & SerializerFeature.WriteMapNullValue.mask) == 0) {
            mw.visitVarInsn(ALOAD, context.var("out"));
            mw.visitMethodInsn(INVOKEVIRTUAL, SerializeWriter, "isWriteMapNullValue", "()Z");
            mw.visitJumpInsn(IFEQ, _else);
        }

        mw.visitLabel(_write_null);

        mw.visitVarInsn(ALOAD, context.var("out"));
        mw.visitVarInsn(ILOAD, context.var("seperator"));
        mw.visitMethodInsn(INVOKEVIRTUAL, SerializeWriter, "write", "(I)V");

        mw.visitVarInsn(ALOAD, context.var("out"));
        mw.visitVarInsn(ALOAD, Context.fieldName);
        mw.visitMethodInsn(INVOKEVIRTUAL, SerializeWriter, "writeFieldName", "(Ljava/lang/String;)V");

        mw.visitVarInsn(ALOAD, context.var("out"));
        mw.visitLdcInsn(features);
        // features

        if (propertyClass == String.class || propertyClass == Character.class) {
            mw.visitLdcInsn(SerializerFeature.WriteNullStringAsEmpty.mask);
        } else if (Number.class.isAssignableFrom(propertyClass)) {
            mw.visitLdcInsn(SerializerFeature.WriteNullNumberAsZero.mask);
        } else if (propertyClass == Boolean.class) {
            mw.visitLdcInsn(SerializerFeature.WriteNullBooleanAsFalse.mask);
        } else if (Collection.class.isAssignableFrom(propertyClass) || propertyClass.isArray()) {
            mw.visitLdcInsn(SerializerFeature.WriteNullListAsEmpty.mask);
        } else {
            mw.visitLdcInsn(0);
        }
        mw.visitMethodInsn(INVOKEVIRTUAL, SerializeWriter, "writeNull", "(II)V");

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
    
    private void _getListFieldItemSer(Context context, MethodVisitor mw, FieldInfo fieldInfo, Class<?> itemType) {
        Label notNull_ = new Label();
        mw.visitVarInsn(ALOAD, 0);
        mw.visitFieldInsn(GETFIELD, context.className, fieldInfo.name + "_asm_list_item_ser_", desc(ObjectSerializer.class));
        mw.visitJumpInsn(IFNONNULL, notNull_);

        mw.visitVarInsn(ALOAD, 0);
        mw.visitVarInsn(ALOAD, Context.serializer);
        mw.visitLdcInsn(com.alibaba.fastjson.asm.Type.getType(desc(itemType)));
        mw.visitMethodInsn(INVOKEVIRTUAL, JSONSerializer, "getObjectWriter",
                           "(Ljava/lang/Class;)" + desc(ObjectSerializer.class));

        mw.visitFieldInsn(PUTFIELD, context.className, fieldInfo.name + "_asm_list_item_ser_", desc(ObjectSerializer.class));

        mw.visitLabel(notNull_);

        mw.visitVarInsn(ALOAD, 0);
        mw.visitFieldInsn(GETFIELD, context.className, fieldInfo.name + "_asm_list_item_ser_", desc(ObjectSerializer.class));
    }
    
    private void _getFieldSer(Context context, MethodVisitor mw, FieldInfo fieldInfo) {
        Label notNull_ = new Label();
        mw.visitVarInsn(ALOAD, 0);
        mw.visitFieldInsn(GETFIELD, context.className, fieldInfo.name + "_asm_ser_", desc(ObjectSerializer.class));
        mw.visitJumpInsn(IFNONNULL, notNull_);

        mw.visitVarInsn(ALOAD, 0);
        mw.visitVarInsn(ALOAD, Context.serializer);
        mw.visitLdcInsn(com.alibaba.fastjson.asm.Type.getType(desc(fieldInfo.fieldClass)));
        mw.visitMethodInsn(INVOKEVIRTUAL, JSONSerializer, "getObjectWriter",
                           "(Ljava/lang/Class;)" + desc(ObjectSerializer.class));

        mw.visitFieldInsn(PUTFIELD, context.className, fieldInfo.name + "_asm_ser_", desc(ObjectSerializer.class));

        mw.visitLabel(notNull_);

        mw.visitVarInsn(ALOAD, 0);
        mw.visitFieldInsn(GETFIELD, context.className, fieldInfo.name + "_asm_ser_", desc(ObjectSerializer.class));
    }
}
