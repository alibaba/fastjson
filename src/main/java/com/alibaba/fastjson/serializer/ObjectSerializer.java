/*
 * Copyright 1999-2018 Alibaba Group.
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
package com.alibaba.fastjson.serializer;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Interface representing a custom serializer for fastjson. You should write a custom serializer, if
 * you are not happy with the default serialization done by fastjson. You will also need to register
 * this serializer through {@link com.alibaba.fastjson.serializer.SerializeConfig#put(Type, ObjectSerializer)}.
 *
 * <pre>
 * public static class Result {
 *     public ResultCode code;
 * }
 * 
 * public static enum ResultCode {
 *     LOGIN_FAILURE(8), INVALID_ARGUMENT(0), SIGN_ERROR(17);
 *     public final int value;
 *     ResultCode(int value){
 *         this.value = value;
 *     }
 * }
 * 
 * public static class ResultCodeSerilaizer implements ObjectSerializer {
 *     public void write(JSONSerializer serializer, 
 *                       Object object, 
 *                       Object fieldName, 
 *                       Type fieldType,
 *                       int features) throws IOException {
 *         serializer.write(((ResultCode) object).value);
 *     }
 * }
 * 
 * SerializeConfig.getGlobalInstance().put(ResultCode.class, new ResultCodeSerilaizer());
 * 
 * Result result = new Result();
 * result.code = ResultCode.SIGN_ERROR;
 * String json = JSON.toJSONString(result, config); // {"code":17}
 * Assert.assertEquals("{\"code\":17}", json);
 * </pre>
 * @author wenshao[szujobs@hotmail.com]
 */
public interface ObjectSerializer {
    
    /**
     * fastjson invokes this call-back method during serialization when it encounters a field of the
     * specified type.
     * @param serializer 
     * @param object src the object that needs to be converted to Json.
     * @param fieldName parent object field name
     * @param fieldType parent object field type
     * @param features parent object field serializer features
     * @throws IOException
     */
    void write(JSONSerializer serializer, //
               Object object, //
               Object fieldName, //
               Type fieldType, //
               int features) throws IOException;
}
