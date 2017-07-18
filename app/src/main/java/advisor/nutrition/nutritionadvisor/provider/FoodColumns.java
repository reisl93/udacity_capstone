package advisor.nutrition.nutritionadvisor.provider;

import net.simonvt.schematic.annotation.ConflictResolutionType;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

import static net.simonvt.schematic.annotation.DataType.Type.INTEGER;
import static net.simonvt.schematic.annotation.DataType.Type.REAL;
import static net.simonvt.schematic.annotation.DataType.Type.TEXT;

public interface FoodColumns {
    @DataType(INTEGER)
    @PrimaryKey(onConflict = ConflictResolutionType.REPLACE)
    String _ID = "_id";

    @DataType(TEXT)
    @NotNull
    String NAME = "name";

    @DataType(REAL)
    @NotNull
    String CARBOHYDRATES = "carbohydrates";

    @DataType(REAL)
    @NotNull
    String FAT = "fat";

    @DataType(REAL)
    @NotNull
    String PROTEIN = "proteins";

    @DataType(REAL)
    @NotNull
    String PORTION_SIZE = "portion_size";

    @DataType(TEXT)
    @NotNull
    String MEASURE = "measure";

}
