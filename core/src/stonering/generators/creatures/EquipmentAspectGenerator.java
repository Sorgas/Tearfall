package stonering.generators.creatures;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import stonering.entity.local.unit.aspects.equipment.EquipmentSlot;
import stonering.entity.local.unit.aspects.equipment.GrabEquipmentSlot;
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

    public EquipmentAspect generateEquipmentAspect(JsonValue creature) {
        EquipmentAspect equipmentAspect = null;
        try {
            JsonValue template = findTemplate(creature);
            equipmentAspect = new EquipmentAspect(null);
            generateAspectWithSlots(template, equipmentAspect);
            initDesiredSlots(creature, template, equipmentAspect);
        } catch (DescriptionNotFoundException e) {
            e.printStackTrace();
        }
        return equipmentAspect;
    }

    /**
     * Returns template for creature, or throws an exception.
     *
     * @throws DescriptionNotFoundException
     */
    private JsonValue findTemplate(JsonValue creature) throws DescriptionNotFoundException {
        String template = creature.getString("body_template");
        for (JsonValue t : templates) {
            if (t.getString("title").equals(template)) return t;
        }
        throw new DescriptionNotFoundException("Body template " + template + "not found");
    }

    /**
     * Loops through body parts of creature, generating slots for them.
     */
    private void generateAspectWithSlots(JsonValue template, EquipmentAspect aspect) {
        for (JsonValue bp : template.get("body")) { // read template to map
            EquipmentSlot slot = generateSlotByBodyPart(bp);
            if (slot instanceof GrabEquipmentSlot) {
                aspect.getGrabSlots().put(slot.limbName, (GrabEquipmentSlot) slot);
            }
            aspect.getSlots().put(slot.limbName, slot);
        }
    }

    /**
     * Creates slots and counter for {@link stonering.entity.local.unit.aspects.needs.WearNeed}
     */
    private void initDesiredSlots(JsonValue creature, JsonValue template, EquipmentAspect equipmentAspect) {
        String[] limbs;
        if (creature.has("limbs_to_cover")) {
            limbs = creature.get("limbs_to_cover").asStringArray();
        } else if (template.has("limbs_to_cover")) {
            limbs = creature.get("limbs_to_cover").asStringArray();
        } else return;
        for (String s : limbs) {
            equipmentAspect.getDesiredSlots().add(equipmentAspect.getSlots().get(s));
        }
        equipmentAspect.setEmptyDesiredSlotsCount(equipmentAspect.getDesiredSlots().size());
    }

    /**
     * Creates {@link EquipmentSlot} from template.
     */
    private EquipmentSlot generateSlotByBodyPart(JsonValue partTemplate) {
        EquipmentSlot slot;
        if (Arrays.asList(partTemplate.get("tags").asStringArray()).contains("grab")) {
            slot = new GrabEquipmentSlot(partTemplate.getString("title"), partTemplate.getString("type"));
        } else {
            slot = new EquipmentSlot(partTemplate.getString("title"), partTemplate.getString("type"));
        }
        return slot;
    }
}