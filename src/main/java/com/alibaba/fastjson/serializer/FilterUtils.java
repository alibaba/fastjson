package com.alibaba.fastjson.serializer;

import java.util.List;

public class FilterUtils {

    public static char writeBefore(JSONSerializer serializer, Object object, char seperator) {
        List<BeforeFilter> beforeFilters = serializer.beforeFilters;
        if (beforeFilters != null) {
            for (BeforeFilter beforeFilter : beforeFilters) {
                seperator = beforeFilter.writeBefore(serializer, object, seperator);
            }
        }
        return seperator;
    }
    
    public static char writeAfter(JSONSerializer serializer, Object object, char seperator) {
        List<AfterFilter> afterFilters = serializer.afterFilters;
        if (afterFilters != null) {
            for (AfterFilter afterFilter : afterFilters) {
                seperator = afterFilter.writeAfter(serializer, object, seperator);
            }
        }
        return seperator;
    }
    
    public static Object processValue(JSONSerializer serializer, Object object, String key, boolean propertyValueBool) {
        List<ValueFilter> valueFilters = serializer.valueFilters;
        
        Object propertyValue = null;
        if (serializer.out.writeNonStringValueAsString) {
            propertyValue = Boolean.toString(propertyValueBool);
        }
        
        if (valueFilters != null) {
            if (propertyValue != null) {
                propertyValue = propertyValueBool;
            }
            for (ValueFilter valueFilter : valueFilters) {
                propertyValue = valueFilter.process(object, key, propertyValue);
            }
        }

        return propertyValue;
    }
    
    public static Object processValue(JSONSerializer serializer, Object object, String key, byte propertyValueInt) {
        List<ValueFilter> valueFilters = serializer.valueFilters;
        
        Object propertyValue = null;
        if (serializer.out.writeNonStringValueAsString) {
            propertyValue = Short.toString(propertyValueInt);
        }
        
        if (valueFilters != null) {
            if (propertyValue != null) {
                propertyValue = propertyValueInt;
            }
            for (ValueFilter valueFilter : valueFilters) {
                propertyValue = valueFilter.process(object, key, propertyValue);
            }
        }

        return propertyValue;
    }
    
    public static Object processValue(JSONSerializer serializer, Object object, String key, short propertyValueInt) {
        List<ValueFilter> valueFilters = serializer.valueFilters;
        
        Object propertyValue = null;
        if (serializer.out.writeNonStringValueAsString) {
            propertyValue = Short.toString(propertyValueInt);
        }
        
        if (valueFilters != null) {
            if (propertyValue != null) {
                propertyValue = propertyValueInt;
            }
            for (ValueFilter valueFilter : valueFilters) {
                propertyValue = valueFilter.process(object, key, propertyValue);
            }
        }

        return propertyValue;
    }
    
    public static Object processValue(JSONSerializer serializer, Object object, String key, int propertyValueInt) {
        List<ValueFilter> valueFilters = serializer.valueFilters;
        
        Object propertyValue = null;
        if (serializer.out.writeNonStringValueAsString) {
            propertyValue = Integer.toString(propertyValueInt);
        }
        
        if (valueFilters != null) {
            if (propertyValue != null) {
                propertyValue = propertyValueInt;
            }
            for (ValueFilter valueFilter : valueFilters) {
                propertyValue = valueFilter.process(object, key, propertyValue);
            }
        }

        return propertyValue;
    }
    
    public static Object processValue(JSONSerializer serializer, Object object, String key, long propertyValueLong) {
        List<ValueFilter> valueFilters = serializer.valueFilters;
        
        Object propertyValue = null;
        if (serializer.out.writeNonStringValueAsString) {
            propertyValue = Long.toString(propertyValueLong);
        }
        
        if (valueFilters != null) {
            if (propertyValue != null) {
                propertyValue = propertyValueLong;
            }
            for (ValueFilter valueFilter : valueFilters) {
                propertyValue = valueFilter.process(object, key, propertyValue);
            }
        }

        return propertyValue;
    }
    
    public static Object processValue(JSONSerializer serializer, Object object, String key, float propertyValueFloat) {
        List<ValueFilter> valueFilters = serializer.valueFilters;
        
        Object propertyValue = null;
        if (serializer.out.writeNonStringValueAsString) {
            propertyValue = Float.toString(propertyValueFloat);
        }
        
        if (valueFilters != null) {
            if (propertyValue != null) {
                propertyValue = propertyValueFloat;
            }
            for (ValueFilter valueFilter : valueFilters) {
                propertyValue = valueFilter.process(object, key, propertyValue);
            }
        }

        return propertyValue;
    }
    
