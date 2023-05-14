package com.alibaba.fastjson.support.geo;

import com.alibaba.fastjson.annotation.JSONType;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @since 1.2.68
 */
@JSONType(typeName = "Feature", orders = {"type", "id", "bbox", "coordinates", "properties"})
public class Feature
        extends Geometry {
    private String id;
    private Geometry geometry;
    private Map<String, String> properties = new LinkedHashMap<String, String>();

    public Feature() {
        super("Feature");
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
