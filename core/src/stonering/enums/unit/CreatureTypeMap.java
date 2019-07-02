package stonering.enums.unit;

import com.badlogic.gdx.utils.Json;
import stonering.util.global.FileLoader;

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
        if(instance == null) instance = new CreatureTypeMap();
        return instance;
    }

    private void loadTemplates() {
        Json json = new Json();
        ArrayList<RawBodyTemplate> types = json.fromJson(ArrayList.class, RawBodyTemplate.class, FileLoader.BODY_TEMPLATE_PATH);
        for (RawBodyTemplate type : types) {
            bodyTemplates.put(type.name, type);
        }
    }

    private void loadCreatures() {
        Json json = new Json();
        ArrayList<RawCreatureType> types = json.fromJson(ArrayList.class, RawCreatureType.class, FileLoader.CREATURES_PATH);
        for (RawCreatureType rawType : types) {
            creatureTypes.put(rawType.name, processRawCreatureType(rawType));
        }
    }

    private BodyTemplate processRawBodyTemplate(RawBodyTemplate rawBodyTemplate) {
        BodyTemplate bodyTemplate = new BodyTemplate(rawBodyTemplate);

    }

    private CreatureType processRawCreatureType(RawCreatureType rawType) {
        CreatureType type = new CreatureType(rawType);
        type.bodyTemplate = bodyTemplates.get(rawType.bodyTemplate);
        return type;
    }

    public CreatureType getCreatureType(String specimen) {
        return creatureTypes.get(specimen);
    }
}
