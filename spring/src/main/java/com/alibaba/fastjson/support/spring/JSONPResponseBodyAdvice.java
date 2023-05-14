package com.alibaba.fastjson.support.spring;

import com.alibaba.fastjson.JSONPObject;
import com.alibaba.fastjson.support.spring.annotation.ResponseJSONP;
import com.alibaba.fastjson.util.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by SongLing.Dong on 7/22/2017.
 * <p>
 * Wrap with the return object from method annotated by <code>@ResponseJSONP</code>
 * in order to be serialized into jsonp format.
 * </p>
 * <p>
 * <p>
 * url: /path/to/your/api?<b>callback=functionName</b>
 * </p>
 *
 * @see JSONPObject
 * @see ResponseJSONP
 * @since Spring 4.2 when ResponseBodyAdvice is supported.
 * <p>
 * In Spring 3.x, use method directly return a <code>JSONPObject</code> instead.
 * </p>
 */
@Order(Integer.MIN_VALUE)//before FastJsonViewResponseBodyAdvice
@ControllerAdvice
public class JSONPResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    public final Log logger = LogFactory.getLog(this.getClass());

    public JSONPResponseBodyAdvice() {
    }


    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {


        return FastJsonHttpMessageConverter.class.isAssignableFrom(converterType)
                &&
                (returnType.getContainingClass().isAnnotationPresent(ResponseJSONP.class) || returnType.hasMethodAnnotation(ResponseJSONP.class));
    }

    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
                                  ServerHttpResponse response) {

        ResponseJSONP responseJsonp = returnType.getMethodAnnotation(ResponseJSONP.class);
        if(responseJsonp == null){
            responseJsonp = returnType.getContainingClass().getAnnotation(ResponseJSONP.class);
        }

        HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();
        String callbackMethodName = servletRequest.getParameter(responseJsonp.callback());

        if (!IOUtils.isValidJsonpQueryParam(callbackMethodName)) {
            if (logger.isDebugEnabled()) {
                logger.debug("Invalid jsonp parameter value:" + callbackMethodName);
            }
            callbackMethodName = null;
        }

        JSONPObject jsonpObject = new JSONPObject(callbackMethodName);
        jsonpObject.addParameter(body);
        beforeBodyWriteInternal(jsonpObject, selectedContentType, returnType, request, response);
        return jsonpObject;
    }


    public void beforeBodyWriteInternal(JSONPObject jsonpObject, MediaType contentType,
                                        MethodParameter returnType, ServerHttpRequest request, ServerHttpResponse response) {
        //MediaType contentTypeToUse = getContentType(contentType, request, response);
        //response.getHeaders().setContentType(contentTypeToUse);
    }

    /**
     * Return the content type to set the response to.
     * This implementation always returns "application/javascript".
     *
     * @param contentType the content type selected through content negotiation
     * @param request     the current request
     * @param response    the current response
     * @return the content type to set the response to
     */
    protected MediaType getContentType(MediaType contentType, ServerHttpRequest request, ServerHttpResponse response) {
        return FastJsonHttpMessageConverter.APPLICATION_JAVASCRIPT;
    }
}
