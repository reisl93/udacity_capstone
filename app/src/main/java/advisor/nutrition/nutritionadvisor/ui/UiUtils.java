package advisor.nutrition.nutritionadvisor.ui;

import android.view.View;

public class UiUtils {

    public static void setVisibility(final int visibility, View... views){
        for (final View view : views){
            view.setVisibility(visibility);
        }
    }
}
