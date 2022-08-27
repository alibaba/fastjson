package com.alibaba.fastjson.serializer;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.IOException;
import java.lang.reflect.Type;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.ParseContext;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;

public class AwtCodec implements ObjectSerializer, ObjectDeserializer {

    public final static AwtCodec instance = new AwtCodec();
    
    public static boolean support(Class<?> clazz) {
        return clazz == Point.class //
               || clazz == Rectangle.class //
               || clazz == Font.class //
               || clazz == Color.class //
        ;
    }

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType,
                      int features) throws IOException {
        SerializeWriter out = serializer.out;

        if (object == null) {
            out.writeNull();
            return;
        }

        char sep = '{';

        if (object instanceof Point) {
            Point font = (Point) object;
            
            sep = writeClassName(out, Point.class, sep);
            
            out.writeFieldValue(sep, "x", font.x);
            out.writeFieldValue(',', "y", font.y);
        } else if (object instanceof Font) {
            Font font = (Font) object;
            
            sep = writeClassName(out, Font.class, sep);
            
            out.writeFieldValue(sep, "name", font.getName());
            out.writeFieldValue(',', "style", font.getStyle());
            out.writeFieldValue(',', "size", font.getSize());
        } else if (object instanceof Rectangle) {
            Rectangle rectangle = (Rectangle) object;
            
            sep = writeClassName(out, Rectangle.class, sep);
            
            out.writeFieldValue(sep, "x", rectangle.x);
            out.writeFieldValue(',', "y", rectangle.y);
            out.writeFieldValue(',', "width", rectangle.width);
            out.writeFieldValue(',', "height", rectangle.height);
        } else if (object instanceof Color) {
            Color color = (Color) object;
            
            sep = writeClassName(out, Color.class, sep);
            
            out.writeFieldValue(sep, "r", color.getRed());
            out.writeFieldValue(',', "g", color.getGreen());
            out.writeFieldValue(',', "b", color.getBlue());
            if (color.getAlpha() > 0) {
                out.writeFieldValue(',', "alpha", color.getAlpha());
            }
        } else {
            throw new JSONException("not support awt class : " + object.getClass().getName());
        }

        out.write('}');

    }

    protected char writeClassName(SerializeWriter out, Class<?> clazz, char sep) {
        if (out.isEnabled(SerializerFeature.WriteClassName)) {
            out.write('{');
            out.writeFieldName(JSON.DEFAULT_TYPE_KEY);
            out.writeString(clazz.getName());
            sep = ',';
        }
        return sep;
    }

    @SuppressWarnings("unchecked")

    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        JSONLexer lexer = parser.lexer;

        if (lexer.token() == JSONToken.NULL) {
            lexer.nextToken(JSONToken.COMMA);
            return null;
        }

        if (lexer.token() != JSONToken.LBRACE && lexer.token() != JSONToken.COMMA) {
            throw new JSONException("syntax error");
        }
        lexer.nextToken();

        T obj;
        if (type == Point.class) {
            obj = (T) parsePoint(parser, fieldName);
        } else if (type == Rectangle.class) {
            obj = (T) parseRectangle(parser);
        } else if (type == Color.class) {
            obj = (T) parseColor(parser);
        } else if (type == Font.class) {
            obj = (T) parseFont(parser);
        } else {
            throw new JSONException("not support awt class : " + type);
        }

        ParseContext context = parser.getContext();
        parser.setContext(obj, fieldName);
        parser.setContext(context);

        return obj;
    }
    
    protected Font parseFont(DefaultJSONParser parser) {
        JSONLexer lexer = parser.lexer;
        
        int size = 0, style = 0;
        String name = null;
        for (;;) {
            if (lexer.token() == JSONToken.RBRACE) {
                lexer.nextToken();
                break;
            }

            String key;
            if (lexer.token() == JSONToken.LITERAL_STRING) {
                key = lexer.stringVal();
                lexer.nextTokenWithColon(JSONToken.LITERAL_INT);
            } else {
                throw new JSONException("syntax error");
            }


            if (key.equalsIgnoreCase("name")) {
                if (lexer.token() == JSONToken.LITERAL_STRING) {
                    name = lexer.stringVal();
                    lexer.nextToken();
                } else {
                    throw new JSONException("syntax error");
                }
            } else if (key.equalsIgnoreCase("style")) {
                if (lexer.token() == JSONToken.LITERAL_INT) {
                    style = lexer.intValue();
                    lexer.nextToken();
                } else {
                    throw new JSONException("syntax error");
                }
            } else if (key.equalsIgnoreCase("size")) {
                if (lexer.token() == JSONToken.LITERAL_INT) {
                    size = lexer.intValue();
                    lexer.nextToken();
                } else {
                    throw new JSONException("syntax error");
                }
            } else {
                throw new JSONException("syntax error, " + key);
            }

            if (lexer.token() == JSONToken.COMMA) {
                lexer.nextToken(JSONToken.LITERAL_STRING);
            }
        }

        return new Font(name, style, size);
    }
    
    protected Color parseColor(DefaultJSONParser parser) {
        JSONLexer lexer = parser.lexer;
        
        int r = 0, g = 0, b = 0, alpha = 0;
        for (;;) {
            if (lexer.token() == JSONToken.RBRACE) {
                lexer.nextToken();
                break;
            }

            String key;
            if (lexer.token() == JSONToken.LITERAL_STRING) {
                key = lexer.stringVal();
                lexer.nextTokenWithColon(JSONToken.LITERAL_INT);
            } else {
                throw new JSONException("syntax error");
            }

            int val;
            if (lexer.token() == JSONToken.LITERAL_INT) {
                val = lexer.intValue();
                lexer.nextToken();
            } else {
                throw new JSONException("syntax error");
            }

            if (key.equalsIgnoreCase("r")) {
                r = val;
            } else if (key.equalsIgnoreCase("g")) {
                g = val;
            } else if (key.equalsIgnoreCase("b")) {
                b = val;
            } else if (key.equalsIgnoreCase("alpha")) {
                alpha = val;
            } else {
                throw new JSONException("syntax error, " + key);
            }

            if (lexer.token() == JSONToken.COMMA) {
                lexer.nextToken(JSONToken.LITERAL_STRING);
            }
        }

        return new Color(r, g, b, alpha);
    }
    
    protected Rectangle parseRectangle(DefaultJSONParser parser) {
        JSONLexer lexer = parser.lexer;
        
        int x = 0, y = 0, width = 0, height = 0;
        for (;;) {
            if (lexer.token() == JSONToken.RBRACE) {
                lexer.nextToken();
                break;
            }

            String key;
            if (lexer.token() == JSONToken.LITERAL_STRING) {
                key = lexer.stringVal();
                lexer.nextTokenWithColon(JSONToken.LITERAL_INT);
            } else {
                throw new JSONException("syntax error");
            }

            int val;
            int token = lexer.token();
            if (token == JSONToken.LITERAL_INT) {
                val = lexer.intValue();
                lexer.nextToken();
            } else if (token == JSONToken.LITERAL_FLOAT) {
                val = (int) lexer.floatValue();
                lexer.nextToken();
            } else {
                throw new JSONException("syntax error");
            }

            if (key.equalsIgnoreCase("x")) {
                x = val;
            } else if (key.equalsIgnoreCase("y")) {
                y = val;
            } else if (key.equalsIgnoreCase("width")) {
                width = val;
            } else if (key.equalsIgnoreCase("height")) {
                height = val;
            } else {
                throw new JSONException("syntax error, " + key);
            }

            if (lexer.token() == JSONToken.COMMA) {
                lexer.nextToken(JSONToken.LITERAL_STRING);
            }
        }

        return new Rectangle(x, y, width, height);
    }

    protected Point parsePoint(DefaultJSONParser parser, Object fieldName) {
        JSONLexer lexer = parser.lexer;
        
        int x = 0, y = 0;
        for (;;) {
            if (lexer.token() == JSONToken.RBRACE) {
                lexer.nextToken();
                break;
            }

            String key;
            if (lexer.token() == JSONToken.LITERAL_STRING) {
                key = lexer.stringVal();

                if (JSON.DEFAULT_TYPE_KEY.equals(key)) {
                    parser.acceptType("java.awt.Point");
                    continue;
                }

                if ("$ref".equals(key)) {
                    return (Point) parseRef(parser, fieldName);
                }

                lexer.nextTokenWithColon(JSONToken.LITERAL_INT);
            } else {
                throw new JSONException("syntax error");
            }

            int token = lexer.token();
            int val;
            if (token == JSONToken.LITERAL_INT) {
                val = lexer.intValue();
                lexer.nextToken();
            } else if(token == JSONToken.LITERAL_FLOAT) {
                val = (int) lexer.floatValue();
                lexer.nextToken();
            } else {
                throw new JSONException("syntax error : " + lexer.tokenName());
            }

            if (key.equalsIgnoreCase("x")) {
                x = val;
            } else if (key.equalsIgnoreCase("y")) {
                y = val;
            } else {
                throw new JSONException("syntax error, " + key);
            }

            if (lexer.token() == JSONToken.COMMA) {
                lexer.nextToken(JSONToken.LITERAL_STRING);
            }
        }

        return new Point(x, y);
    }

    private Object parseRef(DefaultJSONParser parser, Object fieldName) {
        JSONLexer lexer = parser.getLexer();
        lexer.nextTokenWithColon(JSONToken.LITERAL_STRING);
        String ref = lexer.stringVal();
        parser.setContext(parser.getContext(), fieldName);
        parser.addResolveTask(new DefaultJSONParser.ResolveTask(parser.getContext(), ref));
        parser.popContext();
        parser.setResolveStatus(DefaultJSONParser.NeedToResolve);
        lexer.nextToken(JSONToken.RBRACE);
        parser.accept(JSONToken.RBRACE);
        return null;
    }

    public int getFastMatchToken() {
        return JSONToken.LBRACE;
    }
}
