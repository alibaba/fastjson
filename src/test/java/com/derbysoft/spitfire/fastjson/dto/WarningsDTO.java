package com.derbysoft.spitfire.fastjson.dto;

import java.util.ArrayList;
import java.util.List;

public class WarningsDTO extends AbstractDTO{
    private List<WarningDTO> warningList = new ArrayList<WarningDTO>();

    public List<WarningDTO> getWarningList() {
        return warningList;
    }

    public void setWarningList(List<WarningDTO> warnings) {
        this.warningList = warnings;
    }

}
