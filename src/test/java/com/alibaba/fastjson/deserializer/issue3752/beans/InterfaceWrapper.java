package com.alibaba.fastjson.deserializer.issue3752.beans;

import java.io.Serializable;

/**
 * Wrapper class for testing deserialize interface collection.
 */
public class InterfaceWrapper implements Serializable {
    /**
     * The instance of user defined interface collection.
     */
    private UserDefinedCollectionInterface collection = new UserDefinedCollectionInstance();

    /**
     * Getter of the collection instance
     * @return collection
     */
    public UserDefinedCollectionInterface getCollection() {
        return collection;
    }

    /**
     * Setter of the collection instance
     * @param userDefinedCollection the instance
     */
    public void setCollection(UserDefinedCollectionInterface userDefinedCollection) {
        this.collection = userDefinedCollection;
    }
}