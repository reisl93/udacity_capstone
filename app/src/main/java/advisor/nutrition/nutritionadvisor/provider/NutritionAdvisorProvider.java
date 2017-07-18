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
        public final static String PLAN_TARGET_PORTIONS = "user_target_portions";
        public final static String PLAN_CALCULATED_PORTIONS = "user_calculated_portions";
        public final static String FOOD_PROTEINS = "food_proteins";
        public final static String FOOD_FAT = "food_fat";
        public final static String FOOD_CARBS = "food_carbs";
        public final static String FOOD_MEASURE = "food_measure";
        public final static String FOOD_PORTION_SIZE = "food_portion_size";
        public final static String FOOD_NAME = "food_name";

        public static String[] JOIN_PROJECTION = new String[]{PLAN_FOOD_ID, PLAN_DATE, PLAN_NAME, FOOD_PROTEINS,
                FOOD_FAT, FOOD_CARBS, FOOD_MEASURE, FOOD_PORTION_SIZE, PLAN_TARGET_PORTIONS, FOOD_NAME, PLAN_CALCULATED_PORTIONS};
        public final static int INDEX_PLAN_FOOD_ID = 0;
        public final static int INDEX_PLAN_DATE = 1;
        public final static int INDEX_PLAN_NAME = 2;
        public final static int INDEX_FOOD_PROTEINS = 3;
        public final static int INDEX_FOOD_FAT = 4;
        public final static int INDEX_FOOD_CARBS = 5;
        public final static int INDEX_FOOD_MEASURE = 6;
        public final static int INDEX_FOOD_PORTION_SIZE = 7;
        public final static int INDEX_PLAN_TARGET_PORTIONS = 8;
        public final static int INDEX_FOOD_NAME = 9;
        public final static int INDEX_PLAN_CALCULATED_PORTIONS = 10;


        @MapColumns
        public static Map<String, String> mapColumns() {
            HashMap<String, String> mapping = new HashMap<>();
            mapping.put(PLAN_FOOD_ID, NutritionAdvisorDatabase.USER_DAY_FOOD + "." + UserDayFoodColumns.FOOD_ID);
            mapping.put(PLAN_DATE, NutritionAdvisorDatabase.USER_DAY_FOOD + "." + UserDayFoodColumns.DATE);
            mapping.put(PLAN_NAME, NutritionAdvisorDatabase.USER_DAY_FOOD + "." + UserDayFoodColumns.USER_NAME);
            mapping.put(PLAN_TARGET_PORTIONS, NutritionAdvisorDatabase.USER_DAY_FOOD + "." + UserDayFoodColumns.TARGET_PORTIONS);
            mapping.put(PLAN_CALCULATED_PORTIONS, NutritionAdvisorDatabase.USER_DAY_FOOD + "." + UserDayFoodColumns.CALCULATED_PORTIONS);

            mapping.put(FOOD_PROTEINS, NutritionAdvisorDatabase.FOODS + "." + FoodColumns.PROTEIN);
            mapping.put(FOOD_FAT, NutritionAdvisorDatabase.FOODS + "." + FoodColumns.FAT);
            mapping.put(FOOD_CARBS, NutritionAdvisorDatabase.FOODS + "." + FoodColumns.CARBOHYDRATES);
            mapping.put(FOOD_MEASURE, NutritionAdvisorDatabase.FOODS + "." + FoodColumns.MEASURE);
            mapping.put(FOOD_PORTION_SIZE, NutritionAdvisorDatabase.FOODS + "." + FoodColumns.PORTION_SIZE);
            mapping.put(FOOD_NAME, NutritionAdvisorDatabase.FOODS + "." + FoodColumns.NAME);

            return mapping;
        }

        @InexactContentUri(
                path = "FoodJoins/date/*/user/*",
                name = "FOOD_JOIN_DATE",
                type = "vnd.android.cursor.item/foods_join_date",
                join = "JOIN " + NutritionAdvisorDatabase.USER_DAY_FOOD + " ON " +
                        NutritionAdvisorDatabase.FOODS + "." + FoodColumns._ID + " = " + NutritionAdvisorDatabase.USER_DAY_FOOD + "." + UserDayFoodColumns.FOOD_ID,
                whereColumn = {UserDayFoodColumns.DATE, UserDayFoodColumns.USER_NAME},
                defaultSort = FoodColumns.NAME + " DESC",
                pathSegment = {2, 4})
        public static Uri withUserAndDate(String user, String date) {
            return Uri.parse(content + AUTHORITY + "/FoodJoins/date/" + date + "/user/" + user);
        }


        @InexactContentUri(
                path = "FoodJoins/user/*",
                name = "FOOD_JOIN",
                type = "vnd.android.cursor.item/foods_join",
                join = "JOIN " + NutritionAdvisorDatabase.FOODS + " ON " +
                        NutritionAdvisorDatabase.FOODS + "." + FoodColumns._ID + " = " + NutritionAdvisorDatabase.USER_DAY_FOOD + "." + UserDayFoodColumns.FOOD_ID,
                whereColumn = UserDayFoodColumns.USER_NAME,
                defaultSort = FoodColumns.NAME + " DESC",
                pathSegment = 2)
        public static Uri withUser(String user) {
            return Uri.parse(content + AUTHORITY + "/FoodJoins/user/" + user);
        }
    }


    @TableEndpoint(table = NutritionAdvisorDatabase.USERS)
    public static class Users {
        @ContentUri(path = NutritionAdvisorDatabase.USERS,
                type = "vnd.android.cursor.dir/users",
                defaultSort = UserColumns.NAME + " ASC")
        public static final Uri USERS = Uri.parse(content + AUTHORITY + "/" + NutritionAdvisorDatabase.USERS);

    }

    @TableEndpoint(table = NutritionAdvisorDatabase.USER_DAY)
    public static class UserDay {
        @ContentUri(path = NutritionAdvisorDatabase.USER_DAY,
                type = "vnd.android.cursor.dir/user.day.all",
                defaultSort = UserDayColumns.DATE + " ASC")
        public static final Uri USER_DAY = Uri.parse(content + AUTHORITY + "/" + NutritionAdvisorDatabase.USER_DAY);

        @InexactContentUri(
                path = "UserDay/date/*/user/*",
                name = "USER_DAY_DATEUSER",
                type = "vnd.android.cursor.item/user.day.dateuser",
                whereColumn = {UserDayColumns.DATE, UserDayColumns.USER_NAME},
                defaultSort = UserDayColumns.USER_NAME + " DESC",
                pathSegment = {2, 4})
        public static Uri withUserAndDate(String user, String date) {
            return Uri.parse(content + AUTHORITY + "/UserDay/date/" + date + "/user/" + user);
        }
    }

    @TableEndpoint(table = NutritionAdvisorDatabase.USER_DAY_FOOD)
    public static class UserDayFood {
        @ContentUri(path = NutritionAdvisorDatabase.USER_DAY_FOOD,
                type = "vnd.android.cursor.dir/user.day.food.all",
                defaultSort = UserDayFoodColumns.DATE + " ASC")
        public static final Uri USER_DAY_FOOD = Uri.parse(content + AUTHORITY + "/" + NutritionAdvisorDatabase.USER_DAY_FOOD);

        @InexactContentUri(
                path = "UserDayFood/date/*/user/*/food/#",
                name = "USER_DAY_FOOD_DATEUSERFOOD",
                type = "vnd.android.cursor.item/user.day.food.dateuserfoodid",
                whereColumn = {UserDayFoodColumns.DATE, UserDayFoodColumns.USER_NAME, UserDayFoodColumns.FOOD_ID},
                defaultSort = UserDayFoodColumns.USER_NAME + " DESC",
                pathSegment = {2, 4, 6})
        public static Uri withUserAndDateAndFoodId(String user, String date, int foodId) {
            return Uri.parse(content + AUTHORITY + "/UserDayFood/date/" + date + "/user/" + user + "/food/" + foodId);
        }
    }
}
