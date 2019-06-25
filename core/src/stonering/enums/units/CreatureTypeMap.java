package stonering.enums.units;

import com.badlogic.gdx.utils.Json;
import stonering.util.global.FileLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
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
        ArrayList<BodyTemplate> types = json.fromJson(ArrayList.class, BodyTemplate.class, FileLoader.CREATURES_PATH);
        for (BodyTemplate type : types) {
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

    private CreatureType processRawCreatureType(RawCreatureType rawType) {
        CreatureType type = new CreatureType(rawType);
        type.bodyTemplate = bodyTemplates.get(rawType.bodyTemplate);
        return type;
    }
}
