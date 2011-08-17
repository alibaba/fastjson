package com.alibaba.fastjson.parser.deserializer;

import static com.alibaba.fastjson.util.ASMUtils.getDesc;
import static com.alibaba.fastjson.util.ASMUtils.getType;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.asm.ClassWriter;
import com.alibaba.fastjson.asm.FieldVisitor;
import com.alibaba.fastjson.asm.Label;
import com.alibaba.fastjson.asm.MethodVisitor;
import com.alibaba.fastjson.asm.Opcodes;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.DefaultJSONParser.ResolveTask;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONScanner;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.ParseContext;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.SymbolTable;
import com.alibaba.fastjson.parser.deserializer.ASMJavaBeanDeserializer.InnerJavaBeanDeserializer;
import com.alibaba.fastjson.util.ASMClassLoader;
import com.alibaba.fastjson.util.DeserializeBeanInfo;
import com.alibaba.fastjson.util.FieldInfo;

public class ASMDeserializerFactory implements Opcodes {

    private static final ASMDeserializerFactory instance    = new ASMDeserializerFactory();

    private ASMClassLoader                      classLoader = new ASMClassLoader();

    private final AtomicLong                    seed        = new AtomicLong();

    public String getGenClassName(Class<?> clazz) {
        return "Fastjson_ASM_" + clazz.getSimpleName() + "_" + seed.incrementAndGet();
    }

    public String getGenFieldDeserializer(Class<?> clazz, FieldInfo fieldInfo) {
        String name = "Fastjson_ASM__Field_" + clazz.getSimpleName();
        name += "_" + fieldInfo.getName() + "_" + seed.incrementAndGet();

        return name;
    }

    public ASMDeserializerFactory(){

    }

    public final static ASMDeserializerFactory getInstance() {
        return instance;
    }

    public ObjectDeserializer createJavaBeanDeserializer(ParserConfig config, Class<?> clazz) throws Exception {
        if (clazz.isPrimitive()) {
            throw new IllegalArgumentException("not support type :" + clazz.getName());
        }

        String className = getGenClassName(clazz);

        ClassWriter cw = new ClassWriter();
        cw.visit(V1_5, ACC_PUBLIC + ACC_SUPER, className, getType(ASMJavaBeanDeserializer.class), null);

        DeserializeBeanInfo beanInfo = DeserializeBeanInfo.computeSetters(clazz);

        _init(cw, new Context(className, config, beanInfo, 3));
        _createInstance(cw, new Context(className, config, beanInfo, 3));
        _deserialze(cw, new Context(className, config, beanInfo, 4));

        byte[] code = cw.toByteArray();

         org.apache.commons.io.IOUtils.write(code, new java.io.FileOutputStream(
         "/usr/alibaba/workspace/fastjson-asm/target/classes/"
         + className + ".class"));

        Class<?> exampleClass = classLoader.defineClassPublic(className, code, 0, code.length);

        Constructor<?> constructor = exampleClass.getConstructor(ParserConfig.class, Class.class);
        Object instance = constructor.newInstance(config, clazz);

        return (ObjectDeserializer) instance;
    }

