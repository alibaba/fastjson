package com.alibaba.fastjson.codegen;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

import com.alibaba.fastjson.util.DeserializeBeanInfo;
import com.alibaba.fastjson.util.FieldInfo;
import com.alibaba.fastjson.util.TypeUtils;

public class DeserializerGen extends ClassGen {

    private DeserializeBeanInfo beanInfo;
    private String              genClassName;

    public DeserializerGen(Class<?> clazz, Appendable out){
        super(clazz, out);
    }

    @Override
    public void gen() throws IOException {
        beanInfo = DeserializeBeanInfo.computeSetters(clazz, type);
        genClassName = clazz.getSimpleName() + "GenDecoder";

        print("package ");
        print(clazz.getPackage().getName());
        println(";");
        println();

        println("import java.lang.reflect.Type;");
        println();

        println("import com.alibaba.fastjson.parser.DefaultJSONParser;");
        println("import com.alibaba.fastjson.parser.DefaultJSONParser.ResolveTask;");
        println("import com.alibaba.fastjson.parser.ParserConfig;");
        println("import com.alibaba.fastjson.parser.Feature;");
        println("import com.alibaba.fastjson.parser.JSONLexerBase;");
        println("import com.alibaba.fastjson.parser.JSONToken;");
        println("import com.alibaba.fastjson.parser.ParseContext;");
        println("import com.alibaba.fastjson.parser.deserializer.ASMJavaBeanDeserializer;");
        println("import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;");
        println();

        print("public class ");
        print(genClassName);
        print(" extends ASMJavaBeanDeserializer implements ObjectDeserializer {");
        incrementIndent();
        println();

        genConstructor();

        genCreateInstance();

        genDeserialze();

        endClass();
    }

    protected void genCreateInstance() throws IOException {
        println();
        print("public Object createInstance(DefaultJSONParser parser, Type type) {");
        incrementIndent();
        println();

        print("return new ");
        print(clazz.getSimpleName());
        print("();");
        println();

        decrementIndent();
        println();
        print("}");
    }

