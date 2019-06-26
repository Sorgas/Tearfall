package stonering.enums.items.recipe;

import java.util.List;

/**
 * Determines crafting of item part. Is part of {@link Recipe}.
 *
 * @author Alexander on 05.01.2019.
 */
public class ItemPartRecipe {
    public String itemPart;
    public List<String> itemTypes;
    public String materialTag;

    public ItemPartRecipe(String itemPart, List<String> itemTypes, String materialTag) {
        this.itemPart = itemPart;
        this.itemTypes = itemTypes;
        this.materialTag = materialTag;
    }
}
