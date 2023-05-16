package com.alibaba.fastjson.serializer.issue4152;

//CS304 (manually written) Issue link: https://github.com/alibaba/fastjson/issues/4152
public class DeptVO {

    private Integer id;

    private String deptName;

    public DeptVO(Integer id, String deptName) {
        this.id = id;
        this.deptName = deptName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }
}
