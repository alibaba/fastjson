package com.alibaba.json.bvt.bug.bug201810;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class LatLngTest extends TestCase {
    public void test_latlng() throws Exception {
        LatLng v = new LatLng();
        JSON.toJSONString(v);
    }

    public static class LatLng implements Serializable {
        /**
         * serialVersionUID
         */
        private static final long serialVersionUID = -9176496417369601807L;

        public LatLng() {}

        public LatLng(Double lat, Double lng) {
            this.lat = lat;
            this.lng = lng;
        }

        /**
         * 纬度
         */
        @Min(-90)
        @Max(90)
        @NotNull
        private Double lat;
        /**
         * 经度
         */
        @Min(-180)
        @Max(180)
        @NotNull
        private Double lng;

        /**
         * Getter method for property <tt>lat</tt>.
         *
         * @return property value of lat
         */
        public Double getLat() {
            return lat;
        }

        /**
         * Setter method for property <tt>lat</tt>.
         *
         * @param lat value to be assigned to property lat
         */
        public void setLat(Double lat) {
            this.lat = lat;
        }

        /**
         * Getter method for property <tt>lng</tt>.
         *
         * @return property value of lng
         */
        public Double getLng() {
            return lng;
        }

        /**
         * Setter method for property <tt>lng</tt>.
         *
         * @param lng value to be assigned to property lng
         */
        public void setLng(Double lng) {
            this.lng = lng;
        }

        @Override
        public String toString() {
            return lat + " " + lng;
        }

    }
}
