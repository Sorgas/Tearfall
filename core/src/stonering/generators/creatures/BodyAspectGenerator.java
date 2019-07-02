package stonering.generators.creatures;

import com.badlogic.gdx.utils.JsonValue;
import stonering.enums.unit.BodyTemplate;
import stonering.enums.unit.CreatureType;
import stonering.enums.unit.CreatureTypeMap;
import stonering.exceptions.DescriptionNotFoundException;
import stonering.exceptions.FaultDescriptionException;
import stonering.entity.local.unit.aspects.body.BodyAspect;

import java.util.Arrays;
import java.util.HashMap;

/**
 * Generates {@link BodyAspect} for creature by given species.
 *
 * @author Alexander Kuzyakov on 19.10.2017.
 */
public class BodyAspectGenerator {

    public BodyAspect generateBodyAspect(CreatureType type) {
        BodyAspect aspect = new BodyAspect(null);
        aspect.setBodyPartsToCover();

        try {
            BodyAspect bodyAspect = generateBodyAspectFromTemplate(template);
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

    private BodyAspect generateBodyAspectFromTemplate(String specimen)  {
        BodyTemplate template = CreatureTypeMap.instance().getTemplate(specimen);
        BodyAspect bodyAspect = new BodyAspect(null);
        bodyAspect.
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


}
