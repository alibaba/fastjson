package com.alibaba.fastjson.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

public class ServiceLoader {

    private static final String      PREFIX     = "META-INF/services/";

    private static final Set<String> loadedUrls = new HashSet<String>();

    @SuppressWarnings("unchecked")
    public static <T> Set<T> load(Class<T> clazz, ClassLoader classLoader) {
        if (classLoader == null) {
            return Collections.emptySet();
        }
        
        Set<T> services = new HashSet<T>();

        String className = clazz.getName();
        String path = PREFIX + className;

        Set<String> serviceNames = new HashSet<String>();

        try {
            Enumeration<URL> urls = classLoader.getResources(path);
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                if (loadedUrls.contains(url.toString())) {
                    continue;
                }
                load(url, serviceNames);
                loadedUrls.add(url.toString());
            }
        } catch (Throwable ex) {
            // skip
        }

        for (String serviceName : serviceNames) {
            try {
                Class<?> serviceClass = classLoader.loadClass(serviceName);
                T service = (T) serviceClass.newInstance();
                services.add(service);
            } catch (Exception e) {
                // skip
            }
        }

        return services;
    }

    public static void load(URL url, Set<String> set) throws IOException {
        InputStream is = null;
        BufferedReader reader = null;
        try {
            is = url.openStream();
            reader = new BufferedReader(new InputStreamReader(is, "utf-8"));
            for (;;) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }

                int ci = line.indexOf('#');
                if (ci >= 0) {
                    line = line.substring(0, ci);
                }
                line = line.trim();
                if (line.length() == 0) {
                    continue;
                }
                set.add(line);
            }
        } finally {
            IOUtils.close(reader);
            IOUtils.close(is);
        }
    }
}
