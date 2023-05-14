package com.alibaba.json.bvt.issue_3100;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Issue3131 extends TestCase {
    public void test_for_issue() throws Exception {
        List orgs = new ArrayList();
        UserOrg org = new UserOrg("111","222" );
        orgs.add(org);
        String s = JSON.toJSONString(new Orgs("111", orgs));
        System.out.println(s);
        Orgs userOrgs = JSON.parseObject(s, Orgs.class);
        System.out.println(JSON.toJSONString(userOrgs));
    }

    public static class Orgs<T extends Org> implements Serializable
    {
        /**
         */
        private static final long serialVersionUID = -1L;

        private String name;

        private List<T> orgs;

        public Orgs() {

        }

        public Orgs(String name, List<T> orgs)
        {
            this.name = name;
            this.orgs = orgs;
        }

        public String getName()
        {
            return name;
        }

        public void setName(String name)
        {
            this.name = name;
        }

        public List<T> getOrgs()
        {
            return orgs;
        }

        public void setOrgs(List<T> orgs)
        {
            this.orgs = orgs;
        }

        public void add(T org) {
            if (orgs == null) {
                orgs = new ArrayList<T>();
            }
            orgs.add(org);
        }
    }

    public static class UserOrg extends Org implements Serializable{

        private String name;

        private String idcard;

        public UserOrg() {

        }

        public UserOrg(String name, String idcard)
        {
            super (name);
            this.name = name;
            this.idcard = idcard;
        }

        public String getName()
        {
            return name;
        }

        public void setName(String name)
        {
            this.name = name;
        }

        public String getIdcard()
        {
            return idcard;
        }

        public void setIdcard(String idcard)
        {
            this.idcard = idcard;
        }
    }

    public static abstract class Org implements Serializable{

        private String name;

        public Org() {

        }

        public Org(String name)
        {
            this.name = name;
        }

        public String getName()
        {
            return name;
        }

        public void setName(String name)
        {
            this.name = name;
        }
    }
}
