package stonering.entity.local.items.aspects;

import stonering.entity.local.Aspect;
import stonering.entity.local.AspectHolder;

public class SeedAspect extends Aspect {
    public static final String NAME = "seed";
    private String specimen;

    public SeedAspect(AspectHolder aspectHolder, String specimen) {
        super(aspectHolder);
        this.specimen = specimen;
    }

    public String getSpecimen() {
        return specimen;
    }

    public void setSpecimen(String specimen) {
        this.specimen = specimen;
    }
}
