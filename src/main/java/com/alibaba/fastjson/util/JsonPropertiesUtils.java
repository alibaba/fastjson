package com.alibaba.fastjson.util;

import java.io.InputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Properties;

/**
 * @author niaoge[niaoge@gmail.com]
 */
public class JsonPropertiesUtils {
    public  final  static String FASTJSON_PROPERTIES  ="fastjson.properties";
    
    public final static String FASTJSON_COMPATIBLEWITHJAVABEAN="fastjson.compatibleWithJavaBean";
    
    public final static String FASTJSON_COMPATIBLEWITHFIELDNAME="fastjson.compatibleWithFieldName";
    
    public final static Properties DEFAULT_PROPERTIES =new Properties();
    
    static {
        try {
            new PropertiesInitializer().autoConfig();
        } catch (Exception e) {
        }
    }
    
    
    static class PropertiesInitializer{
        public void autoConfig(){
            loadPropertiesFromFile();
            TypeUtils.compatibleWithJavaBean ="true".equals(getStringProperty(FASTJSON_COMPATIBLEWITHJAVABEAN)) ;
            TypeUtils.compatibleWithFieldName ="true".equals(getStringProperty(FASTJSON_COMPATIBLEWITHFIELDNAME)) ;
        }
    }
    
    public static String getStringProperty(String name) {
        String prop = null;
        try {
            prop = System.getProperty(name);
        } catch (SecurityException e) {
            ; // skip
        }
        return (prop == null) ? DEFAULT_PROPERTIES.getProperty(name) : prop;
    }
    
    public static void loadPropertiesFromFile(){
        InputStream imputStream = AccessController.doPrivileged(new PrivilegedAction<InputStream>() {
            public InputStream run() {
                ClassLoader cl = Thread.currentThread().getContextClassLoader();
                if (cl != null) {
                    return cl.getResourceAsStream(FASTJSON_PROPERTIES);
                } else {
                    return ClassLoader.getSystemResourceAsStream(FASTJSON_PROPERTIES);
                }
            }
        });
        
        if (null != imputStream) {
            try {
                DEFAULT_PROPERTIES.load(imputStream);
                imputStream.close();
            } catch (java.io.IOException e) {
                // skip
            }
        }
    }
}