    public static Object processValue(JSONSerializer serializer, Object object, String key, double propertyValueDouble) {
        List<ValueFilter> valueFilters = serializer.valueFilters;
        
        Object propertyValue = null;
        if (serializer.out.writeNonStringValueAsString) {
            propertyValue = Double.toString(propertyValueDouble);
        }
        
        if (valueFilters != null) {
            if (propertyValue != null) {
                propertyValue = propertyValueDouble;
            }
            for (ValueFilter valueFilter : valueFilters) {
                propertyValue = valueFilter.process(object, key, propertyValue);
            }
        }

        return propertyValue;
    }

    public static Object processValue(JSONSerializer serializer, Object object, String key, Object propertyValue) {
        
        if (propertyValue != null // 
                && serializer.out.writeNonStringValueAsString) {
            if (propertyValue instanceof Number || propertyValue instanceof Boolean) {
                propertyValue = propertyValue.toString();
            }
        }
        
        List<ValueFilter> valueFilters = serializer.valueFilters;
        if (valueFilters != null) {
            for (ValueFilter valueFilter : valueFilters) {
                propertyValue = valueFilter.process(object, key, propertyValue);
            }
        }

        return propertyValue;
    }

    public static String processKey(JSONSerializer serializer, Object object, String key, Object propertyValue) {
        List<NameFilter> nameFilters = serializer.nameFilters;
        if (nameFilters != null) {
            for (NameFilter nameFilter : nameFilters) {
                key = nameFilter.process(object, key, propertyValue);
            }
        }

        return key;
    }

    public static String processKey(JSONSerializer serializer, Object object, String key, byte intValue) {
        List<NameFilter> nameFilters = serializer.nameFilters;
        if (nameFilters != null) {
            Object propertyValue = Byte.valueOf(intValue);

            for (NameFilter nameFilter : nameFilters) {
                key = nameFilter.process(object, key, propertyValue);
            }
        }

        return key;
    }

    public static String processKey(JSONSerializer serializer, Object object, String key, short intValue) {
        List<NameFilter> nameFilters = serializer.nameFilters;
        if (nameFilters != null) {
            Object propertyValue = Short.valueOf(intValue);

            for (NameFilter nameFilter : nameFilters) {
                key = nameFilter.process(object, key, propertyValue);
            }
        }

        return key;
    }

    public static String processKey(JSONSerializer serializer, Object object, String key, int intValue) {
        List<NameFilter> nameFilters = serializer.nameFilters;
        if (nameFilters != null) {
            Object propertyValue = Integer.valueOf(intValue);

            for (NameFilter nameFilter : nameFilters) {
                key = nameFilter.process(object, key, propertyValue);
            }
        }

        return key;
    }

    public static String processKey(JSONSerializer serializer, Object object, String key, long intValue) {
        List<NameFilter> nameFilters = serializer.nameFilters;
        if (nameFilters != null) {
            Object propertyValue = Long.valueOf(intValue);

            for (NameFilter nameFilter : nameFilters) {
                key = nameFilter.process(object, key, propertyValue);
            }
        }

        return key;
    }

    public static String processKey(JSONSerializer serializer, Object object, String key, float intValue) {
        List<NameFilter> nameFilters = serializer.nameFilters;
        if (nameFilters != null) {
            Object propertyValue = Float.valueOf(intValue);

            for (NameFilter nameFilter : nameFilters) {
                key = nameFilter.process(object, key, propertyValue);
            }
        }

        return key;
    }

    public static String processKey(JSONSerializer serializer, Object object, String key, double intValue) {
        List<NameFilter> nameFilters = serializer.nameFilters;
        if (nameFilters != null) {
            Object propertyValue = Double.valueOf(intValue);

            for (NameFilter nameFilter : nameFilters) {
                key = nameFilter.process(object, key, propertyValue);
            }
        }

        return key;
    }

    public static String processKey(JSONSerializer serializer, Object object, String key, boolean intValue) {
        List<NameFilter> nameFilters = serializer.nameFilters;
        if (nameFilters != null) {
            Object propertyValue = Boolean.valueOf(intValue);

            for (NameFilter nameFilter : nameFilters) {
                key = nameFilter.process(object, key, propertyValue);
            }
        }

        return key;
    }

    public static String processKey(JSONSerializer serializer, Object object, String key, char intValue) {
        List<NameFilter> nameFilters = serializer.nameFilters;
        if (nameFilters != null) {
            Object propertyValue = Character.valueOf(intValue);

            for (NameFilter nameFilter : nameFilters) {
                key = nameFilter.process(object, key, propertyValue);
            }
        }

        return key;
    }

