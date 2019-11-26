package stonering.entity.item.aspects;

import stonering.entity.Aspect;
import stonering.entity.Entity;

/**
 * Items with this aspect can be planted as seeds on farms, and create plants when in goon conditions.
 */
public class SeedAspect extends Aspect {
    public static final String NAME = "seed";
    public String specimen;

    public SeedAspect(Entity entity) {
        super(entity);
    }
}
