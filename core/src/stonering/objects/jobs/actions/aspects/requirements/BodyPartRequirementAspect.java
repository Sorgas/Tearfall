package stonering.objects.jobs.actions.aspects.requirements;

import com.sun.istack.internal.NotNull;
import stonering.objects.jobs.actions.Action;
import stonering.objects.local_actors.unit.aspects.BodyAspect;

import java.util.Arrays;

/**
 * Checks if creature has bodypart with specific type or tag.
 *
 * @author Alexander Kuzyakov on 03.02.2018.
 */
public class BodyPartRequirementAspect extends RequirementsAspect {
    private String bodyPartType;
    private boolean useTags;

    public BodyPartRequirementAspect(Action action, @NotNull String bodyPartType, boolean useTags) {
        super(action);
        this.bodyPartType = bodyPartType;
        this.useTags = useTags;
    }

    @Override
    public boolean check() {
        BodyAspect bodyAspect = (BodyAspect) action.getTask().getPerformer().getAspects().get("body");
        if (bodyAspect != null) {
            if (useTags) {
                for (BodyAspect.BodyPart bodyPart : bodyAspect.getBodyParts().values()) {
                    if (Arrays.stream(bodyPart.tags).anyMatch(s -> s.equals(bodyPartType))) {
                        return true;
                    }
                }
            } else {
                return bodyAspect.getBodyParts().values().stream().anyMatch(bodyPart -> bodyPart.type.equals(bodyPartType));
            }
        }
        return true;
    }
}
