package com.alibaba.fastjson.serializer.issue3666.beans;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * JSON排序
 * @author oyhp
 */

@Data
@Getter
@Setter
public class Ordered3666 {
    @JSONField(ordinal = 4)
    private String routerDesc;
    @JSONField(ordinal = 3)
    private String flowIp;
    @JSONField(ordinal = 2)
    private String flowPort;
    @JSONField(ordinal = 1)
    private String flowSampleRate;
}
