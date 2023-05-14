package com.alibaba.fastjson.support.geo;

import com.alibaba.fastjson.annotation.JSONType;
import java.util.ArrayList;
import java.util.List;

/**
 * @since 1.2.68
 */
@JSONType(typeName = "GeometryCollection", orders = {"type", "bbox", "geometries"})
public class GeometryCollection extends Geometry {
    private List<Geometry> geometries = new ArrayList<Geometry>();

    public GeometryCollection() {
        super("GeometryCollection");
    }

    public List<Geometry> getGeometries() {
        return geometries;
    }
}
