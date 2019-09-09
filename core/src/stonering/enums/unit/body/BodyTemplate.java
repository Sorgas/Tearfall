package stonering.enums.unit.body;

import stonering.enums.unit.CreatureTypeMap;
import stonering.enums.unit.body.raw.RawBodyTemplate;

import java.util.HashMap;
import java.util.List;

/**
 * Represents body structure of a creature. Store in {@link CreatureTypeMap}.
 *
 * @author Alexander on 29.05.2019.
 */
public class BodyTemplate {
    public String name;
    public List<String> defaultLayers;
    public List<String> needs;
    public List<String> limbsToCover;
    public HashMap<String, BodyPart> body; // name to bodyPart
    public HashMap<String, List<String>> slots; // slot name to default limbs

    public BodyTemplate(RawBodyTemplate rawBodyTemplate) {
        name = rawBodyTemplate.name;
        defaultLayers = rawBodyTemplate.defaultLayers;
        needs = rawBodyTemplate.needs;
        limbsToCover = rawBodyTemplate.desiredSlots;
        body = new HashMap<>();
        slots = new HashMap<>();
    }
}
