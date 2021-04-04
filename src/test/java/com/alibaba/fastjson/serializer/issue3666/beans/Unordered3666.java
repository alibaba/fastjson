package com.alibaba.fastjson.serializer.issue3666.beans;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * JSON不排序
 * @author oyhp
 */

@Data
@Getter
@Setter
public class Unordered3666 {
    private String routerDesc;
    private String flowIp;
    private String flowPort;
    private String flowSampleRate;
}
