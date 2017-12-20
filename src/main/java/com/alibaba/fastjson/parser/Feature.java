/*
 * Copyright 1999-2017 Alibaba Group.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.fastjson.parser;


/**
 * @author wenshao[szujobs@hotmail.com]
 */
public enum Feature {
    /**
	 * 
	 */
    AutoCloseSource,
    /**
	 * 
	 */
    AllowComment,
    /**
	 * 
	 */
    AllowUnQuotedFieldNames,
    /**
	 * 
	 */
    AllowSingleQuotes,
    /**
	 * 
	 */
    InternFieldNames,
    /**
	 * 
	 */
    AllowISO8601DateFormat,

    /**
     * {"a":1,,,"b":2}
     */
    AllowArbitraryCommas,

    /**
     * 
     */
    UseBigDecimal,
    
    /**
     * @since 1.1.2
     */
    IgnoreNotMatch,

    /**
     * @since 1.1.3
     */
    SortFeidFastMatch,
    
    /**
     * @since 1.1.3
     */
    DisableASM,
    
    /**
     * @since 1.1.7
     */
    DisableCircularReferenceDetect,
    
    /**
     * @since 1.1.10
     */
    InitStringFieldAsEmpty,
    
    /**
     * @since 1.1.35
     * 
     */
    SupportArrayToBean,
    
    /**
     * @since 1.2.3
     * 
     */
    OrderedField,
    
    /**
     * @since 1.2.5
     * 
     */
    DisableSpecialKeyDetect,
    
    /**
     * @since 1.2.9
     */
    UseObjectArray,

    /**
     * @since 1.2.22, 1.1.54.android
     */
    SupportNonPublicField,

    /**
     * @since 1.2.29
     *
     * disable autotype key '@type'
     */
    IgnoreAutoType,

    /**
     * @since 1.2.30
     *
     * disable field smart match, improve performance in some scenarios.
     */
    DisableFieldSmartMatch,

    /**
     * @since 1.2.41, backport to 1.1.66.android
     */
    SupportAutoType,

    /**
     * @since 1.2.42
     */
    NonStringKeyAsString,

    /**
     * @since 1.2.45
     */
    CustomMapDeserializer
    ;

    Feature(){
        mask = (1 << ordinal());
    }

    public final int mask;

    public final int getMask() {
        return mask;
    }

    public static boolean isEnabled(int features, Feature feature) {
        return (features & feature.mask) != 0;
    }

    public static int config(int features, Feature feature, boolean state) {
        if (state) {
            features |= feature.mask;
        } else {
            features &= ~feature.mask;
        }

        return features;
    }
    
    public static int of(Feature[] features) {
        if (features == null) {
            return 0;
        }
        
        int value = 0;
        
        for (Feature feature: features) {
            value |= feature.mask;
        }
        
        return value;
    }
}
