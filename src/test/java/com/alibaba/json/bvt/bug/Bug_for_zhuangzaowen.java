package com.alibaba.json.bvt.bug;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;

public class Bug_for_zhuangzaowen extends TestCase {

    public void test_for_zhuangzaowen() throws Exception {
        String value = "{\"begin\":1340263804415,\"buildIds\":[\"42\"],\"end\":1340265305070,\"endBuildId\":\"50\",\"id\":\"4\",\"jobs\":[\"cb-intl-rfqma-UT\",\"cb-intl-rfqma-selenium\"],\"owners\":[\"wb_jianping.shenjp\"],\"triggerBuildId\":\"42\"}";
        System.out.println(JSON.parseObject(value, JenkinsFailedPhase.class, Feature.DisableASM));
    }

    public static class JenkinsFailedPhase {// extends BaseEntity<JenkinsFailedPhase> {

        private String             id;
        public static final String KEY_NAME_SPACE = "phase";

        private Set<String>        owners;

        private List<String>       buildIds;

        private Set<String>        jobs;

        private Date               begin;

        private Date               end;

        private String             endBuildId;

        private String             triggerBuildId;

        /*
         * @Override public String generateKey(String id) { return KeyUtils.generatePhaseKey(id); }
         */

        public Set<String> getOwners() {
            return owners;
        }

        public void setOwners(Set<String> owners) {
            this.owners = owners;
        }

        public void addOwner(String owner) {
            if (owners == null) {
                owners = new HashSet<String>();
            }
            owners.add(owner);
        }

        public List<String> getBuildIds() {
            return buildIds;
        }

        public void setBuildIds(List<String> buildIds) {
            this.buildIds = buildIds;
        }

        public void addBuild(String bid) {
            if (buildIds == null) {
                buildIds = new ArrayList<String>();
            }
            buildIds.add(bid);
        }

        public Set<String> getJobs() {
            return jobs;
        }

        public void setJobs(Set<String> jobs) {
            this.jobs = jobs;
        }

        public void addJobs(String job) {
            if (this.jobs == null) {
                jobs = new HashSet<String>();
            }
            jobs.add(job);
        }

        public Date getEnd() {
            return end;
        }

        public void setEnd(Date end) {
            this.end = end;
        }

        public Date getBegin() {
            return begin;
        }

        public void setBegin(Date begin) {
            this.begin = begin;
        }

        public String getEndBuildId() {
            return endBuildId;
        }

        public void setEndBuildId(String endBuildId) {
            this.endBuildId = endBuildId;
        }

        public String getTriggerBuildId() {
            return triggerBuildId;
        }

        public void setTriggerBuildId(String triggerBuildId) {
            this.triggerBuildId = triggerBuildId;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

    }
}
