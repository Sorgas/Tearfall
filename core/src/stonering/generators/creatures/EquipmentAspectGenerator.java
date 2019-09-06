package stonering.generators.creatures;

import stonering.entity.unit.aspects.equipment.EquipmentSlot;
import stonering.entity.unit.aspects.equipment.GrabEquipmentSlot;
import stonering.enums.unit.body.BodyPart;
import stonering.enums.unit.CreatureType;
import stonering.entity.unit.aspects.equipment.EquipmentAspect;
import stonering.enums.unit.body.BodyTemplate;

import java.util.List;

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
        for (String slotName : type.bodyTemplate.slots.keySet()) {
            EquipmentSlot slot = generateSlot(slotName, type.bodyTemplate.slots.get(slotName), type);
        }
        for (BodyPart part : type.bodyTemplate.body.values()) {
            EquipmentSlot slot = generateSlotByBodyPart(part);
            if (slot instanceof GrabEquipmentSlot) {
                aspect.grabSlots.put(slot.limbName, (GrabEquipmentSlot) slot);
            }
            aspect.slots.put(slot.limbName, slot);
        }
    }

    private EquipmentSlot generateSlot(String slotName, List<String> limbs, CreatureType type) {
        if(limbs.stream().anyMatch(s -> type.bodyTemplate.body.get(s).tags.contains("grab"))) {
            return new GrabEquipmentSlot();
        } else {
            return new EquipmentSlot();
        }
    }

    /**
     * Creates slots and counter for {@link stonering.entity.unit.aspects.needs.WearNeed}
     */
    private void initDesiredSlots(CreatureType type, EquipmentAspect equipmentAspect) {
        for (String limbType : type.limbsToCover) {
            type.bodyTemplate.body.values().stream().
                    filter(part -> part.type.equals(limbType)).
                    forEach(part -> equipmentAspect.desiredSlots.add(equipmentAspect.slots.get(part.name)));
        }
    }

    /**
     * Creates {@link EquipmentSlot} for bodyparts from template.
     */
    private EquipmentSlot generateSlotByBodyPart(BodyPart part) {
        if (part.tags.contains("grab")) {
            return new GrabEquipmentSlot(part.name, part.type);
        }
        return new EquipmentSlot(part.name, part.type);
    }
}