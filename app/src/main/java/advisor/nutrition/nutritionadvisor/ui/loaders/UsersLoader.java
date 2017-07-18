package advisor.nutrition.nutritionadvisor.ui.loaders;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import advisor.nutrition.nutritionadvisor.provider.NutritionAdvisorProvider;
import advisor.nutrition.nutritionadvisor.provider.UserColumns;

@SuppressWarnings("FieldCanBeLocal") // for sake of better code readability
public abstract class UsersLoader implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String[] USERS_PROJECTION = {UserColumns.NAME};
    public static final int LOADER_ID = 221828;
    private final int INDEX_NAME = 0;

    private final Context mContext;

    public UsersLoader(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(mContext, NutritionAdvisorProvider.Users.USERS, USERS_PROJECTION, null, null, null);
    }

    protected String[] getUsers(final Cursor cursor) {
        final String[] users;
        if (cursor != null && cursor.moveToFirst()) {
            users = new String[cursor.getCount()];
            for (int i = 0; !cursor.isAfterLast(); i++) {
                users[i] = cursor.getString(INDEX_NAME);
                cursor.moveToNext();
            }
        } else {
            users = new String[0];
        }
        return users;
    }
}
