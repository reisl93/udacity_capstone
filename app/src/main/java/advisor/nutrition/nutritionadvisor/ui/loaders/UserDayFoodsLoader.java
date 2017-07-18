package advisor.nutrition.nutritionadvisor.ui.loaders;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import advisor.nutrition.nutritionadvisor.data.Day;
import advisor.nutrition.nutritionadvisor.data.Food;
import advisor.nutrition.nutritionadvisor.provider.NutritionAdvisorProvider;

@SuppressWarnings("FieldCanBeLocal")
public abstract class UserDayFoodsLoader implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final int LOADER_ID = 253212;

    private final Context mContext;
    private final String mDate;
    private final String mUsername;

    public UserDayFoodsLoader(Context mContext, String mDate, String mUsername) {
        this.mContext = mContext;
        this.mDate = mDate;
        this.mUsername = mUsername;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(mContext,
                NutritionAdvisorProvider.FoodJoins.withUserAndDate(mUsername, mDate),
                NutritionAdvisorProvider.FoodJoins.JOIN_PROJECTION, null, null, null);
    }

    public void updateDay(final Day day, final Cursor cursor){
        if (cursor != null && day != null && cursor.moveToFirst()) {
            day.setUserName(cursor.getString(NutritionAdvisorProvider.FoodJoins.INDEX_PLAN_NAME));
            day.setDate(cursor.getString(NutritionAdvisorProvider.FoodJoins.INDEX_PLAN_DATE));
            if (day.getFoodList() == null){
                day.setFoodList(new ArrayList<Food>());
            }
            final List<Food> foodList = day.getFoodList();
            while (!cursor.isAfterLast()){
                final int food_id = cursor.getInt(NutritionAdvisorProvider.FoodJoins.INDEX_PLAN_FOOD_ID);
                Optional<Food> foodWithId = Iterators.tryFind(foodList.iterator(), new FindFoodId(food_id));
                final Food food;
                if (foodWithId.isPresent()){
                    food = foodWithId.get();
                } else {
                    food = new Food();
                    foodList.add(food);
                }
                food.setId(food_id);
                food.setPreferencePortions(cursor.getDouble(NutritionAdvisorProvider.FoodJoins.INDEX_PLAN_TARGET_PORTIONS));
                food.setCalculatedPortions(cursor.getDouble(NutritionAdvisorProvider.FoodJoins.INDEX_PLAN_CALCULATED_PORTIONS));
                food.setPortionSize(cursor.getDouble(NutritionAdvisorProvider.FoodJoins.INDEX_FOOD_PORTION_SIZE));
                food.setCarbs(cursor.getDouble(NutritionAdvisorProvider.FoodJoins.INDEX_FOOD_CARBS));
                food.setFat(cursor.getDouble(NutritionAdvisorProvider.FoodJoins.INDEX_FOOD_FAT));
                food.setProteins(cursor.getDouble(NutritionAdvisorProvider.FoodJoins.INDEX_FOOD_PROTEINS));
                food.setName(cursor.getString(NutritionAdvisorProvider.FoodJoins.INDEX_FOOD_NAME));
                food.setMeasure(cursor.getString(NutritionAdvisorProvider.FoodJoins.INDEX_FOOD_MEASURE));

                cursor.moveToNext();
            }
        }
    }

    private final static class FindFoodId implements Predicate<Food> {
        private final int mFoodID;

        private FindFoodId(int mFoodID) {
            this.mFoodID = mFoodID;
        }

        @Override
        public boolean apply(@Nullable Food input) {
            return input.getId() == mFoodID;
        }
    }

}
