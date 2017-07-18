package advisor.nutrition.nutritionadvisor.ui.loaders;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import java.util.HashMap;
import java.util.Map;

import advisor.nutrition.nutritionadvisor.data.Day;
import advisor.nutrition.nutritionadvisor.provider.NutritionAdvisorProvider;
import advisor.nutrition.nutritionadvisor.provider.UserDayColumns;
import timber.log.Timber;

public abstract class DateDayMappingLoader implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final int LOADER_ID = 928321;

    private final Context mContext;
    private final String mUserName;

    public DateDayMappingLoader(Context mContext, String mUserName) {
        this.mContext = mContext;
        this.mUserName = mUserName;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Timber.d("starting loader with username %s", mUserName);
        return new CursorLoader(mContext, NutritionAdvisorProvider.UserDay.withUser(mUserName), null, null, null, null);
    }

    protected Map<String, Day> getUserDayMapping(final Cursor cursor) {
        Map<String, Day> stringDayHashMap = new HashMap<>();
        if (cursor != null && cursor.moveToFirst() && cursor.getCount() > 0) {
            while (!cursor.isAfterLast()) {
                final Day day = new Day();
                day.setTargetCarbs(cursor.getInt(cursor.getColumnIndex(UserDayColumns.TARGET_CARBS)));
                day.setTargetFat(cursor.getInt(cursor.getColumnIndex(UserDayColumns.TARGET_FAT)));
                day.setTargetProteins(cursor.getInt(cursor.getColumnIndex(UserDayColumns.TARGET_PROTEINS)));
                day.setCalculatedCarbs(cursor.getInt(cursor.getColumnIndex(UserDayColumns.CALCULATED_CARBS)));
                day.setCalculatedFat(cursor.getInt(cursor.getColumnIndex(UserDayColumns.CALCULATED_FAT)));
                day.setCalculatedProteins(cursor.getInt(cursor.getColumnIndex(UserDayColumns.CALCULATED_PROTEINS)));
                day.setDate(cursor.getString(cursor.getColumnIndex(UserDayColumns.DATE)));
                day.setUserName(cursor.getString(cursor.getColumnIndex(UserDayColumns.USER_NAME)));
                stringDayHashMap.put(day.getDate(), day);
                cursor.moveToNext();
            }
        }
        return stringDayHashMap;
    }
}
