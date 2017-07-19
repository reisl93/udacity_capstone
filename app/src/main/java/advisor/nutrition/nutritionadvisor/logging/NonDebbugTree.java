package advisor.nutrition.nutritionadvisor.logging;

import android.util.Log;

import timber.log.Timber;

public class NonDebbugTree extends Timber.Tree {
    @Override
    protected void log(int priority, String tag, String message, Throwable t) {
        if (priority == Log.VERBOSE || priority == Log.DEBUG) {
            return;
        }

        if (t != null) {
            Log.e(tag, message, t);
        } else {
            switch (priority) {
                case Log.INFO:
                    Log.i(tag, message);
                    break;
                case Log.WARN:
                    Log.i(tag, message);
                    break;
                case Log.ERROR:
                    Log.i(tag, message);
                    break;
                case Log.ASSERT:
                default:
                    Log.e(tag, message);

            }
        }
    }
}
