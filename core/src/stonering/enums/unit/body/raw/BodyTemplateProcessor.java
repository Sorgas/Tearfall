package stonering.enums.unit.body.raw;

import stonering.enums.unit.body.BodyPart;
import stonering.enums.unit.body.BodyTemplate;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Processes {@link RawBodyTemplate} into {@link BodyTemplate}.
 *
 * @author Alexander on 02.07.2019.
 */
public class BodyTemplateProcessor {
    private static final String LEFT_PREFIX = "left ";
    private static final String RIGHT_PREFIX = "right ";

    public BodyTemplate process(RawBodyTemplate rawTemplate) {
        BodyTemplate template = new BodyTemplate(rawTemplate);
        Map<String, RawBodyPart> rawPartMap = rawTemplate.body.stream().collect(Collectors.toMap(part -> part.type, part -> part, (a, b) -> b)); // part name to part
        updateMirroringFlags(rawPartMap);
        rawTemplate.slots = mirrorSlots(rawTemplate, rawPartMap);
        rawTemplate.slots.forEach(slot -> template.slots.put(slot.get(0), slot.subList(1, slot.size())));
        rawPartMap = doubleMirroredParts(rawPartMap);
        fillBodyParts(rawPartMap, template);
        return template;
    }

    /**
     * Mirrors slots which use only mirrored parts. Mirrors only limbs in a slot, if there are non-mirrored limbs in that slot.
     * Also mirrors desired slots.
     */
    private List<List<String>> mirrorSlots(RawBodyTemplate rawTemplate, Map<String, RawBodyPart> rawPartMap) {
        List<List<String>> newSlots = new ArrayList<>();
        for (List<String> slot : rawTemplate.slots) {
            String slotName = slot.get(0);
            List<String> slotLimbs = slot.subList(1, slot.size());
            if (containsOnlyMirroredLimbs(slotLimbs, rawPartMap)) { // create two slots (names are prefixed)
                newSlots.add(slot.stream().map(s -> LEFT_PREFIX + s).collect(Collectors.toList())); // left copy of a slot
                newSlots.add(slot.stream().map(s -> RIGHT_PREFIX + s).collect(Collectors.toList())); // right copy of a slot
                if (rawTemplate.desiredSlots.contains(slotName)) {
                    rawTemplate.desiredSlots.remove(slotName);
                    rawTemplate.desiredSlots.add(LEFT_PREFIX + slotName);
                    rawTemplate.desiredSlots.add(RIGHT_PREFIX + slotName);
                }
            } else { // some limbs are single, so mirrored limbs are duplicated in same slot
                List<String> newSlot = slotLimbs.stream() // copy some limbs with prefixes
                        .flatMap(s -> rawPartMap.get(s).mirrored ? Stream.of(LEFT_PREFIX + s, RIGHT_PREFIX + s) : Stream.of(s))
                        .collect(Collectors.toList());
                newSlot.add(0, slotName);
                newSlots.add(newSlot);
            }
        }
        return newSlots;
    }

    /**
     * Multiplies limbs if they are mirrored.
     */
    private Map<String, RawBodyPart> doubleMirroredParts(Map<String, RawBodyPart> map) {
        Map<String, RawBodyPart> newMap = new HashMap<>();
        // double mirrored parts
        for (RawBodyPart bodyPart : map.values()) {
            if (bodyPart.mirrored) {
                RawBodyPart leftPart = new RawBodyPart(bodyPart); // copy parts
                RawBodyPart rightPart = new RawBodyPart(bodyPart);
                leftPart.name = LEFT_PREFIX + leftPart.name; // update name
                rightPart.name = RIGHT_PREFIX + rightPart.name;
                if (map.get(bodyPart.root).mirrored) { // root is mirrored
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

    private boolean containsOnlyMirroredLimbs(List<String> slotLimbs, Map<String, RawBodyPart> rawPartMap) {
        return slotLimbs.stream().allMatch(limb -> rawPartMap.get(limb).mirrored);
    }

    /**
     * Observes limbs tree and copies mirroring flags from limbs to their children.
     * There should be only one flag on the path from root to leaf limb.
     */
    private void updateMirroringFlags(Map<String, RawBodyPart> map) {
        for (RawBodyPart currentLimb : map.values()) {
            if (currentLimb.mirrored) continue; // limb already have mirroring
            RawBodyPart limb = currentLimb;
            while (!limb.root.equals("body")) { // to the root limb
                if (map.get(limb.root).mirrored) { // limb with mirroring found
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
