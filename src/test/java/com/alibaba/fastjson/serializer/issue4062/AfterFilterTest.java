package com.alibaba.fastjson.serializer.issue4062;

import com.alibaba.fastjson.serializer.AfterFilter;

//CS304 (manually written) Issue link: https://github.com/alibaba/fastjson/issues/4062
public class AfterFilterTest extends AfterFilter {
    private DeptVO dept = new DeptVO(1,"afterDeptName");

    @Override
    public void writeAfter(Object object) {

        if(object instanceof UserVO){

            this.writeKeyValue("afterFilterOne",dept);

            this.writeKeyValue("afterFilterTwo","two");

        }
    }
}
