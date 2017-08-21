package com.alibaba.fastjson.support.spring;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONPObject;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.util.IOUtils;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.view.AbstractView;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Fastjson for Spring MVC View.
 *
 * @author libinsong1204@gmail.com
 * @author VictorZeng
 * @see AbstractView
 * @since 1.2.9
 */

public class FastJsonJsonView extends AbstractView {


    /**
     * default content type
     */
    public static final String DEFAULT_CONTENT_TYPE = "application/json;charset=UTF-8";

    /**
     * Default content type for JSONP: "application/javascript".
     */
    public static final String DEFAULT_JSONP_CONTENT_TYPE = "application/javascript";

    /**
     * Pattern for validating jsonp callback parameter values.
     */
    private static final Pattern CALLBACK_PARAM_PATTERN = Pattern.compile("[0-9A-Za-z_\\.]*");

    @Deprecated
    protected Charset charset = Charset.forName("UTF-8");

    @Deprecated
    protected SerializerFeature[] features = new SerializerFeature[0];

    @Deprecated
    protected SerializeFilter[] filters = new SerializeFilter[0];

    @Deprecated
    protected String dateFormat;

    /**
     * renderedAttributes
     */
    private Set<String> renderedAttributes;

    /**
     * disableCaching
     */
    private boolean disableCaching = true;

    /**
     * updateContentLength
     */
    private boolean updateContentLength = true;

    /**
     * extractValueFromSingleKeyModel
     */
    private boolean extractValueFromSingleKeyModel = false;

    /**
     * with fastJson config
     */
    private FastJsonConfig fastJsonConfig = new FastJsonConfig();

    /**
     * jsonp parameter name
     */
    private String[] jsonpParameterNames = {"jsonp", "callback"};

    /**
     * Set default param.
     */
    public FastJsonJsonView() {

        setContentType(DEFAULT_CONTENT_TYPE);
        setExposePathVariables(false);
    }

    /**
     * @return the fastJsonConfig.
     * @since 1.2.11
     */
    public FastJsonConfig getFastJsonConfig() {
        return fastJsonConfig;
    }

    /**
     * @param fastJsonConfig the fastJsonConfig to set.
     * @since 1.2.11
     */
    public void setFastJsonConfig(FastJsonConfig fastJsonConfig) {
        this.fastJsonConfig = fastJsonConfig;
    }

    @Deprecated
    public void setSerializerFeature(SerializerFeature... features) {
        this.fastJsonConfig.setSerializerFeatures(features);
    }

    @Deprecated
    public Charset getCharset() {
        return this.fastJsonConfig.getCharset();
    }

    @Deprecated
    public void setCharset(Charset charset) {
        this.fastJsonConfig.setCharset(charset);
    }

    @Deprecated
    public String getDateFormat() {
        return this.fastJsonConfig.getDateFormat();
    }

    @Deprecated
    public void setDateFormat(String dateFormat) {
        this.fastJsonConfig.setDateFormat(dateFormat);
    }

    @Deprecated
    public SerializerFeature[] getFeatures() {
        return this.fastJsonConfig.getSerializerFeatures();
    }

    @Deprecated
    public void setFeatures(SerializerFeature... features) {
        this.fastJsonConfig.setSerializerFeatures(features);
    }

    @Deprecated
    public SerializeFilter[] getFilters() {
        return this.fastJsonConfig.getSerializeFilters();
    }

    @Deprecated
    public void setFilters(SerializeFilter... filters) {
        this.fastJsonConfig.setSerializeFilters(filters);
    }

    /**
     * Set renderedAttributes.
     *
     * @param renderedAttributes renderedAttributes
     */
    public void setRenderedAttributes(Set<String> renderedAttributes) {
        this.renderedAttributes = renderedAttributes;
    }

    /**
     * Check extractValueFromSingleKeyModel.
     *
     * @return extractValueFromSingleKeyModel
     */
    public boolean isExtractValueFromSingleKeyModel() {
        return extractValueFromSingleKeyModel;
    }

    /**
     * Set extractValueFromSingleKeyModel.
     *
     * @param extractValueFromSingleKeyModel
     */
    public void setExtractValueFromSingleKeyModel( 
            boolean extractValueFromSingleKeyModel) {
        this.extractValueFromSingleKeyModel = extractValueFromSingleKeyModel;
    }