    protected void genDeserialze() throws IOException {
        if (beanInfo.getFieldList().size() == 0) {
            return;
        }

        for (FieldInfo fieldInfo : beanInfo.getFieldList()) {
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

        List<FieldInfo> fieldList = new ArrayList<FieldInfo>(beanInfo.getFieldList());
        Collections.sort(fieldList);

        println();
        print("public Object deserialze(DefaultJSONParser parser, Type type, Object fieldName) {");
        incrementIndent();
        println();

        println("JSONLexerBase lexer = (JSONLexerBase) parser.getLexer();");
        println();

        println("if (!lexer.isEnabled(Feature.SortFeidFastMatch)) {");
        println("\treturn super.deserialze(parser, type, fieldName);");
        println("}");

        println();

        println("if (isSupportArrayToBean(lexer)) {");
        println("\t// deserialzeArrayMapping");
        println("}");

        println();
        println("if (lexer.scanType(\"Department\") == JSONLexerBase.NOT_MATCH) {");
        println("\treturn super.deserialze(parser, type, fieldName);");
        println("}");

        println();

        println("ParseContext mark_context = parser.getContext();");
        println("int matchedCount = 0;");

        print(clazz.getSimpleName());
        print(" instance = ");
        Constructor<?> defaultConstructor = beanInfo.getDefaultConstructor();
        if (Modifier.isPublic(defaultConstructor.getModifiers())) {
            print("new ");
            print(clazz.getSimpleName());
            println("();");
        } else {
            print("(");
            print(clazz.getSimpleName());
            print(") createInstance(parser);");
        }

        println();

        println("ParseContext context = parser.getContext();");
        println("ParseContext childContext = parser.setContext(context, instance, fieldName);");

        println();

        println("if (lexer.matchStat == JSONLexerBase.END) {");
        println("\treturn instance;");
        println("}");

        println();

        println("int matchStat = 0;");

        int fieldListSize = fieldList.size();
        for (int i = 0; i < fieldListSize; i += 32) {
            print("int _asm_flag_");
            print(Integer.toString(i / 32));
            println(" = 0;");
        }

        for (int i = 0; i < fieldListSize; ++i) {
            FieldInfo fieldInfo = fieldList.get(i);
            Class<?> fieldClass = fieldInfo.getFieldClass();

            if (fieldClass == boolean.class) {
                print("boolean ");
                printFieldVarName(fieldInfo);
                println(" = false;");
            } else if (fieldClass == byte.class //
                       || fieldClass == short.class //
                       || fieldClass == int.class //
                       || fieldClass == long.class //
                       || fieldClass == float.class //
                       || fieldClass == double.class //
            ) {
                print(fieldClass.getSimpleName());
                print(" ");
                printFieldVarName(fieldInfo);
                println(" = 0;");
            } else {
                if (fieldClass == String.class) {
                    print("String ");
                    printFieldVarName(fieldInfo);
                    println(";");

                    println("if (lexer.isEnabled(Feature.InitStringFieldAsEmpty)) {");
                    print("\t");
                    printFieldVarName(fieldInfo);
                    println(" = lexer.stringDefaultValue();");
                    print("\t");
                    genSetFlag(i);
                    println("} else {");
                    print("\t");
                    printFieldVarName(fieldInfo);
                    println(" = null;");
                    println("}");
                } else {
                    printClassName(fieldClass);
                    print(" ");
                    printFieldVarName(fieldInfo);
                    print(" = null;");
                    println();
                }
            }
        }

        println("boolean endFlag = false, restFlag = false;");
        println();

        for (int i = 0; i < fieldListSize; ++i) {
            print("if ((!endFlag) && (!restFlag)) {");
            incrementIndent();
            println();

            FieldInfo fieldInfo = fieldList.get(i);
            Class<?> fieldClass = fieldInfo.getFieldClass();
            Type fieldType = fieldInfo.getFieldType();

            if (fieldClass == boolean.class) {
                printFieldVarName(fieldInfo);
                print(" = lexer.scanFieldBoolean(");
                printFieldPrefix(fieldInfo);
                println(");");
            } else if (fieldClass == byte.class || fieldClass == short.class || fieldClass == int.class) {
                printFieldVarName(fieldInfo);
                print(" = lexer.scanFieldInt(");
                printFieldPrefix(fieldInfo);
                println(");");
            } else if (fieldClass == long.class) {
                printFieldVarName(fieldInfo);
                print(" = lexer.scanFieldLong(");
                printFieldPrefix(fieldInfo);
                println(");");
            } else if (fieldClass == float.class) {
                printFieldVarName(fieldInfo);
                print(" = lexer.scanFieldFloat(");
                printFieldPrefix(fieldInfo);
                println(");");
            } else if (fieldClass == double.class) {
                printFieldVarName(fieldInfo);
                print(" = lexer.scanFieldDouble(");
                printFieldPrefix(fieldInfo);
                println(");");
            } else if (fieldClass == String.class) {
                printFieldVarName(fieldInfo);
                print(" = lexer.scanFieldString(");
                printFieldPrefix(fieldInfo);
                println(");");
            } else if (fieldClass.isEnum()) {
                print("String ");
                printFieldVarEnumName(fieldInfo);

                print(" = lexer.scanFieldSymbol(");
                printFieldPrefix(fieldInfo);
                println(", parser.getSymbolTable());");

                print("if (");
                printFieldVarEnumName(fieldInfo);
                println(" == null) {");
                print("\t");
                printFieldVarName(fieldInfo);
                print(" = ");
                printClassName(fieldClass);
                print(".valueOf(");
                printFieldVarEnumName(fieldInfo);
                println(");");
                println("}");
            } else if (Collection.class.isAssignableFrom(fieldClass)) {
                Class<?> itemClass = TypeUtils.getCollectionItemClass(fieldType);

                if (itemClass == String.class) {
                    printFieldVarName(fieldInfo);
                    print(" = (");
                    printClassName(fieldClass);
                    print(") lexer.scanFieldStringArray(");
                    printFieldPrefix(fieldInfo);
                    print(", ");
                    printClassName(fieldClass);
                    print(".class);");
                    println();
                } else {
                    genDeserialzeList(fieldInfo, i, itemClass);
                    if (i == fieldListSize - 1) {
                        genEndCheck();
                    }
                }
            } else {
                genDeserialzeObject(fieldInfo, i);

                if (i == fieldListSize - 1) {
                    genEndCheck();
                }
            }

            println("if(lexer.matchStat > 0) {");
            print("\t");
            genSetFlag(i);
            println("\tmatchedCount++;");
            println("}");

            println("if(lexer.matchStat == JSONLexerBase.NOT_MATCH) {");
            println("\trestFlag = true;");
            println("}");

  
            println("if(lexer.matchStat != JSONLexerBase.END) {");
            println("\tendFlag = true;");
            println("}");
            

            decrementIndent();
            println();
            println("}");
        }

        genBatchSet(fieldList, true);

        println();
        println("if (restFlag) {");
        println("\treturn super.parseRest(parser, type, fieldName, instance);");
        println("}");

        println();
        print("return instance;");
        println();

        decrementIndent();
        println();
        print("}");
    }

    private void genBatchSet(List<FieldInfo> fieldList, boolean flag) throws IOException {
        for (int i = 0, size = fieldList.size(); i < size; ++i) {
            FieldInfo fieldInfo = fieldList.get(i);
            
            String varName = "_asm_flag_" + (i / 32);
            if (flag) {
                print("if ((");
                print(varName);
                print(" & ");
                print(Integer.toString(1 << i));
                print(") != 0) {");
                println();
                incrementIndent();
            }
            
            if (fieldInfo.getMethod() != null) {
                print("\tinstance.");
                print(fieldInfo.getMethod().getName());
                print("(");
                printFieldVarName(fieldInfo);
                println(");");
            } else {
                print("\tinstance.");
                print(fieldInfo.getField().getName());
                print(" = ");
                printFieldVarName(fieldInfo);
                println(";");
            }

            if (flag) {
                decrementIndent();
                println();
                println("}");
            }
        }
    }

    private void genEndCheck() throws IOException {
        println("if (matchedCount <= 0 || lexer.token() != JSONToken.RBRACE) {");
        println("\trestFlag = true;");
        println("} else if (lexer.token() == JSONToken.COMMA) {");
        println("\tlexer.nextToken();");
        println("}");
    }

    protected void genDeserialzeList(FieldInfo fieldInfo, int i, Class<?> itemClass) throws IOException {
        print("if (lexer.matchField(");
        printFieldPrefix(fieldInfo);
        print(")) {");
        println();
        print("\t");
        genSetFlag(i);
        println("\tif (lexer.token() == JSONToken.NULL) {");
        println("\t\tlexer.nextToken(JSONToken.COMMA);");
        println("\t} else {");
        println("\t\tif (lexer.token() == JSONToken.LBRACKET) {");
        print("\t\t\tif(");
        printListFieldItemDeser(fieldInfo);
        print(" == null) {");
        println();

        print("\t\t\t\t");
        printListFieldItemDeser(fieldInfo);
        print(" = parser.getConfig().getDeserializer(");
        printClassName(itemClass);
        print(".class);");
        println();

        print("\t\t\t}");
        println();

        print("\t\t\tfinal int fastMatchToken = ");
        printListFieldItemDeser(fieldInfo);
        print(".getFastMatchToken();");
        println();
        println("\t\t\tlexer.nextToken(fastMatchToken);");

        // _newCollection
        print("\t\t\t");
        printFieldVarName(fieldInfo);
        print(" = ");
        Class<?> fieldClass = fieldInfo.getFieldClass();
        if (fieldClass.isAssignableFrom(ArrayList.class)) {
            print("new java.util.ArrayList();");
        } else if (fieldClass.isAssignableFrom(LinkedList.class)) {
            print("new java.util.LinkedList();");
        } else if (fieldClass.isAssignableFrom(HashSet.class)) {
            print("new java.util.HashSet();");
        } else if (fieldClass.isAssignableFrom(TreeSet.class)) {
            print("new java.util.TreeSet();");
        } else {
            print("new ");
            printClassName(fieldClass);
            print("();");
        }
        println();

        println("\t\t\tParseContext listContext = parser.getContext();");
        print("\t\t\tparser.setContext(");
        printFieldVarName(fieldInfo);
        print(", \"");
        print(fieldInfo.getName());
        print("\");");
        println();

        println();
        println("\t\t\tfor(int i = 0; ;++i) {");

        println("\t\t\t\tif (lexer.token() == JSONToken.RBRACKET) {");
        println("\t\t\t\t\tbreak;");
        println("\t\t\t\t}");
        print("\t\t\t\t");
        printClassName(itemClass);
        print(" itemValue = ");
        printListFieldItemDeser(fieldInfo);
        print(".deserialze(parser, ");
        printListFieldItemType(fieldInfo);
        println(", i);");

        print("\t\t\t\t");
        printFieldVarName(fieldInfo);
        println(".add(itemValue);");

        print("\t\t\t\tparser.checkListResolve(");
        printFieldVarName(fieldInfo);
        println(");");

        println("\t\t\t\tif (lexer.token() == JSONToken.COMMA) {");
        println("\t\t\t\t\tlexer.nextToken(fastMatchToken);");
        println("\t\t\t\t}");

        // end for
        println("\t\t\t}");

        println("\t\t\tparser.setContext(listContext);");

        println("\t\t\tif (lexer.token() != JSONToken.RBRACKET) {");
        println("\t\t\t\trestFlag = true;");
        println("\t\t\t}");
        println("\t\t\tlexer.nextToken(JSONToken.COMMA);");

        println();
        println("\t\t} else {");
        println("\t\t\trestFlag = true;");
        println("\t\t}");
        println("\t}");
        println("}");
    }

    protected void genDeserialzeObject(FieldInfo fieldInfo, int i) throws IOException {
        print("if (lexer.matchField(");
        printFieldPrefix(fieldInfo);
        print(")) {");
        println();
        print("\t");
        genSetFlag(i);
        println("\tmatchedCount++;");

        // _deserObject
        print("if (");
        printFieldDeser(fieldInfo);
        print(" == null) {");
        println();

        print("\t");
        printFieldDeser(fieldInfo);
        print(" = parser.getConfig().getDeserializer(");
        printClassName(fieldInfo.getFieldClass());
        println(".class);");
        println("}");

        print("\t");
        printFieldDeser(fieldInfo);
        print(".deserialze(parser, ");
        if (fieldInfo.getFieldType() instanceof Class) {
            printClassName(fieldInfo.getFieldClass());
            print(".class");
        } else {
            print("getFieldType(\"");
            println(fieldInfo.getName());
            print("\")");
        }
        print(",\"");
        print(fieldInfo.getName());
        println("\");");

        println("\tif(parser.getResolveStatus() == DefaultJSONParser.NeedToResolve) {");
        println("\t\tResolveTask resolveTask = parser.getLastResolveTask();");
        println("\t\tresolveTask.setOwnerContext(parser.getContext());");
        print("\t\tresolveTask.setFieldDeserializer(this.getFieldDeserializer(\"");
        print(fieldInfo.getName());
        println("\"));");
        println("\t\tparser.setResolveStatus(DefaultJSONParser.NONE);");
        println("\t}");
        println("}");
    }

    private void printFieldVarName(FieldInfo fieldInfo) throws IOException {
        print(fieldInfo.getName());
        print("_gen");
    }

    private void printFieldVarEnumName(FieldInfo fieldInfo) throws IOException {
        print(fieldInfo.getName());
        print("_gen_enum_name");
    }

    private void printFieldPrefix(FieldInfo fieldInfo) throws IOException {
        print(fieldInfo.getName());
        print("_gen_prefix__");
    }

    private void printListFieldItemDeser(FieldInfo fieldInfo) throws IOException {
        print(fieldInfo.getName());
        print("_gen_list_item_deser__");
    }

    private void printFieldDeser(FieldInfo fieldInfo) throws IOException {
        print(fieldInfo.getName());
        print("_gen_deser__");
    }

    private void printListFieldItemType(FieldInfo fieldInfo) throws IOException {
        print(fieldInfo.getName());
        print("_gen_list_item_type__");
    }

    private void genSetFlag(int flag) throws IOException {
        String varName = "_asm_flag_" + (flag / 32);
        print(varName);
        print(" |= ");
        print(Integer.toString(1 << flag));
        print(";");
        println();
    }

    protected void genConstructor() throws IOException {
        for (int i = 0, size = beanInfo.getFieldList().size(); i < size; ++i) {
            FieldInfo fieldInfo = beanInfo.getFieldList().get(i);
            print("private char[] ");
            printFieldPrefix(fieldInfo);
            print(" = \"\\\"");
            print(fieldInfo.getName());
            print("\\\":\".toCharArray();");
            println();
        }

        println();

        boolean fieldDeserFlag = false;
        for (int i = 0, size = beanInfo.getFieldList().size(); i < size; ++i) {
            FieldInfo fieldInfo = beanInfo.getFieldList().get(i);
            Class<?> fieldClass = fieldInfo.getFieldClass();

            if (fieldClass.isPrimitive()) {
                continue;
            }

            if (fieldClass.isEnum()) {
                continue;
            }

            print("private ObjectDeserializer ");

            if (Collection.class.isAssignableFrom(fieldClass)) {
                printListFieldItemDeser(fieldInfo);
            } else {
                printFieldDeser(fieldInfo);
            }
            println(";");
            fieldDeserFlag = true;

            if (Collection.class.isAssignableFrom(fieldClass)) {
                print("private Type ");
                printListFieldItemType(fieldInfo);
                print(" = ");
                Class<?> fieldItemClass = TypeUtils.getCollectionItemClass(fieldInfo.getFieldType());
                printClassName(fieldItemClass);
                println(".class;");
            }
        }

        if (fieldDeserFlag) {
            println();
        }

        // constructor
        print("public ");
        print(genClassName);
        print(" (ParserConfig config, Class clazz) {");
        incrementIndent();
        println();

        println("super(config, clazz);");

        decrementIndent();
        println();
        print("}");
        println();
    }

}
