package stonering.enums.unit.raw;

import stonering.enums.unit.BodyPart;
import stonering.enums.unit.BodyTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * Processes {@link RawBodyTemplate} into {@link BodyTemplate}.
 *
 * @author Alexander on 02.07.2019.
 */
public class BodyTemplateProcessor {
    private static final String LEFT_PREFIX = " left";
    private static final String RIGHT_PREFIX = " right";

    public BodyTemplate process(RawBodyTemplate rawBodyTemplate) {
        BodyTemplate bodyTemplate = new BodyTemplate(rawBodyTemplate);
        Map<String, RawBodyPart> rawPartMap = collectPartsToMap(rawBodyTemplate);
        rawPartMap = doubleMirroredParts(rawPartMap);
        fillBodyParts(rawPartMap, bodyTemplate);

        return bodyTemplate;
    }

    /**
     * Creates map of body part name mapped to raw parts.
     */
    private Map<String, RawBodyPart> collectPartsToMap(RawBodyTemplate rawBodyTemplate) {
        Map<String, RawBodyPart> map = new HashMap<>();
        for (RawBodyPart part : rawBodyTemplate.body) {
            map.put(part.name, part);
        }
        return map;
    }

    /**
     * Checks if any parent of body part is mirrored, and doubles this body part if so.
     */
    private Map<String, RawBodyPart> doubleMirroredParts(Map<String, RawBodyPart> map) {
        Map<String, RawBodyPart> newMap = new HashMap<>();
        // go up to the root and update flags
        for (RawBodyPart value : map.values()) {
            RawBodyPart part = value;
            while (!part.root.equals("body")) {
                if (map.get(part.root).mirrored) value.mirrored = true;
                part = map.get(part.root);
            }
        }
        // double mirrored parts
        for (RawBodyPart value : map.values()) {
            if (value.mirrored) {
                RawBodyPart leftPart = new RawBodyPart(value);
                RawBodyPart rightPart = new RawBodyPart(value);
                leftPart.name = LEFT_PREFIX + leftPart.name;
                rightPart.name = RIGHT_PREFIX + rightPart.name;
                if (map.get(value.root).mirrored) {
                    leftPart.root = LEFT_PREFIX + leftPart.root;
                    rightPart.root = RIGHT_PREFIX + rightPart.root;
                }
                newMap.put(leftPart.name, leftPart);
                newMap.put(rightPart.name, rightPart);
            } else {
                newMap.put(value.name, value);
            }
        }
        return newMap;
    }

    /**
     * Creates body parts and links the between each other.
     */
    private void fillBodyParts(Map<String, RawBodyPart> rawPartsMap, BodyTemplate bodyTemplate) {
        for (RawBodyPart value : rawPartsMap.values()) {
            bodyTemplate.body.put(value.name, new BodyPart(value));
        }
        for (BodyPart part : bodyTemplate.body.values()) {
            part.root = bodyTemplate.body.get(rawPartsMap.get(part.name).root);
        }
    }
}
