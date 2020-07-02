package stonering.entity.job.action;

import stonering.entity.job.action.target.ActionTarget;
import stonering.entity.unit.aspects.health.HealthAspect;
import stonering.entity.unit.aspects.job.SkillAspect;
import stonering.enums.unit.Skill;
import stonering.enums.unit.SkillMap;

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
        skill = SkillMap.getSkill(SKILL_NAME);

        onStart = () -> {
            speed = 1 + getSpeedBonus() + getUnitPerformance(); // 1 for non-trained not tired worker
        };
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
        return task.performer.get(HealthAspect.class).properties.get("performance");
    }

    private int getPerformerLevel() {
        return task.performer.get(SkillAspect.class).getSkill(SKILL_NAME).state.getLevel();
    }
}
