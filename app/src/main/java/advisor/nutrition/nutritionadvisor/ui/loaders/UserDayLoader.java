package advisor.nutrition.nutritionadvisor.ui.loaders;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import advisor.nutrition.nutritionadvisor.provider.NutritionAdvisorProvider;

public abstract class UserDayLoader implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final int LOADER_ID = 127012;

    private final Context mContext;
    private final String mDate;
    private final String mUsername;

    public UserDayLoader(Context mContext, String mDate, String mUsername) {
        this.mContext = mContext;
        this.mDate = mDate;
        this.mUsername = mUsername;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(mContext,
                NutritionAdvisorProvider.UserDay.withUserAndDate(mUsername, mDate),
                null, null, null, null);
    }
}