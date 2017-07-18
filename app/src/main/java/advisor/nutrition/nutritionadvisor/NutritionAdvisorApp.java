package advisor.nutrition.nutritionadvisor;

import android.app.Application;
import android.util.Log;

import net.ypresto.timbertreeutils.CrashlyticsLogTree;

import timber.log.Timber;

public class NutritionAdvisorApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new CrashlyticsLogTree(Log.INFO));
        }
    }
}
