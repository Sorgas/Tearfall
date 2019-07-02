package stonering.enums.unit;

import stonering.entity.local.unit.aspects.body.BodyPart;

import java.util.HashMap;
import java.util.List;

/**
 * Represents body structure of a creature.
 *
 * @author Alexander on 29.05.2019.
 */
public class BodyTemplate {
    public String name;
    public List<String> defaultLayers;
    public List<String> needs;
    public HashMap<String, BodyPart> body; // name to bodyPart

    public BodyTemplate(RawBodyTemplate rawBodyTemplate) {
        name = rawBodyTemplate.name;
        defaultLayers = rawBodyTemplate.defaultLayers;
        needs = rawBodyTemplate.needs;
        body = new HashMap<>();
    }
}
