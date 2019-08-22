package stonering.generators.creatures;

import stonering.entity.unit.aspects.JobsAspect;
import stonering.enums.unit.CreatureType;
import stonering.enums.unit.CreatureTypeMap;
import stonering.generators.creatures.needs.NeedAspectGenerator;
import stonering.entity.unit.aspects.MovementAspect;
import stonering.entity.unit.aspects.PlanningAspect;
import stonering.entity.unit.Unit;
import stonering.util.global.Logger;

/**
 * Creates creatures from json files by specimen name.
 *
 * @author Alexander Kuzyakov on 03.12.2017.
 */
public class CreatureGenerator {
    private BodyAspectGenerator bodyAspectGenerator;
    private EquipmentAspectGenerator equipmentAspectGenerator;
    private NeedAspectGenerator needAspectGenerator;
    private AttributeAspectGenerator attributeAspectGenerator;

    public CreatureGenerator() {
        bodyAspectGenerator = new BodyAspectGenerator();
        equipmentAspectGenerator = new EquipmentAspectGenerator();
        needAspectGenerator = new NeedAspectGenerator();
        attributeAspectGenerator = new AttributeAspectGenerator();
    }

    /**
     * Generates unit and fills it's aspects.
     */
    public Unit generateUnit(String specimen) {
        Logger.GENERATION.log("generating unit " + specimen);
        CreatureType type = CreatureTypeMap.instance().getCreatureType(specimen);
        if (type == null) return null;
        Unit unit = new Unit(type);
        addMandatoryAspects(unit);
        addOptionalAspects(unit);
        return unit;
    }

    /**
     * Create aspects and add them to unit.
     */
    private void addMandatoryAspects(Unit unit) {
        CreatureType type = unit.getType();
        unit.addAspect(bodyAspectGenerator.generateBodyAspect(type));
        unit.addAspect(new PlanningAspect(null));
        unit.addAspect(new MovementAspect(null));
        unit.addAspect(needAspectGenerator.generateNeedAspect(type));
        unit.addAspect(attributeAspectGenerator.generateAttributeAspect(unit));
    }

    private void addOptionalAspects(Unit unit) {
        CreatureType type = unit.getType();
        for (String aspect : type.aspects) {
            switch(aspect) {
                case "equipment" : {
                    unit.addAspect(equipmentAspectGenerator.generateEquipmentAspect(type));
                    continue;
                }
                case "jobs" : {
                    unit.addAspect(new JobsAspect(null));
                    continue;
                }
            }
        }
    }
}
