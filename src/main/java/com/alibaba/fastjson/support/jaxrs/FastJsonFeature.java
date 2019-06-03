package com.alibaba.fastjson.support.jaxrs;

import org.glassfish.jersey.CommonProperties;
import org.glassfish.jersey.internal.InternalProperties;
import org.glassfish.jersey.internal.util.PropertiesHelper;

import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;

/**
 * <p>Title: FastJsonFeature</p>
 * <p>Description: FastJsonFeature</p>
 *
 * @author Victor.Zxy
 * @see Feature
 * @since 1.2.37
 */
public class FastJsonFeature implements Feature {

    private final static String JSON_FEATURE = FastJsonFeature.class.getSimpleName();

    @Override
    public boolean configure(final FeatureContext context) {
        try {
            final Configuration config = context.getConfiguration();

            final String jsonFeature = CommonProperties.getValue(
                    config.getProperties()
                    , config.getRuntimeType()
                    , InternalProperties.JSON_FEATURE, JSON_FEATURE,
                    String.class
            );

            // Other JSON providers registered.
            if (!JSON_FEATURE.equalsIgnoreCase(jsonFeature)) {
                return false;
            }

            // Disable other JSON providers.
            context.property(
                    PropertiesHelper.getPropertyNameForRuntime(InternalProperties.JSON_FEATURE, config.getRuntimeType())
                    , JSON_FEATURE);

            // Register FastJson.
            if (!config.isRegistered(FastJsonProvider.class)) {
                context.register(FastJsonProvider.class, MessageBodyReader.class, MessageBodyWriter.class);
            }
        } catch (NoSuchMethodError e) {
            // skip
        }

        return true;
    }
}
