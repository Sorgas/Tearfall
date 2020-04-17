package stonering.enums.buildings;

import stonering.util.geometry.IntVector2;

/**
 * @author Alexander on 12.03.2020
 */
public class RawBuildingTypeProcessor {
    public BuildingType process(RawBuildingType raw) {
        BuildingType type = new BuildingType(raw);
        for (int i = 0; i < type.sprites.length; i++) {
            type.sprites[i] = new IntVector2(
                    raw.atlasXY[0] + raw.sprites[i][0],
                    raw.atlasXY[1] + raw.sprites[i][1]);
        }
        return type;
    }
}
