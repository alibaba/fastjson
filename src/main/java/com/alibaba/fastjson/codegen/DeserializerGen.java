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
        print("public ");
        print(clazz.getSimpleName());
        print(" createInstance(DefaultJSONParser parser, Type type) {");
        incrementIndent();
        println();

        print("return new ");
        print(clazz.getSimpleName());
        print("();");

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
        print("public ");
        print(clazz.getSimpleName());
        print(" deserialze(DefaultJSONParser parser, Type type, Object fieldName) {");
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
        print("if (lexer.scanType(\"");
        printClassName(clazz);
        println(")\") == JSONLexerBase.NOT_MATCH) {");
        println("\treturn super.deserialze(parser, type, fieldName);");
        println("}");

        println();

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
        println("parser.setContext(context, instance, fieldName);");
        println();

        println("if (lexer.matchStat == JSONLexerBase.END) {");
        println("\treturn instance;");
        println("}");

        println();

        // println("boolean endFlag = false, restFlag = false;");
        // println();

        int fieldListSize = fieldList.size();

        for (int i = 0; i < fieldListSize; ++i) {
            if (i == 0) {
                print("{");
            } else {
                // print("if ((!endFlag) && (!restFlag)) {");
                print("if (lexer.matchStat != JSONLexerBase.END) {");
            }
            incrementIndent();
            println();

            FieldInfo fieldInfo = fieldList.get(i);
            Class<?> fieldClass = fieldInfo.getFieldClass();
            Type fieldType = fieldInfo.getFieldType();

            boolean printSetField = true;
            if (fieldClass == boolean.class) {
                print("boolean ");
                printFieldVarName(fieldInfo);
                print(" = lexer.scanFieldBoolean(");
                printFieldPrefix(fieldInfo);
                println(");");
            } else if (fieldClass == byte.class || fieldClass == short.class || fieldClass == int.class) {
                print(fieldClass.getSimpleName());
                print(" ");
                printFieldVarName(fieldInfo);
                print(" = lexer.scanFieldInt(");
                printFieldPrefix(fieldInfo);
                println(");");
            } else if (fieldClass == long.class) {
                print("long ");
                printFieldVarName(fieldInfo);
                print(" = lexer.scanFieldLong(");
                printFieldPrefix(fieldInfo);
                println(");");
            } else if (fieldClass == float.class) {
                print("float ");
                printFieldVarName(fieldInfo);
                print(" = lexer.scanFieldFloat(");
                printFieldPrefix(fieldInfo);
                println(");");
            } else if (fieldClass == double.class) {
                print("double ");
                printFieldVarName(fieldInfo);
                print(" = lexer.scanFieldDouble(");
                printFieldPrefix(fieldInfo);
                println(");");
            } else if (fieldClass == String.class) {
                print("String ");
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

            } else if (Collection.class.isAssignableFrom(fieldClass)) {
                Class<?> itemClass = TypeUtils.getCollectionItemClass(fieldType);

                if (itemClass == String.class) {
                    printClassName(fieldClass);
                    print("<String> ");
                    printFieldVarName(fieldInfo);
                    print(" = (");
                    printClassName(fieldClass);
                    print("<String>) lexer.scanFieldStringArray(");
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
                    printSetField = false;
                }
            } else {
                genDeserialzeObject(fieldInfo, i);
            }

            if (printSetField) {
                println("if(lexer.matchStat > 0) {");
                if (fieldClass.isEnum()) {
                    print("\t");
                    printClassName(fieldClass);
                    print(" ");
                    printFieldVarName(fieldInfo);
                    print(" = ");
                    printClassName(fieldClass);
                    print(".valueOf(");
                    printFieldVarEnumName(fieldInfo);
                    println(");");
                }

                print("\t");
                genSetField(fieldInfo);

                if (String.class == fieldClass) {
                    println("} else if (lexer.isEnabled(Feature.InitStringFieldAsEmpty)) {");
                    print("\t");
                    printFieldVarName(fieldInfo);
                    println(" = lexer.stringDefaultValue();");
                    // genSetFlag(i);
                    print("}");
                } else {
                    print("}");
                }
            }

            // println("if(lexer.matchStat == JSONLexerBase.NOT_MATCH) {");
            // println("\trestFlag = true;");
            // println("} else if(lexer.matchStat != JSONLexerBase.END) {");
            // println("\tendFlag = true;");
            // print("}");

            decrementIndent();
            println();
            println("}");
        }

        // genBatchSet(fieldList, true);

        println();
        println("if (lexer.matchStat != JSONLexerBase.END) {");
        println("\tparser.setContext(context);");
        println();

        print("\treturn (");
        print(clazz.getSimpleName());
        print(") super.parseRest(parser, type, fieldName, instance);");
        println();
        println("}");
        
        println();
        println("lexer.matchStat = 0;");

        println();
        print("return instance;");
        println();

        decrementIndent();
        println();
        print("}");
    }


    protected void genSetField(FieldInfo fieldInfo) throws IOException {
        if (fieldInfo.getMethod() != null) {
            print("instance.");
            print(fieldInfo.getMethod().getName());
            print("(");
            printFieldVarName(fieldInfo);
            println(");");
        } else {
            print("instance.");
            print(fieldInfo.getField().getName());
            print(" = ");
            printFieldVarName(fieldInfo);
            println(";");
        }
    }

    protected String getFlagVarName(int i) {
        boolean duplateName = false;
        for (FieldInfo fieldInfo : beanInfo.getFieldList()) {
            if (fieldInfo.getName().equals("valueFlag")) {
                duplateName = true;
                break;
            }
        }

        if ((!duplateName) && beanInfo.getFieldList().size() < 32) {
            return "valueFlag";
        }

        return "__valueFlag_" + (i / 32);
    }

    private void genEndCheck() throws IOException {
        println("\tif (lexer.token() != JSONToken.RBRACE) {");
        println("\t\tthrow new com.alibaba.fastjson.JSONException(\"exepct '}', but \" + lexer.tokenName());");
        println("\t} else if (lexer.token() == JSONToken.COMMA) {");
        println("\t\tlexer.nextToken();");
        println("\t}");
    }

    protected void genDeserialzeList(FieldInfo fieldInfo, int i, Class<?> itemClass) throws IOException {
        Class<?> fieldClass = fieldInfo.getFieldClass();

        print("if (lexer.matchField(");
        printFieldPrefix(fieldInfo);
        print(")) {");
        println();

        print("\t");
        printClassName(fieldClass);
        print("<");
        printClassName(itemClass);
        print("> ");
        printFieldVarName(fieldInfo);
        println(" = null;");

        println("\tif (lexer.token() == JSONToken.NULL) {");
        println("\t\tlexer.nextToken(JSONToken.COMMA);");
        println("\t} else {");

        print("\t\t");
        printFieldVarName(fieldInfo);
        print(" = ");

        if (fieldClass.isAssignableFrom(ArrayList.class)) {
            print("new java.util.ArrayList");
            print("<");
            printClassName(itemClass);
            print("> ");
        } else if (fieldClass.isAssignableFrom(LinkedList.class)) {
            print("new java.util.LinkedList");
            print("<");
            printClassName(itemClass);
            print("> ");
        } else if (fieldClass.isAssignableFrom(HashSet.class)) {
            print("new java.util.HashSet");
            print("<");
            printClassName(itemClass);
            print("> ");
        } else if (fieldClass.isAssignableFrom(TreeSet.class)) {
            print("new java.util.TreeSet");
            print("<");
            printClassName(itemClass);
            print("> ");
        } else {
            print("new ");
            printClassName(fieldClass);
        }
        print("();");
        println();

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

        println("\t\t\tlexer.nextToken(JSONToken.COMMA);");

        println();
        println("\t\t} else {");
        print("\t\t\tparser.parseArray(");
        printListFieldItemType(fieldInfo);
        print(", ");
        printFieldVarName(fieldInfo);
        println(");");
        println("\t\t}");
        println("\t}");

        print("\t");
        genSetField(fieldInfo);

        println("}");
    }

    protected void genDeserialzeObject(FieldInfo fieldInfo, int i) throws IOException {
        printClassName(fieldInfo.getFieldClass());
        print(" ");
        printFieldVarName(fieldInfo);
        print(" = null;");
        println();

        print("if (lexer.matchField(");
        printFieldPrefix(fieldInfo);
        print(")) {");
        println();
        print("\t");

        // _deserObject
        print("if (");
        printFieldDeser(fieldInfo);
        print(" == null) {");
        println();

        print("\t\t");
        printFieldDeser(fieldInfo);
        print(" = parser.getConfig().getDeserializer(");
        printClassName(fieldInfo.getFieldClass());
        println(".class);");
        println("\t}");

        print("\t");
        printFieldVarName(fieldInfo);
        print(" = ");
        printFieldDeser(fieldInfo);
        print(".deserialze(parser, ");
        if (fieldInfo.getFieldType() instanceof Class) {
            printClassName(fieldInfo.getFieldClass());
            print(".class");
        } else {
            print("getFieldType(\"");
            print(fieldInfo.getName());
            print("\")");
        }
        print(", \"");
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
        
        if (i == beanInfo.getFieldList().size() - 1) {
            genEndCheck();
        }
        
        println("}");
    }

    private void printFieldVarName(FieldInfo fieldInfo) throws IOException {
        if ("type".equals(fieldInfo.getName())) {
            print("typeVal");
        } else {
            print(fieldInfo.getName());
        }
    }

    private void printFieldVarEnumName(FieldInfo fieldInfo) throws IOException {
        print(fieldInfo.getName());
        print("_gen_enum_name");
    }

    private void printFieldPrefix(FieldInfo fieldInfo) throws IOException {
        print("PF_");
        print(fieldInfo.getName().toUpperCase());
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

    protected void genConstructor() throws IOException {
        for (int i = 0, size = beanInfo.getFieldList().size(); i < size; ++i) {
            FieldInfo fieldInfo = beanInfo.getFieldList().get(i);
            print("final static char[] ");
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

            if (fieldClass == String.class) {
                continue;
            }

            if (fieldClass != String.class) {
                if (Collection.class.isAssignableFrom(fieldClass)) {
                    Class<?> fieldItemClass = TypeUtils.getCollectionItemClass(fieldInfo.getFieldType());
                    if (fieldItemClass != String.class) {
                        print("private ObjectDeserializer ");
                        printListFieldItemDeser(fieldInfo);
                        println(";");
                        fieldDeserFlag = true;
                    }
                } else {
                    print("private ObjectDeserializer ");
                    printFieldDeser(fieldInfo);
                    println(";");
                    fieldDeserFlag = true;
                }
            }

            if (Collection.class.isAssignableFrom(fieldClass)) {
                Class<?> fieldItemClass = TypeUtils.getCollectionItemClass(fieldInfo.getFieldType());

                if (fieldItemClass != String.class) {
                    print("private Type ");
                    printListFieldItemType(fieldInfo);
                    print(" = ");

                    printClassName(fieldItemClass);
                    println(".class;");
                }
            }
        }

        if (fieldDeserFlag) {
            println();
        }

        // constructor
        print("public ");
        print(genClassName);
        print(" (ParserConfig config, Class<?> clazz) {");
        incrementIndent();
        println();

        println("super(config, clazz);");

        decrementIndent();
        println();
        print("}");
        println();
    }

}
