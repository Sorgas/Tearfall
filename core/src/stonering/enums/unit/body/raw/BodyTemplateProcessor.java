package stonering.enums.unit.body.raw;

import stonering.enums.unit.body.BodyPart;
import stonering.enums.unit.body.BodyTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Processes {@link RawBodyTemplate} into {@link BodyTemplate}.
 *
 * @author Alexander on 02.07.2019.
 */
public class BodyTemplateProcessor {
    private static final String LEFT_PREFIX = "left ";
    private static final String RIGHT_PREFIX = "right ";

    public BodyTemplate process(RawBodyTemplate rawBodyTemplate) {
        BodyTemplate bodyTemplate = new BodyTemplate(rawBodyTemplate);
        Map<String, RawBodyPart> rawPartMap = rawBodyTemplate.body.stream().collect(Collectors.toMap(part -> part.type, part -> part, (a, b) -> b));
        updateMirroringFlags(rawPartMap);
        rawBodyTemplate.slots = mirrorSlots(rawBodyTemplate, rawPartMap);
        fillSlots(rawBodyTemplate, bodyTemplate);
        rawPartMap = doubleMirroredParts(rawPartMap);
        fillBodyParts(rawPartMap, bodyTemplate);
        return bodyTemplate;
    }

    /**
     * Multiplies limbs if they are mirrored.
     */
    private Map<String, RawBodyPart> doubleMirroredParts(Map<String, RawBodyPart> map) {
        Map<String, RawBodyPart> newMap = new HashMap<>();
        // double mirrored parts
        for (RawBodyPart bodyPart : map.values()) {
            if (bodyPart.mirrored != null) {
                RawBodyPart leftPart = new RawBodyPart(bodyPart); // copy parts
                RawBodyPart rightPart = new RawBodyPart(bodyPart);
                leftPart.name = LEFT_PREFIX + leftPart.name; // update name
                rightPart.name = RIGHT_PREFIX + rightPart.name;
                if (map.get(bodyPart.root).mirrored != null) { // root is mirrored
                    leftPart.root = LEFT_PREFIX + leftPart.root; // update root
                    rightPart.root = RIGHT_PREFIX + rightPart.root;
                }
                newMap.put(leftPart.name, leftPart);
                newMap.put(rightPart.name, rightPart);
            } else {
                newMap.put(bodyPart.name, bodyPart);
            }
        }
        return newMap;
    }


    private List<List<String>> mirrorSlots(RawBodyTemplate rawTemplate, Map<String, RawBodyPart> rawPartMap) {
        List<List<String>> newSlots = new ArrayList<>();
        for (List<String> slot : rawTemplate.slots) {
            if(containsOnlyMirroriedLibms(slot.subList(1, slot.size()), rawPartMap)) { // create two slots (names are prefixed)
                List<String> leftSlot = new ArrayList<>();
                slot.forEach(s -> leftSlot.add(LEFT_PREFIX + s));
                newSlots.add(leftSlot);
                List<String> rightSlot = new ArrayList<>();
                slot.forEach(s -> rightSlot.add(RIGHT_PREFIX + s));
                newSlots.add(rightSlot);
            } else { // some limbs are single, so 1 slot remains, mirrored limbs duplicated in slot
                List<String> newSlot = new ArrayList<>();
                newSlot.add(slot.get(0));
                for (String slotLimb : slot.subList(1, slot.size())) {
                    if(rawPartMap.get(slotLimb).mirrored != null) {
                        newSlot.add(LEFT_PREFIX + slotLimb); // mirror limbs which will be mirrored in template
                        newSlot.add(RIGHT_PREFIX + slotLimb);
                    } else {
                        newSlot.add(slotLimb);
                    }
                }
                newSlots.add(newSlot);
            }
        }
        return newSlots;
    }

    private boolean containsOnlyMirroriedLibms(List<String> slotLimbs, Map<String, RawBodyPart> rawPartMap) {
        return slotLimbs.stream().anyMatch(limb -> rawPartMap.get(limb).mirrored == null);
    }

    private void fillSlots(RawBodyTemplate rawTemplate, BodyTemplate template) {
        for (List<String> slot : rawTemplate.slots) {
            template.slots.put(slot.get(0), slot.subList(1, slot.size()));
        }
    }

    /**
     * Observes limbs tree and copies mirroring flags from limbs to their children.
     * There should be only one flag on the path from root to leaf limb.
     */
    private void updateMirroringFlags(Map<String, RawBodyPart> map) {
        for (RawBodyPart currentLimb : map.values()) {
            if (currentLimb.mirrored != null) continue; // limb already have mirroring
            RawBodyPart limb = currentLimb;

            while (!limb.root.equals("body")) { // to the root limb
                if (map.get(limb.root).mirrored != null) { // limb with mirroring found
                    currentLimb.mirrored = map.get(limb.root).mirrored; // copy flag
                    break;
                }
                limb = map.get(limb.root); // go to next limb
            }
        }
    }

    /**
     * Creates body parts and links the between each other.
     */
    private void fillBodyParts(Map<String, RawBodyPart> rawPartsMap, BodyTemplate bodyTemplate) {
        for (RawBodyPart rawPart : rawPartsMap.values()) { // create limbs
            bodyTemplate.body.put(rawPart.name, new BodyPart(rawPart));
        }
        for (BodyPart part : bodyTemplate.body.values()) { // link limbs
            part.root = bodyTemplate.body.get(rawPartsMap.get(part.name).root);
        }
    }
}
