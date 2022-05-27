package com.alibaba.fastjson.serializer.issue4062;

//CS304 (manually written) Issue link: https://github.com/alibaba/fastjson/issues/4062
public class UserVO {

    private Integer id;

    private String userName;

    private Integer deptId;

    public UserVO(Integer id, String userName, Integer deptId) {
        this.id = id;
        this.userName = userName;
        this.deptId = deptId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getDeptId() {
        return deptId;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }
}
