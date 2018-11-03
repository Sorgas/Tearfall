package stonering.entity.local.crafting;

import stonering.entity.local.items.selectors.ItemSelector;
import stonering.entity.local.items.selectors.ResourceAmountItemSelector;
import stonering.entity.local.items.selectors.ResourceItemSelector;
import stonering.entity.local.items.selectors.SimpleItemSelector;
import stonering.enums.items.ItemType;

import java.util.HashMap;

/**
 * Contains {@link ItemType}, and variants for crafting selected by player.
 *
 * @author Alexander on 27.10.2018.
 */
public class ItemOrder {
    private ItemType type;
    private HashMap<String, ItemSelector> selectors; // itemPart to items selected for variant.

    public ItemOrder(ItemType type) {
        this.type = type;
        selectors = new HashMap<>();
        createDefaultSelectors();
    }

    /**
     * Updates one selector. Used when payer selects some items for crafting.
     *
     * @param part
     * @param selector
     */
    public void setSelector(String part, ItemSelector selector) {
        selectors.put(part, selector);
    }

    private void createDefaultSelectors() {
        for (ItemPartCraftingStep step : type.getSteps()) {
            if (!step.isOptional()) {
                if (!step.getVariants().isEmpty()) {
                    CraftingComponentVariant variant = step.getVariants().get(0);
                    //TODO amount
//                    selectors.put(step.getTitle(), new ResourceAmountItemSelector(variant.getAmount(), variant.getType(), variant.getMaterial()));
                    selectors.put(step.getTitle(), new ResourceItemSelector(variant.getMaterial()));
                }
            }
        }
    }

    public ItemType getType() {
        return type;
    }

    public HashMap<String, ItemSelector> getSelectors() {
        return selectors;
    }
}
