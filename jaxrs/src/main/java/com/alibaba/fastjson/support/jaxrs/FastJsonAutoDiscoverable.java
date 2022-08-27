package com.alibaba.fastjson.support.jaxrs;

import org.glassfish.jersey.internal.spi.AutoDiscoverable;

import javax.annotation.Priority;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.FeatureContext;

/**
 * <p>Title: FastJsonAutoDiscoverable</p>
 * <p>Description: FastJsonAutoDiscoverable</p>
 *
 * @author Victor.Zxy
 * @see AutoDiscoverable
 * @since 1.2.37
 */
@Priority(AutoDiscoverable.DEFAULT_PRIORITY - 1)
public class FastJsonAutoDiscoverable implements AutoDiscoverable {

    public static final String FASTJSON_AUTO_DISCOVERABLE = "fastjson.auto.discoverable";

    public volatile static boolean autoDiscover = true;

    static {
        try {
            autoDiscover = Boolean.parseBoolean(
                    System.getProperty(FASTJSON_AUTO_DISCOVERABLE, String.valueOf(autoDiscover)));
        } catch (Throwable ex) {
            //skip
        }
    }

    public void configure(final FeatureContext context) {

        final Configuration config = context.getConfiguration();

        // Register FastJson.
        if (!config.isRegistered(FastJsonFeature.class) && autoDiscover) {

            context.register(FastJsonFeature.class);
        }
    }

}
