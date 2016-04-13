package data.taobao.cart;

import data.taobao.cart.Group.GroupFields;

public class Group extends CartObject<GroupFields> {

    public static class GroupFields {
        public String groupId;
        public boolean isRelationItem;
    }
}
