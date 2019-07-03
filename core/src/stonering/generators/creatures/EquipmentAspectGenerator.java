package stonering.generators.creatures;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import stonering.entity.local.unit.aspects.equipment.EquipmentSlot;
import stonering.entity.local.unit.aspects.equipment.GrabEquipmentSlot;
import stonering.enums.unit.BodyPart;
import stonering.enums.unit.CreatureType;
import stonering.exceptions.DescriptionNotFoundException;
import stonering.entity.local.unit.aspects.equipment.EquipmentAspect;
import stonering.util.global.FileLoader;

import java.util.Arrays;

/**
 * Generates {@link EquipmentAspect} wi9th slots by json.
 *
 * @author Alexander on 19.09.2018.
 */
public class EquipmentAspectGenerator {
    private Json json;
    private JsonReader reader;
    private JsonValue templates;

    public EquipmentAspectGenerator() {
        reader = new JsonReader();
        json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        templates = reader.parse(FileLoader.getFile(FileLoader.BODY_TEMPLATE_PATH));
    }

    public EquipmentAspect generateEquipmentAspect(CreatureType type) {
        EquipmentAspect equipmentAspect = null;
        equipmentAspect = new EquipmentAspect(null);
        generateAspectWithSlots(type, equipmentAspect);
        initDesiredSlots(type, equipmentAspect);
        return equipmentAspect;
    }

    /**
     * Loops through body parts of creature, generating slots for them.
     */
    private void generateAspectWithSlots(CreatureType type, EquipmentAspect aspect) {
        for (BodyPart part : type.bodyTemplate.body.values()) {
            EquipmentSlot slot = generateSlotByBodyPart(part);
            if (slot instanceof GrabEquipmentSlot) {
                aspect.getGrabSlots().put(slot.limbName, (GrabEquipmentSlot) slot);
            }
            aspect.getSlots().put(slot.limbName, slot);
        }
    }

    /**
     * Creates slots and counter for {@link stonering.entity.local.unit.aspects.needs.WearNeed}
     */
    private void initDesiredSlots(CreatureType type, EquipmentAspect equipmentAspect) {
        for (String limbName : type.limbsToCover) {
            equipmentAspect.getDesiredSlots().add(equipmentAspect.getSlots().get(limbName));
        }
        equipmentAspect.setEmptyDesiredSlotsCount(equipmentAspect.getDesiredSlots().size());
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