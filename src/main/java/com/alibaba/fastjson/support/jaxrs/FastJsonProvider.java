package com.alibaba.fastjson.support.jaxrs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * JAX-RS Provider for fastjson.
 *
 * @author smallnest
 *
 */
@Provider
public class FastJsonProvider implements MessageBodyReader<Object>, MessageBodyWriter<Object> {
   
	// default charset
	private String DEFAULT_CHARSET = "UTF-8";

	private String charset = DEFAULT_CHARSET;
	 
	private Class<?>[] clazzes = null;

    public SerializeConfig serializeConfig = SerializeConfig.getGlobalInstance();
    public ParserConfig parserConfig = ParserConfig.getGlobalInstance();
    public SerializerFeature[] serializerFeatures = new SerializerFeature[0];
    public Feature[] features = new Feature[0];

    @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo uriInfo;

    /**
     * Can serialize/deserialize all types.
     */
    public FastJsonProvider() {

    }

    /**
     * Set charset. the default charset is UTF-8
     */
    public FastJsonProvider(String charset) {
    	this.charset = charset;
    }

    /**
     * Only serialize/deserialize all types in clazzes.
     */
    public FastJsonProvider(Class<?>[] clazzes) {
        this.clazzes = clazzes;
    }

    /**
     * Check whether a class can be serialized or deserialized. It can check
     * based on packages, annotations on entities or explicit classes.
     *
     * @param type class need to check
     * @return true if valid
     */
    protected boolean isValidType(Class<?> type, Annotation[] classAnnotations) {
        if (type == null)
            return false;

        if (clazzes != null) {
            for (Class<?> cls : clazzes) { // must strictly equal. Don't check
                                            // inheritance
                if (cls == type)
                    return true;
            }

            return false;
        }

        return true;
    }

    /**
     * Check media type like "application/json".
     *
     * @param mediaType
     *            media type
     * @return true if the media type is valid
     */
    protected boolean hasMatchingMediaType(MediaType mediaType) {
        if (mediaType != null) {
            String subtype = mediaType.getSubtype();
            return "json".equalsIgnoreCase(subtype) || subtype.endsWith("+json") || "javascript".equals(subtype) || "x-javascript".equals(subtype);
        }
        return true;
    }

    /*
     * /********************************************************** /* Partial
     * MessageBodyWriter impl
     * /**********************************************************
     */

    /**
     * Method that JAX-RS container calls to try to check whether given value
     * (of specified type) can be serialized by this provider.
     */
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        if (!hasMatchingMediaType(mediaType)) {
            return false;
        }

        return isValidType(type, annotations);
    }

    /**
     * Method that JAX-RS container calls to try to figure out serialized length
     * of given value. always return -1 to denote "not known".
     */
    public long getSize(Object t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return -1;
    }

    /**
     * Method that JAX-RS container calls to serialize given value.
     */
    public void writeTo(Object t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders,
            OutputStream entityStream) throws IOException, WebApplicationException {
        SerializeFilter filter = null;

        SerializerFeature[] serializerFeatures = this.serializerFeatures;
        if(uriInfo != null &&  uriInfo.getQueryParameters().containsKey("pretty")) {
            if (serializerFeatures == null)
                serializerFeatures = new  SerializerFeature[]{SerializerFeature.PrettyFormat};
            else {
                List<SerializerFeature> featureList = Arrays.asList(serializerFeatures);
                featureList.add(SerializerFeature.PrettyFormat);
                serializerFeatures = featureList.toArray(serializerFeatures);
            }
        }

        String jsonStr = JSON.toJSONString(t, filter, serializerFeatures);
        
        if (jsonStr != null) {
        	
        	this.outputStreamWriteString(entityStream, jsonStr);
        }
    }

    /*
     * /********************************************************** /*
     * MessageBodyReader impl
     * /**********************************************************
     */

    /**
     * Method that JAX-RS container calls to try to check whether values of
     * given type (and media type) can be deserialized by this provider.
     */
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        if (!hasMatchingMediaType(mediaType)) {
            return false;
        }

        return isValidType(type, annotations);
    }

    /**
     * Method that JAX-RS container calls to deserialize given value.
     */
    public Object readFrom(Class<Object> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> httpHeaders,
            InputStream entityStream) throws IOException, WebApplicationException {
        String input = null;
        try {
            input = this.inputStreamReadString(entityStream);
        } catch (Exception e) {
            // skip ??
        }

        if (input == null) {
            return null;
        }

        return JSON.parseObject(input, type, parserConfig, JSON.DEFAULT_PARSER_FEATURE, features);
    }

    /**
     * Method that inputStream read String with charset.
     */
    private String inputStreamReadString(InputStream in) {
		
		StringBuffer buffer = new StringBuffer();

		BufferedReader br;
		
		try {
			br = new BufferedReader(new InputStreamReader(in, charset));
			
			String line;
			
			while ((line = br.readLine()) != null) {
			
				buffer.append(line);
			}
			
		} catch (Exception ex) {
			 throw new JSONException("read string from reader error", ex);
		}
		
		return buffer.toString();
	}
    
    /**
     * Method that outputStream write String with charset.
     */
    private void outputStreamWriteString(OutputStream out, String str) {
    	
    	BufferedWriter bw;
    	
		try {
			bw = new BufferedWriter(new OutputStreamWriter(out, charset));
			
			bw.write(str);
			
			bw.flush();
			
		} catch (Exception ex) {
			 throw new JSONException("write string to writer error", ex);
		}
    }
}