    void _deserialze(ClassWriter cw, Context context) {
        if (context.getFieldInfoList().size() == 0) {
            return;
        }

        for (FieldInfo fieldInfo : context.getFieldInfoList()) {
            Class<?> fieldClass = fieldInfo.getFieldClass();
            Type fieldType = fieldInfo.getFieldType();

            if (fieldClass == char.class) {
                return;
            }

            if (Collection.class.isAssignableFrom(fieldClass)) {
                if (fieldType instanceof ParameterizedType) {
                    Type itemType = ((ParameterizedType) fieldType).getActualTypeArguments()[0];
                    if (itemType instanceof Class) {
                        continue;
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            }
        }

        Collections.sort(context.getFieldInfoList());

        MethodVisitor mw = cw.visitMethod(ACC_PUBLIC, "deserialze", "(" + getDesc(DefaultJSONParser.class)
                                                                    + getDesc(Type.class)
                                                                    + "Ljava/lang/Object;)Ljava/lang/Object;", null,
                                          null);

        Label reset_ = new Label();
        Label super_ = new Label();
        Label end_ = new Label();

        mw.visitVarInsn(ALOAD, 1);
        mw.visitMethodInsn(INVOKEVIRTUAL, getType(DefaultJSONParser.class), "getLexer", "()" + getDesc(JSONLexer.class));
        mw.visitTypeInsn(CHECKCAST, getType(JSONScanner.class)); // cast
        mw.visitVarInsn(ASTORE, context.var("lexer"));

        mw.visitVarInsn(ALOAD, context.var("lexer"));
        mw.visitFieldInsn(GETSTATIC, getType(Feature.class), "SortFeidFastMatch", "L" + getType(Feature.class) + ";");
        mw.visitMethodInsn(INVOKEVIRTUAL, getType(JSONScanner.class), "isEnabled", "(" + "L" + getType(Feature.class)
                                                                                   + ";" + ")Z");
        mw.visitJumpInsn(IFEQ, super_);

        mw.visitVarInsn(ALOAD, context.var("lexer"));
        mw.visitLdcInsn(context.getClazz().getName());
        mw.visitMethodInsn(INVOKEVIRTUAL, getType(JSONScanner.class), "scanType", "(Ljava/lang/String;)I");
        
        mw.visitFieldInsn(GETSTATIC, getType(JSONScanner.class), "NOT_MATCH", "I");
        mw.visitJumpInsn(IF_ICMPEQ, super_);
        // matchType

        mw.visitVarInsn(ALOAD, context.var("lexer"));
        mw.visitMethodInsn(INVOKEVIRTUAL, getType(JSONScanner.class), "getBufferPosition", "()I");
        mw.visitVarInsn(ISTORE, context.var("mark"));

        mw.visitVarInsn(ALOAD, context.var("lexer"));
        mw.visitMethodInsn(INVOKEVIRTUAL, getType(JSONScanner.class), "getCurrent", "()C");
        mw.visitVarInsn(ISTORE, context.var("mark_ch"));

        mw.visitVarInsn(ALOAD, context.var("lexer"));
        mw.visitMethodInsn(INVOKEVIRTUAL, getType(JSONScanner.class), "token", "()I");
        mw.visitVarInsn(ISTORE, context.var("mark_token"));

        Constructor<?> defaultConstructor = context.getBeanInfo().getDefaultConstructor();

        // create instance
        if (context.getClazz().isInterface()) {
            mw.visitVarInsn(ALOAD, 0);
            mw.visitMethodInsn(INVOKESPECIAL, getType(ASMJavaBeanDeserializer.class), "createInstance",
                               "()Ljava/lang/Object;");
            mw.visitTypeInsn(CHECKCAST, getType(context.getClazz())); // cast
            mw.visitVarInsn(ASTORE, context.var("instance"));
        } else {
            if (defaultConstructor != null) {
                mw.visitTypeInsn(NEW, getType(context.getClazz()));
                mw.visitInsn(DUP);
                mw.visitMethodInsn(INVOKESPECIAL, getType(context.getClazz()), "<init>", "()V");
                mw.visitVarInsn(ASTORE, context.var("instance"));
            } else {
                mw.visitInsn(ACONST_NULL);
                mw.visitTypeInsn(CHECKCAST, getType(context.getClazz())); // cast
                mw.visitVarInsn(ASTORE, context.var("instance"));
            }
        }

        {
            mw.visitVarInsn(ALOAD, 1); // parser
            mw.visitMethodInsn(INVOKEVIRTUAL, getType(DefaultJSONParser.class), "getContext",
                               "()Lcom/alibaba/fastjson/parser/ParseContext;");
            mw.visitVarInsn(ASTORE, context.var("context"));

            mw.visitVarInsn(ALOAD, 1); // parser
            mw.visitVarInsn(ALOAD, context.var("context"));
            mw.visitVarInsn(ALOAD, context.var("instance"));
            mw.visitVarInsn(ALOAD, 3); // fieldName
            mw.visitMethodInsn(INVOKEVIRTUAL,
                               getType(DefaultJSONParser.class),
                               "setContext",
                               "(Lcom/alibaba/fastjson/parser/ParseContext;Ljava/lang/Object;Ljava/lang/Object;)Lcom/alibaba/fastjson/parser/ParseContext;");
            mw.visitVarInsn(ASTORE, context.var("childContext"));
        }

        for (int i = 0, size = context.getFieldInfoList().size(); i < size; ++i) {
            FieldInfo fieldInfo = context.getFieldInfoList().get(i);
            Class<?> fieldClass = fieldInfo.getFieldClass();
            Type fieldType = fieldInfo.getFieldType();

            mw.visitVarInsn(ALOAD, context.var("lexer"));
            mw.visitVarInsn(ALOAD, 0);
            mw.visitFieldInsn(GETFIELD, context.getClassName(), fieldInfo.getName() + "_asm_prefix__", "[C");
            if (fieldClass == boolean.class) {
                mw.visitMethodInsn(INVOKEVIRTUAL, getType(JSONScanner.class), "scanFieldBoolean", "([C)Z");
                mw.visitVarInsn(ISTORE, context.var(fieldInfo.getName() + "_asm"));

            } else if (fieldClass == byte.class) {
                mw.visitMethodInsn(INVOKEVIRTUAL, getType(JSONScanner.class), "scanFieldInt", "([C)I");
                mw.visitVarInsn(ISTORE, context.var(fieldInfo.getName() + "_asm"));

            } else if (fieldClass == short.class) {
                mw.visitMethodInsn(INVOKEVIRTUAL, getType(JSONScanner.class), "scanFieldInt", "([C)I");
                mw.visitVarInsn(ISTORE, context.var(fieldInfo.getName() + "_asm"));

            } else if (fieldClass == int.class) {
                mw.visitMethodInsn(INVOKEVIRTUAL, getType(JSONScanner.class), "scanFieldInt", "([C)I");
                mw.visitVarInsn(ISTORE, context.var(fieldInfo.getName() + "_asm"));

            } else if (fieldClass == long.class) {
                mw.visitMethodInsn(INVOKEVIRTUAL, getType(JSONScanner.class), "scanFieldLong", "([C)J");
                mw.visitVarInsn(LSTORE, context.var(fieldInfo.getName() + "_asm", 2));

            } else if (fieldClass == float.class) {
                mw.visitMethodInsn(INVOKEVIRTUAL, getType(JSONScanner.class), "scanFieldFloat", "([C)F");
                mw.visitVarInsn(FSTORE, context.var(fieldInfo.getName() + "_asm"));

            } else if (fieldClass == double.class) {
                mw.visitMethodInsn(INVOKEVIRTUAL, getType(JSONScanner.class), "scanFieldDouble", "([C)D");
                mw.visitVarInsn(DSTORE, context.var(fieldInfo.getName() + "_asm", 2));

            } else if (fieldClass == String.class) {
                mw.visitMethodInsn(INVOKEVIRTUAL, getType(JSONScanner.class), "scanFieldString",
                                   "([C)Ljava/lang/String;");
                mw.visitVarInsn(ASTORE, context.var(fieldInfo.getName() + "_asm"));
            } else if (fieldClass == byte[].class) {
                mw.visitMethodInsn(INVOKEVIRTUAL, getType(JSONScanner.class), "scanFieldByteArray", "([C)[B");
                mw.visitVarInsn(ASTORE, context.var(fieldInfo.getName() + "_asm"));
            } else if (fieldClass.isEnum()) {
                Label enumNull_ = new Label();
                mw.visitInsn(ACONST_NULL);
                mw.visitTypeInsn(CHECKCAST, getType(fieldClass)); // cast
                mw.visitVarInsn(ASTORE, context.var(fieldInfo.getName() + "_asm"));

                mw.visitVarInsn(ALOAD, 1);
                mw.visitMethodInsn(INVOKEVIRTUAL, getType(DefaultJSONParser.class), "getSymbolTable",
                                   "()" + getDesc(SymbolTable.class));

                mw.visitMethodInsn(INVOKEVIRTUAL, getType(JSONScanner.class), "scanFieldSymbol",
                                   "([C" + getDesc(SymbolTable.class) + ")Ljava/lang/String;");
                mw.visitInsn(DUP);
                mw.visitVarInsn(ASTORE, context.var(fieldInfo.getName() + "_asm_enumName"));

                mw.visitJumpInsn(IFNULL, enumNull_);
                mw.visitVarInsn(ALOAD, context.var(fieldInfo.getName() + "_asm_enumName"));
                mw.visitMethodInsn(INVOKESTATIC, getType(fieldClass), "valueOf", "(Ljava/lang/String;)"
                                                                                 + getDesc(fieldClass));
                mw.visitVarInsn(ASTORE, context.var(fieldInfo.getName() + "_asm"));
                mw.visitLabel(enumNull_);

            } else if (Collection.class.isAssignableFrom(fieldClass)) {
                Class<?> itemType = (Class<?>) ((ParameterizedType) fieldType).getActualTypeArguments()[0];
                if (itemType == String.class) {
                    mw.visitMethodInsn(INVOKEVIRTUAL, getType(JSONScanner.class), "scanFieldStringArray",
                                       "([C)" + getDesc(ArrayList.class));
                    mw.visitVarInsn(ASTORE, context.var(fieldInfo.getName() + "_asm"));
                } else {
                    _deserialze_list_obj(context, mw, reset_, fieldInfo, fieldClass, itemType);

                    if (i == size - 1) {
                        _deserialize_endCheck(context, mw, reset_);
                    }
                    continue;
                }

            } else {
                _deserialze_obj(context, mw, reset_, fieldInfo, fieldClass);

                if (i == size - 1) {
                    _deserialize_endCheck(context, mw, reset_);
                }

                continue;
            }

            mw.visitVarInsn(ALOAD, context.var("lexer"));
            mw.visitFieldInsn(GETFIELD, getType(JSONScanner.class), "matchStat", "I");
            mw.visitFieldInsn(GETSTATIC, getType(JSONScanner.class), "NOT_MATCH", "I");
            mw.visitJumpInsn(IF_ICMPEQ, reset_);

            if (i == size - 1) {
                mw.visitVarInsn(ALOAD, context.var("lexer"));
                mw.visitFieldInsn(GETFIELD, getType(JSONScanner.class), "matchStat", "I");
                mw.visitFieldInsn(GETSTATIC, getType(JSONScanner.class), "END", "I");
                mw.visitJumpInsn(IF_ICMPNE, reset_);
            }
        }

        if (!context.getClazz().isInterface()) {
            if (defaultConstructor != null) {
                _batchSet(context, mw);
            } else {
                mw.visitTypeInsn(NEW, getType(context.getClazz()));
                mw.visitInsn(DUP);

                Constructor<?> creatorConstructor = context.getBeanInfo().getCreatorConstructor();
                if (creatorConstructor != null) {
                    _loadCreatorParameters(context, mw);

                    mw.visitMethodInsn(INVOKESPECIAL, getType(context.getClazz()), "<init>",
                                       getDesc(creatorConstructor));
                    mw.visitVarInsn(ASTORE, context.var("instance"));
                } else {
                    Method factoryMethod = context.getBeanInfo().getFactoryMethod();
                    if (factoryMethod != null) {
                        _loadCreatorParameters(context, mw);
                        mw.visitMethodInsn(INVOKESTATIC, getType(factoryMethod.getDeclaringClass()),
                                           factoryMethod.getName(), getDesc(factoryMethod));
                        mw.visitVarInsn(ASTORE, context.var("instance"));
                    } else {
                        throw new JSONException("TODO");
                    }
                }
            }
        }

        _setContext(context, mw);
        mw.visitVarInsn(ALOAD, context.var("instance"));
        mw.visitInsn(ARETURN);

        mw.visitLabel(reset_);

        // void reset(int mark, char mark_ch)
        mw.visitVarInsn(ALOAD, context.var("lexer"));
        mw.visitVarInsn(ILOAD, context.var("mark"));
        mw.visitVarInsn(ILOAD, context.var("mark_ch"));
        mw.visitVarInsn(ILOAD, context.var("mark_token"));
        mw.visitMethodInsn(INVOKEVIRTUAL, getType(JSONScanner.class), "reset", "(ICI)V");

        _setContext(context, mw);

        mw.visitLabel(super_);
        mw.visitVarInsn(ALOAD, 0);
        mw.visitVarInsn(ALOAD, 1);
        mw.visitVarInsn(ALOAD, 2);
        mw.visitVarInsn(ALOAD, 3);
        mw.visitMethodInsn(INVOKESPECIAL, getType(ASMJavaBeanDeserializer.class), "deserialze",
                           "(" + getDesc(DefaultJSONParser.class) + getDesc(Type.class)
                                   + "Ljava/lang/Object;)Ljava/lang/Object;");
        mw.visitInsn(ARETURN);

        mw.visitLabel(end_);

        mw.visitMaxs(4, context.getVariantCount());
        mw.visitEnd();
    }

    private void _loadCreatorParameters(Context context, MethodVisitor mw) {
        List<FieldInfo> fieldInfoList = context.getBeanInfo().getFieldList();

        for (int i = 0, size = fieldInfoList.size(); i < size; ++i) {
            FieldInfo fieldInfo = fieldInfoList.get(i);
            Class<?> fieldClass = fieldInfo.getFieldClass();
            Type fieldType = fieldInfo.getFieldType();

            if (fieldClass == boolean.class) {
                mw.visitVarInsn(ILOAD, context.var(fieldInfo.getName() + "_asm"));
            } else if (fieldClass == byte.class) {
                mw.visitVarInsn(ILOAD, context.var(fieldInfo.getName() + "_asm"));
            } else if (fieldClass == short.class) {
                mw.visitVarInsn(ILOAD, context.var(fieldInfo.getName() + "_asm"));
            } else if (fieldClass == int.class) {
                mw.visitVarInsn(ILOAD, context.var(fieldInfo.getName() + "_asm"));
            } else if (fieldClass == long.class) {
                mw.visitVarInsn(LLOAD, context.var(fieldInfo.getName() + "_asm"));
                mw.visitMethodInsn(INVOKEVIRTUAL, getType(context.getClazz()), fieldInfo.getMethod().getName(), "(J)V");
                continue;
            } else if (fieldClass == float.class) {
                mw.visitVarInsn(FLOAD, context.var(fieldInfo.getName() + "_asm"));
            } else if (fieldClass == double.class) {
                mw.visitVarInsn(DLOAD, context.var(fieldInfo.getName() + "_asm", 2));
            } else if (fieldClass == String.class) {
                mw.visitVarInsn(ALOAD, context.var(fieldInfo.getName() + "_asm"));
            } else if (fieldClass.isEnum()) {
                mw.visitVarInsn(ALOAD, context.var(fieldInfo.getName() + "_asm"));
            } else if (Collection.class.isAssignableFrom(fieldClass)) {
                Type itemType = ((ParameterizedType) fieldType).getActualTypeArguments()[0];
                if (itemType == String.class) {
                    mw.visitVarInsn(ALOAD, context.var(fieldInfo.getName() + "_asm"));
                    mw.visitTypeInsn(CHECKCAST, getType(fieldClass)); // cast
                } else {
                    mw.visitVarInsn(ALOAD, context.var(fieldInfo.getName() + "_asm"));
                }
            } else {
                mw.visitVarInsn(ALOAD, context.var(fieldInfo.getName() + "_asm"));
            }
        }
    }

    private void _batchSet(Context context, MethodVisitor mw) {
        for (int i = 0, size = context.getFieldInfoList().size(); i < size; ++i) {
            FieldInfo fieldInfo = context.getFieldInfoList().get(i);
            Class<?> fieldClass = fieldInfo.getFieldClass();
            Type fieldType = fieldInfo.getFieldType();

            mw.visitVarInsn(ALOAD, context.var("instance"));
            if (fieldClass == boolean.class) {
                mw.visitVarInsn(ILOAD, context.var(fieldInfo.getName() + "_asm"));
            } else if (fieldClass == byte.class) {
                mw.visitVarInsn(ILOAD, context.var(fieldInfo.getName() + "_asm"));
            } else if (fieldClass == short.class) {
                mw.visitVarInsn(ILOAD, context.var(fieldInfo.getName() + "_asm"));
            } else if (fieldClass == int.class) {
                mw.visitVarInsn(ILOAD, context.var(fieldInfo.getName() + "_asm"));
            } else if (fieldClass == long.class) {
                mw.visitVarInsn(LLOAD, context.var(fieldInfo.getName() + "_asm"));
                mw.visitMethodInsn(INVOKEVIRTUAL, getType(context.getClazz()), fieldInfo.getMethod().getName(), "(J)V");
                continue;
            } else if (fieldClass == float.class) {
                mw.visitVarInsn(FLOAD, context.var(fieldInfo.getName() + "_asm"));
            } else if (fieldClass == double.class) {
                mw.visitVarInsn(DLOAD, context.var(fieldInfo.getName() + "_asm", 2));
            } else if (fieldClass == String.class) {
                mw.visitVarInsn(ALOAD, context.var(fieldInfo.getName() + "_asm"));
            } else if (fieldClass.isEnum()) {
                mw.visitVarInsn(ALOAD, context.var(fieldInfo.getName() + "_asm"));
            } else if (Collection.class.isAssignableFrom(fieldClass)) {
                Type itemType = ((ParameterizedType) fieldType).getActualTypeArguments()[0];
                if (itemType == String.class) {
                    mw.visitVarInsn(ALOAD, context.var(fieldInfo.getName() + "_asm"));
                    mw.visitTypeInsn(CHECKCAST, getType(fieldClass)); // cast
                } else {
                    mw.visitVarInsn(ALOAD, context.var(fieldInfo.getName() + "_asm"));
                }
            } else {
                mw.visitVarInsn(ALOAD, context.var(fieldInfo.getName() + "_asm"));
            }

            int INVAKE_TYPE;
            if (context.getClazz().isInterface()) {
                INVAKE_TYPE = INVOKEINTERFACE;
            } else {
                INVAKE_TYPE = INVOKEVIRTUAL;
            }
            mw.visitMethodInsn(INVAKE_TYPE, getType(fieldInfo.getDeclaringClass()), fieldInfo.getMethod().getName(),
                               getDesc(fieldInfo.getMethod()));
        }
    }

    private void _setContext(Context context, MethodVisitor mw) {
        mw.visitVarInsn(ALOAD, 1); // parser
        mw.visitVarInsn(ALOAD, context.var("context"));
        mw.visitMethodInsn(INVOKEVIRTUAL, getType(DefaultJSONParser.class), "setContext",
                           "(Lcom/alibaba/fastjson/parser/ParseContext;)V");

        // TODO childContext is null
        mw.visitVarInsn(ALOAD, context.var("childContext"));
        mw.visitVarInsn(ALOAD, context.var("instance"));
        mw.visitMethodInsn(INVOKEVIRTUAL, getType(ParseContext.class), "setObject", "(Ljava/lang/Object;)V");
    }

    private void _deserialize_endCheck(Context context, MethodVisitor mw, Label reset_) {
        mw.visitVarInsn(ALOAD, context.var("lexer"));
        mw.visitMethodInsn(INVOKEVIRTUAL, getType(JSONScanner.class), "token", "()I");
        mw.visitFieldInsn(GETSTATIC, getType(JSONToken.class), "RBRACE", "I");
        mw.visitJumpInsn(IF_ICMPNE, reset_);

        mw.visitVarInsn(ALOAD, context.var("lexer"));
        mw.visitFieldInsn(GETSTATIC, getType(JSONToken.class), "COMMA", "I");
        mw.visitMethodInsn(INVOKEVIRTUAL, getType(JSONScanner.class), "nextToken", "(I)V");
    }

    private void _deserialze_list_obj(Context context, MethodVisitor mw, Label reset_, FieldInfo fieldInfo,
                                      Class<?> fieldClass, Class<?> itemType) {
        mw.visitMethodInsn(INVOKEVIRTUAL, getType(JSONScanner.class), "matchField", "([C)Z");
        mw.visitJumpInsn(IFEQ, reset_);

        Label notNull_ = new Label();
        mw.visitVarInsn(ALOAD, 0);
        mw.visitFieldInsn(GETFIELD, context.getClassName(), fieldInfo.getName() + "_asm_list_item_deser__",
                          getDesc(ObjectDeserializer.class));
        mw.visitJumpInsn(IFNONNULL, notNull_);

        mw.visitVarInsn(ALOAD, 0);

        mw.visitVarInsn(ALOAD, 1);
        mw.visitMethodInsn(INVOKEVIRTUAL, getType(DefaultJSONParser.class), "getConfig",
                           "()" + getDesc(ParserConfig.class));
        mw.visitLdcInsn(com.alibaba.fastjson.asm.Type.getType(getDesc(itemType)));
        mw.visitMethodInsn(INVOKEVIRTUAL, getType(ParserConfig.class), "getDeserializer",
                           "(" + getDesc(Type.class) + ")" + getDesc(ObjectDeserializer.class));

        mw.visitFieldInsn(PUTFIELD, context.getClassName(), fieldInfo.getName() + "_asm_list_item_deser__",
                          getDesc(ObjectDeserializer.class));

        mw.visitLabel(notNull_);

        // if (lexer.token() != JSONToken.LBRACKET) reset
        mw.visitVarInsn(ALOAD, context.var("lexer"));
        mw.visitMethodInsn(INVOKEVIRTUAL, getType(JSONScanner.class), "token", "()I");
        mw.visitFieldInsn(GETSTATIC, getType(JSONToken.class), "LBRACKET", "I");
        mw.visitJumpInsn(IF_ICMPNE, reset_);

        mw.visitVarInsn(ALOAD, 0);
        mw.visitFieldInsn(GETFIELD, context.getClassName(), fieldInfo.getName() + "_asm_list_item_deser__",
                          getDesc(ObjectDeserializer.class));
        mw.visitMethodInsn(INVOKEINTERFACE, getType(ObjectDeserializer.class), "getFastMatchToken", "()I");
        mw.visitVarInsn(ISTORE, context.var("fastMatchToken"));

        mw.visitVarInsn(ALOAD, context.var("lexer"));
        mw.visitVarInsn(ILOAD, context.var("fastMatchToken"));
        mw.visitMethodInsn(INVOKEVIRTUAL, getType(JSONScanner.class), "nextToken", "(I)V");

        mw.visitTypeInsn(NEW, getType(ArrayList.class));
        mw.visitInsn(DUP);
        mw.visitMethodInsn(INVOKESPECIAL, getType(ArrayList.class), "<init>", "()V");
        mw.visitTypeInsn(CHECKCAST, getType(fieldClass)); // cast
        mw.visitVarInsn(ASTORE, context.var(fieldInfo.getName() + "_asm"));

        Label loop_ = new Label();
        Label loop_end_ = new Label();

        // for (;;) {
        mw.visitInsn(ICONST_0);
        mw.visitVarInsn(ISTORE, context.var("i"));
        mw.visitLabel(loop_);
        // if (lexer.isEnabled(Feature.AllowArbitraryCommas)) {
        // while (lexer.token() == JSONToken.COMMA) {
        // lexer.nextToken();
        // continue;
        // }
        // }
        // if (lexer.token() == JSONToken.RBRACKET) {
        // break;
        // }
        mw.visitVarInsn(ALOAD, context.var("lexer"));
        mw.visitMethodInsn(INVOKEVIRTUAL, getType(JSONScanner.class), "token", "()I");
        mw.visitFieldInsn(GETSTATIC, getType(JSONToken.class), "RBRACKET", "I");
        mw.visitJumpInsn(IF_ICMPEQ, loop_end_);

        // Object value = itemDeserializer.deserialze(parser, null);
        // array.add(value);

        mw.visitVarInsn(ALOAD, 0);
        mw.visitFieldInsn(GETFIELD, context.getClassName(), fieldInfo.getName() + "_asm_list_item_deser__",
                          getDesc(ObjectDeserializer.class));
        mw.visitVarInsn(ALOAD, 1);
        mw.visitInsn(ACONST_NULL);
        mw.visitVarInsn(ILOAD, context.var("i"));
        mw.visitMethodInsn(INVOKESTATIC, getType(Integer.class), "valueOf", "(I)Ljava/lang/Integer;");
        mw.visitMethodInsn(INVOKEINTERFACE, getType(ObjectDeserializer.class), "deserialze",
                           "(Lcom/alibaba/fastjson/parser/DefaultJSONParser;Ljava/lang/reflect/Type;Ljava/lang/Object;)Ljava/lang/Object;");
        mw.visitVarInsn(ASTORE, context.var("list_item_value"));

        mw.visitIincInsn(context.var("i"), 1);

        mw.visitVarInsn(ALOAD, context.var(fieldInfo.getName() + "_asm"));
        mw.visitVarInsn(ALOAD, context.var("list_item_value"));
        if (fieldClass.isInterface()) {
            mw.visitMethodInsn(INVOKEINTERFACE, getType(fieldClass), "add", "(Ljava/lang/Object;)Z");
        } else {
            mw.visitMethodInsn(INVOKEVIRTUAL, getType(fieldClass), "add", "(Ljava/lang/Object;)Z");
        }
        mw.visitInsn(POP);

        // if (lexer.token() == JSONToken.COMMA) {
        // lexer.nextToken(itemDeserializer.getFastMatchToken());
        // continue;
        // }
        mw.visitVarInsn(ALOAD, context.var("lexer"));
        mw.visitMethodInsn(INVOKEVIRTUAL, getType(JSONScanner.class), "token", "()I");
        mw.visitFieldInsn(GETSTATIC, getType(JSONToken.class), "COMMA", "I");
        mw.visitJumpInsn(IF_ICMPNE, loop_);

        mw.visitVarInsn(ALOAD, context.var("lexer"));
        mw.visitVarInsn(ILOAD, context.var("fastMatchToken"));
        mw.visitMethodInsn(INVOKEVIRTUAL, getType(JSONScanner.class), "nextToken", "(I)V");
        mw.visitJumpInsn(GOTO, loop_);

        mw.visitLabel(loop_end_);

        mw.visitVarInsn(ALOAD, context.var("lexer"));
        mw.visitMethodInsn(INVOKEVIRTUAL, getType(JSONScanner.class), "token", "()I");
        mw.visitFieldInsn(GETSTATIC, getType(JSONToken.class), "RBRACKET", "I");
        mw.visitJumpInsn(IF_ICMPNE, reset_);

        mw.visitVarInsn(ALOAD, context.var("lexer"));
        mw.visitFieldInsn(GETSTATIC, getType(JSONToken.class), "COMMA", "I");
        mw.visitMethodInsn(INVOKEVIRTUAL, getType(JSONScanner.class), "nextToken", "(I)V");
        // lexer.nextToken(JSONToken.COMMA);

    }

    private void _deserialze_obj(Context context, MethodVisitor mw, Label reset_, FieldInfo fieldInfo,
                                 Class<?> fieldClass) {
        mw.visitMethodInsn(INVOKEVIRTUAL, getType(JSONScanner.class), "matchField", "([C)Z");
        mw.visitJumpInsn(IFEQ, reset_);

        Label notNull_ = new Label();
        mw.visitVarInsn(ALOAD, 0);
        mw.visitFieldInsn(GETFIELD, context.getClassName(), fieldInfo.getName() + "_asm_deser__",
                          getDesc(ObjectDeserializer.class));
        mw.visitJumpInsn(IFNONNULL, notNull_);

        mw.visitVarInsn(ALOAD, 0);

        mw.visitVarInsn(ALOAD, 1);
        mw.visitMethodInsn(INVOKEVIRTUAL, getType(DefaultJSONParser.class), "getConfig",
                           "()" + getDesc(ParserConfig.class));
        mw.visitLdcInsn(com.alibaba.fastjson.asm.Type.getType(getDesc(fieldInfo.getFieldClass())));
        mw.visitMethodInsn(INVOKEVIRTUAL, getType(ParserConfig.class), "getDeserializer",
                           "(" + getDesc(Type.class) + ")" + getDesc(ObjectDeserializer.class));

        mw.visitFieldInsn(PUTFIELD, context.getClassName(), fieldInfo.getName() + "_asm_deser__",
                          getDesc(ObjectDeserializer.class));

        mw.visitLabel(notNull_);

        mw.visitVarInsn(ALOAD, 0);
        mw.visitFieldInsn(GETFIELD, context.getClassName(), fieldInfo.getName() + "_asm_deser__",
                          getDesc(ObjectDeserializer.class));
        mw.visitVarInsn(ALOAD, 1);
        mw.visitLdcInsn(com.alibaba.fastjson.asm.Type.getType(getDesc(fieldInfo.getFieldClass())));
        mw.visitLdcInsn(fieldInfo.getName());
        mw.visitMethodInsn(INVOKEINTERFACE, getType(ObjectDeserializer.class), "deserialze",
                           "(" + getDesc(DefaultJSONParser.class) + getDesc(Type.class)
                                   + "Ljava/lang/Object;)Ljava/lang/Object;");
        mw.visitTypeInsn(CHECKCAST, getType(fieldClass)); // cast
        mw.visitVarInsn(ASTORE, context.var(fieldInfo.getName() + "_asm"));

        Label _end_if = new Label();

        mw.visitVarInsn(ALOAD, 1);
        mw.visitMethodInsn(INVOKEVIRTUAL, getType(DefaultJSONParser.class), "getResolveStatus", "()I");
        mw.visitFieldInsn(GETSTATIC, getType(DefaultJSONParser.class), "NeedToResolve", "I");
        mw.visitJumpInsn(IF_ICMPNE, _end_if);

        // ResolveTask task = parser.getLastResolveTask();
        // task.setFieldDeserializer(this);
        // task.setOwnerContext(parser.getContext());

        mw.visitVarInsn(ALOAD, 1);
        mw.visitMethodInsn(INVOKEVIRTUAL, getType(DefaultJSONParser.class), "getLastResolveTask",
                           "()" + getDesc(ResolveTask.class));
        mw.visitVarInsn(ASTORE, context.var("resolveTask"));

        mw.visitVarInsn(ALOAD, context.var("resolveTask"));
        mw.visitVarInsn(ALOAD, 1);
        mw.visitMethodInsn(INVOKEVIRTUAL, getType(DefaultJSONParser.class), "getContext",
                           "()" + getDesc(ParseContext.class));
        mw.visitMethodInsn(INVOKEVIRTUAL, getType(ResolveTask.class), "setOwnerContext", "("
                                                                                         + getDesc(ParseContext.class)
                                                                                         + ")V");

        mw.visitVarInsn(ALOAD, context.var("resolveTask"));
        mw.visitVarInsn(ALOAD, 0);
        mw.visitLdcInsn(fieldInfo.getName());
        mw.visitMethodInsn(INVOKEVIRTUAL, getType(ASMJavaBeanDeserializer.class), "getFieldDeserializer",
                           "(Ljava/lang/String;)" + getDesc(FieldDeserializer.class));
        mw.visitMethodInsn(INVOKEVIRTUAL, getType(ResolveTask.class), "setFieldDeserializer",
                           "(" + getDesc(FieldDeserializer.class) + ")V");

        mw.visitVarInsn(ALOAD, 1);
        mw.visitFieldInsn(GETSTATIC, getType(DefaultJSONParser.class), "NONE", "I");
        mw.visitMethodInsn(INVOKEVIRTUAL, getType(DefaultJSONParser.class), "setResolveStatus", "(I)V");

        mw.visitLabel(_end_if);

    }

    public FieldDeserializer createFieldDeserializer(ParserConfig mapping, Class<?> clazz, FieldInfo fieldInfo)
                                                                                                               throws Exception {
        Class<?> fieldClass = fieldInfo.getFieldClass();

        if (fieldClass == int.class || fieldClass == long.class || fieldClass == String.class) {
            return createStringFieldDeserializer(mapping, clazz, fieldInfo);
        }

        FieldDeserializer fieldDeserializer = mapping.createFieldDeserializerWithoutASM(mapping, clazz, fieldInfo);
        return fieldDeserializer;
    }

    public FieldDeserializer createStringFieldDeserializer(ParserConfig mapping, Class<?> clazz, FieldInfo fieldInfo)
                                                                                                                     throws Exception {
        Class<?> fieldClass = fieldInfo.getFieldClass();
        Method method = fieldInfo.getMethod();

        String className = getGenFieldDeserializer(clazz, fieldInfo);

        ClassWriter cw = new ClassWriter();
        Class<?> superClass;
        if (fieldClass == int.class) {
            superClass = IntegerFieldDeserializer.class;
        } else if (fieldClass == long.class) {
            superClass = LongFieldDeserializer.class;
        } else {
            superClass = StringFieldDeserializer.class;
        }

        int INVAKE_TYPE;
        if (clazz.isInterface()) {
            INVAKE_TYPE = INVOKEINTERFACE;
        } else {
            INVAKE_TYPE = INVOKEVIRTUAL;
        }

        cw.visit(V1_5, ACC_PUBLIC + ACC_SUPER, className, getType(superClass), null);

        {
            MethodVisitor mw = cw.visitMethod(ACC_PUBLIC, "<init>", "(" + getDesc(ParserConfig.class)
                                                                    + getDesc(Class.class) + getDesc(FieldInfo.class)
                                                                    + ")V", null, null);
            mw.visitVarInsn(ALOAD, 0);
            mw.visitVarInsn(ALOAD, 1);
            mw.visitVarInsn(ALOAD, 2);
            mw.visitVarInsn(ALOAD, 3);
            mw.visitMethodInsn(INVOKESPECIAL, getType(superClass), "<init>", "(" + getDesc(ParserConfig.class)
                                                                             + getDesc(Class.class)
                                                                             + getDesc(FieldInfo.class) + ")V");

            mw.visitInsn(RETURN);
            mw.visitMaxs(4, 6);
            mw.visitEnd();
        }

        if (method != null) {
            if (fieldClass == int.class) {
                MethodVisitor mw = cw.visitMethod(ACC_PUBLIC, "setValue", "(" + getDesc(Object.class) + "I)V", null,
                                                  null);
                mw.visitVarInsn(ALOAD, 1);
                mw.visitTypeInsn(CHECKCAST, getType(method.getDeclaringClass())); // cast
                mw.visitVarInsn(ILOAD, 2);
                mw.visitMethodInsn(INVAKE_TYPE, getType(method.getDeclaringClass()), method.getName(), "(I)V");

                mw.visitInsn(RETURN);
                mw.visitMaxs(3, 3);
                mw.visitEnd();
            } else if (fieldClass == long.class) {
                MethodVisitor mw = cw.visitMethod(ACC_PUBLIC, "setValue", "(" + getDesc(Object.class) + "J)V", null,
                                                  null);
                mw.visitVarInsn(ALOAD, 1);
                mw.visitTypeInsn(CHECKCAST, getType(method.getDeclaringClass())); // cast
                mw.visitVarInsn(LLOAD, 2);
                mw.visitMethodInsn(INVAKE_TYPE, getType(method.getDeclaringClass()), method.getName(), "(J)V");

                mw.visitInsn(RETURN);
                mw.visitMaxs(3, 4);
                mw.visitEnd();
            } else {
                // public void setValue(Object object, Object value)
                MethodVisitor mw = cw.visitMethod(ACC_PUBLIC, "setValue", "(" + getDesc(Object.class)
                                                                          + getDesc(Object.class) + ")V", null, null);
                mw.visitVarInsn(ALOAD, 1);
                mw.visitTypeInsn(CHECKCAST, getType(method.getDeclaringClass())); // cast
                mw.visitVarInsn(ALOAD, 2);
                mw.visitTypeInsn(CHECKCAST, getType(fieldClass)); // cast
                mw.visitMethodInsn(INVAKE_TYPE, getType(method.getDeclaringClass()), method.getName(),
                                   "(" + getDesc(fieldClass) + ")V");

                mw.visitInsn(RETURN);
                mw.visitMaxs(3, 3);
                mw.visitEnd();
            }
        }

        byte[] code = cw.toByteArray();

        Class<?> exampleClass = classLoader.defineClassPublic(className, code, 0, code.length);

        Constructor<?> constructor = exampleClass.getConstructor(ParserConfig.class, Class.class, FieldInfo.class);
        Object instance = constructor.newInstance(mapping, clazz, fieldInfo);

        return (FieldDeserializer) instance;
    }

    static class Context {

        private int                       variantIndex = 5;

        private Map<String, Integer>      variants     = new HashMap<String, Integer>();

        private Class<?>                  clazz;
        private final DeserializeBeanInfo beanInfo;
        private String                    className;
        private List<FieldInfo>           fieldInfoList;

        public Context(String className, ParserConfig config, DeserializeBeanInfo beanInfo, int initVariantIndex){
            this.className = className;
            this.clazz = beanInfo.getClazz();
            this.variantIndex = initVariantIndex;
            this.beanInfo = beanInfo;
            fieldInfoList = new ArrayList<FieldInfo>(beanInfo.getFieldList());
        }

        public String getClassName() {
            return className;
        }

        public List<FieldInfo> getFieldInfoList() {
            return fieldInfoList;
        }

        public DeserializeBeanInfo getBeanInfo() {
            return beanInfo;
        }

        public Class<?> getClazz() {
            return clazz;
        }

        public int getVariantCount() {
            return variantIndex;
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

        public int var(String name) {
            Integer i = variants.get(name);
            if (i == null) {
                variants.put(name, variantIndex++);
            }
            i = variants.get(name);
            return i.intValue();
        }
    }

    private void _init(ClassWriter cw, Context context) {
        for (int i = 0, size = context.getFieldInfoList().size(); i < size; ++i) {
            FieldInfo fieldInfo = context.getFieldInfoList().get(i);

            // public FieldVisitor visitField(final int access, final String name, final String desc, final String
            // signature, final Object value) {
            FieldVisitor fw = cw.visitField(ACC_PUBLIC, fieldInfo.getName() + "_asm_prefix__", "[C");
            fw.visitEnd();
        }

        for (int i = 0, size = context.getFieldInfoList().size(); i < size; ++i) {
            FieldInfo fieldInfo = context.getFieldInfoList().get(i);
            Class<?> fieldClass = fieldInfo.getFieldClass();

            if (fieldClass.isPrimitive()) {
                continue;
            }

            if (fieldClass.isEnum()) {

            } else if (Collection.class.isAssignableFrom(fieldClass)) {
                FieldVisitor fw = cw.visitField(ACC_PUBLIC, fieldInfo.getName() + "_asm_list_item_deser__",
                                                getDesc(ObjectDeserializer.class));
                fw.visitEnd();
            } else {
                FieldVisitor fw = cw.visitField(ACC_PUBLIC, fieldInfo.getName() + "_asm_deser__",
                                                getDesc(ObjectDeserializer.class));
                fw.visitEnd();
            }
        }

        MethodVisitor mw = cw.visitMethod(ACC_PUBLIC, "<init>", "(" + getDesc(ParserConfig.class)
                                                                + getDesc(Class.class) + ")V", null, null);
        mw.visitVarInsn(ALOAD, 0);
        mw.visitVarInsn(ALOAD, 1);
        mw.visitVarInsn(ALOAD, 2);
        mw.visitMethodInsn(INVOKESPECIAL, getType(ASMJavaBeanDeserializer.class), "<init>",
                           "(" + getDesc(ParserConfig.class) + getDesc(Class.class) + ")V");

        mw.visitVarInsn(ALOAD, 0);
        mw.visitFieldInsn(GETFIELD, getType(ASMJavaBeanDeserializer.class), "serializer",
                          getDesc(InnerJavaBeanDeserializer.class));
        mw.visitMethodInsn(INVOKEVIRTUAL, getType(JavaBeanDeserializer.class), "getFieldDeserializerMap",
                           "()" + getDesc(Map.class));
        mw.visitInsn(POP);

        // init fieldNamePrefix
        for (int i = 0, size = context.getFieldInfoList().size(); i < size; ++i) {
            FieldInfo fieldInfo = context.getFieldInfoList().get(i);

            mw.visitVarInsn(ALOAD, 0);
            mw.visitLdcInsn("\"" + fieldInfo.getName() + "\":"); // public char[] toCharArray()
            mw.visitMethodInsn(INVOKEVIRTUAL, getType(String.class), "toCharArray", "()" + getDesc(char[].class));
            mw.visitFieldInsn(PUTFIELD, context.getClassName(), fieldInfo.getName() + "_asm_prefix__", "[C");

        }

        mw.visitInsn(RETURN);
        mw.visitMaxs(4, 4);
        mw.visitEnd();
    }

    private void _createInstance(ClassWriter cw, Context context) {
        MethodVisitor mw = cw.visitMethod(ACC_PUBLIC, "createInstance", "(" + getDesc(DefaultJSONParser.class)
                                                                        + getDesc(Type.class) + ")Ljava/lang/Object;",
                                          null, null);
        mw.visitTypeInsn(NEW, getType(context.getClazz()));
        mw.visitInsn(DUP);
        mw.visitMethodInsn(INVOKESPECIAL, getType(context.getClazz()), "<init>", "()V");
        mw.visitInsn(ARETURN);
        mw.visitMaxs(3, 3);
        mw.visitEnd();
    }

}
