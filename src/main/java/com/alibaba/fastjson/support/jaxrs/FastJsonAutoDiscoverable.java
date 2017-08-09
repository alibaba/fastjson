package com.alibaba.fastjson.support.jaxrs;

import org.glassfish.jersey.CommonProperties;
import org.glassfish.jersey.internal.InternalProperties;
import org.glassfish.jersey.internal.spi.AutoDiscoverable;
import org.glassfish.jersey.internal.util.PropertiesHelper;

import javax.annotation.Priority;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.FeatureContext;

/**
 * <p>Title: FastJsonAutoDiscoverable</p>
 * <p>Description: FastJsonAutoDiscoverable</p>
 *
 * @author Victor.Zxy
 * @see AutoDiscoverable
 * @since 1.2.36
 */
@Priority(AutoDiscoverable.DEFAULT_PRIORITY + 1)
public class FastJsonAutoDiscoverable implements AutoDiscoverable {

    private final static String JSON_FEATURE = "FastJsonFeature";

    @Override
    public void configure(FeatureContext context) {

        final Configuration config = context.getConfiguration();

        final String jsonFeature = CommonProperties.getValue(config.getProperties(), config.getRuntimeType(), InternalProperties.JSON_FEATURE, JSON_FEATURE,
                String.class);

        if (JSON_FEATURE.equalsIgnoreCase(jsonFeature)) {

            // Disable other JSON providers.
            context.property(PropertiesHelper.getPropertyNameForRuntime(InternalProperties.JSON_FEATURE, config.getRuntimeType()), JSON_FEATURE);
        }

        // Register FastJson.
        if (!config.isRegistered(FastJsonProvider.class)) {

            context.register(FastJsonProvider.class);
        }
    }
}
