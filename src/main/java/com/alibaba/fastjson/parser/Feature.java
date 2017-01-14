/*
 * Copyright 1999-2101 Alibaba Group.
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
     * @since 1.2.5, backport to 1.1.49.android
     */
    OrderedField,
    
    /**
     * @since 1.2.5, backport to 1.1.49.android
     */
    DisableSpecialKeyDetect,

    /**
     * @since 1.2.22, backport to 1.1.54.android
     */
    SupportNonPublicField
    ;

    private Feature(){
        mask = (1 << ordinal());
    }

    public final int mask;
}