    public static boolean applyName(JSONSerializer serializer, Object object, String key) {
        List<PropertyPreFilter> filters = serializer.propertyPreFilters;

        if (filters == null) {
            return true;
        }

        for (PropertyPreFilter filter : filters) {
            if (!filter.apply(serializer, object, key)) {
                return false;
            }
        }

        return true;
    }

    public static boolean apply(JSONSerializer serializer, Object object, String key, Object propertyValue) {
        List<PropertyFilter> propertyFilters = serializer.propertyFilters;

        if (propertyFilters == null) {
            return true;
        }

        for (PropertyFilter propertyFilter : propertyFilters) {
            if (!propertyFilter.apply(object, key, propertyValue)) {
                return false;
            }
        }

        return true;
    }

    public static boolean apply(JSONSerializer serializer, Object object, String key, byte value) {
        List<PropertyFilter> propertyFilters = serializer.propertyFilters;

        if (propertyFilters != null) {
            boolean apply = true;

            Object propertyValue = Byte.valueOf(value);
            for (PropertyFilter propertyFilter : propertyFilters) {
                if (!propertyFilter.apply(object, key, propertyValue)) {
                    return false;
                }
            }

            return apply;
        }

        return true;
    }

    public static boolean apply(JSONSerializer serializer, Object object, String key, short value) {
        List<PropertyFilter> propertyFilters = serializer.propertyFilters;

        if (propertyFilters != null) {
            boolean apply = true;

            Object propertyValue = Short.valueOf(value);
            for (PropertyFilter propertyFilter : propertyFilters) {
                if (!propertyFilter.apply(object, key, propertyValue)) {
                    return false;
                }
            }

            return apply;
        }

        return true;
    }

    public static boolean apply(JSONSerializer serializer, Object object, String key, int value) {
        List<PropertyFilter> propertyFilters = serializer.propertyFilters;

        if (propertyFilters != null) {
            boolean apply = true;

            Object propertyValue = Integer.valueOf(value);
            for (PropertyFilter propertyFilter : propertyFilters) {
                if (!propertyFilter.apply(object, key, propertyValue)) {
                    return false;
                }
            }

            return apply;
        }

        return true;
    }

    public static boolean apply(JSONSerializer serializer, Object object, String key, char value) {
        List<PropertyFilter> propertyFilters = serializer.propertyFilters;

        if (propertyFilters != null) {
            boolean apply = true;

            Object propertyValue = Character.valueOf(value);
            for (PropertyFilter propertyFilter : propertyFilters) {
                if (!propertyFilter.apply(object, key, propertyValue)) {
                    return false;
                }
            }

            return apply;
        }

        return true;
    }

    public static boolean apply(JSONSerializer serializer, Object object, String key, long value) {
        List<PropertyFilter> propertyFilters = serializer.propertyFilters;

        if (propertyFilters != null) {
            boolean apply = true;

            Object propertyValue = Long.valueOf(value);
            for (PropertyFilter propertyFilter : propertyFilters) {
                if (!propertyFilter.apply(object, key, propertyValue)) {
                    return false;
                }
            }

            return apply;
        }

        return true;
    }

    public static boolean apply(JSONSerializer serializer, Object object, String key, float value) {
        List<PropertyFilter> propertyFilters = serializer.propertyFilters;

        if (propertyFilters != null) {
            boolean apply = true;

            Object propertyValue = Float.valueOf(value);
            for (PropertyFilter propertyFilter : propertyFilters) {
                if (!propertyFilter.apply(object, key, propertyValue)) {
                    return false;
                }
            }

            return apply;
        }

        return true;
    }

    public static boolean apply(JSONSerializer serializer, Object object, String key, double value) {
        List<PropertyFilter> propertyFilters = serializer.propertyFilters;

        if (propertyFilters != null) {
            boolean apply = true;

            Object propertyValue = Double.valueOf(value);
            for (PropertyFilter propertyFilter : propertyFilters) {
                if (!propertyFilter.apply(object, key, propertyValue)) {
                    return false;
                }
            }

            return apply;
        }

        return true;
    }
    
    public static boolean applyLabel(JSONSerializer serializer, String label) {
        List<LabelFilter> labelFilters = serializer.labelFilters;

        if (labelFilters != null) {
            boolean apply = true;

            for (LabelFilter propertyFilter : labelFilters) {
                if (!propertyFilter.apply(label)) {
                    return false;
                }
            }

            return apply;
        }

        return true;
    }
}
