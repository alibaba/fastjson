package com.alibaba.fastjson.deserializer.issue3752.beans;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Class for testing deserialize instantiable collection.
 */
public class UserDefinedCollectionInstance
        extends ArrayList<String>
        implements UserDefinedCollectionInterface, Serializable {

}