package com.derbysoft.spitfire.fastjson.dto;

public class RateDTO extends AbstractDTO{
    private DateRangeDTO dateRange;
    private SimpleAmountDTO pureAmount;
    private MealsIncludedDTO mealsIncluded;

    public DateRangeDTO getDateRange() {
        return dateRange;
    }

    public void setDateRange(DateRangeDTO dateRange) {
        this.dateRange = dateRange;
    }

    public SimpleAmountDTO getPureAmount() {
        return pureAmount;
    }

    public void setPureAmount(SimpleAmountDTO pureAmount) {
        this.pureAmount = pureAmount;
    }

    public MealsIncludedDTO getMealsIncluded() {
        return mealsIncluded;
    }

    public void setMealsIncluded(MealsIncludedDTO mealsIncluded) {
        this.mealsIncluded = mealsIncluded;
    }
}
