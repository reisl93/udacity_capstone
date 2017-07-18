package advisor.nutrition.nutritionadvisor.ui.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import advisor.nutrition.nutritionadvisor.R;
import advisor.nutrition.nutritionadvisor.provider.NutritionAdvisorProvider;
import advisor.nutrition.nutritionadvisor.provider.WidgetFoodColumns;
import timber.log.Timber;

public class FoodListWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewsFactory(this.getApplicationContext());
    }
}

class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private final Context mContext;
    private Cursor mFoodListCursor;

    ListRemoteViewsFactory(Context applicationContext) {
        mContext = applicationContext;
    }

    @Override
    public void onCreate() {}

    @Override
    public void onDataSetChanged() {
        Timber.d("Widget loaded food list...");

        if (mFoodListCursor != null) mFoodListCursor.close();
        mFoodListCursor = mContext.getContentResolver().query(
                NutritionAdvisorProvider.WidgetFood.FOODS, null, null, null, null
        );
        Timber.d("Widget loaded food list: " + getCount());
    }

    @Override
    public void onDestroy() {
        mFoodListCursor.close();
    }

    @Override
    public int getCount() {
        if (mFoodListCursor == null) return 0;
        return mFoodListCursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        Timber.d("updating view at %d cursor here?", position);
        if (mFoodListCursor == null || mFoodListCursor.getCount() == 0) return null;

        Timber.d("creating position %d",position);

        mFoodListCursor.moveToPosition(position);
        final String name = mFoodListCursor.getString(
                mFoodListCursor.getColumnIndex(WidgetFoodColumns.NAME));
        final String measure = mFoodListCursor.getString(
                mFoodListCursor.getColumnIndex(WidgetFoodColumns.MEASURE));
        final String portion = String.valueOf(mFoodListCursor.getDouble(
                mFoodListCursor.getColumnIndex(WidgetFoodColumns.PORTION)))
                .replaceAll("\\.[0]+", "");

        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.widget_grid_view_item_food);

        views.setTextViewText(R.id.widget_tv_name, name);
        views.setTextViewText(R.id.widget_tv_measure, measure);
        views.setTextViewText(R.id.widget_tv_portion, portion);

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

}



