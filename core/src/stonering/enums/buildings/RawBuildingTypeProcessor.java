package stonering.enums.buildings;

import stonering.enums.blocks.PassageEnum;
import stonering.util.geometry.IntVector2;

/**
 * @author Alexander on 12.03.2020
 */
public class RawBuildingTypeProcessor {
    public BuildingType process(RawBuildingType raw) {
        BuildingType type = new BuildingType(raw);
        type.passageArray = new PassageEnum[raw.size[0]][raw.size[1]];
        char[] chars = raw.passage.toCharArray();
        for (int x = 0; x < raw.size[0]; x++) {
            for (int y = 0; y < raw.size[1]; y++) {
                int i = y * raw.size[0] + x;
                type.passageArray[x][y] = PassageEnum.get(chars[i]);
            }
        }
        for (int i = 0; i < type.sprites.length; i++) {
            type.sprites[i] = new IntVector2(
                    raw.atlasXY[0] + raw.sprites[i][0],
                    raw.atlasXY[1] + raw.sprites[i][1]);
        }
        return type;
    }
}
