package stonering.enums.items.recipe;

import stonering.util.global.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Recipe for crafting.
 * Contains info about result and required materials.
 *
 * @author Alexander on 19.11.2018.
 */
public class Recipe {
    public String name;     // recipe NAME
    public String title;    // displayed name
    public String itemName; // item NAME, points to ItemType
    public List<ItemPartRecipe> parts;  // itemPart NAME to material categories.

    public Recipe(String title) {
        this.title = title;
        parts = new ArrayList();
    }

    /**
     * Looks for {@link ItemPartRecipe} by name of item part.
     */
    public ItemPartRecipe getItemPartRecipe(String itemPartName) {
        for (ItemPartRecipe part : parts) {
            if(part.itemPart.equals(itemPartName)) return part;
        }
        Logger.TASKS.logWarn("Item part with name " + itemPartName + " not found in recipe " + name);
        return null;
    }

    @Override
    public String toString() {
        return title;
    }
}
