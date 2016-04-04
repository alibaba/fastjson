/*
 * Copyright 1999-2101 Alibaba Group.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.fastjson.serializer;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONStreamAware;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.util.TypeUtils;

/**
 * @author wenshao[szujobs@hotmail.com]
 */
public class MiscCodec implements ObjectSerializer, ObjectDeserializer {

    public final static MiscCodec instance = new MiscCodec();

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType,
                      int features) throws IOException {
        SerializeWriter out = serializer.out;
        
        if (object == null) {
            out.writeNull();
            return;
        }
        
        Object objClass = object.getClass();
        
        String strVal;
        if (objClass == SimpleDateFormat.class) {
            String pattern = ((SimpleDateFormat) object).toPattern();

            if (out.isEnabled(SerializerFeature.WriteClassName)) {
                if (object.getClass() != fieldType) {
                    out.write('{');
                    out.writeFieldName(JSON.DEFAULT_TYPE_KEY);
                    serializer.write(object.getClass().getName());
                    out.writeFieldValue(',', "val", pattern);
                    out.write('}');
                    return;
                }
            }
            
            strVal = pattern;
        } else if (objClass == Class.class) {
            Class<?> clazz = (Class<?>) object;
            strVal = clazz.getName();
        } else if (objClass == InetSocketAddress.class) {
            InetSocketAddress address = (InetSocketAddress) object;

            InetAddress inetAddress = address.getAddress();

            out.write('{');
            if (inetAddress != null) {
                out.writeFieldName("address");
                serializer.write(inetAddress);
                out.write(',');
            }
            out.writeFieldName("port");
            out.writeInt(address.getPort());
            out.write('}');
            return;
        } else if (object instanceof File) {
            strVal = ((File) object).getPath();
        } else if (object instanceof InetAddress) {
            strVal = ((InetAddress) object).getHostAddress();
        } else if (object instanceof TimeZone) {
            TimeZone timeZone = (TimeZone) object;
            strVal = timeZone.getID();
        } else if (object instanceof JSONStreamAware) {
            JSONStreamAware aware = (JSONStreamAware) object;
            aware.writeJSONString(out);
            return;
        } else {
            strVal = object.toString();
        }
        
        out.writeString(strVal);
    }

    @SuppressWarnings("unchecked")
    public <T> T deserialze(DefaultJSONParser parser, Type clazz, Object fieldName) {
        JSONLexer lexer = parser.lexer;
        
        if (clazz == InetSocketAddress.class) {
            if (lexer.token() == JSONToken.NULL) {
                lexer.nextToken();
                return null;
            }

            parser.accept(JSONToken.LBRACE);

            InetAddress address = null;
            int port = 0;
            for (;;) {
                String key = lexer.stringVal();
                lexer.nextToken(JSONToken.COLON);
               

                if (key.equals("address")) {
                    parser.accept(JSONToken.COLON);
                    address = parser.parseObject(InetAddress.class);
                } else if (key.equals("port")) {
                    parser.accept(JSONToken.COLON);
                    if (lexer.token() != JSONToken.LITERAL_INT) {
                        throw new JSONException("port is not int");
                    }
                    port = lexer.intValue();
                    lexer.nextToken();
                } else {
                    parser.accept(JSONToken.COLON);
                    parser.parse();
                }

                if (lexer.token() == JSONToken.COMMA) {
                    lexer.nextToken();
                    continue;
                }

                break;
            }

            parser.accept(JSONToken.RBRACE);

            return (T) new InetSocketAddress(address, port);
        }
        
        Object objVal;
        
        if (parser.resolveStatus == DefaultJSONParser.TypeNameRedirect) {
            parser.resolveStatus = DefaultJSONParser.NONE;
            parser.accept(JSONToken.COMMA);

            if (lexer.token() == JSONToken.LITERAL_STRING) {
                if (!"val".equals(lexer.stringVal())) {
                    throw new JSONException("syntax error");
                }
                lexer.nextToken();
            } else {
                throw new JSONException("syntax error");
            }

            parser.accept(JSONToken.COLON);

            objVal = parser.parse();

            parser.accept(JSONToken.RBRACE);
        } else {
            objVal = parser.parse();
        }
        
        String strVal;

        if (objVal == null) {
            strVal = null;
        } else if (objVal instanceof String) {
            strVal = (String) objVal;
        } else {
            throw new JSONException("expect string");
        }

        if (strVal == null || strVal.length() == 0) {
            return null;
        }

        if (clazz == UUID.class) {
            return (T) UUID.fromString(strVal);
        }

        if (clazz == URI.class) {
            return (T) URI.create(strVal);
        }

        if (clazz == URL.class) {
            try {
                return (T) new URL(strVal);
            } catch (MalformedURLException e) {
                throw new JSONException("create url error", e);
            }
        }

        if (clazz == Pattern.class) {
            return (T) Pattern.compile(strVal);
        }

        if (clazz == Locale.class) {
            String[] items = strVal.split("_");

            if (items.length == 1) {
                return (T) new Locale(items[0]);
            }

            if (items.length == 2) {
                return (T) new Locale(items[0], items[1]);
            }

            return (T) new Locale(items[0], items[1], items[2]);
        }
        
        if (clazz == SimpleDateFormat.class) {
            return (T) new SimpleDateFormat(strVal);
        }
        
        if (clazz == InetAddress.class || clazz == Inet4Address.class  || clazz == Inet6Address.class) {
            try {
                return (T) InetAddress.getByName(strVal);
            } catch (UnknownHostException e) {
                throw new JSONException("deserialize inet adress error", e);
            }
        }
        
        if (clazz == File.class) {
            return (T) new File(strVal);
        }
        
        if (clazz == TimeZone.class) {
            return (T) TimeZone.getTimeZone(strVal);
        }
        
        if (clazz instanceof ParameterizedType) {
            ParameterizedType parmeterizedType = (ParameterizedType) clazz;
            clazz = parmeterizedType.getRawType();
        }
        
        if (clazz == Class.class) {
            return (T) TypeUtils.loadClass(strVal, parser.getConfig().getDefaultClassLoader());
        }
        
        throw new JSONException("MiscCodec not support " + clazz);
    }

    public int getFastMatchToken() {
        return JSONToken.LITERAL_STRING;
    }
}
