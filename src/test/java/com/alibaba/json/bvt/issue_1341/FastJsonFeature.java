package com.alibaba.json.bvt.issue_1341;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.jaxrs.FastJsonProvider;
import org.glassfish.jersey.CommonProperties;
import org.glassfish.jersey.internal.InternalProperties;
import org.glassfish.jersey.internal.util.PropertiesHelper;

import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;

class FastJsonFeature implements Feature {

    private final static String JSON_FEATURE = FastJsonFeature.class.getSimpleName();

    public boolean configure(final FeatureContext context) {
        final Configuration config = context.getConfiguration();
        final String jsonFeature = CommonProperties.getValue(config.getProperties(), config.getRuntimeType(), InternalProperties.JSON_FEATURE, JSON_FEATURE,
                String.class);
        // Other JSON providers registered.
        if (!JSON_FEATURE.equalsIgnoreCase(jsonFeature)) {
            return false;
        }
        // Disable other JSON providers.
        context.property(PropertiesHelper.getPropertyNameForRuntime(InternalProperties.JSON_FEATURE, config.getRuntimeType()), JSON_FEATURE);
        // Register FastJson.
        if (!config.isRegistered(FastJsonProvider.class)) {
            //DisableCircularReferenceDetect
            FastJsonProvider fastJsonProvider = new FastJsonProvider();
            FastJsonConfig fastJsonConfig = new FastJsonConfig();
            //fastJsonConfig.setSerializerFeatures(SerializerFeature.DisableCircularReferenceDetect,SerializerFeature.BrowserSecure);

            fastJsonConfig.setSerializerFeatures(SerializerFeature.DisableCircularReferenceDetect);

            fastJsonProvider.setFastJsonConfig(fastJsonConfig);

            context.register(fastJsonProvider, MessageBodyReader.class, MessageBodyWriter.class);
        }
        return true;
    }
}