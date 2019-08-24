package stonering.generators.creatures.needs;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import stonering.entity.unit.aspects.needs.NeedEnum;
import stonering.enums.unit.CreatureType;
import stonering.entity.unit.aspects.NeedAspect;
import stonering.util.global.FileLoader;
import stonering.util.global.Logger;

/**
 * Generates needs for creatures.
 *
 * @author Alexander on 21.09.2018.
 */
public class NeedAspectGenerator {
    private Json json;
    private JsonReader reader;
    private JsonValue templates;

    public NeedAspectGenerator() {
        init();
    }

    private void init() {
        reader = new JsonReader();
        json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        templates = reader.parse(FileLoader.getFile(FileLoader.BODY_TEMPLATE_PATH));
    }

    public NeedAspect generateNeedAspect(CreatureType type) {
        NeedAspect needAspect = new NeedAspect(null);
        for (String need : type.bodyTemplate.needs) {
            if(!NeedEnum.map.containsKey(need)) {
                Logger.GENERATION.logError("Creature type " + type + " has invalid need " + need);
            }
            switch (need) {
                case "wear": //TODO make enum of aspects
                    needAspect.needs.add(NeedEnum.WEAR);
                    break;

            }
        }
        return needAspect;
    }
}
