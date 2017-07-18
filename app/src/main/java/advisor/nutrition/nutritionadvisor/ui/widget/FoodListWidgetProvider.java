package advisor.nutrition.nutritionadvisor.ui.widget;

import android.annotation.TargetApi;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.RemoteViews;

import advisor.nutrition.nutritionadvisor.R;
import timber.log.Timber;

public class FoodListWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        Timber.d("updateAppWidget %d ",appWidgetId);
        appWidgetManager.updateAppWidget(appWidgetId, getFoodListRemoteView(context));
    }

    public static RemoteViews getFoodListRemoteView(Context context) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_food);
        Intent intent = new Intent(context, FoodListWidgetService.class);
        views.setRemoteAdapter(R.id.widget_lv_foodlist, intent);
        views.setEmptyView(R.id.widget_lv_foodlist, R.id.tv_no_foods);
        return views;
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Timber.d("onUpdate - updating widget");
        FoodListUpdateService.startActionUpdateFoodListWidgets(context);
    }

    public static void updateFoodListWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager,
                                          int appWidgetId, Bundle newOptions) {
        FoodListUpdateService.startActionUpdateFoodListWidgets(context);
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // Perform any action when one or more AppWidget instances have been deleted
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}
