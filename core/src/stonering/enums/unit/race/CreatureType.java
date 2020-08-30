package stonering.enums.unit.race;

import stonering.enums.unit.body.BodyTemplate;
import stonering.enums.unit.health.GameplayStatEnum;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public final Map<GameplayStatEnum, Float> statMap = new HashMap<>();

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
