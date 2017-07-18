package advisor.nutrition.nutritionadvisor.ui.nutrition.calculator;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import advisor.nutrition.nutritionadvisor.data.Day;
import advisor.nutrition.nutritionadvisor.data.Food;
import advisor.nutrition.nutritionadvisor.provider.FoodColumns;
import advisor.nutrition.nutritionadvisor.provider.NutritionAdvisorProvider;
import advisor.nutrition.nutritionadvisor.provider.UserPlanColumns;

@SuppressWarnings("FieldCanBeLocal")
abstract class FoodsForSpecificDayLoader implements LoaderManager.LoaderCallbacks<Cursor> {

    static final int LOADER_ID = 253212;

    static final String[] FOODS_PROJECTION = {
            UserPlanColumns.FOOD_ID,
            UserPlanColumns.PORTIONS,
            FoodColumns.NAME,
            FoodColumns.CARBOHYDRATES,
            FoodColumns.FAT,
            FoodColumns.PROTEIN,
            FoodColumns.MEASURE,
            FoodColumns.PORTION_SIZE
    };
    final static int INDEX_FOOD_ID = 0;
    final static int INDEX_PORTIONS = 1;
    final static int INDEX_NAME = 2;
    final static int INDEX_CARBOHYDRATES = 3;
    final static int INDEX_FAT = 4;
    final static int INDEX_PROTEIN = 5;
    final static int INDEX_MEASURE = 6;
    final static int INDEX_PORTION_SIZE = 7;

    private final Context mContext;
    private final String mDate;
    private final String mUsername;

    public FoodsForSpecificDayLoader(Context mContext, String mDate, String mUsername) {
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

    protected Day convertFoodIds(final Cursor cursor) {
        final Day day;
        day = new Day();
        day.setDay(mDate);
        day.setUserName(mUsername);
        if (cursor != null && cursor.moveToFirst()) {

            final Food[] foodList = new Food[cursor.getCount()];
            day.setFoodList(foodList);
            for (int i = 0; !cursor.isAfterLast(); i++) {
                Food food = NutritionCalculatorUtils.convertCursorTooFood(cursor);
                foodList[i] = food;
                cursor.moveToNext();
            }
        }
        return day;
    }

}
