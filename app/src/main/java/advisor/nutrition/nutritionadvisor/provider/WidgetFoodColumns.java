package advisor.nutrition.nutritionadvisor.provider;

import net.simonvt.schematic.annotation.ConflictResolutionType;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

import static net.simonvt.schematic.annotation.DataType.Type.INTEGER;
import static net.simonvt.schematic.annotation.DataType.Type.REAL;
import static net.simonvt.schematic.annotation.DataType.Type.TEXT;

public interface WidgetFoodColumns {
    @DataType(INTEGER)
    @PrimaryKey(onConflict = ConflictResolutionType.REPLACE)
    String _ID = "_id";

    @DataType(TEXT)
    @NotNull
    String NAME = "name";

    @DataType(REAL)
    @NotNull
    String PORTION = "portion";

    @DataType(TEXT)
    @NotNull
    String MEASURE = "measure";
}
