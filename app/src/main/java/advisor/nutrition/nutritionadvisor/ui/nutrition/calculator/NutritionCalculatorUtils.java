package advisor.nutrition.nutritionadvisor.ui.nutrition.calculator;

import android.database.Cursor;
import android.support.annotation.NonNull;

import advisor.nutrition.nutritionadvisor.data.Food;
import advisor.nutrition.nutritionadvisor.provider.NutritionAdvisorProvider;

class NutritionCalculatorUtils {
    @NonNull
    static Food convertCursorTooFood(Cursor cursor) {
        Food food = new Food();
        food.setCarbs(cursor.getDouble(NutritionAdvisorProvider.FoodJoins.INDEX_FOOD_CARBS));
        food.setFat(cursor.getDouble(NutritionAdvisorProvider.FoodJoins.INDEX_FOOD_FAT));
        food.setProteins(cursor.getDouble(NutritionAdvisorProvider.FoodJoins.INDEX_FOOD_PROTEINS));
        food.setPortionSize(cursor.getDouble(NutritionAdvisorProvider.FoodJoins.INDEX_FOOD_PORTION_SIZE));
        food.setPortions(cursor.getDouble(NutritionAdvisorProvider.FoodJoins.INDEX_PLAN_PORTIONS));
        food.setId(cursor.getInt(NutritionAdvisorProvider.FoodJoins.INDEX_PLAN_FOOD_ID));
        food.setName(cursor.getString(NutritionAdvisorProvider.FoodJoins.INDEX_FOOD_NAME));
        food.setMeasure(cursor.getString(NutritionAdvisorProvider.FoodJoins.INDEX_FOOD_MEASURE));
        return food;
    }
}
