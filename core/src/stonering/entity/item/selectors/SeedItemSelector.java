package stonering.entity.item.selectors;

import stonering.entity.item.Item;
import stonering.entity.item.aspects.SeedAspect;

public class SeedItemSelector extends SingleItemSelector {
    private String specimen;

    public SeedItemSelector(String specimen) {
        this.specimen = specimen;
    }

    @Override
    public boolean checkItem(Item item) {
        SeedAspect aspect = item.getAspect(SeedAspect.class);
        if(aspect == null) return false;
        return specimen.equals(aspect.getSpecimen());
    }
}
