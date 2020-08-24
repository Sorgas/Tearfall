package stonering.enums.unit.need;

import stonering.entity.job.Task;
import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.MoodEffect;
import stonering.entity.unit.aspects.body.BodyAspect;
import stonering.entity.unit.aspects.need.NeedAspect;
import stonering.entity.unit.aspects.need.NeedState;
import stonering.enums.action.TaskPriorityEnum;

/**
 * Abstract class for needs. 
 * Defines task priority calculation and disease.
 * Need priority should be checked before creating task for performance purposes.
 *
 * @author Alexander Kuzyakov on 21.09.2018.
 */
public abstract class Need {
    public final String relatedDisease;
    public final String moodEffectKey;
    public NeedEnum need;

    public Need(String relatedDisease, String moodEffectKey) {
        this.relatedDisease = relatedDisease;
        this.moodEffectKey = moodEffectKey;
    }

    public abstract TaskPriorityEnum countPriority(Unit unit);

    public abstract boolean isSatisfied(Unit unit);

    public abstract Task tryCreateTask(Unit unit);

    public abstract MoodEffect getMoodPenalty(Unit unit, NeedState state);

    public float needLevel(Unit unit) {
        return unit.get(NeedAspect.class).needs.get(need).getRelativeValue();
    }

    public float diseaseLevel(Unit unit) {
        return unit.get(BodyAspect.class).getDiseaseProgress(relatedDisease);
    }
}
