package advisor.nutrition.nutritionadvisor.provider;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.MapColumns;
import net.simonvt.schematic.annotation.TableEndpoint;

import java.util.HashMap;
import java.util.Map;

@ContentProvider(authority = NutritionAdvisorProvider.AUTHORITY, database = NutritionAdvisorDatabase.class)
public class NutritionAdvisorProvider {

    public static final String AUTHORITY = "advisor.nutrition.nutritionadvisor.NutritionAdvisorProvider";
    private static String content = "content://";

    @TableEndpoint(table = NutritionAdvisorDatabase.FOODS)
    public static class Foods {
        @ContentUri(path = NutritionAdvisorDatabase.FOODS,
                type = "vnd.android.cursor.dir/foods",
                defaultSort = FoodColumns.NAME + " ASC")
        public static final Uri FOODS = Uri.parse(content + AUTHORITY + "/" + NutritionAdvisorDatabase.FOODS);
    }

    @TableEndpoint(table = NutritionAdvisorDatabase.FOODS)
    public static class FoodJoins {
        public final static String PLAN_FOOD_ID = "user_food_id";
        public final static String PLAN_DATE = "user_date";
        public final static String PLAN_NAME = "user_plans_name";
        public final static String PLAN_PORTIONS = "plan_portions";
        public final static String FOOD_PROTEINS = "food_proteins";
        public final static String FOOD_FAT = "food_fat";
        public final static String FOOD_CARBS = "food_carbs";
        public final static String FOOD_MEASURE = "food_measure";
        public final static String FOOD_PORTION_SIZE = "food_portion_size";
        public final static String FOOD_NAME = "food_name";

        public static String[] JOIN_PROJECTION = new String[]{PLAN_FOOD_ID, PLAN_DATE, PLAN_NAME, FOOD_PROTEINS,
                FOOD_FAT, FOOD_CARBS, FOOD_MEASURE, FOOD_PORTION_SIZE, PLAN_PORTIONS, FOOD_NAME};
        public final static int INDEX_PLAN_FOOD_ID = 0;
        public final static int INDEX_PLAN_DATE = 1;
        public final static int INDEX_PLAN_NAME = 2;
        public final static int INDEX_FOOD_PROTEINS = 3;
        public final static int INDEX_FOOD_FAT = 4;
        public final static int INDEX_FOOD_CARBS = 5;
        public final static int INDEX_FOOD_MEASURE = 6;
        public final static int INDEX_FOOD_PORTION_SIZE = 7;
        public final static int INDEX_PLAN_PORTIONS = 8;
        public final static int INDEX_FOOD_NAME = 9;


        @MapColumns
        public static Map<String, String> mapColumns(){
            HashMap<String, String> mapping = new HashMap<>();
            mapping.put(PLAN_FOOD_ID, NutritionAdvisorDatabase.USER_PLANS + "." + UserPlanColumns.FOOD_ID);
            mapping.put(PLAN_DATE, NutritionAdvisorDatabase.USER_PLANS + "." + UserPlanColumns.DATE);
            mapping.put(PLAN_NAME, NutritionAdvisorDatabase.USER_PLANS + "." + UserPlanColumns.USER_NAME);
            mapping.put(PLAN_PORTIONS, NutritionAdvisorDatabase.USER_PLANS + "." + UserPlanColumns.PORTIONS);

            mapping.put(FOOD_PROTEINS, NutritionAdvisorDatabase.FOODS + "." + FoodColumns.PROTEIN);
            mapping.put(FOOD_FAT, NutritionAdvisorDatabase.FOODS + "." + FoodColumns.FAT);
            mapping.put(FOOD_CARBS, NutritionAdvisorDatabase.FOODS + "." + FoodColumns.CARBOHYDRATES);
            mapping.put(FOOD_MEASURE, NutritionAdvisorDatabase.FOODS + "." + FoodColumns.MEASURE);
            mapping.put(FOOD_PORTION_SIZE, NutritionAdvisorDatabase.FOODS + "." + FoodColumns.PORTION_SIZE);
            mapping.put(FOOD_NAME, NutritionAdvisorDatabase.FOODS + "." + FoodColumns.NAME);

            return mapping;
        }

        @InexactContentUri(
                path = "date/*/user/*",
                name = "FOOD_JOIN_DATE",
                type = "vnd.android.cursor.item/foods_join_date",
                join = "JOIN " + NutritionAdvisorDatabase.USER_PLANS + " ON " +
                        NutritionAdvisorDatabase.FOODS + "." + FoodColumns._ID + " = " + NutritionAdvisorDatabase.USER_PLANS + "." + UserPlanColumns.FOOD_ID,
                whereColumn = {UserPlanColumns.DATE, UserPlanColumns.USER_NAME},
                defaultSort = FoodColumns.NAME + " DESC",
                pathSegment = {1,3})
        public static Uri withUserAndDate(String user, String date) {
            return Uri.parse(content + AUTHORITY + "/date/" + date + "/user/" + user);
        }


        @InexactContentUri(
                path = "user/*",
                name = "FOOD_JOIN",
                type = "vnd.android.cursor.item/foods_join",
                join = "JOIN " + NutritionAdvisorDatabase.FOODS + " ON " +
                        NutritionAdvisorDatabase.FOODS + "." + FoodColumns._ID + " = " + NutritionAdvisorDatabase.USER_PLANS + "." + UserPlanColumns.FOOD_ID,
                whereColumn = UserPlanColumns.USER_NAME,
                defaultSort = FoodColumns.NAME + " DESC",
                pathSegment = 1)
        public static Uri withUser(String user) {
            return Uri.parse(content + AUTHORITY + "/user/" + user );
        }
    }


    @TableEndpoint(table = NutritionAdvisorDatabase.USERS)
    public static class Users {
        @ContentUri(path = NutritionAdvisorDatabase.USERS,
                type = "vnd.android.cursor.dir/users",
                defaultSort = UserColumns.NAME + " ASC")
        public static final Uri USERS = Uri.parse(content + AUTHORITY + "/" + NutritionAdvisorDatabase.USERS);

    }

    @TableEndpoint(table = NutritionAdvisorDatabase.USER_PLANS)
    public static class UserPlans {
        @ContentUri(path = NutritionAdvisorDatabase.USER_PLANS,
                type = "vnd.android.cursor.dir/user.plans",
                defaultSort = UserPlanColumns.DATE + " ASC")
        public static final Uri USER_PLANS = Uri.parse(content + AUTHORITY + "/" + NutritionAdvisorDatabase.USER_PLANS);
    }

}
