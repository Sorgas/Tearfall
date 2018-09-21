package stonering.generators.creatures;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import stonering.exceptions.DescriptionNotFoundException;
import stonering.exceptions.FaultDescriptionException;
import stonering.objects.local_actors.unit.aspects.EquipmentAspect;
import stonering.utils.global.FileLoader;

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
        init();
    }

    private void init() {
        reader = new JsonReader();
        json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        templates = reader.parse(FileLoader.getBodyTemplateFile());
    }

    public EquipmentAspect generateEquipmentAspect(JsonValue creature) {
        try {
            JsonValue template = findTemplate(creature.getString("body_template"));
            EquipmentAspect equipmentAspect = generateEquipmentAspectFromTemplate(template);
            Arrays.stream(creature.get("limbs_to_cover").asStringArray()).forEach(s -> {
                equipmentAspect.getDesiredSlots().add(equipmentAspect.getSlots().get(s));
            });
            return equipmentAspect;
        } catch (DescriptionNotFoundException | FaultDescriptionException e) {
            e.printStackTrace();
            return null;
        }
    }

    private JsonValue findTemplate(String template) throws DescriptionNotFoundException {
        for (JsonValue t : templates) {
            if (t.getString("title").equals(template)) return t;
        }
        throw new DescriptionNotFoundException("Body template " + template + "not found");
    }

    private EquipmentAspect generateEquipmentAspectFromTemplate(JsonValue template) throws FaultDescriptionException {
        EquipmentAspect equipmentAspect = new EquipmentAspect(null);
        for (JsonValue bp : template.get("body")) { // read template to map
            EquipmentAspect.EquipmentSlot slot = generateSlotByBodyPart(bp, equipmentAspect);
            if (slot instanceof EquipmentAspect.GrabEquipmentSlot) {
                equipmentAspect.getGrabSlots().put(slot.limbName, (EquipmentAspect.GrabEquipmentSlot) slot);
            }
            equipmentAspect.getSlots().put(slot.limbName, slot);
        }
        return equipmentAspect;
    }

    /**
     * Creates {@link EquipmentAspect.EquipmentSlot} from template.
     *
     * @param partTemplate
     * @param equipmentAspect
     * @return
     */
    private EquipmentAspect.EquipmentSlot generateSlotByBodyPart(JsonValue partTemplate, EquipmentAspect equipmentAspect) {
        EquipmentAspect.EquipmentSlot slot;
        if (Arrays.asList(partTemplate.get("tags").asStringArray()).contains("grab")) {
            slot = equipmentAspect.new GrabEquipmentSlot(partTemplate.getString("type"));
        } else {
            slot = equipmentAspect.new EquipmentSlot(partTemplate.getString("type"));
        }
        return slot;
    }
}
