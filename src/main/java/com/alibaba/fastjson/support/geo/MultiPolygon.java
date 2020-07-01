package com.alibaba.fastjson.support.geo;

import com.alibaba.fastjson.annotation.JSONType;

/**
 * @since 1.2.68
 */
@JSONType(typeName = "MultiPolygon", orders = {"type", "bbox", "coordinates"})
public class MultiPolygon
        extends Geometry {
    private double[][][][] coordinates;

    public MultiPolygon() {
        super("MultiPolygon");
    }

    public double[][][][] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(double[][][][] coordinates) {
        this.coordinates = coordinates;
    }
}
