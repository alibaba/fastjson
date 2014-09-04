package com.alibaba.fastjson.parser.deserializer;

import static com.alibaba.fastjson.util.ASMUtils.getDesc;
import static com.alibaba.fastjson.util.ASMUtils.getType;

import java.io.FileOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicLong;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.asm.ASMException;
import com.alibaba.fastjson.asm.ClassWriter;
import com.alibaba.fastjson.asm.FieldVisitor;
import com.alibaba.fastjson.asm.Label;
import com.alibaba.fastjson.asm.MethodVisitor;
import com.alibaba.fastjson.asm.Opcodes;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.DefaultJSONParser.ResolveTask;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONLexerBase;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.ParseContext;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.SymbolTable;
import com.alibaba.fastjson.parser.deserializer.ASMJavaBeanDeserializer.InnerJavaBeanDeserializer;
import com.alibaba.fastjson.util.ASMClassLoader;
import com.alibaba.fastjson.util.ASMUtils;
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

    public boolean isExternalClass(Class<?> clazz) {
        return classLoader.isExternalClass(clazz);
    }

    public ObjectDeserializer createJavaBeanDeserializer(ParserConfig config, Class<?> clazz, Type type)
                                                                                                        throws Exception {
        if (clazz.isPrimitive()) {
            throw new IllegalArgumentException("not support type :" + clazz.getName());
        }

        String className = getGenClassName(clazz);

        ClassWriter cw = new ClassWriter();
        cw.visit(V1_5, ACC_PUBLIC + ACC_SUPER, className, getType(ASMJavaBeanDeserializer.class), null);

        DeserializeBeanInfo beanInfo = DeserializeBeanInfo.computeSetters(clazz, type);

        _init(cw, new Context(className, config, beanInfo, 3));
        _createInstance(cw, new Context(className, config, beanInfo, 3));
        _deserialze(cw, new Context(className, config, beanInfo, 4));
        _deserialzeArrayMapping(cw, new Context(className, config, beanInfo, 4));
        byte[] code = cw.toByteArray();

        if(JSON.dumpClass != null){
            FileOutputStream fos=null;
            try {
                fos=new FileOutputStream(JSON.dumpClass+ File.separator
                        + className + ".class");
                org.apache.commons.io.IOUtils.write(code, fos);
            }catch (Exception ex){
                System.err.println("FASTJSON dump class:"+className+"失败:"+ex.getMessage());
            }finally {
                if(fos!=null){
                    fos.close();
                }
            }
        }

        Class<?> exampleClass = classLoader.defineClassPublic(className, code, 0, code.length);

        Constructor<?> constructor = exampleClass.getConstructor(ParserConfig.class, Class.class);
        Object instance = constructor.newInstance(config, clazz);

        return (ObjectDeserializer) instance;
    }

    void _setFlag(MethodVisitor mw, Context context, int i) {
        String varName = "_asm_flag_" + (i / 32);

        mw.visitVarInsn(ILOAD, context.var(varName));
        mw.visitLdcInsn(1 << i);
        mw.visitInsn(IOR);
        mw.visitVarInsn(ISTORE, context.var(varName));
    }

    void _isFlag(MethodVisitor mw, Context context, int i, Label label) {
        mw.visitVarInsn(ILOAD, context.var("_asm_flag_" + (i / 32)));
        mw.visitLdcInsn(1 << i);
        mw.visitInsn(IAND);

        mw.visitJumpInsn(IFEQ, label);
    }

    void _deserialzeArrayMapping(ClassWriter cw, Context context) {
        MethodVisitor mw = cw.visitMethod(ACC_PUBLIC, "deserialzeArrayMapping",
                                          "(" + getDesc(DefaultJSONParser.class) + getDesc(Type.class)
                                                  + "Ljava/lang/Object;)Ljava/lang/Object;", null, null);

        defineVarLexer(context, mw);

        _createInstance(context, mw);

        List<FieldInfo> sortedFieldInfoList = context.getBeanInfo().getSortedFieldList();
        int fieldListSize = sortedFieldInfoList.size();
        for (int i = 0; i < fieldListSize; ++i) {
            final boolean last = (i == fieldListSize - 1);
            final char seperator = last ? ']' : ',';

            FieldInfo fieldInfo = sortedFieldInfoList.get(i);
            Class<?> fieldClass = fieldInfo.getFieldClass();
            Type fieldType = fieldInfo.getFieldType();
            if (fieldClass == byte.class //
                || fieldClass == short.class //
                || fieldClass == int.class) {
                mw.visitVarInsn(ALOAD, context.var("lexer"));
                mw.visitVarInsn(BIPUSH, seperator);
                mw.visitMethodInsn(INVOKEVIRTUAL, getType(JSONLexerBase.class), "scanInt", "(C)I");
                mw.visitVarInsn(ISTORE, context.var(fieldInfo.getName() + "_asm"));
            } else if (fieldClass == long.class) {
                mw.visitVarInsn(ALOAD, context.var("lexer"));
                mw.visitVarInsn(BIPUSH, seperator);
                mw.visitMethodInsn(INVOKEVIRTUAL, getType(JSONLexerBase.class), "scanLong", "(C)J");
                mw.visitVarInsn(LSTORE, context.var(fieldInfo.getName() + "_asm", 2));
            } else if (fieldClass == boolean.class) {
                mw.visitVarInsn(ALOAD, context.var("lexer"));
                mw.visitVarInsn(BIPUSH, seperator);
                mw.visitMethodInsn(INVOKEVIRTUAL, getType(JSONLexerBase.class), "scanBoolean", "(C)Z");
                mw.visitVarInsn(ISTORE, context.var(fieldInfo.getName() + "_asm"));
            } else if (fieldClass == float.class) {
                mw.visitVarInsn(ALOAD, context.var("lexer"));
                mw.visitVarInsn(BIPUSH, seperator);
                mw.visitMethodInsn(INVOKEVIRTUAL, getType(JSONLexerBase.class), "scanFloat", "(C)F");
                mw.visitVarInsn(FSTORE, context.var(fieldInfo.getName() + "_asm"));
            } else if (fieldClass == double.class) {
                mw.visitVarInsn(ALOAD, context.var("lexer"));
                mw.visitVarInsn(BIPUSH, seperator);
                mw.visitMethodInsn(INVOKEVIRTUAL, getType(JSONLexerBase.class), "scanDouble", "(C)D");
                mw.visitVarInsn(DSTORE, context.var(fieldInfo.getName() + "_asm", 2));
            } else if (fieldClass == char.class) {
                mw.visitVarInsn(ALOAD, context.var("lexer"));
                mw.visitVarInsn(BIPUSH, seperator);
                mw.visitMethodInsn(INVOKEVIRTUAL, getType(JSONLexerBase.class), "scanString", "(C)Ljava/lang/String;");
                mw.visitInsn(ICONST_0);
                mw.visitMethodInsn(INVOKEVIRTUAL, getType(String.class), "charAt", "(I)C");
                mw.visitVarInsn(ISTORE, context.var(fieldInfo.getName() + "_asm"));
            } else if (fieldClass == String.class) {
                mw.visitVarInsn(ALOAD, context.var("lexer"));
                mw.visitVarInsn(BIPUSH, seperator);
                mw.visitMethodInsn(INVOKEVIRTUAL, getType(JSONLexerBase.class), "scanString", "(C)Ljava/lang/String;");
                mw.visitVarInsn(ASTORE, context.var(fieldInfo.getName() + "_asm"));
            } else if (fieldClass.isEnum()) {
                mw.visitVarInsn(ALOAD, context.var("lexer"));
                mw.visitLdcInsn(com.alibaba.fastjson.asm.Type.getType(getDesc(fieldClass)));
                mw.visitVarInsn(ALOAD, 1);
                mw.visitMethodInsn(INVOKEVIRTUAL, getType(DefaultJSONParser.class), "getSymbolTable",
                                   "()" + getDesc(SymbolTable.class));
                mw.visitVarInsn(BIPUSH, seperator);
                mw.visitMethodInsn(INVOKEVIRTUAL, getType(JSONLexerBase.class), "scanEnum", "(Ljava/lang/Class;"
                                                                                        + getDesc(SymbolTable.class)
                                                                                        + "C)Ljava/lang/Enum;");
                mw.visitTypeInsn(CHECKCAST, getType(fieldClass)); // cast
                mw.visitVarInsn(ASTORE, context.var(fieldInfo.getName() + "_asm"));
            } else if (Collection.class.isAssignableFrom(fieldClass)) {
                Class<?> itemClass = getCollectionItemClass(fieldType);
                if (itemClass == String.class) {
                    mw.visitVarInsn(ALOAD, context.var("lexer"));
                    mw.visitLdcInsn(com.alibaba.fastjson.asm.Type.getType(getDesc(fieldClass)));
                    mw.visitVarInsn(BIPUSH, seperator);
                    mw.visitMethodInsn(INVOKEVIRTUAL, getType(JSONLexerBase.class), "scanStringArray",
                                       "(Ljava/lang/Class;C)Ljava/util/Collection;");
                    mw.visitVarInsn(ASTORE, context.var(fieldInfo.getName() + "_asm"));

                } else {
                    mw.visitVarInsn(ALOAD, 1);
                    if (i == 0) {
                        mw.visitFieldInsn(GETSTATIC, getType(JSONToken.class), "LBRACKET", "I");
                    } else {
                        mw.visitFieldInsn(GETSTATIC, getType(JSONToken.class), "COMMA", "I");
                    }
                    mw.visitFieldInsn(GETSTATIC, getType(JSONToken.class), "LBRACKET", "I");
                    mw.visitMethodInsn(INVOKEVIRTUAL, getType(DefaultJSONParser.class), "accept", "(II)V");

                    _newCollection(mw, fieldClass);
                    mw.visitInsn(DUP);
                    mw.visitVarInsn(ASTORE, context.var(fieldInfo.getName() + "_asm"));
                    _getCollectionFieldItemDeser(context, mw, fieldInfo, itemClass);
                    mw.visitVarInsn(ALOAD, 1);
                    mw.visitLdcInsn(com.alibaba.fastjson.asm.Type.getType(getDesc(itemClass)));
                    mw.visitVarInsn(ALOAD, 3);
                    mw.visitMethodInsn(INVOKESTATIC, getType(ASMUtils.class), "parseArray",
                                       "(Ljava/util/Collection;" //
                                               + getDesc(ObjectDeserializer.class) //
                                               + getDesc(DefaultJSONParser.class) //
                                               + "Ljava/lang/reflect/Type;Ljava/lang/Object;)V");
                }

            } else {
                mw.visitVarInsn(ALOAD, 1);
                if (i == 0) {
                    mw.visitFieldInsn(GETSTATIC, getType(JSONToken.class), "LBRACKET", "I");
                } else {
                    mw.visitFieldInsn(GETSTATIC, getType(JSONToken.class), "COMMA", "I");
                }
                mw.visitFieldInsn(GETSTATIC, getType(JSONToken.class), "LBRACKET", "I");
                mw.visitMethodInsn(INVOKEVIRTUAL, getType(DefaultJSONParser.class), "accept", "(II)V");

                _deserObject(context, mw, fieldInfo, fieldClass);

                mw.visitVarInsn(ALOAD, 1);
                if (!last) {
                    mw.visitFieldInsn(GETSTATIC, getType(JSONToken.class), "COMMA", "I");
                    mw.visitFieldInsn(GETSTATIC, getType(JSONToken.class), "LBRACKET", "I");
                } else {
                    mw.visitFieldInsn(GETSTATIC, getType(JSONToken.class), "RBRACKET", "I");
                    mw.visitFieldInsn(GETSTATIC, getType(JSONToken.class), "EOF", "I");
                }
                mw.visitMethodInsn(INVOKEVIRTUAL, getType(DefaultJSONParser.class), "accept", "(II)V");
                continue;
            }
        }

        _batchSet(context, mw, false);

        // lexer.nextToken(JSONToken.COMMA);
        mw.visitVarInsn(ALOAD, context.var("lexer"));
        mw.visitFieldInsn(GETSTATIC, getType(JSONToken.class), "COMMA", "I");
        mw.visitMethodInsn(INVOKEVIRTUAL, getType(JSONLexerBase.class), "nextToken", "(I)V");

        mw.visitVarInsn(ALOAD, context.var("instance"));
        mw.visitInsn(ARETURN);
        mw.visitMaxs(5, context.getVariantCount());
        mw.visitEnd();
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
        Label return_ = new Label();
        Label end_ = new Label();

        defineVarLexer(context, mw);

        _isEnable(context, mw, Feature.SortFeidFastMatch);
        mw.visitJumpInsn(IFEQ, super_);

        {
            Label next_ = new Label();
            mw.visitVarInsn(ALOAD, context.var("lexer"));
            mw.visitMethodInsn(INVOKEVIRTUAL, getType(JSONLexerBase.class), "token", "()I");
            mw.visitFieldInsn(GETSTATIC, getType(JSONToken.class), "LBRACKET", "I");
            mw.visitJumpInsn(IF_ICMPNE, next_);

            mw.visitVarInsn(ALOAD, 0);
            mw.visitVarInsn(ALOAD, 1);
            mw.visitVarInsn(ALOAD, 2);
            mw.visitVarInsn(ALOAD, 3);
            mw.visitMethodInsn(INVOKESPECIAL, context.getClassName(), "deserialzeArrayMapping",
                               "(" + getDesc(DefaultJSONParser.class) + getDesc(Type.class)
                                       + "Ljava/lang/Object;)Ljava/lang/Object;");
            mw.visitInsn(ARETURN);

            mw.visitLabel(next_);
            // deserialzeArrayMapping
        }

        mw.visitVarInsn(ALOAD, context.var("lexer"));
        mw.visitLdcInsn(context.getClazz().getName());
        mw.visitMethodInsn(INVOKEVIRTUAL, getType(JSONLexerBase.class), "scanType", "(Ljava/lang/String;)I");

        mw.visitFieldInsn(GETSTATIC, getType(JSONLexerBase.class), "NOT_MATCH", "I");
        mw.visitJumpInsn(IF_ICMPEQ, super_);

        mw.visitVarInsn(ALOAD, 1); // parser
        mw.visitMethodInsn(INVOKEVIRTUAL, getType(DefaultJSONParser.class), "getContext",
                           "()Lcom/alibaba/fastjson/parser/ParseContext;");
        mw.visitVarInsn(ASTORE, context.var("mark_context"));

        // ParseContext context = parser.getContext();
        mw.visitInsn(ICONST_0);
        mw.visitVarInsn(ISTORE, context.var("matchedCount"));

        _createInstance(context, mw);

        {
            mw.visitVarInsn(ALOAD, 1); // parser
            mw.visitMethodInsn(INVOKEVIRTUAL, getType(DefaultJSONParser.class), "getContext",
                               "()" + ASMUtils.getDesc(ParseContext.class));
            mw.visitVarInsn(ASTORE, context.var("context"));

            mw.visitVarInsn(ALOAD, 1); // parser
            mw.visitVarInsn(ALOAD, context.var("context"));
            mw.visitVarInsn(ALOAD, context.var("instance"));
            mw.visitVarInsn(ALOAD, 3); // fieldName
            mw.visitMethodInsn(INVOKEVIRTUAL, getType(DefaultJSONParser.class), "setContext",
                               "(" + ASMUtils.getDesc(ParseContext.class) + "Ljava/lang/Object;Ljava/lang/Object;)"
                                       + ASMUtils.getDesc(ParseContext.class));
            mw.visitVarInsn(ASTORE, context.var("childContext"));
        }

        mw.visitVarInsn(ALOAD, context.var("lexer"));
        mw.visitFieldInsn(GETFIELD, getType(JSONLexerBase.class), "matchStat", "I");
        mw.visitFieldInsn(GETSTATIC, getType(JSONLexerBase.class), "END", "I");
        mw.visitJumpInsn(IF_ICMPEQ, return_);

        mw.visitInsn(ICONST_0); // UNKOWN
        mw.visitIntInsn(ISTORE, context.var("matchStat"));

        int fieldListSize = context.getFieldInfoList().size();
        for (int i = 0; i < fieldListSize; i += 32) {
            mw.visitInsn(ICONST_0);
            mw.visitVarInsn(ISTORE, context.var("_asm_flag_" + (i / 32)));
        }

        // declare and init
        for (int i = 0; i < fieldListSize; ++i) {
            FieldInfo fieldInfo = context.getFieldInfoList().get(i);
            Class<?> fieldClass = fieldInfo.getFieldClass();

            if (fieldClass == boolean.class //
                || fieldClass == byte.class //
                || fieldClass == short.class //
                || fieldClass == int.class) {
                mw.visitInsn(ICONST_0);
                mw.visitVarInsn(ISTORE, context.var(fieldInfo.getName() + "_asm"));
            } else if (fieldClass == long.class) {
                mw.visitInsn(LCONST_0);
                mw.visitVarInsn(LSTORE, context.var(fieldInfo.getName() + "_asm", 2));
            } else if (fieldClass == float.class) {
                mw.visitInsn(FCONST_0);
                mw.visitVarInsn(FSTORE, context.var(fieldInfo.getName() + "_asm"));
            } else if (fieldClass == double.class) {
                mw.visitInsn(DCONST_0);
                mw.visitVarInsn(DSTORE, context.var(fieldInfo.getName() + "_asm", 2));
            } else {
                if (fieldClass == String.class) {
                    Label flagEnd_ = new Label();
                    _isEnable(context, mw, Feature.InitStringFieldAsEmpty);
                    mw.visitJumpInsn(IFEQ, flagEnd_);
                    _setFlag(mw, context, i);
                    mw.visitLabel(flagEnd_);

                    mw.visitVarInsn(ALOAD, context.var("lexer"));
                    mw.visitMethodInsn(INVOKEVIRTUAL, getType(JSONLexerBase.class), "stringDefaultValue",
                                       "()Ljava/lang/String;");
                } else {
                    mw.visitInsn(ACONST_NULL);
                }

                mw.visitTypeInsn(CHECKCAST, getType(fieldClass)); // cast
                mw.visitVarInsn(ASTORE, context.var(fieldInfo.getName() + "_asm"));
            }
        }

        for (int i = 0; i < fieldListSize; ++i) {
            FieldInfo fieldInfo = context.getFieldInfoList().get(i);
            Class<?> fieldClass = fieldInfo.getFieldClass();
            Type fieldType = fieldInfo.getFieldType();

            Label notMatch_ = new Label();

            if (fieldClass == boolean.class) {
                mw.visitVarInsn(ALOAD, context.var("lexer"));
                mw.visitVarInsn(ALOAD, 0);
                mw.visitFieldInsn(GETFIELD, context.getClassName(), fieldInfo.getName() + "_asm_prefix__", "[C");
                mw.visitMethodInsn(INVOKEVIRTUAL, getType(JSONLexerBase.class), "scanFieldBoolean", "([C)Z");
                mw.visitVarInsn(ISTORE, context.var(fieldInfo.getName() + "_asm"));
            } else if (fieldClass == byte.class) {
                mw.visitVarInsn(ALOAD, context.var("lexer"));
                mw.visitVarInsn(ALOAD, 0);
                mw.visitFieldInsn(GETFIELD, context.getClassName(), fieldInfo.getName() + "_asm_prefix__", "[C");
                mw.visitMethodInsn(INVOKEVIRTUAL, getType(JSONLexerBase.class), "scanFieldInt", "([C)I");
                mw.visitVarInsn(ISTORE, context.var(fieldInfo.getName() + "_asm"));

            } else if (fieldClass == short.class) {
                mw.visitVarInsn(ALOAD, context.var("lexer"));
                mw.visitVarInsn(ALOAD, 0);
                mw.visitFieldInsn(GETFIELD, context.getClassName(), fieldInfo.getName() + "_asm_prefix__", "[C");
                mw.visitMethodInsn(INVOKEVIRTUAL, getType(JSONLexerBase.class), "scanFieldInt", "([C)I");
                mw.visitVarInsn(ISTORE, context.var(fieldInfo.getName() + "_asm"));

            } else if (fieldClass == int.class) {
                mw.visitVarInsn(ALOAD, context.var("lexer"));
                mw.visitVarInsn(ALOAD, 0);
                mw.visitFieldInsn(GETFIELD, context.getClassName(), fieldInfo.getName() + "_asm_prefix__", "[C");
                mw.visitMethodInsn(INVOKEVIRTUAL, getType(JSONLexerBase.class), "scanFieldInt", "([C)I");
                mw.visitVarInsn(ISTORE, context.var(fieldInfo.getName() + "_asm"));

            } else if (fieldClass == long.class) {
                mw.visitVarInsn(ALOAD, context.var("lexer"));
                mw.visitVarInsn(ALOAD, 0);
                mw.visitFieldInsn(GETFIELD, context.getClassName(), fieldInfo.getName() + "_asm_prefix__", "[C");
                mw.visitMethodInsn(INVOKEVIRTUAL, getType(JSONLexerBase.class), "scanFieldLong", "([C)J");
                mw.visitVarInsn(LSTORE, context.var(fieldInfo.getName() + "_asm", 2));

            } else if (fieldClass == float.class) {
                mw.visitVarInsn(ALOAD, context.var("lexer"));
                mw.visitVarInsn(ALOAD, 0);
                mw.visitFieldInsn(GETFIELD, context.getClassName(), fieldInfo.getName() + "_asm_prefix__", "[C");
                mw.visitMethodInsn(INVOKEVIRTUAL, getType(JSONLexerBase.class), "scanFieldFloat", "([C)F");
                mw.visitVarInsn(FSTORE, context.var(fieldInfo.getName() + "_asm"));

            } else if (fieldClass == double.class) {
                mw.visitVarInsn(ALOAD, context.var("lexer"));
                mw.visitVarInsn(ALOAD, 0);
                mw.visitFieldInsn(GETFIELD, context.getClassName(), fieldInfo.getName() + "_asm_prefix__", "[C");
                mw.visitMethodInsn(INVOKEVIRTUAL, getType(JSONLexerBase.class), "scanFieldDouble", "([C)D");
                mw.visitVarInsn(DSTORE, context.var(fieldInfo.getName() + "_asm", 2));

            } else if (fieldClass == String.class) {
                Label notEnd_ = new Label();

                mw.visitIntInsn(ILOAD, context.var("matchStat"));
                mw.visitInsn(ICONST_4); // END
                mw.visitJumpInsn(IF_ICMPNE, notEnd_);

                mw.visitVarInsn(ALOAD, context.var("lexer"));
                mw.visitMethodInsn(INVOKEVIRTUAL, getType(JSONLexerBase.class), "stringDefaultValue",
                                   "()Ljava/lang/String;");
                mw.visitVarInsn(ASTORE, context.var(fieldInfo.getName() + "_asm"));
                mw.visitJumpInsn(GOTO, notMatch_);

                mw.visitLabel(notEnd_);

                mw.visitVarInsn(ALOAD, context.var("lexer"));
                mw.visitVarInsn(ALOAD, 0);
                mw.visitFieldInsn(GETFIELD, context.getClassName(), fieldInfo.getName() + "_asm_prefix__", "[C");
                mw.visitMethodInsn(INVOKEVIRTUAL, getType(JSONLexerBase.class), "scanFieldString", "([C)Ljava/lang/String;");
                mw.visitVarInsn(ASTORE, context.var(fieldInfo.getName() + "_asm"));

            } else if (fieldClass.isEnum()) {
                mw.visitVarInsn(ALOAD, context.var("lexer"));
                mw.visitVarInsn(ALOAD, 0);
                mw.visitFieldInsn(GETFIELD, context.getClassName(), fieldInfo.getName() + "_asm_prefix__", "[C");
                Label enumNull_ = new Label();
                mw.visitInsn(ACONST_NULL);
                mw.visitTypeInsn(CHECKCAST, getType(fieldClass)); // cast
                mw.visitVarInsn(ASTORE, context.var(fieldInfo.getName() + "_asm"));

                mw.visitVarInsn(ALOAD, 1);
                mw.visitMethodInsn(INVOKEVIRTUAL, getType(DefaultJSONParser.class), "getSymbolTable",
                                   "()" + getDesc(SymbolTable.class));

                mw.visitMethodInsn(INVOKEVIRTUAL, getType(JSONLexerBase.class), "scanFieldSymbol",
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
                mw.visitVarInsn(ALOAD, context.var("lexer"));
                mw.visitVarInsn(ALOAD, 0);
                mw.visitFieldInsn(GETFIELD, context.getClassName(), fieldInfo.getName() + "_asm_prefix__", "[C");

                Class<?> itemClass = getCollectionItemClass(fieldType);

                if (itemClass == String.class) {
                    mw.visitLdcInsn(com.alibaba.fastjson.asm.Type.getType(getDesc(fieldClass))); // cast
                    mw.visitMethodInsn(INVOKEVIRTUAL, getType(JSONLexerBase.class), "scanFieldStringArray",
                                       "([CLjava/lang/Class;)" + getDesc(Collection.class));
                    mw.visitVarInsn(ASTORE, context.var(fieldInfo.getName() + "_asm"));
                } else {
                    _deserialze_list_obj(context, mw, reset_, fieldInfo, fieldClass, itemClass, i);

                    if (i == fieldListSize - 1) {
                        _deserialize_endCheck(context, mw, reset_);
                    }
                    continue;
                }
            } else {
                _deserialze_obj(context, mw, reset_, fieldInfo, fieldClass, i);

                if (i == fieldListSize - 1) {
                    _deserialize_endCheck(context, mw, reset_);
                }

                continue;
            }

            mw.visitVarInsn(ALOAD, context.var("lexer"));
            mw.visitFieldInsn(GETFIELD, getType(JSONLexerBase.class), "matchStat", "I");
            Label flag_ = new Label();
            // mw.visitInsn(DUP);
            mw.visitJumpInsn(IFLE, flag_);
            _setFlag(mw, context, i);
            mw.visitLabel(flag_);

            mw.visitVarInsn(ALOAD, context.var("lexer"));
            mw.visitFieldInsn(GETFIELD, getType(JSONLexerBase.class), "matchStat", "I");
            mw.visitInsn(DUP);
            mw.visitVarInsn(ISTORE, context.var("matchStat"));

            mw.visitFieldInsn(GETSTATIC, getType(JSONLexerBase.class), "NOT_MATCH", "I");
            mw.visitJumpInsn(IF_ICMPEQ, reset_);

            // mw.visitFieldInsn(GETSTATIC, getType(System.class), "out", "Ljava/io/PrintStream;");
            // mw.visitVarInsn(ALOAD, context.var("lexer"));
            // mw.visitFieldInsn(GETFIELD, getType(JSONLexerBase.class), "matchStat", "I");
            // mw.visitMethodInsn(INVOKEVIRTUAL, getType(java.io.PrintStream.class), "println", "(I)V");

            mw.visitVarInsn(ALOAD, context.var("lexer"));
            mw.visitFieldInsn(GETFIELD, getType(JSONLexerBase.class), "matchStat", "I");
            mw.visitJumpInsn(IFLE, notMatch_);

            // increment matchedCount
            mw.visitVarInsn(ILOAD, context.var("matchedCount"));
            mw.visitInsn(ICONST_1);
            mw.visitInsn(IADD);
            mw.visitVarInsn(ISTORE, context.var("matchedCount"));

            mw.visitVarInsn(ALOAD, context.var("lexer"));
            mw.visitFieldInsn(GETFIELD, getType(JSONLexerBase.class), "matchStat", "I");
            mw.visitFieldInsn(GETSTATIC, getType(JSONLexerBase.class), "END", "I");
            mw.visitJumpInsn(IF_ICMPEQ, end_);

            // mw.visitFieldInsn(GETSTATIC, getType(System.class), "out", "Ljava/io/PrintStream;");
            // mw.visitVarInsn(ILOAD, context.var("matchedCount"));
            // mw.visitMethodInsn(INVOKEVIRTUAL, getType(java.io.PrintStream.class), "println", "(I)V");

            mw.visitLabel(notMatch_);

            if (i == fieldListSize - 1) {
                mw.visitVarInsn(ALOAD, context.var("lexer"));
                mw.visitFieldInsn(GETFIELD, getType(JSONLexerBase.class), "matchStat", "I");
                mw.visitFieldInsn(GETSTATIC, getType(JSONLexerBase.class), "END", "I");
                mw.visitJumpInsn(IF_ICMPNE, reset_);
            }
        } // endFor

        mw.visitLabel(end_);

        if (!context.getClazz().isInterface() && !Modifier.isAbstract(context.getClazz().getModifiers())) {
            _batchSet(context, mw);
        }

        mw.visitLabel(return_);

        _setContext(context, mw);
        mw.visitVarInsn(ALOAD, context.var("instance"));
        mw.visitInsn(ARETURN);

        mw.visitLabel(reset_);

        _batchSet(context, mw);
        mw.visitVarInsn(ALOAD, 0);
        mw.visitVarInsn(ALOAD, 1);
        mw.visitVarInsn(ALOAD, 2);
        mw.visitVarInsn(ALOAD, 3);
        mw.visitVarInsn(ALOAD, context.var("instance"));
        mw.visitMethodInsn(INVOKEVIRTUAL, getType(ASMJavaBeanDeserializer.class), "parseRest",
                           "(" + getDesc(DefaultJSONParser.class) + getDesc(Type.class)
                                   + "Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;");
        mw.visitTypeInsn(CHECKCAST, getType(context.getClazz())); // cast
        mw.visitInsn(ARETURN);

        mw.visitLabel(super_);
        mw.visitVarInsn(ALOAD, 0);
        mw.visitVarInsn(ALOAD, 1);
        mw.visitVarInsn(ALOAD, 2);
        mw.visitVarInsn(ALOAD, 3);
        mw.visitMethodInsn(INVOKESPECIAL, getType(ASMJavaBeanDeserializer.class), "deserialze",
                           "(" + getDesc(DefaultJSONParser.class) + getDesc(Type.class)
                                   + "Ljava/lang/Object;)Ljava/lang/Object;");
        mw.visitInsn(ARETURN);

        mw.visitMaxs(5, context.getVariantCount());
        mw.visitEnd();
    }

    private Class<?> getCollectionItemClass(Type fieldType) {
        if (fieldType instanceof ParameterizedType) {
            Class<?> itemClass;
            Type actualTypeArgument = ((ParameterizedType) fieldType).getActualTypeArguments()[0];

            if (actualTypeArgument instanceof Class) {
                itemClass = (Class<?>) actualTypeArgument;
                if (!Modifier.isPublic(itemClass.getModifiers())) {
                    throw new ASMException("can not create ASMParser");
                }
            } else {
                throw new ASMException("can not create ASMParser");
            }
            return itemClass;
        }

        return Object.class;
    }

    private void _isEnable(Context context, MethodVisitor mw, Feature feature) {
        mw.visitVarInsn(ALOAD, context.var("lexer"));
        mw.visitFieldInsn(GETSTATIC, getType(Feature.class), feature.name(), "L" + getType(Feature.class) + ";");
        mw.visitMethodInsn(INVOKEVIRTUAL, getType(JSONLexerBase.class), "isEnabled", "(" + "L" + getType(Feature.class)
                                                                                 + ";" + ")Z");
    }

    private void defineVarLexer(Context context, MethodVisitor mw) {
        mw.visitVarInsn(ALOAD, 1);
        mw.visitMethodInsn(INVOKEVIRTUAL, getType(DefaultJSONParser.class), "getLexer", "()" + getDesc(JSONLexer.class));
        mw.visitTypeInsn(CHECKCAST, getType(JSONLexerBase.class)); // cast
        mw.visitVarInsn(ASTORE, context.var("lexer"));
    }

    private void _createInstance(Context context, MethodVisitor mw) {
        Constructor<?> defaultConstructor = context.getBeanInfo().getDefaultConstructor();
        if (Modifier.isPublic(defaultConstructor.getModifiers())) {
            mw.visitTypeInsn(NEW, getType(context.getClazz()));
            mw.visitInsn(DUP);
            mw.visitMethodInsn(INVOKESPECIAL, getType(context.getClazz()), "<init>", "()V");

            mw.visitVarInsn(ASTORE, context.var("instance"));
        } else {
            mw.visitVarInsn(ALOAD, 0);
            mw.visitVarInsn(ALOAD, 1);
            mw.visitMethodInsn(INVOKESPECIAL, getType(ASMJavaBeanDeserializer.class), "createInstance",
                               "(" + getDesc(DefaultJSONParser.class) + ")Ljava/lang/Object;");
            mw.visitTypeInsn(CHECKCAST, getType(context.getClazz())); // cast
            mw.visitVarInsn(ASTORE, context.var("instance"));
        }
    }

    private void _batchSet(Context context, MethodVisitor mw) {
        _batchSet(context, mw, true);
    }

    private void _batchSet(Context context, MethodVisitor mw, boolean flag) {
        for (int i = 0, size = context.getFieldInfoList().size(); i < size; ++i) {
            Label notSet_ = new Label();

            if (flag) {
                _isFlag(mw, context, i, notSet_);
            }

            FieldInfo fieldInfo = context.getFieldInfoList().get(i);
            _loadAndSet(context, mw, fieldInfo);

            if (flag) {
                mw.visitLabel(notSet_);
            }
        }
    }

    private void _loadAndSet(Context context, MethodVisitor mw, FieldInfo fieldInfo) {
        Class<?> fieldClass = fieldInfo.getFieldClass();
        Type fieldType = fieldInfo.getFieldType();

        if (fieldClass == boolean.class) {
            mw.visitVarInsn(ALOAD, context.var("instance"));
            mw.visitVarInsn(ILOAD, context.var(fieldInfo.getName() + "_asm"));
            _set(context, mw, fieldInfo);
        } else if (fieldClass == byte.class //
                   || fieldClass == short.class //
                   || fieldClass == int.class //
                   || fieldClass == char.class) {
            mw.visitVarInsn(ALOAD, context.var("instance"));
            mw.visitVarInsn(ILOAD, context.var(fieldInfo.getName() + "_asm"));
            _set(context, mw, fieldInfo);
        } else if (fieldClass == long.class) {
            mw.visitVarInsn(ALOAD, context.var("instance"));
            mw.visitVarInsn(LLOAD, context.var(fieldInfo.getName() + "_asm", 2));
            if (fieldInfo.getMethod() != null) {
                mw.visitMethodInsn(INVOKEVIRTUAL, getType(context.getClazz()), fieldInfo.getMethod().getName(), getDesc(fieldInfo.getMethod()));
                if (!fieldInfo.getMethod().getReturnType().equals(Void.TYPE)) {
                    mw.visitInsn(POP);
                }
            } else {
                mw.visitFieldInsn(PUTFIELD, getType(fieldInfo.getDeclaringClass()), fieldInfo.getField().getName(),
                                  getDesc(fieldInfo.getFieldClass()));
            }
        } else if (fieldClass == float.class) {
            mw.visitVarInsn(ALOAD, context.var("instance"));
            mw.visitVarInsn(FLOAD, context.var(fieldInfo.getName() + "_asm"));
            _set(context, mw, fieldInfo);
        } else if (fieldClass == double.class) {
            mw.visitVarInsn(ALOAD, context.var("instance"));
            mw.visitVarInsn(DLOAD, context.var(fieldInfo.getName() + "_asm", 2));
            _set(context, mw, fieldInfo);
        } else if (fieldClass == String.class) {
            mw.visitVarInsn(ALOAD, context.var("instance"));
            mw.visitVarInsn(ALOAD, context.var(fieldInfo.getName() + "_asm"));
            _set(context, mw, fieldInfo);
        } else if (fieldClass.isEnum()) {
            mw.visitVarInsn(ALOAD, context.var("instance"));
            mw.visitVarInsn(ALOAD, context.var(fieldInfo.getName() + "_asm"));
            _set(context, mw, fieldInfo);
        } else if (Collection.class.isAssignableFrom(fieldClass)) {
            mw.visitVarInsn(ALOAD, context.var("instance"));
            Type itemType = getCollectionItemClass(fieldType);
            if (itemType == String.class) {
                mw.visitVarInsn(ALOAD, context.var(fieldInfo.getName() + "_asm"));
                mw.visitTypeInsn(CHECKCAST, getType(fieldClass)); // cast
            } else {
                mw.visitVarInsn(ALOAD, context.var(fieldInfo.getName() + "_asm"));
            }
            _set(context, mw, fieldInfo);

        } else {
            // mw.visitFieldInsn(GETSTATIC, getType(System.class), "out", "Ljava/io/PrintStream;");
            // mw.visitIntInsn(ILOAD, context.var(fieldInfo.getName() + "_asm_flag"));
            // mw.visitMethodInsn(INVOKEVIRTUAL, getType(java.io.PrintStream.class), "println", "(I)V");

            // _isFlag(mw, context, i, notSet_);

            mw.visitVarInsn(ALOAD, context.var("instance"));
            mw.visitVarInsn(ALOAD, context.var(fieldInfo.getName() + "_asm"));
            _set(context, mw, fieldInfo);

        }
    }

    private void _set(Context context, MethodVisitor mw, FieldInfo fieldInfo) {
        if (fieldInfo.getMethod() != null) {
            mw.visitMethodInsn(INVOKEVIRTUAL, getType(fieldInfo.getDeclaringClass()), fieldInfo.getMethod().getName(),
                               getDesc(fieldInfo.getMethod()));

            if (!fieldInfo.getMethod().getReturnType().equals(Void.TYPE)) {
                mw.visitInsn(POP);
            }
        } else {
            mw.visitFieldInsn(PUTFIELD, getType(fieldInfo.getDeclaringClass()), fieldInfo.getField().getName(),
                              getDesc(fieldInfo.getFieldClass()));
        }
    }

    private void _setContext(Context context, MethodVisitor mw) {
        mw.visitVarInsn(ALOAD, 1); // parser
        mw.visitVarInsn(ALOAD, context.var("context"));
        mw.visitMethodInsn(INVOKEVIRTUAL, getType(DefaultJSONParser.class), "setContext",
                           "(" + ASMUtils.getDesc(ParseContext.class) + ")V");

        Label endIf_ = new Label();
        mw.visitVarInsn(ALOAD, context.var("childContext"));
        mw.visitJumpInsn(IFNULL, endIf_);

        mw.visitVarInsn(ALOAD, context.var("childContext"));
        mw.visitVarInsn(ALOAD, context.var("instance"));
        mw.visitMethodInsn(INVOKEVIRTUAL, getType(ParseContext.class), "setObject", "(Ljava/lang/Object;)V");

        mw.visitLabel(endIf_);
    }

    private void _deserialize_endCheck(Context context, MethodVisitor mw, Label reset_) {
        Label _end_if = new Label();
        // Label nextToken_ = new Label();

        // mw.visitFieldInsn(GETSTATIC, getType(System.class), "out", "Ljava/io/PrintStream;");
        // mw.visitIntInsn(ILOAD, context.var("matchedCount"));
        // mw.visitMethodInsn(INVOKEVIRTUAL, getType(java.io.PrintStream.class), "println", "(I)V");

        mw.visitIntInsn(ILOAD, context.var("matchedCount"));
        mw.visitJumpInsn(IFLE, reset_);

        // mw.visitFieldInsn(GETSTATIC, getType(System.class), "out", "Ljava/io/PrintStream;");
        // mw.visitVarInsn(ALOAD, context.var("lexer"));
        // mw.visitMethodInsn(INVOKEVIRTUAL, getType(JSONLexerBase.class), "token", "()I");
        // mw.visitMethodInsn(INVOKEVIRTUAL, getType(java.io.PrintStream.class), "println", "(I)V");

        mw.visitVarInsn(ALOAD, context.var("lexer"));
        mw.visitMethodInsn(INVOKEVIRTUAL, getType(JSONLexerBase.class), "token", "()I");
        mw.visitFieldInsn(GETSTATIC, getType(JSONToken.class), "RBRACE", "I");
        mw.visitJumpInsn(IF_ICMPNE, reset_);

        // mw.visitLabel(nextToken_);
        mw.visitVarInsn(ALOAD, context.var("lexer"));
        mw.visitFieldInsn(GETSTATIC, getType(JSONToken.class), "COMMA", "I");
        mw.visitMethodInsn(INVOKEVIRTUAL, getType(JSONLexerBase.class), "nextToken", "(I)V");

        mw.visitLabel(_end_if);
    }

    private void _deserialze_list_obj(Context context, MethodVisitor mw, Label reset_, FieldInfo fieldInfo,
                                      Class<?> fieldClass, Class<?> itemType, int i) {
        Label matched_ = new Label();
        Label _end_if = new Label();

        mw.visitMethodInsn(INVOKEVIRTUAL, getType(JSONLexerBase.class), "matchField", "([C)Z");
        mw.visitJumpInsn(IFNE, matched_);
        mw.visitInsn(ACONST_NULL);
        mw.visitVarInsn(ASTORE, context.var(fieldInfo.getName() + "_asm"));

        mw.visitJumpInsn(GOTO, _end_if);

        mw.visitLabel(matched_);
        _setFlag(mw, context, i);

        Label valueNotNull_ = new Label();
        mw.visitVarInsn(ALOAD, context.var("lexer"));
        mw.visitMethodInsn(INVOKEVIRTUAL, getType(JSONLexerBase.class), "token", "()I");
        mw.visitFieldInsn(GETSTATIC, getType(JSONToken.class), "NULL", "I");
        mw.visitJumpInsn(IF_ICMPNE, valueNotNull_);

        mw.visitVarInsn(ALOAD, context.var("lexer"));
        mw.visitFieldInsn(GETSTATIC, getType(JSONToken.class), "COMMA", "I");
        mw.visitMethodInsn(INVOKEVIRTUAL, getType(JSONLexerBase.class), "nextToken", "(I)V");

        mw.visitInsn(ACONST_NULL);
        mw.visitTypeInsn(CHECKCAST, getType(fieldClass)); // cast
        mw.visitVarInsn(ASTORE, context.var(fieldInfo.getName() + "_asm"));
        // loop_end_

        mw.visitLabel(valueNotNull_);
        // if (lexer.token() != JSONToken.LBRACKET) reset
        mw.visitVarInsn(ALOAD, context.var("lexer"));
        mw.visitMethodInsn(INVOKEVIRTUAL, getType(JSONLexerBase.class), "token", "()I");
        mw.visitFieldInsn(GETSTATIC, getType(JSONToken.class), "LBRACKET", "I");

        mw.visitJumpInsn(IF_ICMPNE, reset_);

        _getCollectionFieldItemDeser(context, mw, fieldInfo, itemType);
        mw.visitMethodInsn(INVOKEINTERFACE, getType(ObjectDeserializer.class), "getFastMatchToken", "()I");
        mw.visitVarInsn(ISTORE, context.var("fastMatchToken"));

        mw.visitVarInsn(ALOAD, context.var("lexer"));
        mw.visitVarInsn(ILOAD, context.var("fastMatchToken"));
        mw.visitMethodInsn(INVOKEVIRTUAL, getType(JSONLexerBase.class), "nextToken", "(I)V");

        _newCollection(mw, fieldClass);

        mw.visitVarInsn(ASTORE, context.var(fieldInfo.getName() + "_asm"));

        { // setContext
            mw.visitVarInsn(ALOAD, 1);
            mw.visitMethodInsn(INVOKEVIRTUAL, getType(DefaultJSONParser.class), "getContext",
                               "()" + ASMUtils.getDesc(ParseContext.class));
            mw.visitVarInsn(ASTORE, context.var("listContext"));

            mw.visitVarInsn(ALOAD, 1); // parser
            mw.visitVarInsn(ALOAD, context.var(fieldInfo.getName() + "_asm"));
            mw.visitLdcInsn(fieldInfo.getName());
            mw.visitMethodInsn(INVOKEVIRTUAL, getType(DefaultJSONParser.class), "setContext",
                               "(Ljava/lang/Object;Ljava/lang/Object;)" + ASMUtils.getDesc(ParseContext.class));
            mw.visitInsn(POP);
        }

        Label loop_ = new Label();
        Label loop_end_ = new Label();

        // for (;;) {
        mw.visitInsn(ICONST_0);
        mw.visitVarInsn(ISTORE, context.var("i"));
        mw.visitLabel(loop_);

        mw.visitVarInsn(ALOAD, context.var("lexer"));
        mw.visitMethodInsn(INVOKEVIRTUAL, getType(JSONLexerBase.class), "token", "()I");
        mw.visitFieldInsn(GETSTATIC, getType(JSONToken.class), "RBRACKET", "I");
        mw.visitJumpInsn(IF_ICMPEQ, loop_end_);

        // Object value = itemDeserializer.deserialze(parser, null);
        // array.add(value);

        mw.visitVarInsn(ALOAD, 0);
        mw.visitFieldInsn(GETFIELD, context.getClassName(), fieldInfo.getName() + "_asm_list_item_deser__",
                          getDesc(ObjectDeserializer.class));
        mw.visitVarInsn(ALOAD, 1);
        mw.visitLdcInsn(com.alibaba.fastjson.asm.Type.getType(getDesc(itemType)));
        mw.visitVarInsn(ILOAD, context.var("i"));
        mw.visitMethodInsn(INVOKESTATIC, getType(Integer.class), "valueOf", "(I)Ljava/lang/Integer;");
        mw.visitMethodInsn(INVOKEINTERFACE, getType(ObjectDeserializer.class), "deserialze",
                           "(" + ASMUtils.getDesc(DefaultJSONParser.class)
                                   + "Ljava/lang/reflect/Type;Ljava/lang/Object;)Ljava/lang/Object;");
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

        mw.visitVarInsn(ALOAD, 1);
        mw.visitVarInsn(ALOAD, context.var(fieldInfo.getName() + "_asm"));
        mw.visitMethodInsn(INVOKEVIRTUAL, getType(DefaultJSONParser.class), "checkListResolve",
                           "(Ljava/util/Collection;)V");

        // if (lexer.token() == JSONToken.COMMA) {
        // lexer.nextToken(itemDeserializer.getFastMatchToken());
        // continue;
        // }
        mw.visitVarInsn(ALOAD, context.var("lexer"));
        mw.visitMethodInsn(INVOKEVIRTUAL, getType(JSONLexerBase.class), "token", "()I");
        mw.visitFieldInsn(GETSTATIC, getType(JSONToken.class), "COMMA", "I");
        mw.visitJumpInsn(IF_ICMPNE, loop_);

        mw.visitVarInsn(ALOAD, context.var("lexer"));
        mw.visitVarInsn(ILOAD, context.var("fastMatchToken"));
        mw.visitMethodInsn(INVOKEVIRTUAL, getType(JSONLexerBase.class), "nextToken", "(I)V");
        mw.visitJumpInsn(GOTO, loop_);

        mw.visitLabel(loop_end_);

        // mw.visitVarInsn(ASTORE, context.var("context"));
        // parser.setContext(context);
        { // setContext
            mw.visitVarInsn(ALOAD, 1); // parser
            mw.visitVarInsn(ALOAD, context.var("listContext"));
            mw.visitMethodInsn(INVOKEVIRTUAL, getType(DefaultJSONParser.class), "setContext",
                               "(" + ASMUtils.getDesc(ParseContext.class) + ")V");
        }

        mw.visitVarInsn(ALOAD, context.var("lexer"));
        mw.visitMethodInsn(INVOKEVIRTUAL, getType(JSONLexerBase.class), "token", "()I");
        mw.visitFieldInsn(GETSTATIC, getType(JSONToken.class), "RBRACKET", "I");
        mw.visitJumpInsn(IF_ICMPNE, reset_);

        mw.visitVarInsn(ALOAD, context.var("lexer"));
        mw.visitFieldInsn(GETSTATIC, getType(JSONToken.class), "COMMA", "I");
        mw.visitMethodInsn(INVOKEVIRTUAL, getType(JSONLexerBase.class), "nextToken", "(I)V");
        // lexer.nextToken(JSONToken.COMMA);

        mw.visitLabel(_end_if);
    }

    private void _getCollectionFieldItemDeser(Context context, MethodVisitor mw, FieldInfo fieldInfo, Class<?> itemType) {
        Label notNull_ = new Label();
        mw.visitVarInsn(ALOAD, 0);
        mw.visitFieldInsn(GETFIELD, context.getClassName(), fieldInfo.getName() + "_asm_list_item_deser__",
                          getDesc(ObjectDeserializer.class));
        mw.visitJumpInsn(IFNONNULL, notNull_);

        mw.visitVarInsn(ALOAD, 0);

        mw.visitVarInsn(ALOAD, 1);
        mw.visitMethodInsn(INVOKEVIRTUAL, getType(DefaultJSONParser.class), "getConfig", "()"
                                                                                         + getDesc(ParserConfig.class));
        mw.visitLdcInsn(com.alibaba.fastjson.asm.Type.getType(getDesc(itemType)));
        mw.visitMethodInsn(INVOKEVIRTUAL, getType(ParserConfig.class), "getDeserializer",
                           "(" + getDesc(Type.class) + ")" + getDesc(ObjectDeserializer.class));

        mw.visitFieldInsn(PUTFIELD, context.getClassName(), fieldInfo.getName() + "_asm_list_item_deser__",
                          getDesc(ObjectDeserializer.class));

        mw.visitLabel(notNull_);
        mw.visitVarInsn(ALOAD, 0);
        mw.visitFieldInsn(GETFIELD, context.getClassName(), fieldInfo.getName() + "_asm_list_item_deser__",
                          getDesc(ObjectDeserializer.class));
    }

    private void _newCollection(MethodVisitor mw, Class<?> fieldClass) {
        if (fieldClass.isAssignableFrom(ArrayList.class)) {
            mw.visitTypeInsn(NEW, getType(ArrayList.class));
            mw.visitInsn(DUP);
            mw.visitMethodInsn(INVOKESPECIAL, getType(ArrayList.class), "<init>", "()V");
        } else if (fieldClass.isAssignableFrom(LinkedList.class)) {
            mw.visitTypeInsn(NEW, getType(LinkedList.class));
            mw.visitInsn(DUP);
            mw.visitMethodInsn(INVOKESPECIAL, getType(LinkedList.class), "<init>", "()V");
        } else if (fieldClass.isAssignableFrom(HashSet.class)) {
            mw.visitTypeInsn(NEW, getType(HashSet.class));
            mw.visitInsn(DUP);
            mw.visitMethodInsn(INVOKESPECIAL, getType(HashSet.class), "<init>", "()V");
        } else if (fieldClass.isAssignableFrom(TreeSet.class)) {
            mw.visitTypeInsn(NEW, getType(TreeSet.class));
            mw.visitInsn(DUP);
            mw.visitMethodInsn(INVOKESPECIAL, getType(TreeSet.class), "<init>", "()V");
        } else {
            mw.visitTypeInsn(NEW, getType(fieldClass));
            mw.visitInsn(DUP);
            mw.visitMethodInsn(INVOKESPECIAL, getType(fieldClass), "<init>", "()V");
        }
        mw.visitTypeInsn(CHECKCAST, getType(fieldClass)); // cast
    }

    private void _deserialze_obj(Context context, MethodVisitor mw, Label reset_, FieldInfo fieldInfo,
                                 Class<?> fieldClass, int i) {
        Label matched_ = new Label();
        Label _end_if = new Label();

        mw.visitVarInsn(ALOAD, context.var("lexer"));
        mw.visitVarInsn(ALOAD, 0);
        mw.visitFieldInsn(GETFIELD, context.getClassName(), fieldInfo.getName() + "_asm_prefix__", "[C");
        mw.visitMethodInsn(INVOKEVIRTUAL, getType(JSONLexerBase.class), "matchField", "([C)Z");
        mw.visitJumpInsn(IFNE, matched_);
        mw.visitInsn(ACONST_NULL);
        mw.visitVarInsn(ASTORE, context.var(fieldInfo.getName() + "_asm"));

        mw.visitJumpInsn(GOTO, _end_if);

        mw.visitLabel(matched_);

        _setFlag(mw, context, i);

        // increment matchedCount
        mw.visitVarInsn(ILOAD, context.var("matchedCount"));
        mw.visitInsn(ICONST_1);
        mw.visitInsn(IADD);
        mw.visitVarInsn(ISTORE, context.var("matchedCount"));

        _deserObject(context, mw, fieldInfo, fieldClass);

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
        mw.visitMethodInsn(INVOKEVIRTUAL, getType(DefaultJSONParser.class), "getContext", "()"
                                                                                          + getDesc(ParseContext.class));
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

    private void _deserObject(Context context, MethodVisitor mw, FieldInfo fieldInfo, Class<?> fieldClass) {
        _getFieldDeser(context, mw, fieldInfo);

        mw.visitVarInsn(ALOAD, 1);
        if (fieldInfo.getFieldType() instanceof Class) {
            mw.visitLdcInsn(com.alibaba.fastjson.asm.Type.getType(getDesc(fieldInfo.getFieldClass())));
        } else {
            mw.visitVarInsn(ALOAD, 0);
            mw.visitLdcInsn(fieldInfo.getName());
            mw.visitMethodInsn(INVOKEVIRTUAL, getType(ASMJavaBeanDeserializer.class), "getFieldType",
                               "(Ljava/lang/String;)Ljava/lang/reflect/Type;");
        }
        mw.visitLdcInsn(fieldInfo.getName());
        mw.visitMethodInsn(INVOKEINTERFACE, getType(ObjectDeserializer.class), "deserialze",
                           "(" + getDesc(DefaultJSONParser.class) + getDesc(Type.class)
                                   + "Ljava/lang/Object;)Ljava/lang/Object;");
        mw.visitTypeInsn(CHECKCAST, getType(fieldClass)); // cast
        mw.visitVarInsn(ASTORE, context.var(fieldInfo.getName() + "_asm"));
    }

    private void _getFieldDeser(Context context, MethodVisitor mw, FieldInfo fieldInfo) {
        Label notNull_ = new Label();
        mw.visitVarInsn(ALOAD, 0);
        mw.visitFieldInsn(GETFIELD, context.getClassName(), fieldInfo.getName() + "_asm_deser__",
                          getDesc(ObjectDeserializer.class));
        mw.visitJumpInsn(IFNONNULL, notNull_);

        mw.visitVarInsn(ALOAD, 0);

        mw.visitVarInsn(ALOAD, 1);
        mw.visitMethodInsn(INVOKEVIRTUAL, getType(DefaultJSONParser.class), "getConfig", "()"
                                                                                         + getDesc(ParserConfig.class));
        mw.visitLdcInsn(com.alibaba.fastjson.asm.Type.getType(getDesc(fieldInfo.getFieldClass())));
        mw.visitMethodInsn(INVOKEVIRTUAL, getType(ParserConfig.class), "getDeserializer",
                           "(" + getDesc(Type.class) + ")" + getDesc(ObjectDeserializer.class));

        mw.visitFieldInsn(PUTFIELD, context.getClassName(), fieldInfo.getName() + "_asm_deser__",
                          getDesc(ObjectDeserializer.class));

        mw.visitLabel(notNull_);

        mw.visitVarInsn(ALOAD, 0);
        mw.visitFieldInsn(GETFIELD, context.getClassName(), fieldInfo.getName() + "_asm_deser__",
                          getDesc(ObjectDeserializer.class));
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
                mw.visitMethodInsn(INVAKE_TYPE, getType(method.getDeclaringClass()), method.getName(),
                                   ASMUtils.getDesc(method));

                mw.visitInsn(RETURN);
                mw.visitMaxs(3, 3);
                mw.visitEnd();
            } else if (fieldClass == long.class) {
                MethodVisitor mw = cw.visitMethod(ACC_PUBLIC, "setValue", "(" + getDesc(Object.class) + "J)V", null,
                                                  null);
                mw.visitVarInsn(ALOAD, 1);
                mw.visitTypeInsn(CHECKCAST, getType(method.getDeclaringClass())); // cast
                mw.visitVarInsn(LLOAD, 2);
                mw.visitMethodInsn(INVAKE_TYPE, getType(method.getDeclaringClass()), method.getName(),
                                   ASMUtils.getDesc(method));

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
                                   ASMUtils.getDesc(method));

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
                continue;
            }

            if (Collection.class.isAssignableFrom(fieldClass)) {
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
