package stonering.entity.item.aspects;

import stonering.entity.Aspect;

/**
 * Items with this aspect can be planted as seeds on farms, and create plants when in goon conditions.
 */
public class SeedAspect extends Aspect {
    public final String specimen;

    public SeedAspect(String specimen) {
        this.specimen = specimen;
    }
}
