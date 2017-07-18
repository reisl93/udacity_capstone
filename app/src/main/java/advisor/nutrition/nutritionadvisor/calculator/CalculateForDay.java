package advisor.nutrition.nutritionadvisor.calculator;

import android.content.ContentValues;
import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import advisor.nutrition.nutritionadvisor.data.Day;
import advisor.nutrition.nutritionadvisor.data.Food;
import advisor.nutrition.nutritionadvisor.provider.DataProviderUtils;
import advisor.nutrition.nutritionadvisor.provider.NutritionAdvisorProvider;
import timber.log.Timber;

public class CalculateForDay {

    private final Day mDay;
    private final Context mContext;

    private int minimumStep = 25;

    public CalculateForDay(Context context, Day mDay) {
        this.mDay = mDay;
        this.mContext = context;
    }

    public void startCalculating() {
        calculate();
    }

    private void calculate() {
        Timber.d("calculate - starting...");

        mDay.setCalculatedCarbs(mDay.getTargetFat());
        mDay.setCalculatedFat(mDay.getTargetFat());
        mDay.setCalculatedProteins(mDay.getTargetProteins());

        final List<Food> originalFoodList = mDay.getFoodList();

        //shuffle
        final List<Food> shuffledFoodList = new ArrayList<>(originalFoodList);
        Collections.copy(shuffledFoodList, originalFoodList);
        Collections.shuffle(shuffledFoodList);

        for (final Food originalFood : shuffledFoodList) {
            final Day tmpDay = mDay.copy();
            final Food tmpFood = CalculatorUtils.findFood(tmpDay.getFoodList(), originalFood.getId());

            Timber.v("start to calculate for food %d...", tmpFood.getId());

            // increase portions for this food
            boolean stepImprovedResult = true;
            while (stepImprovedResult) {
                double previousDiff = computePercentualTargetToCalculateDiff(tmpDay);
                tmpFood.setCalculatedPortions(tmpFood.getCalculatedPortions() + minimumStep);
                double newDiff = computePercentualTargetToCalculateDiff(tmpDay);
                //diff gets smaller??
                if (previousDiff > newDiff) {
                    stepImprovedResult = true;
                } else {
                    stepImprovedResult = false;
                    tmpFood.setCalculatedPortions(Math.max(tmpFood.getCalculatedPortions() - minimumStep,0));
                }
            }

            //decrease portions for this food
            stepImprovedResult = true;
            while (stepImprovedResult) {
                double previousDiff = computePercentualTargetToCalculateDiff(tmpDay);
                tmpFood.setCalculatedPortions(Math.max(tmpFood.getCalculatedPortions() - minimumStep, 0));
                double newDiff = computePercentualTargetToCalculateDiff(tmpDay);
                if (previousDiff > newDiff) {
                    stepImprovedResult = true;
                } else {
                    stepImprovedResult = false;
                    tmpFood.setCalculatedPortions(tmpFood.getCalculatedPortions() + minimumStep);
                }
            }

            if (computePercentualTargetToCalculateDiff(mDay) >
                    computePercentualTargetToCalculateDiff(tmpDay)) {
                Timber.v("improved for food %d from %f to %f", tmpFood.getId(), originalFood.getCalculatedPortions(), tmpFood.getCalculatedPortions());
                originalFood.setCalculatedPortions(tmpFood.getCalculatedPortions());
            }
        }

        mDay.setCalculatedCarbs((int) CalculatorUtils.getSumCarbs(originalFoodList));
        mDay.setCalculatedFat((int) CalculatorUtils.getSumFat(originalFoodList));
        mDay.setCalculatedProteins((int) CalculatorUtils.getSumProteins(originalFoodList));

        DataProviderUtils.updateUserDayColumns(mContext, mDay);
        DataProviderUtils.updateUserDayFoodColumns(mContext, mDay);

        Timber.d("calculate - ended...");
    }

    private double computePercentualTargetToCalculateDiff(Day betterResultTry) {
        List<Food> foodList = betterResultTry.getFoodList();
        final double carbs = Math.max(CalculatorUtils.getSumCarbs(foodList), 0.01);
        final double fat = Math.max(CalculatorUtils.getSumFat(foodList), 0.01);
        final double proteins = Math.max(CalculatorUtils.getSumProteins(foodList), 0.01);

        double diffPercent = 0;
        diffPercent += Math.abs(carbs / (Math.max(betterResultTry.getTargetCarbs(), 0.01) / 100) - 100);
        diffPercent += Math.abs(fat / (Math.max(betterResultTry.getTargetFat(), 0.01) / 100) - 100);
        diffPercent += Math.abs(proteins / (Math.max(betterResultTry.getTargetProteins(), 0.01) / 100) - 100);

        return diffPercent;
    }
}
