package stonering.enums.blocks;

import stonering.enums.buildings.BuildingType;

import java.util.HashMap;
import java.util.Map;

/**
 * Different values for tile passage. Numeric values stored on {@link stonering.game.model.local_map.passage.PassageMap}
 * {@link BuildingType} define passage of their blocks with strings of characters listed here.
 * TODO passage for flying and swimming will be here.
 *
 * @author Alexander on 09.03.2020
 */
public enum PassageEnum {
    PASSABLE(1, '_'),
    IMPASSABLE(0, 'X');

    public final byte VALUE;
    public final char CHARACTER;

    private static Map<Character, PassageEnum> map;

    static {
        map = new HashMap<>();
        for (PassageEnum value : PassageEnum.values()) {
            map.put(value.CHARACTER, value);
        }
    }

    PassageEnum(int value, char character) {
        VALUE = (byte) value;
        CHARACTER = character;
    }

    public static PassageEnum get(char character) {
        return map.get(character);
    }
}
