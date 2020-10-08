package stonering.generators.creatures;

import java.util.Optional;

import stonering.entity.RenderAspect;
import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.MoodAspect;
import stonering.entity.unit.aspects.MovementAspect;
import stonering.entity.unit.aspects.job.TaskAspect;
import stonering.enums.unit.race.CreatureType;
import stonering.enums.unit.CreatureTypeMap;
import stonering.generators.creatures.needs.NeedAspectGenerator;
import stonering.stage.renderer.atlas.AtlasesEnum;
import stonering.util.geometry.Position;
import stonering.util.logging.Logger;

/**
 * Creates creatures from json files by specimen name.
 *
 * @author Alexander Kuzyakov on 03.12.2017.
 */
public class CreatureGenerator {
    private BodyAspectGenerator bodyAspectGenerator = new BodyAspectGenerator();
    private EquipmentAspectGenerator equipmentAspectGenerator = new EquipmentAspectGenerator();
    private NeedAspectGenerator needAspectGenerator = new NeedAspectGenerator();
    private HealthAspectGenerator healthAspectGenerator = new HealthAspectGenerator();
    private HumanoidRenderGenerator humanoidRenderGenerator = new HumanoidRenderGenerator();
    private JobSkillAspectGenerator jobSkillAspectGenerator = new JobSkillAspectGenerator();

    public Unit generateUnit(Position position, String specimen) {
        Logger.GENERATION.log("generating unit " + specimen);
        return Optional.ofNullable(CreatureTypeMap.getType(specimen))
                .map(type -> {
                    Unit unit = new Unit(position.clone(), type);
                    addMandatoryAspects(unit);
                    addOptionalAspects(unit);
                    addRenderAspect(unit);
                    return unit;
                })
                .orElse(null);
    }

    private void addMandatoryAspects(Unit unit) {
        CreatureType type = unit.getType();
        unit.add(needAspectGenerator.generateNeedAspect(type));
        unit.add(bodyAspectGenerator.generateBodyAspect(type));
        unit.add(healthAspectGenerator.generateHealthAspect(unit));
        unit.add(new TaskAspect(null));
        unit.add(new MovementAspect(null));
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
    
    private void addRenderAspect(Unit unit) {
        CreatureType type = unit.getType();
        unit.add(type.combinedAppearance != null 
                ? humanoidRenderGenerator.generateRender(type, true) 
                : new RenderAspect(AtlasesEnum.units.getBlockTile(type.atlasXY)));
    }
}
