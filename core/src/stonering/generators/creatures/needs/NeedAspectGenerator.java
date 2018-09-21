package stonering.generators.creatures.needs;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import stonering.exceptions.DescriptionNotFoundException;
import stonering.objects.local_actors.unit.aspects.NeedsAspect;
import stonering.objects.local_actors.unit.aspects.needs.WearNeed;
import stonering.utils.global.FileLoader;

import java.util.HashMap;

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
        templates = reader.parse(FileLoader.getBodyTemplateFile());
    }

    public NeedsAspect generateNeedAspect(JsonValue creature) {
        try {
            NeedsAspect needsAspect = new NeedsAspect(null);
            String[] needs = new String[0];
            needs = creature.has("needs") ?
                    creature.get("needs").asStringArray() :
                    findTemplate(creature.get("body_template").asString()).get("needs").asStringArray();
            for (String need : needs) {
                switch (need) {
                    case "wear":
                        needsAspect.getNeeds().add(new WearNeed());
                        break;
                }
            }
            return needsAspect;
        } catch (DescriptionNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private JsonValue findTemplate(String template) throws DescriptionNotFoundException {
        for (JsonValue t : templates) {
            if (t.getString("title").equals(template)) return t;
        }
        throw new DescriptionNotFoundException("Body template " + template + "not found");
    }
}
