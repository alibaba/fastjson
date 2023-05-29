package com.alibaba.fastjson;

import com.alibaba.fastjson.spi.IPropertyNamingStrategy;

import javax.ws.rs.NotSupportedException;


/**
 * @since 1.2.15
 */
public enum PropertyNamingStrategy {
                                    CamelCase, // camelCase
                                    PascalCase, // PascalCase
                                    SnakeCase, // snake_case
                                    KebabCase, // kebab-case
                                    NoChange,  //
                                    NeverUseThisValueExceptDefaultValue,
                                    Customized(){
                                        private ThreadLocal<IPropertyNamingStrategy> local;
                                        
                                        @Override
                                        public String translate(String propertyName){
                                            if (local == null || local.get() == null){
                                                throw new IllegalArgumentException("not register yet!");    
                                            }
                                            return local.get().translate(propertyName);
                                        }
                                        
                                        @Override
                                        public void register(IPropertyNamingStrategy instance){
                                            if (local == null){
                                                synchronized (this){
                                                    if (local == null){
                                                        local = new ThreadLocal<>();   
                                                    }       
                                                }
                                            }
                                            local.set(instance);
                                        }

                                        @Override
                                        public void unRegister(){
                                            local.remove();
                                        }
                                    }
    ;
                                    

    public String translate(String propertyName) {
        switch (this) {
            case SnakeCase: {
                StringBuilder buf = new StringBuilder();
                for (int i = 0; i < propertyName.length(); ++i) {
                    char ch = propertyName.charAt(i);
                    if (ch >= 'A' && ch <= 'Z') {
                        char ch_ucase = (char) (ch + 32);
                        if (i > 0) {
                            buf.append('_');
                        }
                        buf.append(ch_ucase);
                    } else {
                        buf.append(ch);
                    }
                }
                return buf.toString();
            }
            case KebabCase: {
                StringBuilder buf = new StringBuilder();
                for (int i = 0; i < propertyName.length(); ++i) {
                    char ch = propertyName.charAt(i);
                    if (ch >= 'A' && ch <= 'Z') {
                        char ch_ucase = (char) (ch + 32);
                        if (i > 0) {
                            buf.append('-');
                        }
                        buf.append(ch_ucase);
                    } else {
                        buf.append(ch);
                    }
                }
                return buf.toString();
            }
            case PascalCase: {
                char ch = propertyName.charAt(0);
                if (ch >= 'a' && ch <= 'z') {
                    char[] chars = propertyName.toCharArray();
                    chars[0] -= 32;
                    return new String(chars);
                }

                return propertyName;
            }
            case CamelCase: {
                char ch = propertyName.charAt(0);
                if (ch >= 'A' && ch <= 'Z') {
                    char[] chars = propertyName.toCharArray();
                    chars[0] += 32;
                    return new String(chars);
                }

                return propertyName;
            }
            case NoChange:
            case NeverUseThisValueExceptDefaultValue:
            default:
                return propertyName;
        }
    }

    public void register(IPropertyNamingStrategy instance){
        throw new NotSupportedException();
    }

    public void unRegister(){
        throw new NotSupportedException();
    }
}
