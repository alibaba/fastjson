package com.alibaba.fastjson.serializer;

import java.util.List;

public class FilterUtils {
    public static Object processValue(JSONSerializer serializer, Object object, String key, Object propertyValue) {
        List<ValueFilter> valueFilters = serializer.getValueFiltersDirect();
        if (valueFilters != null) {
            for (ValueFilter valueFilter : valueFilters) {
                propertyValue = valueFilter.process(object, key, propertyValue);
            }
        }

        return propertyValue;
    }
    
    public static String processKey(JSONSerializer serializer, Object object, String key, DelayObject delayPropertyValue) {
        List<NameFilter> nameFilters = serializer.getNameFiltersDirect();
        if (nameFilters != null) {
            for (NameFilter nameFilter : nameFilters) {
                key = nameFilter.process(object, key, delayPropertyValue);
            }
        }

        return key;
    }

    public static boolean apply(JSONSerializer serializer, Object object, String key, DelayObject delayPropertyValue) {
        List<PropertyFilter> propertyFilters = serializer.getPropertyFiltersDirect();
        
        if (propertyFilters != null) {
            boolean apply = true;
            for (PropertyFilter propertyFilter : propertyFilters) {
                if (!propertyFilter.apply(object, key, delayPropertyValue)) {
                    return false;
                }
            }

            return apply;
        }

        return true;
    }

}
