package com.alibaba.json.bvt.issue_3700;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class Issue3732 extends TestCase {
    static class Role{
        int id;

        public Role() {}

        public Role(int id) {
            this.id = id;
        }

        public void setId(int id) {this.id = id;}

        public int getId() {
            return id;
        }
    }
    static class User{
        List<Role> roleList;

        public void setRoleList(List<Role> roleList) {
            this.roleList = roleList;
        }

        public List<Role> getRoleList() {
            return roleList;
        }

//        public void setRoleIdList(List<int> roleIdList){}

        public List<Integer> getRoleIdList(){
            if (roleList == null) return null;
            List<Integer> roleIdList = new ArrayList<Integer>();
            for (Role role : roleList) {
                roleIdList.add(role.id);
            }
            return roleIdList;
        }
    }

    @Test
    public void test_for_issue(){
        Role role1 = new Role(1);
        Role role2 = new Role(2);
        Role role3 = new Role(3);
        List<Role> roleList = new ArrayList<Role>();
        roleList.add(role1);
        roleList.add(role2);
        roleList.add(role3);
        User user = new User();
        user.roleList = roleList;
//        List <int> roleIdList = user.getRoleIdList();

        String userString = JSON.toJSONString(user);
        System.out.println(userString);

        User parseUser = JSON.parseObject(userString,User.class);
    }
}