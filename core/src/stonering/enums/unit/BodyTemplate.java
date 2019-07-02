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
    private HashMap<String, BodyPart> bodyParts; // name to bodyPart

    public BodyTemplate(String name, List<String> defaultLayers, List<String> needs) {
        this.name = name;
        this.defaultLayers = defaultLayers;
        this.needs = needs;
        body
    }
}
