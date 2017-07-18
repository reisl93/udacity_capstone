package advisor.nutrition.nutritionadvisor.provider;

import net.simonvt.schematic.annotation.ConflictResolutionType;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKeyConstraint;

import static net.simonvt.schematic.annotation.DataType.Type.INTEGER;
import static net.simonvt.schematic.annotation.DataType.Type.TEXT;

@PrimaryKeyConstraint(name = "user_day_keys",
        columns = {UserDayColumns.DATE, UserDayColumns.USER_NAME},
        onConflict = ConflictResolutionType.REPLACE)
public interface UserDayColumns {
    @DataType(TEXT)
    @NotNull
    String USER_NAME = "user_name";

    @DataType(TEXT)
    @NotNull
    String DATE = "date";

    @DataType(INTEGER)
    String TARGET_PROTEINS = "target_proteins";

    @DataType(INTEGER)
    String TARGET_FAT = "target_fat";

    @DataType(INTEGER)
    String TARGET_CARBS = "target_carbs";

    @DataType(INTEGER)
    String CALCULATED_PROTEINS = "calculated_proteins";

    @DataType(INTEGER)
    @NotNull
    String CALCULATED_FAT = "calculated_fat";

    @DataType(INTEGER)
    String CALCULATED_CARBS = "calculated_carbs";
}
