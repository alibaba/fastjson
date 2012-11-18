package com.alibaba.json.test.epubview;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class EpubViewMetaData implements Serializable
{
    private static final long serialVersionUID = 8776084797505245120L;
    
    private boolean encrypt = false;
    private Map<String, String> properties = new HashMap<String, String>();

    public Map<String, String> getProperties()
    {
        return properties;
    }

    public void setProperties(Map<String, String> properties)
    {
        this.properties = properties;
    }

    public boolean isEncrypt()
    {
        return encrypt;
    }

    public void setEncrypt(boolean encrypt)
    {
        this.encrypt = encrypt;
    }

    public static long getSerialversionuid()
    {
        return serialVersionUID;
    }
}
