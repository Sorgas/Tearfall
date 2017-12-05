package stonering.generators.creatures;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import stonering.objects.aspects.BodyAspect;
import stonering.objects.creatures.BodyPart;
import stonering.utils.global.FileLoader;

import java.util.ArrayList;

/**
 * Created by Alexander on 19.10.2017.
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

    public BodyAspect generateBody(JsonValue creature) {
        JsonValue template = findTemplate(creature.getString("body_template"));
        BodyAspect bodyAspect = generateBodyAspectFromTemplate(template);
        return bodyAspect;
    }

    private JsonValue findTemplate(String template) {
        for (JsonValue t: templates) {
            if(t.getString("title").equals(template)) return t;
        }
        return null;
    }

    private BodyAspect generateBodyAspectFromTemplate(JsonValue template) {
        BodyAspect bodyAspect = new BodyAspect();
        for (JsonValue bp: template.get("body")) {
            bodyAspect.addBodyPart(generateBodyPart(bp,template));
        }
        return bodyAspect;
    }

    private BodyPart generateBodyPart(JsonValue partTemplate, JsonValue template) {
        BodyPart bodyPart1 = new BodyPart(partTemplate.getString("title"));
        String[] layers = template.get("default_layers").asStringArray();
        if(partTemplate.get("layers") != null) {
            layers = partTemplate.get("layers").asStringArray();
        }
        bodyPart1.setLayers(layers);
        return bodyPart1;
    }
}