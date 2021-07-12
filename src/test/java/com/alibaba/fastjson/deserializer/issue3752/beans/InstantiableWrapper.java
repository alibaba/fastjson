package com.alibaba.fastjson.deserializer.issue3752.beans;

/**
 * A Wrapper class for testing deserialize instantiable collection.
 */
public class InstantiableWrapper {
    /**
     * The instance of instantiable collection.
     */
    private InstantiableCollection ic;

    /**
     * Getter of the instantiable collection.
     * @return the instance of instantiable collection.
     */
    public InstantiableCollection getIc() {
        return ic;
    }

    /**
     * Setter of the instantiable collection.
     * @param instantiableCollection: the instance
     */
    public void setIc(InstantiableCollection instantiableCollection) {
        this.ic = instantiableCollection;
    }
}
