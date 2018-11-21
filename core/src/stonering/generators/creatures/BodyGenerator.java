package stonering.generators.creatures;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import stonering.exceptions.DescriptionNotFoundException;
import stonering.exceptions.FaultDescriptionException;
import stonering.entity.local.unit.Unit;
import stonering.entity.local.unit.aspects.BodyAspect;
import stonering.utils.global.FileLoader;

import java.util.Arrays;
import java.util.HashMap;

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
        templates = reader.parse(FileLoader.getFile(FileLoader.BODY_TEMPLATE_PATH));
    }

    public BodyAspect generateBody(JsonValue creature, Unit unit) {
        try {
            JsonValue template = findTemplate(creature.getString("body_template"));
            BodyAspect bodyAspect = generateBodyAspectFromTemplate(template, unit);
            return bodyAspect;
        } catch (DescriptionNotFoundException | FaultDescriptionException e) {
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

    private BodyAspect generateBodyAspectFromTemplate(JsonValue template, Unit unit) throws FaultDescriptionException {
        BodyAspect bodyAspect = new BodyAspect(unit);
        HashMap<String, BodyAspect.BodyPart> bodyParts = new HashMap<>();
        for (JsonValue bp : template.get("body")) { // read template to map
            BodyAspect.BodyPart bodyPart = generateBodyPart(bp, template, bodyAspect);
            bodyParts.put(bodyPart.title, bodyPart);
        }
        bindBodyParts(bodyParts); // bind limbs to each other
        bodyAspect.setBodyParts(bodyParts); // fill aspect with limbs
        bodyAspect.getBodyPartsToCover().addAll(Arrays.asList(template.get("limbs_to_cover").asStringArray()));
        return bodyAspect;
    }

    /**
     * Ccreates {@link BodyAspect.BodyPart} from template.
     *
     * @param partTemplate
     * @param template
     * @param bodyAspect
     * @return
     */
    private BodyAspect.BodyPart generateBodyPart(JsonValue partTemplate, JsonValue template, BodyAspect bodyAspect) {
        BodyAspect.BodyPart bodyPart = bodyAspect.new BodyPart(partTemplate.getString("title"));
        bodyPart.tags = partTemplate.get("tags").asStringArray();
        bodyPart.rootName = partTemplate.get("root").asString();
        bodyPart.type = partTemplate.get("type").asString();
        String[] layers = template.get("default_layers").asStringArray();
        if (partTemplate.get("layers") != null) {
            layers = partTemplate.get("layers").asStringArray();
        }
        bodyPart.layers = layers;
        return bodyPart;
    }

    /**
     * Links body parts to their base ones, e.g. hands to lower arms, heads to necks etc.
     *
     * @param bodyParts
     * @throws FaultDescriptionException if "root" in body part description is wrong.
     */
    private void bindBodyParts(HashMap<String, BodyAspect.BodyPart> bodyParts) throws FaultDescriptionException {
        for (BodyAspect.BodyPart bodyPart : bodyParts.values()) {
            if (bodyPart.rootName != null && !bodyPart.rootName.equals("body")) {
                if (bodyParts.containsKey(bodyPart.rootName)) {
                    bodyPart.root = bodyParts.get(bodyPart.rootName);
                } else {
                    throw new FaultDescriptionException("Body part " + bodyPart.title + " points to unknown body part " + bodyPart.rootName);
                }
            }
        }
    }
}
