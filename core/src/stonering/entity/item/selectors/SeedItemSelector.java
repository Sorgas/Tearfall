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
        return item != null && item.has(SeedAspect.class) && specimen.equals(item.get(SeedAspect.class).specimen);
    }

    public String getSpecimen() {
        return specimen;
    }
}
