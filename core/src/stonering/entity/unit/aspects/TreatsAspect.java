package stonering.entity.unit.aspects;

import stonering.entity.Aspect;
import stonering.entity.Entity;
import stonering.enums.unit.TreatEnum;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Alexander on 18.12.2019.
 */
public class TreatsAspect extends Aspect {
    public final Set<TreatEnum> treats;

    public TreatsAspect(Entity entity) {
        super(entity);
        treats = new HashSet<>();
    }
}
