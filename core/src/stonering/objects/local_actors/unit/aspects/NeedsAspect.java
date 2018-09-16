package stonering.objects.local_actors.unit.aspects;

import stonering.enums.TaskPrioritiesEnum;
import stonering.objects.local_actors.Aspect;
import stonering.objects.local_actors.AspectHolder;

/**
 * @author Alexander on 16.09.2018.
 */
public class NeedsAspect extends Aspect {

    public NeedsAspect(AspectHolder aspectHolder) {
        super("needs", aspectHolder);
    }

    public void checkNeeds() {

    }

    public TaskPrioritiesEnum getStrongestNeed() {

    }

    private boolean checkWear() {
        BodyAspect bodyAspect = (BodyAspect) aspectHolder.getAspects().get("body");
        EquipmentAspect equipmentAspect = (EquipmentAspect) aspectHolder.getAspects().get("equipment");
        if(bodyAspect != null && equipmentAspect != null) {
            bodyAspect.get
        }
    }
}
