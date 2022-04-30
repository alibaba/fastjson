package com.alibaba.fastjson.parser.deserializer;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.*;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.util.TypeUtils;

import static com.alibaba.fastjson.util.TypeUtils.fnv1a_64_magic_hashcode;
import static com.alibaba.fastjson.util.TypeUtils.fnv1a_64_magic_prime;

@SuppressWarnings("rawtypes")
public class EnumDeserializer implements ObjectDeserializer {

    protected final Class<?> enumClass;
    protected final Enum[]   enums;
    protected final Enum[]   ordinalEnums;
    protected       long[]   enumNameHashCodes;

    public EnumDeserializer(Class<?> enumClass){
        this.enumClass = enumClass;

        ordinalEnums = (Enum[]) enumClass.getEnumConstants();

        Map<Long, Enum> enumMap = new HashMap<Long, Enum>();
        for (Enum e : ordinalEnums) {
            String name = e.name();

            JSONField jsonField = null;
            try {
                Field field = enumClass.getField(name);
                jsonField = TypeUtils.getAnnotation(field, JSONField.class);
                if (jsonField != null) {
                    String jsonFieldName = jsonField.name();
                    if (jsonFieldName.length() > 0) {
                        name = jsonFieldName;
                    }
                }
            } catch (Exception ex) {
                // skip
            }

            long hash = fnv1a_64_magic_hashcode;
            long hash_lower = fnv1a_64_magic_hashcode;
            for (int j = 0; j < name.length(); ++j) {
                char ch = name.charAt(j);

                hash ^= ch;
                hash_lower ^= ((ch >= 'A' && ch <= 'Z') ? (ch + 32) : ch);

                hash *= fnv1a_64_magic_prime;
                hash_lower *= fnv1a_64_magic_prime;
            }

            enumMap.put(hash, e);
            if (hash != hash_lower) {
                enumMap.put(hash_lower, e);
            }

            if (jsonField != null) {
                for (String alterName : jsonField.alternateNames()) {
                    long alterNameHash = fnv1a_64_magic_hashcode;
                    for (int j = 0; j < alterName.length(); ++j) {
                        char ch = alterName.charAt(j);
                        alterNameHash ^= ch;
                        alterNameHash *= fnv1a_64_magic_prime;
                    }
                    if (alterNameHash != hash && alterNameHash != hash_lower) {
                        enumMap.put(alterNameHash, e);
                    }
                }
            }
        }

        this.enumNameHashCodes = new long[enumMap.size()];
        {
            int i = 0;
            for (Long h : enumMap.keySet()) {
                enumNameHashCodes[i++] = h;
            }
            Arrays.sort(this.enumNameHashCodes);
        }

        this.enums = new Enum[enumNameHashCodes.length];
        for (int i = 0; i < this.enumNameHashCodes.length; ++i) {
            long hash = enumNameHashCodes[i];
            Enum e = enumMap.get(hash);
            this.enums[i] = e;
        }
    }

    public Enum getEnumByHashCode(long hashCode) {
        if (enums == null) {
            return null;
        }

        int enumIndex = Arrays.binarySearch(this.enumNameHashCodes, hashCode);

        if (enumIndex < 0) {
            return null;
        }

        return enums[enumIndex];
    }

    public Enum<?> valueOf(int ordinal) {
        return ordinalEnums[ordinal];
    }

    @SuppressWarnings("unchecked")
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        try {
            Object value;
            final JSONLexer lexer = parser.lexer;
            final int token = lexer.token();
            if (token == JSONToken.LITERAL_INT) {
                int intValue = lexer.intValue();
                lexer.nextToken(JSONToken.COMMA);

                if (intValue < 0 || intValue >= ordinalEnums.length) {
                    throw new JSONException("parse enum " + enumClass.getName() + " error, value : " + intValue);
                }

                return (T) ordinalEnums[intValue];
            } else if (token == JSONToken.LITERAL_STRING) {
                String name = lexer.stringVal();
                lexer.nextToken(JSONToken.COMMA);

                if (name.length() == 0) {
                    return null;
                }

                long hash = fnv1a_64_magic_hashcode;
                long hash_lower = fnv1a_64_magic_hashcode;
                for (int j = 0; j < name.length(); ++j) {
                    char ch = name.charAt(j);

                    hash ^= ch;
                    hash_lower ^= ((ch >= 'A' && ch <= 'Z') ? (ch + 32) : ch);

                    hash *= fnv1a_64_magic_prime;
                    hash_lower *= fnv1a_64_magic_prime;
                }

                Enum e = getEnumByHashCode(hash);
                if (e == null && hash_lower != hash) {
                    e = getEnumByHashCode(hash_lower);
                }

                if (e == null && lexer.isEnabled(Feature.ErrorOnEnumNotMatch)) {
                    throw new JSONException("not match enum value, " + enumClass.getName() + " : " + name);
                }
                return (T) e;
            } else if (token == JSONToken.NULL) {
                lexer.nextToken(JSONToken.COMMA);
                return null;
            } else {
                value = parser.parse();
            }

            throw new JSONException("parse enum " + enumClass.getName() + " error, value : " + value);
        } catch (JSONException e) {
            throw e;
        } catch (Exception e) {
            throw new JSONException(e.getMessage(), e);
        }
    }

    public int getFastMatchToken() {
        return JSONToken.LITERAL_INT;
    }
}
