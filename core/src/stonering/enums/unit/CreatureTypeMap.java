package stonering.enums.unit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.badlogic.gdx.utils.Json;

import stonering.enums.unit.body.BodyTemplate;
import stonering.enums.unit.body.raw.BodyTemplateProcessor;
import stonering.enums.unit.body.raw.RawBodyTemplate;
import stonering.enums.unit.race.CreatureType;
import stonering.enums.unit.race.RawCreatureType;
import stonering.util.lang.FileUtil;
import stonering.util.logging.Logger;

/**
 * Loads, and stores {@link CreatureType} and {@link BodyTemplate}.
 *
 * @author Alexander on 29.05.2019.
 */
public class CreatureTypeMap {
    private static CreatureTypeMap instance;
    public final Map<String, CreatureType> creatureTypes = new HashMap<>();
    public final Map<String, BodyTemplate> bodyTemplates = new HashMap<>();

    private CreatureTypeMap() {
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
        BodyTemplateProcessor templateProcessor = new BodyTemplateProcessor();
        ArrayList<RawBodyTemplate> templates = json.fromJson(ArrayList.class, RawBodyTemplate.class, FileUtil.get(FileUtil.BODY_TEMPLATE_PATH));
        templates.stream()
                .map(templateProcessor::process)
                .forEach(template -> bodyTemplates.put(template.name, template));
    }

    private void loadCreatures() {
        Logger.LOADING.logDebug("loading creature types");
        Json json = new Json();
        CreatureTypeProcessor typeProcessor = new CreatureTypeProcessor(this);
        ArrayList<RawCreatureType> types = json.fromJson(ArrayList.class, RawCreatureType.class, FileUtil.get(FileUtil.CREATURES_PATH));
        types.stream()
                .map(typeProcessor::process)
                .filter(Objects::nonNull)
                .forEach(type -> creatureTypes.put(type.name, type));
    }
    
    public static CreatureType getType(String specimen) {
        return instance().creatureTypes.get(specimen);
    }
}
