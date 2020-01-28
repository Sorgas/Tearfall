package stonering.game.model.system.unit;

import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.job.SkillAspect;
import stonering.enums.time.TimeUnitEnum;
import stonering.game.model.system.EntitySystem;

/**
 * @author Alexander on 28.01.2020.
 */
public class CreatureExperienceSystem extends EntitySystem<Unit> {

    public CreatureExperienceSystem() {
        updateInterval = TimeUnitEnum.DAY;
        targetAspects.add(SkillAspect.class);
    }

    @Override
    public void update(Unit entity) {
        // TODO decrease experience in unused skills
    }

    public void giveExperience(Unit unit, String skillName) {
        unit.getAspect(SkillAspect.class).getSkill(skillName).state.changeValue(1);
        //TODO give mood buff on level up
        //TODO upgrade attributes
    }
}
