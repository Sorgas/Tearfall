package stonering.enums.unit.body.raw;

import stonering.enums.unit.body.BodyPart;
import stonering.enums.unit.body.BodyTemplate;
import stonering.util.logging.Logger;

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
    private boolean debugMode = false;

    public BodyTemplate process(RawBodyTemplate rawTemplate) {
        BodyTemplate template = new BodyTemplate(rawTemplate);
        log("processing " + rawTemplate.name + " body template");
        Map<String, RawBodyPart> rawPartMap = rawTemplate.body
                .stream().collect(Collectors.toMap(part -> part.name, part -> part)); // part name to part
        log("    rawPartMap " + rawPartMap.size() + " " + rawPartMap.keySet());
        updateLimbsMirroringFlags(rawPartMap);
        log("    unmirrored slots " + rawTemplate.slots.size());
        mirrorSlots(rawTemplate, rawPartMap);
        rawTemplate.slots.forEach(slot -> template.slots.put(slot.get(0), slot.subList(1, slot.size()))); // copy slots to new template
        log("    mirrored slots " + template.slots.size() + " " + template.slots.keySet());
        doubleMirroredParts(rawPartMap);
        log("    doubled parts " + rawPartMap.size() + " " + rawPartMap.keySet());
        fillBodyParts(rawPartMap, template);
        return template;
    }

    /**
     * Mirrors slots which use only mirrored parts (boots). 
     * Mirrors only limbs in a slot, if there are non-mirrored limbs in that slot (pants).
     * Side prefixes are added in this method. After mirroring limbs, limbs and slots become consistent.
     * Also mirrors desired slots.
     */
    private void mirrorSlots(RawBodyTemplate rawTemplate, Map<String, RawBodyPart> rawPartMap) {
        List<List<String>> newSlots = new ArrayList<>();
        for (List<String> slot : rawTemplate.slots) {
            String slotName = slot.get(0);
            List<String> slotLimbs = slot.subList(1, slot.size());
            if (containsOnlyMirroredLimbs(slotLimbs, rawPartMap)) { // create two slots (names are prefixed)
                log("        slot " + slotName + " gets mirroring");
                newSlots.add(slot.stream().map(s -> LEFT_PREFIX + s).collect(Collectors.toList())); // left copy of a slot
                newSlots.add(slot.stream().map(s -> RIGHT_PREFIX + s).collect(Collectors.toList())); // right copy of a slot
                if (rawTemplate.desiredSlots.contains(slotName)) {
                    rawTemplate.desiredSlots.remove(slotName);
                    rawTemplate.desiredSlots.add(LEFT_PREFIX + slotName);
                    rawTemplate.desiredSlots.add(RIGHT_PREFIX + slotName);
                }
            } else { // some limbs are single, so mirrored limbs are duplicated in same slot
                int notMirroredLimbsSize = slotLimbs.size();
                List<String> newSlotLimbs = slotLimbs.stream() // copy some limbs with prefixes
                        .flatMap(s -> rawPartMap.get(s).mirrored ? Stream.of(LEFT_PREFIX + s, RIGHT_PREFIX + s) : Stream.of(s))
                        .collect(Collectors.toList());
                if(newSlotLimbs.size() > notMirroredLimbsSize) log("        " + (newSlotLimbs.size() - notMirroredLimbsSize) + " limb(s) got mirrored in slot " + slotName);
                newSlotLimbs.add(0, slotName);
                newSlots.add(newSlotLimbs);
            }
        }
        rawTemplate.slots = newSlots;
    }

    /**
     * Multiplies limbs if they are mirrored.
     */
    private void doubleMirroredParts(Map<String, RawBodyPart> map) {
        // double mirrored parts
        Map<String, RawBodyPart> newMap = new HashMap<>();
        for (RawBodyPart part : map.values()) {
            if (part.mirrored) {
                RawBodyPart leftPart = (RawBodyPart) part.clone(); // copy parts
                RawBodyPart rightPart = (RawBodyPart) part.clone();
                leftPart.name = LEFT_PREFIX + leftPart.name; // update name
                rightPart.name = RIGHT_PREFIX + rightPart.name;
                if (map.get(part.root).mirrored) { // root is mirrored
                    leftPart.root = LEFT_PREFIX + leftPart.root; // update root links
                    rightPart.root = RIGHT_PREFIX + rightPart.root;
                }
                newMap.put(leftPart.name, leftPart);
                newMap.put(rightPart.name, rightPart);
            } else {
                newMap.put(part.name, part);
            }
        }
        map.clear();
        map.putAll(newMap);
    }

    private boolean containsOnlyMirroredLimbs(List<String> slotLimbs, Map<String, RawBodyPart> rawPartMap) {
        return slotLimbs.stream().allMatch(limb -> rawPartMap.get(limb).mirrored);
    }

    /**
     * Observes limbs tree and copies mirroring flags from limbs to their children.
     * There should be only one flag on the path from root to leaf limb.
     */
    private void updateLimbsMirroringFlags(Map<String, RawBodyPart> map) {
        map.values().stream()
                .filter(rawBodyPart -> !rawBodyPart.mirrored)
                .filter(rawBodyPart -> {
                    RawBodyPart limb = rawBodyPart;
                    while (!limb.root.equals("body")) { // cycle to the root limb
                        if (map.get(limb.root).mirrored) return true;
                        limb = map.get(limb.root); // go to next limb
                    }
                    return false;
                })
                .forEach(rawBodyPart -> rawBodyPart.mirrored = true);
    }

    /**
     * Creates body parts and links the between each other.
     */
    private void fillBodyParts(Map<String, RawBodyPart> rawPartsMap, BodyTemplate bodyTemplate) {
        rawPartsMap.values() // create limbs
                .forEach(rawPart -> bodyTemplate.body.put(rawPart.name, new BodyPart(rawPart)));
        bodyTemplate.body.values() // link limbs
                .forEach(part -> part.root = bodyTemplate.body.get(rawPartsMap.get(part.name).root));
    }

    private void log(String message) {
        if(debugMode) Logger.LOADING.logDebug(message);
    }
}
