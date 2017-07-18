package advisor.nutrition.nutritionadvisor.provider;

import android.content.ContentValues;
import android.content.Context;

import java.util.List;

import advisor.nutrition.nutritionadvisor.data.Day;
import advisor.nutrition.nutritionadvisor.data.Food;

public class DataProviderUtils {

    public static void updateUserDayColumns(final Context context, final Day day) {
        ContentValues userDay = new ContentValues();
        userDay.put(UserDayColumns.TARGET_PROTEINS, day.getTargetProteins());
        userDay.put(UserDayColumns.TARGET_FAT, day.getTargetFat());
        userDay.put(UserDayColumns.TARGET_CARBS, day.getTargetCarbs());
        userDay.put(UserDayColumns.CALCULATED_CARBS, day.getCalculatedCarbs());
        userDay.put(UserDayColumns.CALCULATED_FAT, day.getCalculatedFat());
        userDay.put(UserDayColumns.CALCULATED_PROTEINS, day.getCalculatedProteins());
        context.getContentResolver().update(NutritionAdvisorProvider.UserDay
                .withUserAndDate(day.getUserName(), day.getDate()), userDay, null, null);
    }

    public static void updateUserDayFoodColumns(final Context context, final Day day) {
        for (final Food food : day.getFoodList()) {
            ContentValues userDayFood = new ContentValues();
            userDayFood.put(UserDayFoodColumns.CALCULATED_PORTIONS, food.getCalculatedPortions());
            userDayFood.put(UserDayFoodColumns.PREFERENCE_PORTIONS, food.getPreferencePortions());
            context.getContentResolver().update(NutritionAdvisorProvider.UserDayFood
                    .withUserAndDateAndFoodId(day.getUserName(), day.getDate(), food.getId()),
                    userDayFood, null, null);
        }
    }

    public static void insertFood(final Context context, final Food food) {
        final ContentValues foodEntry = new ContentValues();
        foodEntry.put(FoodColumns._ID, food.getId());
        foodEntry.put(FoodColumns.CARBOHYDRATES, food.getCarbs());
        foodEntry.put(FoodColumns.FAT, food.getFat());
        foodEntry.put(FoodColumns.PROTEIN, food.getProteins());
        foodEntry.put(FoodColumns.NAME, food.getName());
        foodEntry.put(FoodColumns.PORTION_SIZE, food.getPortionSize());
        foodEntry.put(FoodColumns.MEASURE, food.getMeasure());
        context.getContentResolver().insert(NutritionAdvisorProvider.Foods.FOODS, foodEntry);
    }

    public static void deleteWidgetFoodList(Context context) {
        context.getContentResolver().delete(NutritionAdvisorProvider.WidgetFood.FOODS, null, null);
    }

    public static void updateWidgetFoodList(Context context, Day mDay) {
        List<Food> foodList = mDay.getFoodList();
        ContentValues[] values = new ContentValues[foodList.size()];
        for (int i = 0; i < foodList.size(); i++){
            final Food food = foodList.get(i);
            ContentValues foodContent = new ContentValues();
            foodContent.put(WidgetFoodColumns._ID, food.getId());
            foodContent.put(WidgetFoodColumns.NAME, food.getName());
            foodContent.put(WidgetFoodColumns.PORTION, food.getCalculatedPortions());
            foodContent.put(WidgetFoodColumns.MEASURE, food.getMeasure());
            values[i] = foodContent;
        }
        if (foodList.size() > 0) {
            context.getContentResolver().bulkInsert(NutritionAdvisorProvider.WidgetFood.FOODS, values);
        }
    }
}
