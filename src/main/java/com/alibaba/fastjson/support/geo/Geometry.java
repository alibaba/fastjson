package com.alibaba.fastjson.support.geo;

import com.alibaba.fastjson.annotation.JSONType;

/**
 * @since 1.2.68
 */
@JSONType(seeAlso = {GeometryCollection.class
        , LineString.class
        , MultiLineString.class
        , Point.class
        , MultiPoint.class
        , Polygon.class
        , MultiPolygon.class
        , Feature.class
        , FeatureCollection.class}
    , typeKey = "type")
public abstract class Geometry {
    private final String type;
    private double[] bbox;

    protected Geometry(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public double[] getBbox() {
        return bbox;
    }

    public void setBbox(double[] bbox) {
        this.bbox = bbox;
    }
}
