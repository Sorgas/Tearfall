package stonering.enums.unit;

import com.badlogic.gdx.utils.Json;
import stonering.entity.unit.aspects.needs.NeedEnum;
import stonering.enums.unit.body.BodyTemplate;
import stonering.enums.unit.body.raw.BodyTemplateProcessor;
import stonering.enums.unit.body.raw.RawBodyTemplate;
import stonering.util.global.FileLoader;
import stonering.util.global.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
        Json json = new Json();
        BodyTemplateProcessor processor = new BodyTemplateProcessor();
        ArrayList<RawBodyTemplate> types = json.fromJson(ArrayList.class, RawBodyTemplate.class, FileLoader.getFile(FileLoader.BODY_TEMPLATE_PATH));
        for (RawBodyTemplate type : types) {
            if (validateTemplate(type)) bodyTemplates.put(type.name, processor.process(type));
        }
    }

    private void loadCreatures() {
        Json json = new Json();
        ArrayList<RawCreatureType> types = json.fromJson(ArrayList.class, RawCreatureType.class, FileLoader.getFile(FileLoader.CREATURES_PATH));
        for (RawCreatureType rawType : types) {
            creatureTypes.put(rawType.name, processRawCreatureType(rawType));
        }
    }

    private CreatureType processRawCreatureType(RawCreatureType rawType) {
        CreatureType type = new CreatureType(rawType);
        type.bodyTemplate = bodyTemplates.get(rawType.bodyTemplate);
        if (type.limbsToCover == null) type.limbsToCover = type.bodyTemplate.limbsToCover;
        return type;
    }

    /**
     * Checks that creature is defined correctly.
     */
    private boolean validateTemplate(RawBodyTemplate template) {
        for (String need : template.needs) {
            if (!NeedEnum.map.containsKey(need)) {
                Logger.LOADING.logError("Body template " + template.name + " has invalid need entry: " + need + ".");
                return false;
            }
        }
        return true;
    }

    public CreatureType getCreatureType(String specimen) {
        Logger.UNITS.logError("Creature type " + specimen + "not found");
        return creatureTypes.get(specimen);
    }
}
