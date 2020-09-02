package stonering.enums.unit;

import com.badlogic.gdx.utils.Json;

import stonering.enums.unit.health.GameplayStatEnum;
import stonering.enums.unit.need.NeedEnum;
import stonering.enums.unit.body.BodyTemplate;
import stonering.enums.unit.body.raw.BodyTemplateProcessor;
import stonering.enums.unit.body.raw.RawBodyTemplate;
import stonering.enums.unit.race.CreatureType;
import stonering.enums.unit.race.RawCreatureType;
import stonering.util.lang.FileUtil;
import stonering.util.logging.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Loads, and stores {@link CreatureType} and {@link BodyTemplate}.
 *
 * @author Alexander on 29.05.2019.
 */
public class CreatureTypeMap {
    private static CreatureTypeMap instance;
    private Map<String, CreatureType> creatureTypes;
    private Map<String, BodyTemplate> bodyTemplates;

    private CreatureTypeMap() {
        creatureTypes = new HashMap<>();
        bodyTemplates = new HashMap<>();
        loadTemplates();
        loadCreatures();
    }

    public static CreatureTypeMap instance() {
        if (instance == null) instance = new CreatureTypeMap();
        return instance;
    }

    private void loadTemplates() {
        Logger.LOADING.logDebug("loading body templates");
        Json json = new Json();
        BodyTemplateProcessor processor = new BodyTemplateProcessor();
        ArrayList<RawBodyTemplate> types = json.fromJson(ArrayList.class, RawBodyTemplate.class, FileUtil.get(FileUtil.BODY_TEMPLATE_PATH));
        for (RawBodyTemplate type : types) {
            if (validateTemplate(type)) bodyTemplates.put(type.name, processor.process(type));
        }
    }

    private void loadCreatures() {
        Json json = new Json();
        ArrayList<RawCreatureType> types = json.fromJson(ArrayList.class, RawCreatureType.class, FileUtil.get(FileUtil.CREATURES_PATH));
        for (RawCreatureType rawType : types) {
            CreatureType type = processRawCreatureType(rawType);
            if (type != null) creatureTypes.put(rawType.name, processRawCreatureType(rawType));
        }
    }

    private CreatureType processRawCreatureType(RawCreatureType rawType) {
        CreatureType type = new CreatureType(rawType);
        if (!bodyTemplates.containsKey(rawType.bodyTemplate)) {
            Logger.LOADING.logWarn("Creature " + type.name + " has invalid body template " + type.bodyTemplate);
            return null;
        }
        rawType.statMap.forEach((name, value) -> {
            Optional.ofNullable(GameplayStatEnum.map.get(name))
                    .ifPresentOrElse(stat -> type.statMap.put(stat, value),
                            () -> Logger.LOADING.logError("Invalid stat name " + name + " in creature type " + rawType.name));
        });
        type.bodyTemplate = bodyTemplates.get(rawType.bodyTemplate);
        if (type.desiredSlots == null) type.desiredSlots = type.bodyTemplate.desiredSlots;
        return type;
    }

    /**
     * Checks that creature is defined correctly.
     */
    private boolean validateTemplate(RawBodyTemplate template) {
        return template.needs.stream().anyMatch(need -> !NeedEnum.map.containsKey(need))
                ? Logger.LOADING.logWarn("Body template " + template.name + " has invalid need entry", false)
                : true;
    }

    public CreatureType getCreatureType(String specimen) {
        if (!creatureTypes.containsKey(specimen)) Logger.UNITS.logError("Creature type " + specimen + " not found");
        return creatureTypes.get(specimen);
    }

    public static void main(String[] args) {
        Logger.enableAll();
        CreatureTypeMap.instance();
    }
}
