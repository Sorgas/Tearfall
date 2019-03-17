package stonering.generators.creatures;

import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import stonering.generators.creatures.needs.NeedAspectGenerator;
import stonering.util.geometry.Position;
import stonering.entity.local.unit.aspects.MovementAspect;
import stonering.entity.local.unit.aspects.PlanningAspect;
import stonering.entity.local.unit.Unit;
import stonering.util.global.FileLoader;
import stonering.util.global.TagLoggersEnum;

/**
 * Creates creatures from json files by specimen title.
 *
 * @author Alexander Kuzyakov on 03.12.2017.
 */
public class CreatureGenerator {
    private JsonReader reader;
    private BodyGenerator bodyGenerator;
    private EquipmentAspectGenerator equipmentAspectGenerator;
    private NeedAspectGenerator needAspectGenerator;
    private JsonValue creatures;

    public CreatureGenerator() {
        reader = new JsonReader();
        bodyGenerator = new BodyGenerator();
        equipmentAspectGenerator = new EquipmentAspectGenerator();
        needAspectGenerator = new NeedAspectGenerator();
        creatures = reader.parse(FileLoader.getFile(FileLoader.CREATURES_PATH));
    }

    /**
     * Generates unit and fills it's aspects.
     */
    public Unit generateUnit(String specimen) {
        JsonValue creature = getCreatureDescriptor(specimen);
        if (creature == null) return null;
        Unit unit = new Unit(new Position(0, 0, 0));    // empty unit //TODO change constructor.
        return addAspects(unit, creature);
    }

    /**
     * Create aspects and add them to unit.
     */
    private Unit addAspects(Unit unit, JsonValue creature) {
        unit.addAspect(bodyGenerator.generateBodyAspect(creature));
        unit.addAspect(new PlanningAspect(null));
        unit.addAspect(new MovementAspect(null));
        unit.addAspect(equipmentAspectGenerator.generateEquipmentAspect(creature));
        unit.addAspect(needAspectGenerator.generateNeedAspect(creature));
        return unit;
    }

    /**
     * Fetches creature descriptor from json by creature name.
     * Returns null, if no descriptor exists.
     */
    private JsonValue getCreatureDescriptor(String specimen) {
        for (JsonValue c : creatures) {
            if (c.getString("title").equals(specimen)) return c;
        }
        TagLoggersEnum.GENERATION.logWarn("Creature descriptor with name + " + specimen + " not found.");
        return null;
    }
}
