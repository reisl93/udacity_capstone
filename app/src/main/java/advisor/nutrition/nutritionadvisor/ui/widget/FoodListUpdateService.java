package advisor.nutrition.nutritionadvisor.ui.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import advisor.nutrition.nutritionadvisor.R;
import timber.log.Timber;

public class FoodListUpdateService extends IntentService {

    public static final String ACTION_UPDATE_FOODLIST_WIDGETS = "advisor.nutrition.nutritionadvisor.action.update_foodlist_widgets";

    public FoodListUpdateService() {
        super(FoodListUpdateService.class.getSimpleName());
    }

    public static void startActionUpdateFoodListWidgets(Context context) {
        Intent intent = new Intent(context, FoodListUpdateService.class);
        intent.setAction(ACTION_UPDATE_FOODLIST_WIDGETS);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_UPDATE_FOODLIST_WIDGETS.equals(action)) {
                handleActionUpdateFoodListWidgets();
            }
        }
    }

    private void handleActionUpdateFoodListWidgets() {
        Timber.d("update food list Widget");
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, FoodListWidgetProvider.class));
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_lv_foodlist);
        FoodListWidgetProvider.updateFoodListWidgets(this, appWidgetManager, appWidgetIds);
    }
}
