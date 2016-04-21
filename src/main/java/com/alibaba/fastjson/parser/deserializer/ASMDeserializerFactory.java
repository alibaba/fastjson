package com.alibaba.fastjson.parser.deserializer;

import static com.alibaba.fastjson.util.ASMUtils.desc;
import static com.alibaba.fastjson.util.ASMUtils.type;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicLong;

import com.alibaba.fastjson.asm.ClassWriter;
import com.alibaba.fastjson.asm.FieldWriter;
import com.alibaba.fastjson.asm.Label;
import com.alibaba.fastjson.asm.MethodVisitor;
import com.alibaba.fastjson.asm.MethodWriter;
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
import com.alibaba.fastjson.util.ASMClassLoader;
import com.alibaba.fastjson.util.ASMUtils;
import com.alibaba.fastjson.util.FieldInfo;
import com.alibaba.fastjson.util.JavaBeanInfo;
import com.alibaba.fastjson.util.TypeUtils;

public class ASMDeserializerFactory implements Opcodes {

    public final ASMClassLoader           classLoader;
    protected final AtomicLong            seed              = new AtomicLong();

    final static String                   DefaultJSONParser = type(DefaultJSONParser.class);
    final static String                   JSONLexerBase     = type(JSONLexerBase.class);
    final static String                   JSONToken         = type(JSONToken.class);

    public ASMDeserializerFactory(ClassLoader parentClassLoader){
        classLoader = parentClassLoader instanceof ASMClassLoader //
            ? (ASMClassLoader) parentClassLoader //
            : new ASMClassLoader(parentClassLoader);
    }

    public ObjectDeserializer createJavaBeanDeserializer(ParserConfig config, Class<?> clazz,
                                                         Type type) throws Exception {
        if (clazz.isPrimitive()) {
            throw new IllegalArgumentException("not support type :" + clazz.getName());
        }

        String className = "FastjsonASMDeserializer_" + seed.incrementAndGet() + "_" + clazz.getSimpleName();
        String packageName = ASMDeserializerFactory.class.getPackage().getName();
        String classNameType = packageName.replace('.', '/') + "/" + className;
        String classNameFull = packageName + "." + className;

        ClassWriter cw = new ClassWriter();
        cw.visit(V1_5, ACC_PUBLIC + ACC_SUPER, classNameType,
                 type(JavaBeanDeserializer.class), null);

        JavaBeanInfo beanInfo = JavaBeanInfo.build(clazz, type);

        _init(cw, new Context(classNameType, config, beanInfo, 3));
        _createInstance(cw, new Context(classNameType, config, beanInfo, 3));
        _deserialze(cw, new Context(classNameType, config, beanInfo, 4));
        
        _deserialzeArrayMapping(cw, new Context(classNameType, config, beanInfo, 4));
        byte[] code = cw.toByteArray();

//        if (JSON.DUMP_CLASS != null) {
//            FileOutputStream fos = null;
//            try {
//                fos = new FileOutputStream(JSON.DUMP_CLASS + File.separator + className + ".class");
//                fos.write(code);
//            } catch (Exception ex) {
//                System.err.println("FASTJSON dump class:" + className + "失败:" + ex.getMessage());
//            } finally {
//                if (fos != null) {
//                    fos.close();
//                }
//            }
//        }

        Class<?> exampleClass = defineClassPublic(classNameFull, code, 0, code.length);

        Constructor<?> constructor = exampleClass.getConstructor(ParserConfig.class, Class.class);
        Object instance = constructor.newInstance(config, clazz);

        return (ObjectDeserializer) instance;
    }

