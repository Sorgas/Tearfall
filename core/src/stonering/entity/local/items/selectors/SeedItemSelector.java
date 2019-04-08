package stonering.entity.local.items.selectors;

import stonering.entity.local.items.Item;
import stonering.entity.local.items.aspects.SeedAspect;

import java.util.List;

public class SeedItemSelector extends SingleItemSelector {
    private String specimen;

    public SeedItemSelector(String specimen) {
        this.specimen = specimen;
    }

    @Override
    public Item selectItem(List<Item> items) {
        return items.stream().findAny().orElse(null);
    }

    @Override
    public boolean check(List<Item> items) {
        return items.stream().anyMatch(this::check);
    }

    private boolean check(Item item) {
        SeedAspect aspect = item.getAspect(SeedAspect.class);
        if(aspect == null) return false;
        return specimen.equals(aspect.getSpecimen());
    }
}
