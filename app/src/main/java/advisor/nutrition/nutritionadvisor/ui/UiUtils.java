package advisor.nutrition.nutritionadvisor.ui;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

public class UiUtils {

    public static void setVisibility(final int visibility, View... views){
        for (final View view : views){
            view.setVisibility(visibility);
        }
    }
}
