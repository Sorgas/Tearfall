package stonering.entity.local.items.aspects;

import stonering.entity.local.Aspect;
import stonering.entity.local.Entity;

public class SeedAspect extends Aspect {
    public static final String NAME = "seed";
    private String specimen;

    public SeedAspect(Entity entity, String specimen) {
        super(entity);
        this.specimen = specimen;
    }

    public String getSpecimen() {
        return specimen;
    }

    public void setSpecimen(String specimen) {
        this.specimen = specimen;
    }
}
