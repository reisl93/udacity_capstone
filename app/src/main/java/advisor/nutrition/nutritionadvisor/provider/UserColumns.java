package advisor.nutrition.nutritionadvisor.provider;

import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

import static net.simonvt.schematic.annotation.DataType.Type.TEXT;

public interface UserColumns {
    @DataType(TEXT)
    @PrimaryKey
    @NotNull
    String NAME = "name";
}
