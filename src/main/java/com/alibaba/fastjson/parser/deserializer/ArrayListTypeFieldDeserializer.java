package com.alibaba.fastjson.parser.deserializer;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.ParseContext;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.util.FieldInfo;
import com.alibaba.fastjson.util.ParameterizedTypeImpl;
import jdk.nashorn.internal.parser.JSONParser;

public class ArrayListTypeFieldDeserializer extends FieldDeserializer {

    private final Type         itemType;
    private int                itemFastMatchToken;
    private ObjectDeserializer deserializer;

    public ArrayListTypeFieldDeserializer(ParserConfig mapping, Class<?> clazz, FieldInfo fieldInfo){
        super(clazz, fieldInfo);

        Type fieldType = fieldInfo.fieldType;
        if (fieldType instanceof ParameterizedType) {
            Type argType = ((ParameterizedType) fieldInfo.fieldType).getActualTypeArguments()[0];
            if (argType instanceof WildcardType) {
                WildcardType wildcardType = (WildcardType) argType;
                Type[] upperBounds = wildcardType.getUpperBounds();
                if (upperBounds.length == 1) {
                    argType = upperBounds[0];
                }
            }
            this.itemType = argType;
        } else {
            this.itemType = Object.class;
        }
    }

    public int getFastMatchToken() {
        return JSONToken.LBRACKET;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void parseField(DefaultJSONParser parser, Object object, Type objectType, Map<String, Object> fieldValues) {
        JSONLexer lexer = parser.lexer;
        final int token = lexer.token();
        if (token == JSONToken.NULL
                || (token == JSONToken.LITERAL_STRING && lexer.stringVal().length() == 0)) {
            if (object == null) {
                fieldValues.put(fieldInfo.name, null);
            } else {
                setValue(object, null);
            }
            return;
        }

        ArrayList list = new ArrayList();

        ParseContext context = parser.getContext();

        parser.setContext(context, object, fieldInfo.name);
        parseArray(parser, objectType, list);
        parser.setContext(context);

        if (object == null) {
            fieldValues.put(fieldInfo.name, list);
        } else {
            setValue(object, list);
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public final void parseArray(DefaultJSONParser parser, Type objectType, Collection array) {
        Type itemType = this.itemType;
        ObjectDeserializer itemTypeDeser = this.deserializer;

        if (objectType instanceof ParameterizedType) {
            if (itemType instanceof TypeVariable) {
                ParameterizedType paramType = (ParameterizedType) objectType;

                int paramIndex = indexOfTypeVariable(paramType, (TypeVariable) itemType);

                if (paramIndex != -1) {
                    itemType = paramType.getActualTypeArguments()[paramIndex];
                    if (!itemType.equals(this.itemType)) {
                        itemTypeDeser = parser.getConfig().getDeserializer(itemType);
                    }
                }
            } else if (itemType instanceof ParameterizedType) {
                ParameterizedType parameterizedItemType = (ParameterizedType) itemType;
                Type[] itemActualTypeArgs = parameterizedItemType.getActualTypeArguments();
                if (itemActualTypeArgs.length == 1 && itemActualTypeArgs[0] instanceof TypeVariable) {
                    ParameterizedType paramType = (ParameterizedType) objectType;

                    int paramIndex = indexOfTypeVariable(paramType, (TypeVariable) itemActualTypeArgs[0]);

                    if (paramIndex != -1) {
                        itemActualTypeArgs[0] = paramType.getActualTypeArguments()[paramIndex];
                        itemType = TypeReference.intern(
                                new ParameterizedTypeImpl(itemActualTypeArgs, parameterizedItemType.getOwnerType(), parameterizedItemType.getRawType())
                        );
                    }
                }
            }
        } else if (itemType instanceof TypeVariable && objectType instanceof Class) {
            Class objectClass = (Class) objectType;
            TypeVariable typeVar = (TypeVariable) itemType;

            for (TypeVariable item : objectClass.getTypeParameters()) {
                if (item.getName().equals(typeVar.getName())) {
                    Type[] bounds = item.getBounds();
                    if (bounds.length == 1) {
                        itemType = bounds[0];
                    }
                    break;
                }
            }
        }

        final JSONLexer lexer = parser.lexer;

        final int token = lexer.token();
        if (token == JSONToken.LBRACKET) {
            if (itemTypeDeser == null) {
                itemTypeDeser = deserializer = parser.getConfig().getDeserializer(itemType);
                itemFastMatchToken = deserializer.getFastMatchToken();
            }

            lexer.nextToken(itemFastMatchToken);

            for (int i = 0; ; ++i) {
                if (lexer.isEnabled(Feature.AllowArbitraryCommas)) {
                    while (lexer.token() == JSONToken.COMMA) {
                        lexer.nextToken();
                    }
                }

                if (lexer.token() == JSONToken.RBRACKET) {
                    break;
                }

                Object val = itemTypeDeser.deserialze(parser, itemType, i);
                array.add(val);

                parser.checkListResolve(array);

                if (lexer.token() == JSONToken.COMMA) {
                    lexer.nextToken(itemFastMatchToken);
                }
            }

            lexer.nextToken(JSONToken.COMMA);
        } else if (token == JSONToken.LITERAL_STRING && fieldInfo.unwrapped) {
            String str = lexer.stringVal();
            lexer.nextToken();
            DefaultJSONParser valueParser = new DefaultJSONParser(str);
            valueParser.parseArray(array);
        } else {
            if (itemTypeDeser == null) {
                itemTypeDeser = deserializer = parser.getConfig().getDeserializer(itemType);
            }
            Object val = itemTypeDeser.deserialze(parser, itemType, 0);
            array.add(val);
            parser.checkListResolve(array);
        }
    }

    private int indexOfTypeVariable(ParameterizedType paramType, TypeVariable<?> typeVar) {
        if (paramType.getRawType() instanceof Class) {
            Class<?> clazz = (Class<?>) paramType.getRawType();
            // getTypeParameters() 内部每次都是返回新数组，因此需要抽取成变量
            TypeVariable<? extends Class<?>>[] typeParameters = clazz.getTypeParameters();
            for (int i = 0, size = typeParameters.length; i < size; ++i) {
                if (typeParameters[i].getName().equals(typeVar.getName())) {
                    return i;
                }
            }
        }

        return -1;
    }

}