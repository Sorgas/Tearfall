package stonering.generators.creatures.needs;

import java.util.Objects;

import stonering.entity.unit.aspects.need.NeedAspect;
import stonering.entity.unit.aspects.need.NeedState;
import stonering.enums.unit.race.CreatureType;
import stonering.enums.unit.need.NeedEnum;

/**
 * Generates needs for creatures. Need names are listed in body template.
 * NeedStates are created with 0 values(fully satisfied),
 *
 * @author Alexander on 21.09.2018.
 */
public class NeedAspectGenerator {

    public NeedAspect generateNeedAspect(CreatureType type) {
        NeedAspect needAspect = new NeedAspect(null);
        type.needs.stream()
                .filter(Objects::nonNull)
                .forEach(need -> needAspect.needs.put(need, new NeedState()));
        return needAspect;
    }
}
