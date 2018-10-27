package stonering.entity.local.crafting;

import stonering.entity.local.items.selectors.ItemSelector;
import stonering.enums.items.ItemType;

import java.util.HashMap;

/**
 * Contains {@link stonering.enums.items.ItemType}, and variants for crafting selected by player.
 *
 *
 * @author Alexander on 27.10.2018.
 */
public class ItemOrder {
    private ItemType type;
    private HashMap<String, ItemSelector> selectors; // itemPart to items selected for variant.

    public ItemOrder(ItemType type) {
        this.type = type;
        createDefaultSelectors();
    }

    /**
     * Updates one selector. Used when payer selects some items for crafting.
     * @param part
     * @param selector
     */
    public void setSelector(String part, ItemSelector selector) {

    }

    private void createDefaultSelectors() {
        for (ItemPartCraftingStep step : type.getParts()) {
            if(!step.isOptional()) {
                if(!step.getVariants().isEmpty()) {
                    CraftingComponentVariant variant = step.getVariants().get(0);
                    selectors.put(step.getTitle(), new )
                }
            }
        }
    }
}
