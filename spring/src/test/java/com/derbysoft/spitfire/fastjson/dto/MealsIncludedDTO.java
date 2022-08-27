package com.derbysoft.spitfire.fastjson.dto;

public class MealsIncludedDTO extends AbstractDTO{
    private MealsIncludedType mealsIncludedType;

    private int breakfastNumber;

    public MealsIncludedDTO() {
    }

    public MealsIncludedDTO(MealsIncludedType type) {
    }

    public MealsIncludedType getMealsIncludedType() {
        return mealsIncludedType;
    }

    public void setMealsIncludedType(MealsIncludedType mealsIncludedType) {
        this.mealsIncludedType = mealsIncludedType;
    }

    public int getBreakfastNumber() {
        return breakfastNumber;
    }

    public void setBreakfastNumber(int breakfastNumber) {
        this.breakfastNumber = breakfastNumber;
    }
}
