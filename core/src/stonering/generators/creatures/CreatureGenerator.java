package stonering.generators.creatures;

import stonering.entity.RenderAspect;
import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.MoodAspect;
import stonering.entity.unit.aspects.MovementAspect;
import stonering.entity.unit.aspects.TaskAspect;
import stonering.entity.unit.aspects.job.JobSkillAspect;
import stonering.enums.unit.race.CreatureType;
import stonering.enums.unit.CreatureTypeMap;
import stonering.generators.creatures.needs.NeedAspectGenerator;
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
    private HealthAspectGenerator healthAspectGenerator;
    private HumanoidRenderGenerator humanoidRenderGenerator;
    private JobSkillAspectGenerator jobSkillAspectGenerator;

    public CreatureGenerator() {
        bodyAspectGenerator = new BodyAspectGenerator();
        equipmentAspectGenerator = new EquipmentAspectGenerator();
        needAspectGenerator = new NeedAspectGenerator();
        healthAspectGenerator = new HealthAspectGenerator();
        humanoidRenderGenerator = new HumanoidRenderGenerator();
        jobSkillAspectGenerator = new JobSkillAspectGenerator();
    }

    /**
     * Generates unit and fills it's aspects.
     */
    public Unit generateUnit(Position position, String specimen) {
        Logger.GENERATION.log("generating unit " + specimen);
        CreatureType type = CreatureTypeMap.instance().creatureTypes.get(specimen);
        if (type == null) return null;
        Unit unit = new Unit(position.clone(), type);
        addMandatoryAspects(unit);
        addOptionalAspects(unit);
        return unit;
    }

    /**
     * Create aspects and add them to unit.
     */
    private void addMandatoryAspects(Unit unit) {
        CreatureType type = unit.getType();
        unit.add(needAspectGenerator.generateNeedAspect(type));
        unit.add(bodyAspectGenerator.generateBodyAspect(type));
        unit.add(healthAspectGenerator.generateHealthAspect(unit));
        unit.add(new TaskAspect(null));
        unit.add(new MovementAspect(null));
        unit.add(new RenderAspect(AtlasesEnum.units.getBlockTile(type.atlasXY)));
        unit.add(new MoodAspect());
    }

    private void addOptionalAspects(Unit unit) {
        CreatureType type = unit.getType();
        for (String aspect : type.aspects) {
            switch (aspect) {
                case "equipment": {
                    unit.add(equipmentAspectGenerator.generateEquipmentAspect(type));
                    continue;
                }
                case "jobs": {
                    unit.add(jobSkillAspectGenerator.generate());
                }
            }
        }
    }
}
