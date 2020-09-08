package stonering.enums.unit.race;

import stonering.enums.unit.body.BodyPart;
import stonering.enums.unit.body.BodyTemplate;
import stonering.enums.unit.health.GameplayStatEnum;
import stonering.enums.unit.need.NeedEnum;

import java.util.ArrayList;
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
    public final Map<String, BodyPart> bodyParts = new HashMap<>();
    public final Map<String, List<String>> slots = new HashMap<>(); // slot name to default limbs
    public final List<String> desiredSlots = new ArrayList<>();
    public final List<NeedEnum> needs = new ArrayList<>();
    public final List<String> aspects = new ArrayList<>();
    public int[] atlasXY;
    public String color;
    public CombinedAppearance combinedAppearance;
    public final Map<GameplayStatEnum, Float> statMap = new HashMap<>();

    public CreatureType(RawCreatureType rawType) {
        name = rawType.name;
        title = rawType.title;
        description = rawType.description;
        atlasXY = rawType.atlasXY;
        color = rawType.color;
        aspects.addAll(rawType.aspects);
        combinedAppearance = rawType.combinedAppearance;
    }

    public CreatureType() {}
}
