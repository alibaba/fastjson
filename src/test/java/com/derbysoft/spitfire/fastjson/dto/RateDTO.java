package com.derbysoft.spitfire.fastjson.dto;

public class RateDTO extends AbstractDTO{
    private DateRangeDTO dateRange;
    private SimpleAmountDTO pureAmount;
    private MealsIncludedDTO mealsIncluded;

    public void setDateRange(DateRangeDTO dateRange) {
        this.dateRange = dateRange;
    }

    public void setPureAmount(SimpleAmountDTO pureAmount) {
        this.pureAmount = pureAmount;
    }

    public void setMealsIncluded(MealsIncludedDTO mealsIncluded) {
        this.mealsIncluded = mealsIncluded;
    }
}
