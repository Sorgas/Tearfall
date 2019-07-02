package stonering.enums.unit;

import java.util.List;

/**
 * @author Alexander on 29.05.2019.
 */
public class CreatureType {
    public String name;
    public String title;
    public String description;
    public BodyTemplate bodyTemplate;
    public List<String> limbsToCover;
    public List<String> aspects;
    public int baseHP;
    public int[] atlasXY;
    public String color;

    public CreatureType(RawCreatureType rawType) {
        name = rawType.name;
        title = rawType.title;
        description = rawType.description;
        limbsToCover = rawType.limbsToCover;
        baseHP = rawType.baseHP;
        atlasXY = rawType.atlasXY;
        color = rawType.color;
        aspects = rawType.aspects;
    }
}
