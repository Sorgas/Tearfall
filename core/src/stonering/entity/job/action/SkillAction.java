package stonering.entity.job.action;

import stonering.entity.job.action.target.ActionTarget;
import stonering.entity.unit.aspects.health.HealthAspect;
import stonering.entity.unit.aspects.job.SkillAspect;
import stonering.enums.unit.Skill;
import stonering.enums.unit.SkillsMap;

/**
 * Action that uses particular skill.
 * Skill level of a unit can influence action performing providing speed, quality or quantity bonus.
 *
 * @author Alexander on 27.01.2020.
 */
public class SkillAction extends Action {
    public final String SKILL_NAME;
    private final Skill skill;

    protected SkillAction(ActionTarget actionTarget, String skillName) {
        super(actionTarget);
        SKILL_NAME = skillName;
        skill = SkillsMap.instance().getSkill(SKILL_NAME);
    }

    protected float getSpeedBonus() {
        return skill.speed * getPerformerLevel();
    }

    protected float getQualityBonus() {
        return skill.quality * getPerformerLevel();
    }

    protected float getQuantityBonus() {
        return skill.quantity * getPerformerLevel();
    }

    protected float getUnitPerformance() {
        return task.performer.getAspect(HealthAspect.class).properties.get("performance");
    }

    private int getPerformerLevel() {
        return task.performer.getAspect(SkillAspect.class).getSkill(SKILL_NAME).state.getLevel();
    }
}