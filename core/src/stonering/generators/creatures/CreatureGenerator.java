package stonering.generators.creatures;

import com.badlogic.gdx.utils.JsonValue;
import stonering.enums.unit.CreatureType;
import stonering.enums.unit.CreatureTypeMap;
import stonering.generators.creatures.needs.NeedAspectGenerator;
import stonering.util.geometry.Position;
import stonering.entity.local.unit.aspects.MovementAspect;
import stonering.entity.local.unit.aspects.PlanningAspect;
import stonering.entity.local.unit.Unit;
import stonering.util.global.Logger;

/**
 * Creates creatures from json files by specimen name.
 *
 * @author Alexander Kuzyakov on 03.12.2017.
 */
public class CreatureGenerator {
    private BodyGenerator bodyGenerator;
    private EquipmentAspectGenerator equipmentAspectGenerator;
    private NeedAspectGenerator needAspectGenerator;

    public CreatureGenerator() {
        bodyGenerator = new BodyGenerator();
        equipmentAspectGenerator = new EquipmentAspectGenerator();
        needAspectGenerator = new NeedAspectGenerator();
    }

    /**
     * Generates unit and fills it's aspects.
     */
    public Unit generateUnit(String specimen) {
        CreatureType type = CreatureTypeMap.instance().getCreatureType(specimen);
        if (type == null) return null;
        Unit unit = new Unit(new Position(0, 0, 0));    // empty unit //TODO change constructor.
        return addAspects(unit, type);
    }

    /**
     * Create aspects and add them to unit.
     */
    private Unit addAspects(Unit unit, CreatureType type) {
        unit.addAspect(bodyGenerator.generateBodyAspect(type));
        unit.addAspect(new PlanningAspect(null));
        unit.addAspect(new MovementAspect(null));
        unit.addAspect(equipmentAspectGenerator.generateEquipmentAspect(type));

        unit.addAspect(needAspectGenerator.generateNeedAspect(type));
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
        Logger.GENERATION.logWarn("Creature descriptor with name + " + specimen + " not found.");
        return null;
    }
}
