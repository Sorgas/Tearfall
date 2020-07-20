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
    FLOOR("floor", new FreeFloorValidator()),
    SOIL_FLOOR("soil_floor", new FreeSoilFloorValidator()),
    FARM("farm", pos -> GameMvc.model().get(LocalMap.class).blockType.get(pos) == BlockTypeEnum.FARM.CODE),
    TREE("tree", new TreeExistsValidator()),
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

    PlaceValidatorEnum(String NAME, PositionValidator VALIDATOR) {
        this.NAME = NAME;
        this.VALIDATOR = VALIDATOR;
    }

    public static PositionValidator getValidator(String name) {
        return map.get(name);
    }
}
