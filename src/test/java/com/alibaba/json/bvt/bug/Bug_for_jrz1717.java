package com.alibaba.json.bvt.bug;

import java.util.HashSet;
import java.util.Set;

public class Bug_for_jrz1717 {

    public static class User {

        private Set<String> authorityCodes = new HashSet<String>();

        private Org         currentOrganization;

        public Set<String> getAuthorityCodes() {
            return authorityCodes;
        }

        public void setAuthorityCodes(Set<String> authorityCodes) {
            this.authorityCodes = authorityCodes;
        }

        public Org getCurrentOrganization() {
            return currentOrganization;
        }

        public void setCurrentOrganization(Org currentOrganization) {
            this.currentOrganization = currentOrganization;
        }

    }

    public static class Org {

        public Org parent;

        public Org getParent() {
            return parent;
        }

        public void setParent(Org parent) {
            this.parent = parent;
        }

    }
}
