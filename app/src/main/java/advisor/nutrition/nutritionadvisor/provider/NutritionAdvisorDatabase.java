package advisor.nutrition.nutritionadvisor.provider;

import android.database.sqlite.SQLiteDatabase;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.OnUpgrade;
import net.simonvt.schematic.annotation.Table;

import timber.log.Timber;

@Database(version = NutritionAdvisorDatabase.VERSION)
public class NutritionAdvisorDatabase {

    public static final int VERSION = 1;

    @Table(FoodColumns.class)
    public static final String FOODS = "foods";

    @Table(UserColumns.class)
    public static final String USERS = "users";

    @Table(UserDayFoodColumns.class)
    public static final String USER_DAY_FOOD = "user_day_food";

    @Table(UserDayColumns.class)
    public static final String USER_DAY = "user_day";


    private static final String[] _MIGRATIONS = {
            //"ALTER TABLE " + FOODS + " ADD COLUMN " + FoodColumns.MEASURE + " TEXT;",
            //"ALTER TABLE " + FOODS + " ADD COLUMN " + FoodColumns.PORTION_SIZE + " REAL;"
    };

    @OnUpgrade
    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for (int i = oldVersion; i < newVersion; i++) {
            String migration = _MIGRATIONS[i - 2];
            db.beginTransaction();
            try {
                db.execSQL(migration);
                db.setTransactionSuccessful();
                Timber.i("upgraded DB version %d", i);
            } catch (Exception e) {
                Timber.e(e, "Error executing database migration: %s", migration);
                break;
            } finally {
                db.endTransaction();
            }
        }
        Timber.i("successfully upgraded from version %d to version %d", oldVersion, newVersion);
    }

}
