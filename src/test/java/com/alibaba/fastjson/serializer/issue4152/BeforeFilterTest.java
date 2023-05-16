package com.alibaba.fastjson.serializer.issue4152;

import com.alibaba.fastjson.serializer.BeforeFilter;

//CS304 (manually written) Issue link: https://github.com/alibaba/fastjson/issues/4152
public class BeforeFilterTest extends BeforeFilter {
    private DeptVO dept = new DeptVO(1,"beforFilterDept");

    @Override
    public void writeBefore(Object object) {

        if(object instanceof UserVO){

            this.writeKeyValue("beforeFilterOne",dept);

            this.writeKeyValue("beforeFilterTwo","two");

            this.writeKeyValue("beforeFilterThree",dept);
        }
    }


}

