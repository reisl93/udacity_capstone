package advisor.nutrition.nutritionadvisor.provider;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

@ContentProvider(authority = NutritionAdvisorProvider.AUTHORITY, database = NutritionAdvisorDatabase.class)
public class NutritionAdvisorProvider {

    public static final String AUTHORITY = "advisor.nutrition.nutritionadvisor.NutritionAdvisorProvider";
    private static String content = "content://";

    @TableEndpoint(table = NutritionAdvisorDatabase.FOODS)
    public static class Foods {
        @ContentUri(path = NutritionAdvisorDatabase.FOODS,
                type = "vnd.android.cursor.dir/list",
                defaultSort = FoodColumns.NAME + " ASC")
        public static final Uri FOODS = Uri.parse(content + AUTHORITY + "/" + NutritionAdvisorDatabase.FOODS);
    }

    @TableEndpoint(table = NutritionAdvisorDatabase.USERS)
    public static class Users {
        @ContentUri(path = NutritionAdvisorDatabase.USERS,
                type = "vnd.android.cursor.dir/list",
                defaultSort = UserColumns.NAME + " ASC")
        public static final Uri USERS = Uri.parse(content + AUTHORITY + "/" + NutritionAdvisorDatabase.USERS);

    }

    @TableEndpoint(table = NutritionAdvisorDatabase.USER_PLANS)
    public static class UserPlans {
        @ContentUri(path = NutritionAdvisorDatabase.USER_PLANS,
                type = "vnd.android.cursor.dir/list",
                defaultSort = UserPlanColumns.DATE + " ASC")
        public static final Uri USER_PLANS = Uri.parse(content + AUTHORITY + "/" + NutritionAdvisorDatabase.USER_PLANS);
    }
}
