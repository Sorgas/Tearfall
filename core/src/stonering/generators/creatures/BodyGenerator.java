package stonering.generators.creatures;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import stonering.objects.local_actors.unit.Unit;
import stonering.objects.local_actors.unit.aspects.BodyAspect;
import stonering.utils.global.FileLoader;

/**
 * Generates {@link BodyAspect} for creature by gives species.
 *
 * @author Alexander Kuzyakov on 19.10.2017.
 */
public class BodyGenerator {
    private Json json;
    private JsonReader reader;
    private JsonValue templates;


    public BodyGenerator() {
        init();
    }

    private void init() {
        reader = new JsonReader();
        json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        templates = reader.parse(FileLoader.getBodyTemplateFile());
    }

    public BodyAspect generateBody(JsonValue creature, Unit unit) {
        JsonValue template = findTemplate(creature.getString("body_template"));
        BodyAspect bodyAspect = generateBodyAspectFromTemplate(template, unit);
        return bodyAspect;
    }

    private JsonValue findTemplate(String template) {
        for (JsonValue t : templates) {
            if (t.getString("title").equals(template)) return t;
        }
        return null;
    }

    private BodyAspect generateBodyAspectFromTemplate(JsonValue template, Unit unit) {
        BodyAspect bodyAspect = new BodyAspect(unit);
        for (JsonValue bp : template.get("body")) {
            bodyAspect.addBodyPart(generateBodyPart(bp, template, bodyAspect));
        }
        return bodyAspect;
    }

    private BodyAspect.BodyPart generateBodyPart(JsonValue partTemplate, JsonValue template, BodyAspect bodyAspect) {
        BodyAspect.BodyPart bodyPart = bodyAspect.new BodyPart(partTemplate.getString("title"));
        String[] layers = template.get("default_layers").asStringArray();
        if (partTemplate.get("layers") != null) {
            layers = partTemplate.get("layers").asStringArray();
        }
        bodyPart.layers = layers;
        return bodyPart;
    }
}
