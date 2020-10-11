package stonering.enums.unit;

import java.util.ArrayList;
import java.util.Arrays;

import stonering.enums.unit.body.BodyTemplate;
import stonering.enums.unit.health.GameplayStatEnum;
import stonering.enums.unit.need.NeedEnum;
import stonering.enums.unit.race.CreatureType;
import stonering.enums.unit.race.RawCreatureType;
import stonering.util.logging.Logger;

/**
 * @author Alexander on 08.09.2020.
 */
public class CreatureTypeProcessor {
    private static NeedEnum[] defaultNeeds = {NeedEnum.FOOD, NeedEnum.REST, NeedEnum.WATER}; // needs for most creatures
    private CreatureTypeMap typeMap;

    public CreatureTypeProcessor(CreatureTypeMap typeMap) {
        this.typeMap = typeMap;
    }

    public CreatureType process(RawCreatureType raw) {
        CreatureType type = new CreatureType(raw);
        if (!typeMap.bodyTemplates.containsKey(raw.bodyTemplate)) {
            return Logger.LOADING.logWarn("Creature " + type.name + " has invalid body template " + raw.bodyTemplate, null);
        }

        if(type.combinedAppearance != null) type.combinedAppearance.process();
        Arrays.stream(GameplayStatEnum.values())
                .forEach(value -> type.statMap.put(value, value.DEFAULT)); // save default values
        for (String statName : raw.statMap.keySet()) { // override default values
            if (GameplayStatEnum.map.containsKey(statName)) {
                type.statMap.put(GameplayStatEnum.map.get(statName), raw.statMap.get(statName));
            } else {
                Logger.LOADING.logError("Invalid stat name " + statName + " in creature type " + raw.name);
            }
        }
        BodyTemplate template = typeMap.bodyTemplates.get(raw.bodyTemplate);
        template.body.forEach((name, part) -> type.bodyParts.put(name, part.clone()));
        template.slots.forEach((name, slot) -> type.slots.put(name, new ArrayList<>(slot)));
        type.desiredSlots.addAll(raw.desiredSlots);
        type.needs.addAll(Arrays.asList(defaultNeeds));
        return type;
    }
}
