package stonering.generators.creatures;

import stonering.entity.RenderAspect;
import stonering.entity.unit.aspects.*;
import stonering.entity.unit.aspects.job.JobsAspect;
import stonering.entity.unit.aspects.job.SkillAspect;
import stonering.enums.unit.CreatureType;
import stonering.enums.unit.CreatureTypeMap;
import stonering.game.GameMvc;
import stonering.game.model.system.unit.UnitContainer;
import stonering.generators.creatures.needs.NeedAspectGenerator;
import stonering.entity.unit.Unit;
import stonering.stage.renderer.AtlasesEnum;
import stonering.util.geometry.Position;
import stonering.util.logging.Logger;

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
    private HealthAspectGenerator healthAspectGenerator;

    public CreatureGenerator() {
        bodyAspectGenerator = new BodyAspectGenerator();
        equipmentAspectGenerator = new EquipmentAspectGenerator();
        needAspectGenerator = new NeedAspectGenerator();
        attributeAspectGenerator = new AttributeAspectGenerator();
        healthAspectGenerator = new HealthAspectGenerator();
    }

    /**
     * Generates unit and fills it's aspects.
     */
    public Unit generateUnit(Position position, String specimen) {
        Logger.GENERATION.log("generating unit " + specimen);
        CreatureType type = CreatureTypeMap.instance().getCreatureType(specimen);
        if (type == null) return null;
        Unit unit = new Unit(position.clone(), type);
        addMandatoryAspects(unit);
        addOptionalAspects(unit);
        updateBuffs(unit);
        return unit;
    }

    /**
     * Create aspects and add them to unit.
     */
    private void addMandatoryAspects(Unit unit) {
        CreatureType type = unit.getType();
        unit.add(bodyAspectGenerator.generateBodyAspect(type));
        unit.add(new TaskAspect(null));
        unit.add(new MovementAspect(null));
        unit.add(needAspectGenerator.generateNeedAspect(type));
        unit.add(attributeAspectGenerator.generateAttributeAspect(unit));
        unit.add(new RenderAspect(unit, type.atlasXY, AtlasesEnum.units));
        unit.add(new BuffAspect(unit));
        unit.add(healthAspectGenerator.generateHealthAspect(unit));
        unit.add(new SkillAspect(unit));
    }

    private void addOptionalAspects(Unit unit) {
        CreatureType type = unit.getType();
        for (String aspect : type.aspects) {
            switch(aspect) {
                case "equipment" : {
                    unit.add(equipmentAspectGenerator.generateEquipmentAspect(type));
                    continue;
                }
                case "jobs" : {
                    unit.add(new JobsAspect(null));
                }
            }
        }
    }

    private void updateBuffs(Unit unit) {
        GameMvc.instance().model().get(UnitContainer.class).healthSystem.resetCreatureHealth(unit);
    }
}
