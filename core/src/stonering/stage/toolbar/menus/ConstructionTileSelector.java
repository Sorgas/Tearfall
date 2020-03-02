package stonering.stage.toolbar.menus;

import stonering.enums.blocks.BlockTypeEnum;
import stonering.util.global.Logger;

import static stonering.enums.blocks.BlockTypeEnum.*;

/**
 * Selects tile for showing in construction designation.
 * Returns x of tile.
 *
 * @author Alexander on 02.03.2020
 */
public class ConstructionTileSelector {

    public static int select(BlockTypeEnum type) {
        if (type != SPACE && type != FARM) return type.CODE - 1;
        Logger.BUILDING.logError("Invalid construction");
        return 0;
    }
}
