package stonering.enums.unit;

import stonering.enums.unit.body.BodyTemplate;

import java.util.List;

/**
 * @author Alexander on 29.05.2019.
 */
public class CreatureType {
    public String name;
    public String title;
    public String description;
    public BodyTemplate bodyTemplate;
    public List<String> desiredSlots;
    public List<String> aspects;
    public int baseHP;
    public int[] atlasXY;
    public String color;
    public CombinedAppearance combinedAppearance;
    
    public CreatureType(RawCreatureType rawType) {
        name = rawType.name;
        title = rawType.title;
        description = rawType.description;
        desiredSlots = rawType.limbsToCover;
        baseHP = rawType.baseHP;
        atlasXY = rawType.atlasXY;
        color = rawType.color;
        aspects = rawType.aspects;
        combinedAppearance = rawType.combinedAppearance;
    }

    public CreatureType() {}
}
