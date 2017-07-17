package advisor.nutrition.nutritionadvisor.provider;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

@Database(version = NutritionAdvisorDatabase.VERSION)
public class NutritionAdvisorDatabase {

    public static final int VERSION = 1;

    @Table(FoodColumns.class)
    public static final String FOODS = "foods";

    @Table(UserColumns.class)
    public static final String USERS = "users";

    @Table(UserPlanColumns.class)
    public static final String USER_PLANS = "user_plans";

}
