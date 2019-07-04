package stonering.generators.creatures;

import stonering.enums.unit.CreatureType;
import stonering.enums.unit.CreatureTypeMap;
import stonering.generators.creatures.needs.NeedAspectGenerator;
import stonering.util.geometry.Position;
import stonering.entity.unit.aspects.MovementAspect;
import stonering.entity.unit.aspects.PlanningAspect;
import stonering.entity.unit.Unit;

/**
 * Creates creatures from json files by specimen name.
 *
 * @author Alexander Kuzyakov on 03.12.2017.
 */
public class CreatureGenerator {
    private BodyAspectGenerator bodyAspectGenerator;
    private EquipmentAspectGenerator equipmentAspectGenerator;
    private NeedAspectGenerator needAspectGenerator;

    public CreatureGenerator() {
        bodyAspectGenerator = new BodyAspectGenerator();
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
        unit.addAspect(bodyAspectGenerator.generateBodyAspect(type));
        unit.addAspect(new PlanningAspect(null));
        unit.addAspect(new MovementAspect(null));
        unit.addAspect(equipmentAspectGenerator.generateEquipmentAspect(type));
        unit.addAspect(needAspectGenerator.generateNeedAspect(type));
        return unit;
    }
}
