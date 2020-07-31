package stonering.util.validation.zone.farm;

import stonering.enums.blocks.BlockTypeEnum;
import stonering.enums.items.ItemTagEnum;
import stonering.enums.materials.MaterialMap;
import stonering.game.GameMvc;
import stonering.game.model.GameModel;
import stonering.game.model.local_map.BlockTypeMap;
import stonering.game.model.local_map.LocalMap;
import stonering.game.model.system.building.BuildingContainer;
import stonering.util.geometry.Position;
import stonering.util.validation.PositionValidator;

/**
 * Checks that tile could be assigned to farm zone: tile is floor or farm, soil, no buildings.
 *
 * @author Alexander_Kuzyakov on 05.07.2019.
 */
public class FarmPositionValidator implements PositionValidator {

    @Override
    public Boolean apply(Position position) {
        GameModel model = GameMvc.model();
        BlockTypeMap map = model.get(LocalMap.class).blockType;
        return (map.get(position) == BlockTypeEnum.FLOOR.CODE || map.get(position) == BlockTypeEnum.FARM.CODE) // tile is floor or farm
                && MaterialMap.getMaterial(map.getMaterial(position)).tags.contains(ItemTagEnum.SOIL) // tile is soil
                && model.get(BuildingContainer.class).buildingBlocks.get(position) == null; // no buildings
    }
}
