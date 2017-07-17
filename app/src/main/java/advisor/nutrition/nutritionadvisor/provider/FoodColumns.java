package advisor.nutrition.nutritionadvisor.provider;

import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

import static net.simonvt.schematic.annotation.DataType.Type.INTEGER;
import static net.simonvt.schematic.annotation.DataType.Type.REAL;
import static net.simonvt.schematic.annotation.DataType.Type.TEXT;

public interface FoodColumns {


    @DataType(INTEGER)
    @PrimaryKey
    String _ID = "_id";

    @DataType(TEXT)
    @NotNull
    String NAME = "NAME";

    @DataType(REAL)
    @NotNull
    String CARBOHYDRATES = "CARBOHYDRATES";

    @DataType(REAL)
    @NotNull
    String FAT = "FAT";

    @DataType(REAL)
    @NotNull
    String PROTEIN = "PROTEINS";

}
