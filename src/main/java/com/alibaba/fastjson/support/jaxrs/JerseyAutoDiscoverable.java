package com.alibaba.fastjson.support.jaxrs;

import org.glassfish.jersey.internal.spi.AutoDiscoverable;

import javax.annotation.Priority;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.FeatureContext;

/**
 * <p>Title: JerseyAutoDiscoverable</p>
 * <p>Description: JerseyAutoDiscoverable</p>
 *
 * @author Victor.Zxy
 * @see AutoDiscoverable
 * @since 1.2.36
 */
@Priority(AutoDiscoverable.DEFAULT_PRIORITY + 1)
public class JerseyAutoDiscoverable implements AutoDiscoverable {

    @Override
    public void configure(FeatureContext context) {

        final Configuration config = context.getConfiguration();

        // Register FastJson.
        if (!config.isRegistered(FastJsonProvider.class)) {

            context.register(FastJsonProvider.class);
        }
    }
}
