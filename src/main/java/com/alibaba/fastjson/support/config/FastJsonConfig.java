
package com.alibaba.fastjson.support.config;

import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.util.IOUtils;

import java.nio.charset.Charset;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Config for FastJson.
 *
 * @author VictorZeng
 * @see SerializeConfig
 * @see ParserConfig
 * @see SerializerFeature
 * @see SerializeFilter
 * @see Feature
 * @since 1.2.11
 */

public class FastJsonConfig {

    /**
     * default charset
     */
    private Charset charset;

    /**
     * serializeConfig
     */
    private SerializeConfig serializeConfig;

    /**
     * parserConfig
     */
    private ParserConfig parserConfig;

    /**
     * serializerFeatures
     */
    private SerializerFeature[] serializerFeatures;

    /**
     * serializeFilters
     */
    private SerializeFilter[] serializeFilters;

    /**
     * features
     */
    private Feature[] features;

    /**
     * class level serializeFilter
     */
    private Map<Class<?>, SerializeFilter> classSerializeFilters;

    /**
     * format date type
     */
    private String dateFormat;

    /**
     * init param.
     */
    public FastJsonConfig() {

        this.charset = IOUtils.UTF8;

        this.serializeConfig = SerializeConfig.getGlobalInstance();
        this.parserConfig = ParserConfig.getGlobalInstance();

        this.serializerFeatures = new SerializerFeature[0];
        this.serializeFilters = new SerializeFilter[0];
        this.features = new Feature[0];
    }

    /**
     * @return the serializeConfig
     */
    public SerializeConfig getSerializeConfig() {
        return serializeConfig;
    }

    /**
     * @param serializeConfig the serializeConfig to set
     */
    public void setSerializeConfig(SerializeConfig serializeConfig) {
        this.serializeConfig = serializeConfig;
    }

    /**
     * @return the parserConfig
     */
    public ParserConfig getParserConfig() {
        return parserConfig;
    }

    /**
     * @param parserConfig the parserConfig to set
     */
    public void setParserConfig(ParserConfig parserConfig) {
        this.parserConfig = parserConfig;
    }

    /**
     * @return the serializerFeatures
     */
    public SerializerFeature[] getSerializerFeatures() {
        return serializerFeatures;
    }

    /**
     * @param serializerFeatures the serializerFeatures to set
     */
    public void setSerializerFeatures(SerializerFeature... serializerFeatures) {
        this.serializerFeatures = serializerFeatures;
    }

    /**
     * @return the serializeFilters
     */
    public SerializeFilter[] getSerializeFilters() {
        return serializeFilters;
    }

    /**
     * @param serializeFilters the serializeFilters to set
     */
    public void setSerializeFilters(SerializeFilter... serializeFilters) {
        this.serializeFilters = serializeFilters;
    }

    /**
     * @return the features
     */
    public Feature[] getFeatures() {
        return features;
    }

    /**
     * @param features the features to set
     */
    public void setFeatures(Feature... features) {
        this.features = features;
    }

    /**
     * @return the classSerializeFilters
     */
    public Map<Class<?>, SerializeFilter> getClassSerializeFilters() {
        return classSerializeFilters;
    }

    /**
     * @param classSerializeFilters the classSerializeFilters to set
     */
    public void setClassSerializeFilters(
            Map<Class<?>, SerializeFilter> classSerializeFilters) {

        if (classSerializeFilters == null)
            return;

        for (Entry<Class<?>, SerializeFilter> entry : classSerializeFilters.entrySet())

            this.serializeConfig.addFilter(entry.getKey(), entry.getValue());

        this.classSerializeFilters = classSerializeFilters;
    }

    /**
     * @return the dateFormat
     */
    public String getDateFormat() {
        return dateFormat;
    }

    /**
     * @param dateFormat the dateFormat to set
     */
    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    /**
     * @return the charset
     */
    public Charset getCharset() {
        return charset;
    }

    /**
     * @param charset the charset to set
     */
    public void setCharset(Charset charset) {
        this.charset = charset;
    }
}