    /**
     * Set JSONP request parameter names. Each time a request has one of those
     * parameters, the resulting JSON will be wrapped into a function named as
     * specified by the JSONP request parameter value.
     * <p>The parameter names configured by default are "jsonp" and "callback".
     * @since 4.1
     * @see <a href="http://en.wikipedia.org/wiki/JSONP">JSONP Wikipedia article</a>
     */
    public void setJsonpParameterNames(Set<String> jsonpParameterNames) {
        Assert.notEmpty(jsonpParameterNames, "jsonpParameterName cannot be empty");
        this.jsonpParameterNames = jsonpParameterNames.toArray(new String[jsonpParameterNames.size()]);
    }


    private String getJsonpParameterValue(HttpServletRequest request) {
        if (this.jsonpParameterNames != null) {
            for (String name : this.jsonpParameterNames) {
                String value = request.getParameter(name);

                if (IOUtils.isValidJsonpQueryParam(value)) {
                    return value;
                }

                if (logger.isDebugEnabled()) {
                    logger.debug("Ignoring invalid jsonp parameter value: " + value);
                }
            }
        }
        return null;
    }



    @Override
    protected void renderMergedOutputModel(Map<String, Object> model, //
                                           HttpServletRequest request, //
                                           HttpServletResponse response) throws Exception {
        Object value = filterModel(model);
        String jsonpParameterValue = getJsonpParameterValue(request);
        if(jsonpParameterValue != null) {
            JSONPObject jsonpObject = new JSONPObject(jsonpParameterValue);
            jsonpObject.addParameter(value);
            value = jsonpObject;
        }

        ByteArrayOutputStream outnew = new ByteArrayOutputStream();

        int len = JSON.writeJSONString(outnew, //
                fastJsonConfig.getCharset(), //
                value, //
                fastJsonConfig.getSerializeConfig(), //
                fastJsonConfig.getSerializeFilters(), //
                fastJsonConfig.getDateFormat(), //
                JSON.DEFAULT_GENERATE_FEATURE, //
                fastJsonConfig.getSerializerFeatures());

        if (this.updateContentLength) {
            // Write content length (determined via byte array).
            response.setContentLength(len);
        }

        // Flush byte array to servlet output stream.
        ServletOutputStream out = response.getOutputStream();
        outnew.writeTo(out);
        outnew.close();
        out.flush();
    }

    @Override
    protected void prepareResponse(HttpServletRequest request, //
                                   HttpServletResponse response) {

        setResponseContentType(request, response);
        response.setCharacterEncoding(fastJsonConfig.getCharset().name());
        if (this.disableCaching) {
            response.addHeader("Pragma", "no-cache");
            response.addHeader("Cache-Control", "no-cache, no-store, max-age=0");
            response.addDateHeader("Expires", 1L);
        }
    }

    /**
     * Disables caching of the generated JSON.
     * <p>
     * Default is {@code true}, which will prevent the client from caching the
     * generated JSON.
     */
    public void setDisableCaching(boolean disableCaching) {
        this.disableCaching = disableCaching;
    }

    /**
     * Whether to update the 'Content-Length' header of the response. When set
     * to {@code true}, the response is buffered in order to determine the
     * content length and set the 'Content-Length' header of the response.
     * <p>
     * The default setting is {@code false}.
     */
    public void setUpdateContentLength(boolean updateContentLength) {
        this.updateContentLength = updateContentLength;
    }

    /**
     * Filters out undesired attributes from the given model. The return value
     * can be either another {@link Map}, or a single value object.
     * <p>
     * Default implementation removes {@link BindingResult} instances and
     * entries not included in the {@link #setRenderedAttributes(Set)
     * renderedAttributes} property.
     *
     * @param model the model, as passed on to {@link #renderMergedOutputModel}
     * @return the object to be rendered
     */
    protected Object filterModel(Map<String, Object> model) {
        Map<String, Object> result = new HashMap<String, Object>(model.size());
        Set<String> renderedAttributes = !CollectionUtils.isEmpty(this.renderedAttributes) ? //
                this.renderedAttributes //
                : model.keySet();

        for (Map.Entry<String, Object> entry : model.entrySet()) {
            if (!(entry.getValue() instanceof BindingResult)
                    && renderedAttributes.contains(entry.getKey())) {
                result.put(entry.getKey(), entry.getValue());
            }
        }
        if (extractValueFromSingleKeyModel) {
            if (result.size() == 1) {
                for (Map.Entry<String, Object> entry : result.entrySet()) {
                    return entry.getValue();
                }
            }
        }
        return result;
    }

    @Override
    protected void setResponseContentType(HttpServletRequest request, HttpServletResponse response) {
        if (getJsonpParameterValue(request) != null) {
            response.setContentType(DEFAULT_JSONP_CONTENT_TYPE);
        }
        else {
            super.setResponseContentType(request, response);
        }
    }


}