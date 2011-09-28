package com.derbysoft.spitfire.fastjson.dto;

import java.util.ArrayList;
import java.util.List;

public class RoomTypeDTO extends AbstractDTO {
    private String code;

    private String name;

    private String description;

    private List<String> amenities = new ArrayList<String>();

    private CompositeType compositeType = CompositeType.UNKNOWN;

    private Integer floor;

    private SmokingType smokingType = SmokingType.INDIFFERENT;

    private String sizeMeasurement;

    private Integer bedCount;

    private Integer adultCount;

    private Integer childCount;

    private Integer maxGuestCount;

    private Integer extraBedCount;

    private BathType bathType;

    private InternetDTO internet;

    private TPAExtensionsDTO tpaExtensions;

    public RoomTypeDTO() {
    }

    public RoomTypeDTO(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getAmenities() {
        return amenities;
    }

    public void setAmenities(List<String> amenities) {
        this.amenities = amenities;
    }

    public CompositeType getCompositeType() {
        return compositeType;
    }

    public void setCompositeType(CompositeType compositeType) {
        this.compositeType = compositeType;
    }

    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    public SmokingType getSmokingType() {
        return smokingType;
    }

    public void setSmokingType(SmokingType smokingType) {
        this.smokingType = smokingType;
    }

    public String getSizeMeasurement() {
        return sizeMeasurement;
    }

    public void setSizeMeasurement(String sizeMeasurement) {
        this.sizeMeasurement = sizeMeasurement;
    }

    public TPAExtensionsDTO getTpaExtensions() {
        return tpaExtensions;
    }

    public void setTpaExtensions(TPAExtensionsDTO tpaExtensions) {
        this.tpaExtensions = tpaExtensions;
    }

    public Integer getAdultCount() {
        return adultCount;
    }

    public void setAdultCount(Integer adultCount) {
        this.adultCount = adultCount;
    }

    public Integer getChildCount() {
        return childCount;
    }

    public void setChildCount(Integer childCount) {
        this.childCount = childCount;
    }

    public Integer getMaxGuestCount() {
        return maxGuestCount;
    }

    public void setMaxGuestCount(Integer maxGuestCount) {
        this.maxGuestCount = maxGuestCount;
    }

    public Integer getExtraBedCount() {
        return extraBedCount;
    }

    public void setExtraBedCount(Integer extraBedCount) {
        this.extraBedCount = extraBedCount;
    }

    public BathType getBathType() {
        return bathType;
    }

    public void setBathType(BathType bathType) {
        this.bathType = bathType;
    }

    public Integer getBedCount() {
        return bedCount;
    }

    public void setBedCount(Integer bedCount) {
        this.bedCount = bedCount;
    }

    public InternetDTO getInternet() {
        return internet;
    }

    public void setInternet(InternetDTO internet) {
        this.internet = internet;
    }
}
