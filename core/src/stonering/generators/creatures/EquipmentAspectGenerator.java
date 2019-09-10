package stonering.generators.creatures;

import stonering.entity.unit.aspects.equipment.EquipmentSlot;
import stonering.entity.unit.aspects.equipment.GrabEquipmentSlot;
import stonering.enums.unit.CreatureType;
import stonering.entity.unit.aspects.equipment.EquipmentAspect;
import stonering.enums.unit.body.BodyTemplate;

import java.util.List;
import java.util.Map;

/**
 * Generates {@link EquipmentAspect} with slots by json.
 * Slots are provided by {@link BodyTemplate}.
 *
 * @author Alexander on 19.09.2018.
 */
public class EquipmentAspectGenerator {

    public EquipmentAspectGenerator() {}

    public EquipmentAspect generateEquipmentAspect(CreatureType type) {
        EquipmentAspect equipmentAspect = new EquipmentAspect(null);
        generateSlots(type, equipmentAspect);
        initDesiredSlots(type, equipmentAspect);
        return equipmentAspect;
    }

    /**
     * Loops through body parts of creature, generating slots for them.
     */
    private void generateSlots(CreatureType type, EquipmentAspect aspect) {
        Map<String, List<String>> slotLimbs = type.bodyTemplate.slots;
        for (String name : slotLimbs.keySet()) {
            System.out.println(name + " " + type);
            EquipmentSlot slot = isGrabSlot(name, type)
                    ? new GrabEquipmentSlot(name, slotLimbs.get(name))
                    : new EquipmentSlot(name, slotLimbs.get(name));
            if (slot instanceof GrabEquipmentSlot) {
                aspect.grabSlots.put(name, (GrabEquipmentSlot) slot);
            }
            aspect.slots.put(name, slot);
        }
    }

    private boolean isGrabSlot(String slotName, CreatureType type) {
        return type.bodyTemplate.slots.get(slotName).stream().anyMatch(s -> type.bodyTemplate.body.get(s).tags.contains("grab"));
    }

    /**
     * Creates slots and counter for {@link stonering.entity.unit.aspects.needs.WearNeed}
     */
    private void initDesiredSlots(CreatureType type, EquipmentAspect equipmentAspect) {
        for (String limbType : type.limbsToCover) {
            type.bodyTemplate.body.values().stream().
                    filter(part -> part.name.equals(limbType)).
                    forEach(part -> equipmentAspect.desiredSlots.add(equipmentAspect.slots.get(part.name)));
        }
    }
}