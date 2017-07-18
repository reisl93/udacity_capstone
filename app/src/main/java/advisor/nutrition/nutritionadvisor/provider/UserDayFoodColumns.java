package advisor.nutrition.nutritionadvisor.provider;

import net.simonvt.schematic.annotation.ConflictResolutionType;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKeyConstraint;

import static net.simonvt.schematic.annotation.DataType.Type.INTEGER;
import static net.simonvt.schematic.annotation.DataType.Type.REAL;
import static net.simonvt.schematic.annotation.DataType.Type.TEXT;

@PrimaryKeyConstraint(
        name = "user_day_food_keys",
        columns = {UserDayFoodColumns.DATE, UserDayFoodColumns.USER_NAME, UserDayFoodColumns.FOOD_ID},
        onConflict = ConflictResolutionType.REPLACE)
public interface UserDayFoodColumns {

    @DataType(INTEGER)
    @NotNull
    String FOOD_ID = "food_id";

    @DataType(TEXT)
    @NotNull
    String USER_NAME = "user_name";

    @DataType(TEXT)
    @NotNull
    String DATE = "date";

    @DataType(REAL)
    String PREFERENCE_PORTIONS = "preference_portions";

    @DataType(REAL)
    String CALCULATED_PORTIONS = "calculated_portions";

}