    private Class<?> defineClassPublic(String name, byte[] b, int off, int len) {
        return classLoader.defineClassPublic(name, b, off, len);
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
        MethodVisitor mw = new MethodWriter(cw, ACC_PUBLIC, "deserialzeArrayMapping",
                                            "(L" + DefaultJSONParser + ";Ljava/lang/reflect/Type;Ljava/lang/Object;)Ljava/lang/Object;",
                                            null, null);

        defineVarLexer(context, mw);

        _createInstance(context, mw);

        FieldInfo[] sortedFieldInfoList = context.beanInfo.sortedFields;
        int fieldListSize = sortedFieldInfoList.length;
        for (int i = 0; i < fieldListSize; ++i) {
            final boolean last = (i == fieldListSize - 1);
            final char seperator = last ? ']' : ',';

            FieldInfo fieldInfo = sortedFieldInfoList[i];
            Class<?> fieldClass = fieldInfo.fieldClass;
            Type fieldType = fieldInfo.fieldType;
            if (fieldClass == byte.class //
                || fieldClass == short.class //
                || fieldClass == int.class) {
                mw.visitVarInsn(ALOAD, context.var("lexer"));
                mw.visitVarInsn(BIPUSH, seperator);
                mw.visitMethodInsn(INVOKEVIRTUAL, JSONLexerBase, "scanInt", "(C)I");
                mw.visitVarInsn(ISTORE, context.var(fieldInfo.name + "_asm"));
            } else if (fieldClass == long.class) {
                mw.visitVarInsn(ALOAD, context.var("lexer"));
                mw.visitVarInsn(BIPUSH, seperator);
                mw.visitMethodInsn(INVOKEVIRTUAL, JSONLexerBase, "scanLong", "(C)J");
                mw.visitVarInsn(LSTORE, context.var(fieldInfo.name + "_asm", 2));
            } else if (fieldClass == boolean.class) {
                mw.visitVarInsn(ALOAD, context.var("lexer"));
                mw.visitVarInsn(BIPUSH, seperator);
                mw.visitMethodInsn(INVOKEVIRTUAL, JSONLexerBase, "scanBoolean", "(C)Z");
                mw.visitVarInsn(ISTORE, context.var(fieldInfo.name + "_asm"));
            } else if (fieldClass == float.class) {
                mw.visitVarInsn(ALOAD, context.var("lexer"));
                mw.visitVarInsn(BIPUSH, seperator);
                mw.visitMethodInsn(INVOKEVIRTUAL, JSONLexerBase, "scanFloat", "(C)F");
                mw.visitVarInsn(FSTORE, context.var(fieldInfo.name + "_asm"));
            } else if (fieldClass == double.class) {
                mw.visitVarInsn(ALOAD, context.var("lexer"));
                mw.visitVarInsn(BIPUSH, seperator);
                mw.visitMethodInsn(INVOKEVIRTUAL, JSONLexerBase, "scanDouble", "(C)D");
                mw.visitVarInsn(DSTORE, context.var(fieldInfo.name + "_asm", 2));
            } else if (fieldClass == char.class) {
                mw.visitVarInsn(ALOAD, context.var("lexer"));
                mw.visitVarInsn(BIPUSH, seperator);
                mw.visitMethodInsn(INVOKEVIRTUAL, JSONLexerBase, "scanString",
                                   "(C)Ljava/lang/String;");
                mw.visitInsn(ICONST_0);
                mw.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "charAt", "(I)C");
                mw.visitVarInsn(ISTORE, context.var(fieldInfo.name + "_asm"));
            } else if (fieldClass == String.class) {
                mw.visitVarInsn(ALOAD, context.var("lexer"));
                mw.visitVarInsn(BIPUSH, seperator);
                mw.visitMethodInsn(INVOKEVIRTUAL, JSONLexerBase, "scanString",
                                   "(C)Ljava/lang/String;");
                mw.visitVarInsn(ASTORE, context.var(fieldInfo.name + "_asm"));
            } else if (fieldClass.isEnum()) {
                mw.visitVarInsn(ALOAD, context.var("lexer"));
                mw.visitLdcInsn(com.alibaba.fastjson.asm.Type.getType(desc(fieldClass)));
                mw.visitVarInsn(ALOAD, 1);
                
                mw.visitMethodInsn(INVOKEVIRTUAL, DefaultJSONParser, "getSymbolTable",
                        "()" + desc(SymbolTable.class));
                mw.visitVarInsn(BIPUSH, seperator);
                mw.visitMethodInsn(INVOKEVIRTUAL, JSONLexerBase, "scanEnum",
                                   "(Ljava/lang/Class;" + desc(SymbolTable.class) + "C)Ljava/lang/Enum;");
                mw.visitTypeInsn(CHECKCAST, type(fieldClass)); // cast
                mw.visitVarInsn(ASTORE, context.var(fieldInfo.name + "_asm"));
            } else if (Collection.class.isAssignableFrom(fieldClass)) {
                Class<?> itemClass = TypeUtils.getCollectionItemClass(fieldType);
                if (itemClass == String.class) {
                    mw.visitVarInsn(ALOAD, context.var("lexer"));
                    mw.visitLdcInsn(com.alibaba.fastjson.asm.Type.getType(desc(fieldClass)));
                    mw.visitVarInsn(BIPUSH, seperator);
                    mw.visitMethodInsn(INVOKEVIRTUAL, JSONLexerBase, "scanStringArray", //
                                       "(Ljava/lang/Class;C)Ljava/util/Collection;"
                                       );
                    mw.visitVarInsn(ASTORE, context.var(fieldInfo.name + "_asm"));

                } else {
                    mw.visitVarInsn(ALOAD, 1);
                    if (i == 0) {
                        mw.visitFieldInsn(GETSTATIC, JSONToken, "LBRACKET", "I");
                    } else {
                        mw.visitFieldInsn(GETSTATIC, JSONToken, "COMMA", "I");
                    }
                    mw.visitFieldInsn(GETSTATIC, JSONToken, "LBRACKET", "I");
                    mw.visitMethodInsn(INVOKEVIRTUAL, DefaultJSONParser, "accept", "(II)V");

                    _newCollection(mw, fieldClass, i, false);
                    mw.visitInsn(DUP);
                    mw.visitVarInsn(ASTORE, context.var(fieldInfo.name + "_asm"));
                    _getCollectionFieldItemDeser(context, mw, fieldInfo, itemClass);
                    mw.visitVarInsn(ALOAD, 1);
                    mw.visitLdcInsn(com.alibaba.fastjson.asm.Type.getType(desc(itemClass)));
                    mw.visitVarInsn(ALOAD, 3);
                    mw.visitMethodInsn(INVOKESTATIC, type(ASMUtils.class),
                                       "parseArray",
                                       "(Ljava/util/Collection;" //
                                       + desc(ObjectDeserializer.class) //
                                       + "L" + DefaultJSONParser + ";" //
                                       + "Ljava/lang/reflect/Type;Ljava/lang/Object;)V");
                }
            } else if (fieldClass.isArray()) {
                mw.visitVarInsn(ALOAD, context.var("lexer"));
                mw.visitFieldInsn(GETSTATIC, JSONToken, "LBRACKET", "I");
                mw.visitMethodInsn(INVOKEVIRTUAL, JSONLexerBase, "nextToken", "(I)V");
                
                mw.visitVarInsn(ALOAD, 1); // parser
                mw.visitVarInsn(ALOAD, 0);
                mw.visitLdcInsn(i);
                mw.visitMethodInsn(INVOKEVIRTUAL, type(JavaBeanDeserializer.class),
                                   "getFieldType", "(I)Ljava/lang/reflect/Type;");
                mw.visitMethodInsn(INVOKEVIRTUAL, DefaultJSONParser, "parseObject",
                                   "(Ljava/lang/reflect/Type;)Ljava/lang/Object;");
                
                mw.visitTypeInsn(CHECKCAST, type(fieldClass)); // cast
                mw.visitVarInsn(ASTORE, context.var(fieldInfo.name + "_asm"));
            } else {
                mw.visitVarInsn(ALOAD, 1); // parser
                if (i == 0) {
                    mw.visitFieldInsn(GETSTATIC, JSONToken, "LBRACKET", "I");
                } else {
                    mw.visitFieldInsn(GETSTATIC, JSONToken, "COMMA", "I");
                }
                mw.visitFieldInsn(GETSTATIC, JSONToken, "LBRACKET", "I");
                mw.visitMethodInsn(INVOKEVIRTUAL, DefaultJSONParser, "accept", "(II)V");

                _deserObject(context, mw, fieldInfo, fieldClass, i);

                mw.visitVarInsn(ALOAD, 1);
                if (!last) {
                    mw.visitFieldInsn(GETSTATIC, JSONToken, "COMMA", "I");
                    mw.visitFieldInsn(GETSTATIC, JSONToken, "LBRACKET", "I");
                } else {
                    mw.visitFieldInsn(GETSTATIC, JSONToken, "RBRACKET", "I");
                    mw.visitFieldInsn(GETSTATIC, JSONToken, "EOF", "I");
                }
                mw.visitMethodInsn(INVOKEVIRTUAL, DefaultJSONParser, "accept", "(II)V");
                continue;
            }
        }

        _batchSet(context, mw, false);

        // lexer.nextToken(JSONToken.COMMA);
        mw.visitVarInsn(ALOAD, context.var("lexer"));
        mw.visitFieldInsn(GETSTATIC, JSONToken, "COMMA", "I");
        mw.visitMethodInsn(INVOKEVIRTUAL, JSONLexerBase, "nextToken", "(I)V");

