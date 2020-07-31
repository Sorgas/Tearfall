package stonering.enums.designations;

import stonering.enums.blocks.BlockTypeEnum;
import stonering.game.GameMvc;
import stonering.game.model.local_map.LocalMap;
import stonering.util.validation.*;
import stonering.util.validation.zone.farm.FreeSoilFloorValidator;

import java.util.HashMap;

/**
 * Contains mapping from String placing fiels in blueprints.json to actual validators classes.
 * <p>
 * TODO add validators from plants.
 */
public enum PlaceValidatorEnum {
    SOIL_FLOOR("soil_floor", new FreeSoilFloorValidator()),
    FARM("farm", pos -> GameMvc.model().get(LocalMap.class).blockType.get(pos) == BlockTypeEnum.FARM.CODE),
    TREE_EXISTS("tree", new TreeExistsValidator()),
    DISTANCE_TO_WATER("distance_to_water", new DistanceToWaterValidator()),
    PLANT_CUTTING("plant_cutting", new PlantCuttingValidator()),
    FREE_FLOOR("floor", new FreeFloorValidator()),
    CONSTRUCTION("construction", new ConstructionValidator());

    private static HashMap<String, PositionValidator> map;

    public final String NAME;
    public final PositionValidator VALIDATOR;

    static {
        map = new HashMap<>();
        for (PlaceValidatorEnum placeValidatorEnum : PlaceValidatorEnum.values()) {
            map.put(placeValidatorEnum.NAME, placeValidatorEnum.VALIDATOR);
        }
    }

    PlaceValidatorEnum(String name, PositionValidator validator) {
        this.NAME = name;
        this.VALIDATOR = validator;
    }

    public static PositionValidator getValidator(String name) {
        return map.get(name);
    }
}
