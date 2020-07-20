package stonering.enums.designations;

import stonering.enums.blocks.BlockTypeEnum;
import stonering.game.GameMvc;
import stonering.game.model.local_map.LocalMap;
import stonering.util.validation.FreeFloorValidator;
import stonering.util.validation.ConstructionValidator;
import stonering.util.validation.PositionValidator;

import java.util.HashMap;

/**
 * Contains mapping from String placing fiels in blueprints.json to actual validators classes.
 *
 * TODO add validators from plants.
 */
public enum PlaceValidatorsEnum {
    FLOOR("floor", new FreeFloorValidator()),
    FARM("farm", pos -> GameMvc.model().get(LocalMap.class).blockType.get(pos) == BlockTypeEnum.FARM.CODE),
    CONSTRUCTION("construction", new ConstructionValidator());

    private static HashMap<String, PositionValidator> map;

    public final String NAME;
    public final PositionValidator VALIDATOR;

    static {
        map = new HashMap<>();
        for (PlaceValidatorsEnum placeValidatorsEnum : PlaceValidatorsEnum.values()) {
            map.put(placeValidatorsEnum.NAME, placeValidatorsEnum.VALIDATOR);
        }
    }

    PlaceValidatorsEnum(String NAME, PositionValidator VALIDATOR) {
        this.NAME = NAME;
        this.VALIDATOR = VALIDATOR;
    }

    public static PositionValidator getValidator(String name) {
        return map.get(name);
    }
}
