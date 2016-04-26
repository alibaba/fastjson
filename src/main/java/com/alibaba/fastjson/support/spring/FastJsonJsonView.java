package com.alibaba.fastjson.support.spring;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.view.AbstractView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.util.IOUtils;

/**
 * Fastjson for Spring MVC View.
 *
 * @author libinsong1204@gmail.com
 * @author VictorZeng
 * 
 * @since 1.2.9
 * @see AbstractView
 */

public class FastJsonJsonView extends AbstractView {

	/** default content type */
	public static final String DEFAULT_CONTENT_TYPE = "application/json;charset=UTF-8";

	/** default charset */
	private Charset charset = IOUtils.UTF8;

	/** serializer feature */
	private SerializerFeature[] features = new SerializerFeature[0];

	/** serialize filter */
	private SerializeFilter[] filters = new SerializeFilter[0];

	/** dateFormat */
	private String dateFormat;
	
	/** renderedAttributes */
	private Set<String> renderedAttributes;

	/** disableCaching */
	private boolean disableCaching = true;

	/** updateContentLength */
	private boolean updateContentLength = false;

	/** extractValueFromSingleKeyModel */
	private boolean extractValueFromSingleKeyModel = false;

	/**
	 * Set default param.
	 */
	public FastJsonJsonView() {
		setCharset(charset);
		setContentType(DEFAULT_CONTENT_TYPE);
		setExposePathVariables(false);
	}

	/**
	 * Set renderedAttributes.
	 *
	 * @param renderedAttributes renderedAttributes
	 */
	public void setRenderedAttributes(Set<String> renderedAttributes) {
		this.renderedAttributes = renderedAttributes;
	}

	@Deprecated
	public void setSerializerFeature(SerializerFeature... features) {
		this.setFeatures(features);
	}
	
	/**
	 * Get charset.
	 *
	 * @return charset
	 */
	public Charset getCharset() {
		return this.charset;
	}

	/**
	 * Set charset.
	 * 
	 * @param charset Charset
	 */
	public void setCharset(Charset charset) {
		this.charset = charset;
	}

	/**
	 * Get dateFormat.
	 * 
	 * @return dateFormat
	 */
	public String getDateFormat() {
		return dateFormat;
	}

	/**
	 * Set dateFormat.
	 *
	 * @param dateFormat String
	 */
	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	/**
	 * Get features.
	 *
	 * @return features SerializerFeature[]
	 */
	public SerializerFeature[] getFeatures() {
		return features;
	}

	/**
	 * Set features.
	 *
	 * @param features SerializerFeature[]
	 */
	public void setFeatures(SerializerFeature... features) {
		this.features = features;
	}

	/**
	 * Get filters.
	 *
	 * @return filters SerializeFilter[]
	 */
	public SerializeFilter[] getFilters() {
		return filters;
	}

	/**
	 * Set filters.
	 * 
	 * @param filters SerializeFilter[]
	 */
	public void setFilters(SerializeFilter... filters) {
		this.filters = filters;
	}

	/**
	 * Add SerializeFilter
	 *
	 * @param filter SerializeFilter
	 */
	public void addSerializeFilter(SerializeFilter filter) {
		if (filter == null) {
			return;
		}
		
		SerializeFilter[] filters = new SerializeFilter[this.filters.length + 1];
		List<SerializeFilter> filterList = new ArrayList<>(Arrays.asList(this.filters));
		filterList.add(filter);
		this.filters = filterList.toArray(filters);
	}

	/**
	 * Add SerializerFeature
	 *
	 * @param feature SerializerFeature
	 */
	public void addSerializerFeature(SerializerFeature feature) {
		if (feature == null) {
			return;
		}
		
		SerializerFeature[] features = new SerializerFeature[this.features.length + 1];
		List<SerializerFeature> featureList = new ArrayList<>(Arrays.asList(this.features));
		featureList.add(feature);
		this.features = featureList.toArray(features);
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
	
	@Override
    protected void renderMergedOutputModel(Map<String, Object> model, //
                                           HttpServletRequest request, //
                                           HttpServletResponse response) throws Exception {
	    
		Object value = filterModel(model);
		OutputStream stream = this.updateContentLength ? createTemporaryOutputStream()
            : response.getOutputStream();
        JSON.writeJSONString(value, //
                             stream, //
                             charset, //
                             SerializeConfig.globalInstance, //
                             filters, //
                             dateFormat, //
                             JSON.DEFAULT_GENERATE_FEATURE, //
                             features);
		
		stream.flush();
		
		if (this.updateContentLength) {
			writeToResponse(response, (ByteArrayOutputStream) stream);
		}
	}

	@Override
    protected void prepareResponse(HttpServletRequest request, //
                                   HttpServletResponse response) {
	    
		setResponseContentType(request, response);
		response.setCharacterEncoding(charset.name());
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
	 * @param model
	 *            the model, as passed on to {@link #renderMergedOutputModel}
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

}