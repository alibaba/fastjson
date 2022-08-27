package com.alibaba.fastjson.support.geo;

import com.alibaba.fastjson.annotation.JSONType;

/**
 * @since 1.2.68
 */
@JSONType(typeName = "LineString", orders = {"type", "bbox", "coordinates"})
public class LineString extends Geometry {
    private double[][] coordinates;

    public LineString() {
        super("LineString");
    }

    public double[][] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(double[][] coordinates) {
        this.coordinates = coordinates;
    }
}