        mw.visitVarInsn(ALOAD, context.var("instance"));
        mw.visitInsn(ARETURN);
        mw.visitMaxs(5, context.variantIndex);
        mw.visitEnd();
    }

    void _deserialze(ClassWriter cw, Context context) {
        if (context.fieldInfoList.length == 0) {
            return;
        }

        for (FieldInfo fieldInfo : context.fieldInfoList) {
            Class<?> fieldClass = fieldInfo.fieldClass;
            Type fieldType = fieldInfo.fieldType;

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

        context.fieldInfoList = context.beanInfo.sortedFields;

        MethodVisitor mw = new MethodWriter(cw, ACC_PUBLIC, "deserialze",
                                            "(L" + DefaultJSONParser + ";Ljava/lang/reflect/Type;Ljava/lang/Object;)Ljava/lang/Object;",
                                            null, null);

        Label reset_ = new Label();
        Label super_ = new Label();
        Label return_ = new Label();
        Label end_ = new Label();

        defineVarLexer(context, mw);

        _isEnable(context, mw, Feature.SortFeidFastMatch);
        mw.visitJumpInsn(IFEQ, super_);

        {
            Label next_ = new Label();

            mw.visitVarInsn(ALOAD, 0);
            mw.visitVarInsn(ALOAD, context.var("lexer"));
            mw.visitMethodInsn(INVOKESPECIAL, type(JavaBeanDeserializer.class),
                               "isSupportArrayToBean", "(" + desc(JSONLexer.class) + ")Z");
            mw.visitJumpInsn(IFEQ, next_);
            // isSupportArrayToBean

            mw.visitVarInsn(ALOAD, context.var("lexer"));
            mw.visitMethodInsn(INVOKEVIRTUAL, JSONLexerBase, "token", "()I");
            mw.visitFieldInsn(GETSTATIC, JSONToken, "LBRACKET", "I");
            mw.visitJumpInsn(IF_ICMPNE, next_);

            mw.visitVarInsn(ALOAD, 0);
            mw.visitVarInsn(ALOAD, 1);
            mw.visitVarInsn(ALOAD, 2);
            mw.visitVarInsn(ALOAD, 3);
            mw.visitMethodInsn(INVOKESPECIAL, context.className, "deserialzeArrayMapping",
                               "(L" + DefaultJSONParser + ";Ljava/lang/reflect/Type;Ljava/lang/Object;)Ljava/lang/Object;");
            mw.visitInsn(ARETURN);

            mw.visitLabel(next_);
            // deserialzeArrayMapping
        }

        mw.visitVarInsn(ALOAD, context.var("lexer"));
        mw.visitLdcInsn(context.clazz.getName());
        mw.visitMethodInsn(INVOKEVIRTUAL, JSONLexerBase, "scanType",
                           "(Ljava/lang/String;)I");

        mw.visitFieldInsn(GETSTATIC, JSONLexerBase, "NOT_MATCH", "I");
        mw.visitJumpInsn(IF_ICMPEQ, super_);

        mw.visitVarInsn(ALOAD, 1); // parser
        mw.visitMethodInsn(INVOKEVIRTUAL, DefaultJSONParser, "getContext",
                           "()" + desc(ParseContext.class));
        mw.visitVarInsn(ASTORE, context.var("mark_context"));

        // ParseContext context = parser.getContext();
        mw.visitInsn(ICONST_0);
        mw.visitVarInsn(ISTORE, context.var("matchedCount"));

        _createInstance(context, mw);

        {
            mw.visitVarInsn(ALOAD, 1); // parser
            mw.visitMethodInsn(INVOKEVIRTUAL, DefaultJSONParser, "getContext",
                               "()" + desc(ParseContext.class));
            mw.visitVarInsn(ASTORE, context.var("context"));

            mw.visitVarInsn(ALOAD, 1); // parser
            mw.visitVarInsn(ALOAD, context.var("context"));
            mw.visitVarInsn(ALOAD, context.var("instance"));
            mw.visitVarInsn(ALOAD, 3); // fieldName
            mw.visitMethodInsn(INVOKEVIRTUAL, DefaultJSONParser, "setContext", //
                               "(" + desc(ParseContext.class) + "Ljava/lang/Object;Ljava/lang/Object;)" + desc(ParseContext.class));
            mw.visitVarInsn(ASTORE, context.var("childContext"));
        }

        mw.visitVarInsn(ALOAD, context.var("lexer"));
        mw.visitFieldInsn(GETFIELD, JSONLexerBase, "matchStat", "I");
        mw.visitFieldInsn(GETSTATIC, JSONLexerBase, "END", "I");
        mw.visitJumpInsn(IF_ICMPEQ, return_);

        mw.visitInsn(ICONST_0); // UNKOWN
        mw.visitIntInsn(ISTORE, context.var("matchStat"));

        int fieldListSize = context.fieldInfoList.length;
        for (int i = 0; i < fieldListSize; i += 32) {
            mw.visitInsn(ICONST_0);
            mw.visitVarInsn(ISTORE, context.var("_asm_flag_" + (i / 32)));
        }

        _isEnable(context, mw, Feature.InitStringFieldAsEmpty);
        mw.visitIntInsn(ISTORE, context.var("initStringFieldAsEmpty"));

        // declare and init
        for (int i = 0; i < fieldListSize; ++i) {
            FieldInfo fieldInfo = context.fieldInfoList[i];
            Class<?> fieldClass = fieldInfo.fieldClass;

            if (fieldClass == boolean.class //
                || fieldClass == byte.class //
                || fieldClass == short.class //
                || fieldClass == int.class) {
                mw.visitInsn(ICONST_0);
                mw.visitVarInsn(ISTORE, context.var(fieldInfo.name + "_asm"));
            } else if (fieldClass == long.class) {
                mw.visitInsn(LCONST_0);
                mw.visitVarInsn(LSTORE, context.var(fieldInfo.name + "_asm", 2));
            } else if (fieldClass == float.class) {
                mw.visitInsn(FCONST_0);
                mw.visitVarInsn(FSTORE, context.var(fieldInfo.name + "_asm"));
            } else if (fieldClass == double.class) {
                mw.visitInsn(DCONST_0);
                mw.visitVarInsn(DSTORE, context.var(fieldInfo.name + "_asm", 2));
            } else {
                if (fieldClass == String.class) {
                    Label flagEnd_ = new Label();
                    Label flagElse_ = new Label();
                    mw.visitVarInsn(ILOAD, context.var("initStringFieldAsEmpty"));
                    mw.visitJumpInsn(IFEQ, flagElse_);
                    _setFlag(mw, context, i);
                    mw.visitVarInsn(ALOAD, context.var("lexer"));
                    mw.visitMethodInsn(INVOKEVIRTUAL, JSONLexerBase, "stringDefaultValue",
                                       "()Ljava/lang/String;");
                    mw.visitJumpInsn(GOTO, flagEnd_);

                    mw.visitLabel(flagElse_);
                    mw.visitInsn(ACONST_NULL);

                    mw.visitLabel(flagEnd_);
                } else {
                    mw.visitInsn(ACONST_NULL);
                }

                mw.visitTypeInsn(CHECKCAST, type(fieldClass)); // cast
                mw.visitVarInsn(ASTORE, context.var(fieldInfo.name + "_asm"));
            }
        }

        for (int i = 0; i < fieldListSize; ++i) {
            FieldInfo fieldInfo = context.fieldInfoList[i];
            Class<?> fieldClass = fieldInfo.fieldClass;
            Type fieldType = fieldInfo.fieldType;

            Label notMatch_ = new Label();

            if (fieldClass == boolean.class) {
                mw.visitVarInsn(ALOAD, context.var("lexer"));
                mw.visitVarInsn(ALOAD, 0);
                mw.visitFieldInsn(GETFIELD, context.className, fieldInfo.name + "_asm_prefix__", "[C");
                mw.visitMethodInsn(INVOKEVIRTUAL, JSONLexerBase, "scanFieldBoolean",
                                   "([C)Z");
                mw.visitVarInsn(ISTORE, context.var(fieldInfo.name + "_asm"));
            } else if (fieldClass == byte.class) {
                mw.visitVarInsn(ALOAD, context.var("lexer"));
                mw.visitVarInsn(ALOAD, 0);
                mw.visitFieldInsn(GETFIELD, context.className, fieldInfo.name + "_asm_prefix__", "[C");
                mw.visitMethodInsn(INVOKEVIRTUAL, JSONLexerBase, "scanFieldInt", "([C)I");
                mw.visitVarInsn(ISTORE, context.var(fieldInfo.name + "_asm"));

            } else if (fieldClass == short.class) {
                mw.visitVarInsn(ALOAD, context.var("lexer"));
                mw.visitVarInsn(ALOAD, 0);
                mw.visitFieldInsn(GETFIELD, context.className, fieldInfo.name + "_asm_prefix__", "[C");
                mw.visitMethodInsn(INVOKEVIRTUAL, JSONLexerBase, "scanFieldInt", "([C)I");
                mw.visitVarInsn(ISTORE, context.var(fieldInfo.name + "_asm"));

            } else if (fieldClass == int.class) {
                mw.visitVarInsn(ALOAD, context.var("lexer"));
                mw.visitVarInsn(ALOAD, 0);
                mw.visitFieldInsn(GETFIELD, context.className, fieldInfo.name + "_asm_prefix__", "[C");
                mw.visitMethodInsn(INVOKEVIRTUAL, JSONLexerBase, "scanFieldInt", "([C)I");
                mw.visitVarInsn(ISTORE, context.var(fieldInfo.name + "_asm"));

            } else if (fieldClass == long.class) {
                mw.visitVarInsn(ALOAD, context.var("lexer"));
                mw.visitVarInsn(ALOAD, 0);
                mw.visitFieldInsn(GETFIELD, context.className, fieldInfo.name + "_asm_prefix__", "[C");
                mw.visitMethodInsn(INVOKEVIRTUAL, JSONLexerBase, "scanFieldLong",
                                   "([C)J");
                mw.visitVarInsn(LSTORE, context.var(fieldInfo.name + "_asm", 2));

            } else if (fieldClass == float.class) {
                mw.visitVarInsn(ALOAD, context.var("lexer"));
                mw.visitVarInsn(ALOAD, 0);
                mw.visitFieldInsn(GETFIELD, context.className, fieldInfo.name + "_asm_prefix__", "[C");
                mw.visitMethodInsn(INVOKEVIRTUAL, JSONLexerBase, "scanFieldFloat",
                                   "([C)F");
                mw.visitVarInsn(FSTORE, context.var(fieldInfo.name + "_asm"));

            } else if (fieldClass == double.class) {
                mw.visitVarInsn(ALOAD, context.var("lexer"));
                mw.visitVarInsn(ALOAD, 0);
                mw.visitFieldInsn(GETFIELD, context.className, fieldInfo.name + "_asm_prefix__", "[C");
                mw.visitMethodInsn(INVOKEVIRTUAL, JSONLexerBase, "scanFieldDouble",
                                   "([C)D");
                mw.visitVarInsn(DSTORE, context.var(fieldInfo.name + "_asm", 2));

            } else if (fieldClass == String.class) {
                mw.visitVarInsn(ALOAD, context.var("lexer"));
                mw.visitVarInsn(ALOAD, 0);
                mw.visitFieldInsn(GETFIELD, context.className, fieldInfo.name + "_asm_prefix__", "[C");
                mw.visitMethodInsn(INVOKEVIRTUAL, JSONLexerBase, "scanFieldString",
                                   "([C)Ljava/lang/String;");
                mw.visitVarInsn(ASTORE, context.var(fieldInfo.name + "_asm"));

            } else if (fieldClass.isEnum()) {
                mw.visitVarInsn(ALOAD, context.var("lexer"));
                mw.visitVarInsn(ALOAD, 0);
                mw.visitFieldInsn(GETFIELD, context.className, fieldInfo.name + "_asm_prefix__", "[C");
                Label enumNull_ = new Label();
                mw.visitInsn(ACONST_NULL);
                mw.visitTypeInsn(CHECKCAST, type(fieldClass)); // cast
                mw.visitVarInsn(ASTORE, context.var(fieldInfo.name + "_asm"));

                mw.visitVarInsn(ALOAD, 1);
                
                mw.visitMethodInsn(INVOKEVIRTUAL, DefaultJSONParser, "getSymbolTable",
                                   "()" + desc(SymbolTable.class));

                mw.visitMethodInsn(INVOKEVIRTUAL, JSONLexerBase, "scanFieldSymbol",
                                   "([C" + desc(SymbolTable.class) + ")Ljava/lang/String;");
                mw.visitInsn(DUP);
                mw.visitVarInsn(ASTORE, context.var(fieldInfo.name + "_asm_enumName"));

                mw.visitJumpInsn(IFNULL, enumNull_);
                mw.visitVarInsn(ALOAD, context.var(fieldInfo.name + "_asm_enumName"));
                mw.visitMethodInsn(INVOKESTATIC, type(fieldClass), "valueOf",
                                   "(Ljava/lang/String;)" + desc(fieldClass));
                mw.visitVarInsn(ASTORE, context.var(fieldInfo.name + "_asm"));
                mw.visitLabel(enumNull_);

            } else if (Collection.class.isAssignableFrom(fieldClass)) {
                mw.visitVarInsn(ALOAD, context.var("lexer"));
                mw.visitVarInsn(ALOAD, 0);
                mw.visitFieldInsn(GETFIELD, context.className, fieldInfo.name + "_asm_prefix__", "[C");

                Class<?> itemClass = TypeUtils.getCollectionItemClass(fieldType);

                if (itemClass == String.class) {
                    mw.visitLdcInsn(com.alibaba.fastjson.asm.Type.getType(desc(fieldClass))); // cast
                    mw.visitMethodInsn(INVOKEVIRTUAL, JSONLexerBase,
                                       "scanFieldStringArray", "([CLjava/lang/Class;)" + desc(Collection.class));
                    mw.visitVarInsn(ASTORE, context.var(fieldInfo.name + "_asm"));
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
            mw.visitFieldInsn(GETFIELD, JSONLexerBase, "matchStat", "I");
            Label flag_ = new Label();
            // mw.visitInsn(DUP);
            mw.visitJumpInsn(IFLE, flag_);
            _setFlag(mw, context, i);
            mw.visitLabel(flag_);

            mw.visitVarInsn(ALOAD, context.var("lexer"));
            mw.visitFieldInsn(GETFIELD, JSONLexerBase, "matchStat", "I");
            mw.visitInsn(DUP);
            mw.visitVarInsn(ISTORE, context.var("matchStat"));

            mw.visitFieldInsn(GETSTATIC, JSONLexerBase, "NOT_MATCH", "I");
            mw.visitJumpInsn(IF_ICMPEQ, reset_);

            // mw.visitFieldInsn(GETSTATIC, getType(System.class), "out", "Ljava/io/PrintStream;");
            // mw.visitVarInsn(ALOAD, context.var("lexer"));
            // mw.visitFieldInsn(GETFIELD, JSONLexerBase, "matchStat", "I");
            // mw.visitMethodInsn(INVOKEVIRTUAL, getType(java.io.PrintStream.class), "println", "(I)V");

            mw.visitVarInsn(ALOAD, context.var("lexer"));
            mw.visitFieldInsn(GETFIELD, JSONLexerBase, "matchStat", "I");
            mw.visitJumpInsn(IFLE, notMatch_);

            // increment matchedCount
            mw.visitVarInsn(ILOAD, context.var("matchedCount"));
            mw.visitInsn(ICONST_1);
            mw.visitInsn(IADD);
            mw.visitVarInsn(ISTORE, context.var("matchedCount"));

            mw.visitVarInsn(ALOAD, context.var("lexer"));
            mw.visitFieldInsn(GETFIELD, JSONLexerBase, "matchStat", "I");
            mw.visitFieldInsn(GETSTATIC, JSONLexerBase, "END", "I");
            mw.visitJumpInsn(IF_ICMPEQ, end_);

            // mw.visitFieldInsn(GETSTATIC, getType(System.class), "out", "Ljava/io/PrintStream;");
            // mw.visitVarInsn(ILOAD, context.var("matchedCount"));
            // mw.visitMethodInsn(INVOKEVIRTUAL, getType(java.io.PrintStream.class), "println", "(I)V");

            mw.visitLabel(notMatch_);

            if (i == fieldListSize - 1) {
                mw.visitVarInsn(ALOAD, context.var("lexer"));
                mw.visitFieldInsn(GETFIELD, JSONLexerBase, "matchStat", "I");
                mw.visitFieldInsn(GETSTATIC, JSONLexerBase, "END", "I");
                mw.visitJumpInsn(IF_ICMPNE, reset_);
            }
        } // endFor

        mw.visitLabel(end_);

        if (!context.clazz.isInterface() && !Modifier.isAbstract(context.clazz.getModifiers())) {
            _batchSet(context, mw);
        }

        mw.visitLabel(return_);

        _setContext(context, mw);
        mw.visitVarInsn(ALOAD, context.var("instance"));

        Method buildMethod = context.beanInfo.buildMethod;
        if (buildMethod != null) {
            mw.visitMethodInsn(INVOKEVIRTUAL, type(context.getInstClass()), buildMethod.getName(),
                               "()" + desc(buildMethod.getReturnType()));
        }

        mw.visitInsn(ARETURN);

        mw.visitLabel(reset_);

        _batchSet(context, mw);
        mw.visitVarInsn(ALOAD, 0);
        mw.visitVarInsn(ALOAD, 1);
        mw.visitVarInsn(ALOAD, 2);
        mw.visitVarInsn(ALOAD, 3);
        mw.visitVarInsn(ALOAD, context.var("instance"));
        mw.visitMethodInsn(INVOKEVIRTUAL, type(JavaBeanDeserializer.class),
                           "parseRest",
                           "(L" + DefaultJSONParser + ";Ljava/lang/reflect/Type;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;");
        mw.visitTypeInsn(CHECKCAST, type(context.clazz)); // cast
        mw.visitInsn(ARETURN);

        mw.visitLabel(super_);
        mw.visitVarInsn(ALOAD, 0);
        mw.visitVarInsn(ALOAD, 1);
        mw.visitVarInsn(ALOAD, 2);
        mw.visitVarInsn(ALOAD, 3);
        mw.visitMethodInsn(INVOKESPECIAL, type(JavaBeanDeserializer.class), //
                           "deserialze", //
                           "(L" + DefaultJSONParser + ";Ljava/lang/reflect/Type;Ljava/lang/Object;)Ljava/lang/Object;");
        mw.visitInsn(ARETURN);

        mw.visitMaxs(5, context.variantIndex);
        mw.visitEnd();
        
    }

    private void _isEnable(Context context, MethodVisitor mw, Feature feature) {
        mw.visitVarInsn(ALOAD, context.var("lexer"));
        mw.visitFieldInsn(GETSTATIC, type(Feature.class), feature.name(),
                          desc(Feature.class));
        mw.visitMethodInsn(INVOKEVIRTUAL, JSONLexerBase, "isEnabled",
                           "(" + desc(Feature.class) + ")Z");
    }

    private void defineVarLexer(Context context, MethodVisitor mw) {
        mw.visitVarInsn(ALOAD, 1);
        mw.visitFieldInsn(GETFIELD, DefaultJSONParser, "lexer", desc(JSONLexer.class));
        mw.visitTypeInsn(CHECKCAST, JSONLexerBase); // cast
        mw.visitVarInsn(ASTORE, context.var("lexer"));
    }

    private void _createInstance(Context context, MethodVisitor mw) {
        JavaBeanInfo beanInfo = context.beanInfo;
        Constructor<?> defaultConstructor = beanInfo.defaultConstructor;
        if (Modifier.isPublic(defaultConstructor.getModifiers())) {
            mw.visitTypeInsn(NEW, type(context.getInstClass()));
            mw.visitInsn(DUP);

            mw.visitMethodInsn(INVOKESPECIAL, type(defaultConstructor.getDeclaringClass()), "<init>", "()V");

            mw.visitVarInsn(ASTORE, context.var("instance"));
        } else {
            mw.visitVarInsn(ALOAD, 0);
            mw.visitVarInsn(ALOAD, 1);
            mw.visitVarInsn(ALOAD, 0);
            mw.visitFieldInsn(GETFIELD, type(JavaBeanDeserializer.class), "clazz", "Ljava/lang/Class;");
            mw.visitMethodInsn(INVOKESPECIAL, type(JavaBeanDeserializer.class),
                               "createInstance", "(L" + DefaultJSONParser + ";Ljava/lang/reflect/Type;)Ljava/lang/Object;");
            mw.visitTypeInsn(CHECKCAST, type(context.getInstClass())); // cast
            mw.visitVarInsn(ASTORE, context.var("instance"));
        }
    }

    private void _batchSet(Context context, MethodVisitor mw) {
        _batchSet(context, mw, true);
    }

    private void _batchSet(Context context, MethodVisitor mw, boolean flag) {
        for (int i = 0, size = context.fieldInfoList.length; i < size; ++i) {
            Label notSet_ = new Label();

            if (flag) {
                _isFlag(mw, context, i, notSet_);
            }

            FieldInfo fieldInfo = context.fieldInfoList[i];
            _loadAndSet(context, mw, fieldInfo);

            if (flag) {
                mw.visitLabel(notSet_);
            }
        }
    }

    private void _loadAndSet(Context context, MethodVisitor mw, FieldInfo fieldInfo) {
        Class<?> fieldClass = fieldInfo.fieldClass;
        Type fieldType = fieldInfo.fieldType;

        if (fieldClass == boolean.class) {
            mw.visitVarInsn(ALOAD, context.var("instance"));
            mw.visitVarInsn(ILOAD, context.var(fieldInfo.name + "_asm"));
            _set(context, mw, fieldInfo);
        } else if (fieldClass == byte.class //
                   || fieldClass == short.class //
                   || fieldClass == int.class //
                   || fieldClass == char.class) {
            mw.visitVarInsn(ALOAD, context.var("instance"));
            mw.visitVarInsn(ILOAD, context.var(fieldInfo.name + "_asm"));
            _set(context, mw, fieldInfo);
        } else if (fieldClass == long.class) {
            mw.visitVarInsn(ALOAD, context.var("instance"));
            mw.visitVarInsn(LLOAD, context.var(fieldInfo.name + "_asm", 2));
            if (fieldInfo.method != null) {
                mw.visitMethodInsn(INVOKEVIRTUAL, type(context.getInstClass()), fieldInfo.method.getName(),
                                   desc(fieldInfo.method));
                if (!fieldInfo.method.getReturnType().equals(Void.TYPE)) {
                    mw.visitInsn(POP);
                }
            } else {
                mw.visitFieldInsn(PUTFIELD, type(fieldInfo.declaringClass), fieldInfo.field.getName(),
                                  desc(fieldInfo.fieldClass));
            }
        } else if (fieldClass == float.class) {
            mw.visitVarInsn(ALOAD, context.var("instance"));
            mw.visitVarInsn(FLOAD, context.var(fieldInfo.name + "_asm"));
            _set(context, mw, fieldInfo);
        } else if (fieldClass == double.class) {
            mw.visitVarInsn(ALOAD, context.var("instance"));
            mw.visitVarInsn(DLOAD, context.var(fieldInfo.name + "_asm", 2));
            _set(context, mw, fieldInfo);
        } else if (fieldClass == String.class) {
            mw.visitVarInsn(ALOAD, context.var("instance"));
            mw.visitVarInsn(ALOAD, context.var(fieldInfo.name + "_asm"));
            _set(context, mw, fieldInfo);
        } else if (fieldClass.isEnum()) {
            mw.visitVarInsn(ALOAD, context.var("instance"));
            mw.visitVarInsn(ALOAD, context.var(fieldInfo.name + "_asm"));
            _set(context, mw, fieldInfo);
        } else if (Collection.class.isAssignableFrom(fieldClass)) {
            mw.visitVarInsn(ALOAD, context.var("instance"));
            Type itemType = TypeUtils.getCollectionItemClass(fieldType);
            if (itemType == String.class) {
                mw.visitVarInsn(ALOAD, context.var(fieldInfo.name + "_asm"));
                mw.visitTypeInsn(CHECKCAST, type(fieldClass)); // cast
            } else {
                mw.visitVarInsn(ALOAD, context.var(fieldInfo.name + "_asm"));
            }
            _set(context, mw, fieldInfo);

        } else {
            // mw.visitFieldInsn(GETSTATIC, getType(System.class), "out", "Ljava/io/PrintStream;");
            // mw.visitIntInsn(ILOAD, context.var(fieldInfo.name + "_asm_flag"));
            // mw.visitMethodInsn(INVOKEVIRTUAL, getType(java.io.PrintStream.class), "println", "(I)V");

            // _isFlag(mw, context, i, notSet_);

            mw.visitVarInsn(ALOAD, context.var("instance"));
            mw.visitVarInsn(ALOAD, context.var(fieldInfo.name + "_asm"));
            _set(context, mw, fieldInfo);

        }
    }

    private void _set(Context context, MethodVisitor mw, FieldInfo fieldInfo) {
        if (fieldInfo.method != null) {
            mw.visitMethodInsn(INVOKEVIRTUAL, type(fieldInfo.declaringClass), fieldInfo.method.getName(),
                               desc(fieldInfo.method));

            if (!fieldInfo.method.getReturnType().equals(Void.TYPE)) {
                mw.visitInsn(POP);
            }
        } else {
            mw.visitFieldInsn(PUTFIELD, type(fieldInfo.declaringClass), fieldInfo.field.getName(),
                              desc(fieldInfo.fieldClass));
        }
    }

    private void _setContext(Context context, MethodVisitor mw) {
        mw.visitVarInsn(ALOAD, 1); // parser
        mw.visitVarInsn(ALOAD, context.var("context"));
        mw.visitMethodInsn(INVOKEVIRTUAL, DefaultJSONParser, "setContext",
                           "(" + desc(ParseContext.class) + ")V");

        Label endIf_ = new Label();
        mw.visitVarInsn(ALOAD, context.var("childContext"));
        mw.visitJumpInsn(IFNULL, endIf_);

        mw.visitVarInsn(ALOAD, context.var("childContext"));
        mw.visitVarInsn(ALOAD, context.var("instance"));
        mw.visitFieldInsn(PUTFIELD, type(ParseContext.class), "object", "Ljava/lang/Object;");

        mw.visitLabel(endIf_);
    }

    private void _deserialize_endCheck(Context context, MethodVisitor mw, Label reset_) {
        // Label nextToken_ = new Label();

        // mw.visitFieldInsn(GETSTATIC, getType(System.class), "out", "Ljava/io/PrintStream;");
        // mw.visitIntInsn(ILOAD, context.var("matchedCount"));
        // mw.visitMethodInsn(INVOKEVIRTUAL, getType(java.io.PrintStream.class), "println", "(I)V");

        mw.visitIntInsn(ILOAD, context.var("matchedCount"));
        mw.visitJumpInsn(IFLE, reset_);

        // mw.visitFieldInsn(GETSTATIC, getType(System.class), "out", "Ljava/io/PrintStream;");
        // mw.visitVarInsn(ALOAD, context.var("lexer"));
        // mw.visitMethodInsn(INVOKEVIRTUAL, JSONLexerBase, "token", "()I");
        // mw.visitMethodInsn(INVOKEVIRTUAL, getType(java.io.PrintStream.class), "println", "(I)V");

        mw.visitVarInsn(ALOAD, context.var("lexer"));
        mw.visitMethodInsn(INVOKEVIRTUAL, JSONLexerBase, "token", "()I");
        mw.visitFieldInsn(GETSTATIC, JSONToken, "RBRACE", "I");
        mw.visitJumpInsn(IF_ICMPNE, reset_);

        // mw.visitLabel(nextToken_);
        mw.visitVarInsn(ALOAD, context.var("lexer"));
        mw.visitFieldInsn(GETSTATIC, JSONToken, "COMMA", "I");
        mw.visitMethodInsn(INVOKEVIRTUAL, JSONLexerBase, "nextToken", "(I)V");
    }

    private void _deserialze_list_obj(Context context, MethodVisitor mw, Label reset_, FieldInfo fieldInfo,
                                      Class<?> fieldClass, Class<?> itemType, int i) {
        Label _end_if = new Label();

        mw.visitMethodInsn(INVOKEVIRTUAL, JSONLexerBase, "matchField", "([C)Z");
        mw.visitJumpInsn(IFEQ, _end_if);

        _setFlag(mw, context, i);

        Label valueNotNull_ = new Label();
        mw.visitVarInsn(ALOAD, context.var("lexer"));
        mw.visitMethodInsn(INVOKEVIRTUAL, JSONLexerBase, "token", "()I");
        mw.visitFieldInsn(GETSTATIC, JSONToken, "NULL", "I");
        mw.visitJumpInsn(IF_ICMPNE, valueNotNull_);

        mw.visitVarInsn(ALOAD, context.var("lexer"));
        mw.visitFieldInsn(GETSTATIC, JSONToken, "COMMA", "I");
        mw.visitMethodInsn(INVOKEVIRTUAL, JSONLexerBase, "nextToken", "(I)V");
        mw.visitJumpInsn(GOTO, _end_if);
        // loop_end_

        mw.visitLabel(valueNotNull_);
        
        Label storeCollection_ = new Label();
        Label endSet_ = new Label();
        mw.visitVarInsn(ALOAD, context.var("lexer"));
        mw.visitMethodInsn(INVOKEVIRTUAL, JSONLexerBase, "token", "()I");
        mw.visitFieldInsn(GETSTATIC, JSONToken, "SET", "I");
        mw.visitJumpInsn(IF_ICMPNE, endSet_);

        mw.visitVarInsn(ALOAD, context.var("lexer"));
        mw.visitFieldInsn(GETSTATIC, JSONToken, "LBRACKET", "I");
        mw.visitMethodInsn(INVOKEVIRTUAL, JSONLexerBase, "nextToken", "(I)V");

        mw.visitVarInsn(ALOAD, context.var("lexer"));
        mw.visitMethodInsn(INVOKEVIRTUAL, JSONLexerBase, "token", "()I");
        mw.visitFieldInsn(GETSTATIC, JSONToken, "LBRACKET", "I");

        mw.visitJumpInsn(IF_ICMPNE, reset_);
        
        _newCollection(mw, fieldClass, i, true);
        
        mw.visitJumpInsn(GOTO, storeCollection_);
        
        mw.visitLabel(endSet_);
        
        // if (lexer.token() != JSONToken.LBRACKET) reset
        mw.visitVarInsn(ALOAD, context.var("lexer"));
        mw.visitMethodInsn(INVOKEVIRTUAL, JSONLexerBase, "token", "()I");
        mw.visitFieldInsn(GETSTATIC, JSONToken, "LBRACKET", "I");

        mw.visitJumpInsn(IF_ICMPNE, reset_);
        
        _newCollection(mw, fieldClass, i, false);

        mw.visitLabel(storeCollection_);
        mw.visitVarInsn(ASTORE, context.var(fieldInfo.name + "_asm"));

        _getCollectionFieldItemDeser(context, mw, fieldInfo, itemType);
        mw.visitMethodInsn(INVOKEINTERFACE, type(ObjectDeserializer.class),
                           "getFastMatchToken", "()I");
        mw.visitVarInsn(ISTORE, context.var("fastMatchToken"));

        mw.visitVarInsn(ALOAD, context.var("lexer"));
        mw.visitVarInsn(ILOAD, context.var("fastMatchToken"));
        mw.visitMethodInsn(INVOKEVIRTUAL, JSONLexerBase, "nextToken", "(I)V");


        { // setContext
            mw.visitVarInsn(ALOAD, 1);
            mw.visitMethodInsn(INVOKEVIRTUAL, DefaultJSONParser, "getContext",
                               "()" + desc(ParseContext.class));
            mw.visitVarInsn(ASTORE, context.var("listContext"));

            mw.visitVarInsn(ALOAD, 1); // parser
            mw.visitVarInsn(ALOAD, context.var(fieldInfo.name + "_asm"));
            mw.visitLdcInsn(fieldInfo.name);
            mw.visitMethodInsn(INVOKEVIRTUAL, DefaultJSONParser, "setContext",
                               "(Ljava/lang/Object;Ljava/lang/Object;)" + desc(ParseContext.class));
            mw.visitInsn(POP);
        }

        Label loop_ = new Label();
        Label loop_end_ = new Label();

        // for (;;) {
        mw.visitInsn(ICONST_0);
        mw.visitVarInsn(ISTORE, context.var("i"));
        mw.visitLabel(loop_);

        mw.visitVarInsn(ALOAD, context.var("lexer"));
        mw.visitMethodInsn(INVOKEVIRTUAL, JSONLexerBase, "token", "()I");
        mw.visitFieldInsn(GETSTATIC, JSONToken, "RBRACKET", "I");
        mw.visitJumpInsn(IF_ICMPEQ, loop_end_);

        // Object value = itemDeserializer.deserialze(parser, null);
        // array.add(value);

        mw.visitVarInsn(ALOAD, 0);
        mw.visitFieldInsn(GETFIELD, context.className, fieldInfo.name + "_asm_list_item_deser__",
                          desc(ObjectDeserializer.class));
        mw.visitVarInsn(ALOAD, 1);
        mw.visitLdcInsn(com.alibaba.fastjson.asm.Type.getType(desc(itemType)));
        mw.visitVarInsn(ILOAD, context.var("i"));
        mw.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;");
        mw.visitMethodInsn(INVOKEINTERFACE, type(ObjectDeserializer.class), "deserialze",
                           "(L" + DefaultJSONParser + ";Ljava/lang/reflect/Type;Ljava/lang/Object;)Ljava/lang/Object;");
        mw.visitVarInsn(ASTORE, context.var("list_item_value"));

        mw.visitIincInsn(context.var("i"), 1);

        mw.visitVarInsn(ALOAD, context.var(fieldInfo.name + "_asm"));
        mw.visitVarInsn(ALOAD, context.var("list_item_value"));
        if (fieldClass.isInterface()) {
            mw.visitMethodInsn(INVOKEINTERFACE, type(fieldClass), "add", "(Ljava/lang/Object;)Z");
        } else {
            mw.visitMethodInsn(INVOKEVIRTUAL, type(fieldClass), "add", "(Ljava/lang/Object;)Z");
        }
        mw.visitInsn(POP);

        mw.visitVarInsn(ALOAD, 1);
        mw.visitVarInsn(ALOAD, context.var(fieldInfo.name + "_asm"));
        mw.visitMethodInsn(INVOKEVIRTUAL, DefaultJSONParser, "checkListResolve",
                           "(Ljava/util/Collection;)V");

        // if (lexer.token() == JSONToken.COMMA) {
        // lexer.nextToken(itemDeserializer.getFastMatchToken());
        // continue;
        // }
        mw.visitVarInsn(ALOAD, context.var("lexer"));
        mw.visitMethodInsn(INVOKEVIRTUAL, JSONLexerBase, "token", "()I");
        mw.visitFieldInsn(GETSTATIC, JSONToken, "COMMA", "I");
        mw.visitJumpInsn(IF_ICMPNE, loop_);

        mw.visitVarInsn(ALOAD, context.var("lexer"));
        mw.visitVarInsn(ILOAD, context.var("fastMatchToken"));
        mw.visitMethodInsn(INVOKEVIRTUAL, JSONLexerBase, "nextToken", "(I)V");
        mw.visitJumpInsn(GOTO, loop_);

        mw.visitLabel(loop_end_);

        // mw.visitVarInsn(ASTORE, context.var("context"));
        // parser.setContext(context);
        { // setContext
            mw.visitVarInsn(ALOAD, 1); // parser
            mw.visitVarInsn(ALOAD, context.var("listContext"));
            mw.visitMethodInsn(INVOKEVIRTUAL, DefaultJSONParser, "setContext",
                               "(" + desc(ParseContext.class) + ")V");
        }

        mw.visitVarInsn(ALOAD, context.var("lexer"));
        mw.visitMethodInsn(INVOKEVIRTUAL, JSONLexerBase, "token", "()I");
        mw.visitFieldInsn(GETSTATIC, JSONToken, "RBRACKET", "I");
        mw.visitJumpInsn(IF_ICMPNE, reset_);

        mw.visitVarInsn(ALOAD, context.var("lexer"));
        mw.visitFieldInsn(GETSTATIC, JSONToken, "COMMA", "I");
        mw.visitMethodInsn(INVOKEVIRTUAL, JSONLexerBase, "nextToken", "(I)V");
        // lexer.nextToken(JSONToken.COMMA);

        mw.visitLabel(_end_if);
    }

    private void _getCollectionFieldItemDeser(Context context, MethodVisitor mw, FieldInfo fieldInfo,
                                              Class<?> itemType) {
        Label notNull_ = new Label();
        mw.visitVarInsn(ALOAD, 0);
        mw.visitFieldInsn(GETFIELD, context.className, fieldInfo.name + "_asm_list_item_deser__",
                          desc(ObjectDeserializer.class));
        mw.visitJumpInsn(IFNONNULL, notNull_);

        mw.visitVarInsn(ALOAD, 0);

        mw.visitVarInsn(ALOAD, 1);
        mw.visitMethodInsn(INVOKEVIRTUAL, DefaultJSONParser, "getConfig",
                           "()" + desc(ParserConfig.class));
        mw.visitLdcInsn(com.alibaba.fastjson.asm.Type.getType(desc(itemType)));
        mw.visitMethodInsn(INVOKEVIRTUAL, type(ParserConfig.class), "getDeserializer",
                           "(Ljava/lang/reflect/Type;)" + desc(ObjectDeserializer.class));

        mw.visitFieldInsn(PUTFIELD, context.className, fieldInfo.name + "_asm_list_item_deser__",
                          desc(ObjectDeserializer.class));

        mw.visitLabel(notNull_);
        mw.visitVarInsn(ALOAD, 0);
        mw.visitFieldInsn(GETFIELD, context.className, fieldInfo.name + "_asm_list_item_deser__",
                          desc(ObjectDeserializer.class));
    }

    private void _newCollection(MethodVisitor mw, Class<?> fieldClass, int i, boolean set) {
        if (fieldClass.isAssignableFrom(ArrayList.class) && !set) {
            mw.visitTypeInsn(NEW, "java/util/ArrayList");
            mw.visitInsn(DUP);
            mw.visitMethodInsn(INVOKESPECIAL, "java/util/ArrayList", "<init>", "()V");
        } else if (fieldClass.isAssignableFrom(LinkedList.class) && !set) {
            mw.visitTypeInsn(NEW, type(LinkedList.class));
            mw.visitInsn(DUP);
            mw.visitMethodInsn(INVOKESPECIAL, type(LinkedList.class), "<init>", "()V");
        } else if (fieldClass.isAssignableFrom(HashSet.class)) {
            mw.visitTypeInsn(NEW, type(HashSet.class));
            mw.visitInsn(DUP);
            mw.visitMethodInsn(INVOKESPECIAL, type(HashSet.class), "<init>", "()V");
        } else if (fieldClass.isAssignableFrom(TreeSet.class)) {
            mw.visitTypeInsn(NEW, type(TreeSet.class));
            mw.visitInsn(DUP);
            mw.visitMethodInsn(INVOKESPECIAL, type(TreeSet.class), "<init>", "()V");
        } else if (fieldClass.isAssignableFrom(LinkedHashSet.class)) {
            mw.visitTypeInsn(NEW, type(LinkedHashSet.class));
            mw.visitInsn(DUP);
            mw.visitMethodInsn(INVOKESPECIAL, type(LinkedHashSet.class), "<init>", "()V");
        } else if (set) {
            mw.visitTypeInsn(NEW, type(HashSet.class));
            mw.visitInsn(DUP);
            mw.visitMethodInsn(INVOKESPECIAL, type(HashSet.class), "<init>", "()V");
        } else {
            mw.visitVarInsn(ALOAD, 0);
            mw.visitLdcInsn(i);
            mw.visitMethodInsn(INVOKEVIRTUAL, type(JavaBeanDeserializer.class),
                               "getFieldType", "(I)Ljava/lang/reflect/Type;");
            mw.visitMethodInsn(INVOKESTATIC, type(TypeUtils.class),
                               "createCollection",
                               "(Ljava/lang/reflect/Type;)Ljava/util/Collection;");
        }
        mw.visitTypeInsn(CHECKCAST, type(fieldClass)); // cast
    }

    private void _deserialze_obj(Context context, MethodVisitor mw, Label reset_, FieldInfo fieldInfo,
                                 Class<?> fieldClass, int i) {
        Label matched_ = new Label();
        Label _end_if = new Label();

        mw.visitVarInsn(ALOAD, context.var("lexer"));
        mw.visitVarInsn(ALOAD, 0);
        mw.visitFieldInsn(GETFIELD, context.className, fieldInfo.name + "_asm_prefix__", "[C");
        mw.visitMethodInsn(INVOKEVIRTUAL, JSONLexerBase, "matchField", "([C)Z");
        mw.visitJumpInsn(IFNE, matched_);
        mw.visitInsn(ACONST_NULL);
        mw.visitVarInsn(ASTORE, context.var(fieldInfo.name + "_asm"));

        mw.visitJumpInsn(GOTO, _end_if);

        mw.visitLabel(matched_);

        _setFlag(mw, context, i);

        // increment matchedCount
        mw.visitVarInsn(ILOAD, context.var("matchedCount"));
        mw.visitInsn(ICONST_1);
        mw.visitInsn(IADD);
        mw.visitVarInsn(ISTORE, context.var("matchedCount"));

        _deserObject(context, mw, fieldInfo, fieldClass, i);

        mw.visitVarInsn(ALOAD, 1);
        mw.visitMethodInsn(INVOKEVIRTUAL, DefaultJSONParser, "getResolveStatus", "()I");
        mw.visitFieldInsn(GETSTATIC, DefaultJSONParser, "NeedToResolve", "I");
        mw.visitJumpInsn(IF_ICMPNE, _end_if);

        mw.visitVarInsn(ALOAD, 1);
        mw.visitMethodInsn(INVOKEVIRTUAL, DefaultJSONParser, "getLastResolveTask",
                           "()" + desc(ResolveTask.class));
        mw.visitVarInsn(ASTORE, context.var("resolveTask"));

        mw.visitVarInsn(ALOAD, context.var("resolveTask"));
        mw.visitVarInsn(ALOAD, 1);
        mw.visitMethodInsn(INVOKEVIRTUAL, DefaultJSONParser, "getContext",
                           "()" + desc(ParseContext.class));
        mw.visitFieldInsn(PUTFIELD, type(ResolveTask.class), "ownerContext",
                          desc(ParseContext.class));

        mw.visitVarInsn(ALOAD, context.var("resolveTask"));
        mw.visitVarInsn(ALOAD, 0);
        mw.visitLdcInsn(fieldInfo.name);
        mw.visitMethodInsn(INVOKEVIRTUAL, type(JavaBeanDeserializer.class),
                           "getFieldDeserializer",
                           "(Ljava/lang/String;)" + desc(FieldDeserializer.class));
        mw.visitFieldInsn(PUTFIELD, type(ResolveTask.class), "fieldDeserializer", desc(FieldDeserializer.class));

        mw.visitVarInsn(ALOAD, 1);
        mw.visitFieldInsn(GETSTATIC, DefaultJSONParser, "NONE", "I");
        mw.visitMethodInsn(INVOKEVIRTUAL, DefaultJSONParser, "setResolveStatus", "(I)V");

        mw.visitLabel(_end_if);

    }

    private void _deserObject(Context context, MethodVisitor mw, FieldInfo fieldInfo, Class<?> fieldClass, int i) {
        _getFieldDeser(context, mw, fieldInfo);

        mw.visitVarInsn(ALOAD, 1);
        if (fieldInfo.fieldType instanceof Class) {
            mw.visitLdcInsn(com.alibaba.fastjson.asm.Type.getType(desc(fieldInfo.fieldClass)));
        } else {
            mw.visitVarInsn(ALOAD, 0);
            mw.visitLdcInsn(i);
            mw.visitMethodInsn(INVOKEVIRTUAL, type(JavaBeanDeserializer.class),
                               "getFieldType", "(I)Ljava/lang/reflect/Type;");
        }
        mw.visitLdcInsn(fieldInfo.name);
        mw.visitMethodInsn(INVOKEINTERFACE, type(ObjectDeserializer.class), "deserialze",
                           "(L" + DefaultJSONParser + ";Ljava/lang/reflect/Type;Ljava/lang/Object;)Ljava/lang/Object;");
        mw.visitTypeInsn(CHECKCAST, type(fieldClass)); // cast
        mw.visitVarInsn(ASTORE, context.var(fieldInfo.name + "_asm"));
    }

    private void _getFieldDeser(Context context, MethodVisitor mw, FieldInfo fieldInfo) {
        Label notNull_ = new Label();
        mw.visitVarInsn(ALOAD, 0);
        mw.visitFieldInsn(GETFIELD, context.className, fieldInfo.name + "_asm_deser__",
                          desc(ObjectDeserializer.class));
        mw.visitJumpInsn(IFNONNULL, notNull_);

        mw.visitVarInsn(ALOAD, 0);

        mw.visitVarInsn(ALOAD, 1);
        mw.visitMethodInsn(INVOKEVIRTUAL, DefaultJSONParser, "getConfig",
                           "()" + desc(ParserConfig.class));
        mw.visitLdcInsn(com.alibaba.fastjson.asm.Type.getType(desc(fieldInfo.fieldClass)));
        mw.visitMethodInsn(INVOKEVIRTUAL, type(ParserConfig.class), "getDeserializer",
                           "(Ljava/lang/reflect/Type;)" + desc(ObjectDeserializer.class));

        mw.visitFieldInsn(PUTFIELD, context.className, fieldInfo.name + "_asm_deser__", desc(ObjectDeserializer.class));

        mw.visitLabel(notNull_);

        mw.visitVarInsn(ALOAD, 0);
        mw.visitFieldInsn(GETFIELD, context.className, fieldInfo.name + "_asm_deser__", desc(ObjectDeserializer.class));
    }

    static class Context {

        private int                        variantIndex = 5;
        private final Map<String, Integer> variants     = new HashMap<String, Integer>();

        private final Class<?>             clazz;
        private final JavaBeanInfo         beanInfo;
        private final String               className;
        private FieldInfo[]                fieldInfoList;

        public Context(String className, ParserConfig config, JavaBeanInfo beanInfo, int initVariantIndex){
            this.className = className;
            this.clazz = beanInfo.clazz;
            this.variantIndex = initVariantIndex;
            this.beanInfo = beanInfo;
            fieldInfoList = beanInfo.fields;
        }

        public Class<?> getInstClass() {
            Class<?> instClass = beanInfo.builderClass;
            if (instClass == null) {
                instClass = clazz;
            }

            return instClass;
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
        for (int i = 0, size = context.fieldInfoList.length; i < size; ++i) {
            FieldInfo fieldInfo = context.fieldInfoList[i];

            // public FieldVisitor visitField(final int access, final String name, final String desc, final String
            // signature, final Object value) {
            FieldWriter fw = new FieldWriter(cw, ACC_PUBLIC, fieldInfo.name + "_asm_prefix__", "[C");
            fw.visitEnd();
        }

        for (int i = 0, size = context.fieldInfoList.length; i < size; ++i) {
            FieldInfo fieldInfo = context.fieldInfoList[i];
            Class<?> fieldClass = fieldInfo.fieldClass;

            if (fieldClass.isPrimitive()) {
                continue;
            }

            if (fieldClass.isEnum()) {
                continue;
            }

            if (Collection.class.isAssignableFrom(fieldClass)) {
                FieldWriter fw = new FieldWriter(cw, ACC_PUBLIC, fieldInfo.name + "_asm_list_item_deser__",
                                                 desc(ObjectDeserializer.class));
                fw.visitEnd();
            } else {
                FieldWriter fw = new FieldWriter(cw, ACC_PUBLIC, fieldInfo.name + "_asm_deser__",
                                                 desc(ObjectDeserializer.class));
                fw.visitEnd();
            }
        }

        MethodVisitor mw = new MethodWriter(cw, ACC_PUBLIC, "<init>",
                                            "(" + desc(ParserConfig.class) + "Ljava/lang/Class;)V", null,
                                            null);
        mw.visitVarInsn(ALOAD, 0);
        mw.visitVarInsn(ALOAD, 1);
        mw.visitVarInsn(ALOAD, 2);
        mw.visitMethodInsn(INVOKESPECIAL, type(JavaBeanDeserializer.class), "<init>",
                           "(" + desc(ParserConfig.class) + "Ljava/lang/Class;)V");

        // init fieldNamePrefix
        for (int i = 0, size = context.fieldInfoList.length; i < size; ++i) {
            FieldInfo fieldInfo = context.fieldInfoList[i];

            mw.visitVarInsn(ALOAD, 0);
            mw.visitLdcInsn("\"" + fieldInfo.name + "\":"); // public char[] toCharArray()
            mw.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "toCharArray", "()[C");
            mw.visitFieldInsn(PUTFIELD, context.className, fieldInfo.name + "_asm_prefix__", "[C");

        }

        mw.visitInsn(RETURN);
        mw.visitMaxs(4, 4);
        mw.visitEnd();
    }

    private void _createInstance(ClassWriter cw, Context context) {
        MethodVisitor mw = new MethodWriter(cw, ACC_PUBLIC, "createInstance",
                                            "(L" + DefaultJSONParser + ";Ljava/lang/reflect/Type;)Ljava/lang/Object;",
                                            null, null);

        mw.visitTypeInsn(NEW, type(context.getInstClass()));
        mw.visitInsn(DUP);
        mw.visitMethodInsn(INVOKESPECIAL, type(context.getInstClass()), "<init>", "()V");

        mw.visitInsn(ARETURN);
        mw.visitMaxs(3, 3);
        mw.visitEnd();
    }

}
