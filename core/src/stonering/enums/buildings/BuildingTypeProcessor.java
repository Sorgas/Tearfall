package stonering.enums.buildings;

import java.util.Arrays;
import java.util.List;

import stonering.util.geometry.IntVector2;
import stonering.util.global.Pair;

/**
 * @author Alexander on 12.03.2020
 */
public class BuildingTypeProcessor {
    public BuildingType process(RawBuildingType raw) {
        BuildingType type = new BuildingType(raw);
        raw.aspects.stream()
                .map(this::parseAspectString)
                .forEach(pair -> type.aspects.put(pair.key, pair.value));
        for (int i = 0; i < type.sprites.length; i++) {
            type.sprites[i] = new IntVector2(
                    raw.atlasXY[0] + raw.sprites[i][0],
                    raw.atlasXY[1] + raw.sprites[i][1]);
        }
        return type;
    }

    private Pair<String, List<String>> parseAspectString(String aspectString) {
        String[] aspectParts = aspectString.replace(")", "").split("\\(");
        return new Pair<>(aspectParts[0], aspectParts.length > 1 ? Arrays.asList(aspectParts[1].split(",")) : null);
    }
}
