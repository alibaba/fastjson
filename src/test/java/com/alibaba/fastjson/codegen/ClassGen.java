package com.alibaba.fastjson.codegen;

import java.io.IOException;
import java.lang.reflect.Type;

public abstract class ClassGen {

    protected Class<?>   clazz;
    protected Type       type;
    protected Appendable out;

    private String       indent      = "\t";
    private int          indentCount = 0;

    public ClassGen(Class<?> clazz, Appendable out){
        this(clazz, null, out);
    }

    public ClassGen(Class<?> clazz, Type type, Appendable out){
        this.clazz = clazz;
        this.type = type;
        this.out = out;
    }

    public abstract void gen() throws IOException;

    protected void println() throws IOException {
        out.append("\n");
        printIndent();
    }

    protected void println(String text) throws IOException {
        out.append(text);
        out.append("\n");
        printIndent();
    }

    protected void print(String text) throws IOException {
        out.append(text);
    }

    protected void printPackage() throws IOException {
        print("package ");
        print(clazz.getPackage().getName());
        println(";");
    }

    protected void beginClass(String className) throws IOException {
        print("public class ");
        print(className);
        print(" implements ObjectDeserializer {");
        incrementIndent();
        println();
    }

    protected void endClass() throws IOException {
        decrementIndent();
        println();
        print("}");
        println();
    }

    protected void genField(String name, Class<?> feildClass) throws IOException {
        if (feildClass == char[].class) {
            print("char[]");
        }

        print(" ");
        print(name);
        println(";");
    }

    protected void beginInit(String className) throws IOException {
        print("public ");
        print(className);
        println(" () {");
        incrementIndent();
    }

    protected void endInit() throws IOException {
        decrementIndent();
        print("}");
        println();
    }
    
    public void decrementIndent() {
        this.indentCount -= 1;
    }

    public void incrementIndent() {
        this.indentCount += 1;
    }

    public void printIndent() throws IOException {
        for (int i = 0; i < this.indentCount; ++i) {
            print(this.indent);
        }
    }
    
    protected void printClassName(Class<?> clazz) throws IOException {
        String name = clazz.getName();
        name = name.replace('$', '.');
        print(name);
    }
}
