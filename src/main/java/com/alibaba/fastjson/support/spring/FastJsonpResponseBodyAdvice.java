package com.alibaba.fastjson.support.spring;

import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * A convenient base class for {@code ResponseBodyAdvice} implementations
 * that customize the response before JSON serialization with {@link FastJsonpHttpMessageConverter4}'s concrete
 * subclasses.
 * <p>
 * Compatible Spring MVC version 4.2+
 *
 * @author Jerry.Chen
 * @since 1.2.20
 */
@ControllerAdvice
public class FastJsonpResponseBodyAdvice implements ResponseBodyAdvice<Object> {
    /**
     * Pattern for validating jsonp callback parameter values.
     */
    private static final Pattern CALLBACK_PARAM_PATTERN = Pattern.compile("[0-9A-Za-z_\\.]*");
    private final String[] jsonpQueryParamNames;

    public FastJsonpResponseBodyAdvice(String... queryParamNames) {
        Assert.isTrue(!ObjectUtils.isEmpty(queryParamNames), "At least one query param name is required");
        this.jsonpQueryParamNames = queryParamNames;
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return FastJsonpHttpMessageConverter4.class.isAssignableFrom(converterType);
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
            Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
            ServerHttpResponse response) {
        MappingFastJsonValue container = getOrCreateContainer(body);
        beforeBodyWriteInternal(container, selectedContentType, returnType, request, response);
        return container;
    }

    /**
     * Wrap the body in a {@link MappingFastJsonValue} value container (for providing
     * additional serialization instructions) or simply cast it if already wrapped.
     */
    protected MappingFastJsonValue getOrCreateContainer(Object body) {
        return (body instanceof MappingFastJsonValue ? (MappingFastJsonValue) body : new MappingFastJsonValue(body));
    }

    /**
     * Invoked only if the converter type is {@code FastJsonpHttpMessageConverter4}.
     */
    public void beforeBodyWriteInternal(MappingFastJsonValue bodyContainer, MediaType contentType,
            MethodParameter returnType, ServerHttpRequest request, ServerHttpResponse response) {
        HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();
        for (String name : this.jsonpQueryParamNames) {
            String value = servletRequest.getParameter(name);
            if (value != null) {
                if (!isValidJsonpQueryParam(value)) {
                    continue;
                }
                MediaType contentTypeToUse = getContentType(contentType, request, response);
                response.getHeaders().setContentType(contentTypeToUse);
                bodyContainer.setJsonpFunction(value);
                break;
            }
        }
    }

    /**
     * Validate the jsonp query parameter value. The default implementation
     * returns true if it consists of digits, letters, or "_" and ".".
     * Invalid parameter values are ignored.
     *
     * @param value the query param value, never {@code null}
     */
    protected boolean isValidJsonpQueryParam(String value) {
        return CALLBACK_PARAM_PATTERN.matcher(value).matches();
    }

    /**
     * Return the content type to set the response to.
     * This implementation always returns "application/javascript".
     *
     * @param contentType the content type selected through content negotiation
     * @param request the current request
     * @param response the current response
     * @return the content type to set the response to
     */
    protected MediaType getContentType(MediaType contentType, ServerHttpRequest request, ServerHttpResponse response) {
        return new MediaType("application", "javascript");
    }
}
