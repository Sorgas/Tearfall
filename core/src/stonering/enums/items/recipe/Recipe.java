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
    public final String name;     // recipe NAME
    public final String category; // recipes are divided into categories in workbench menu
    public final String title;    // displayed name
    public final String itemName; // item NAME, points to ItemType
    public List<ItemPartRecipe> parts = new ArrayList<>();  // itemPart NAME to material categories.

    public Recipe(String title) {
        this.title = title;
        category = "";
        name = "";
        itemName = "";
    }

    public Recipe(RawRecipe raw) {
        name = raw.name;
        category = raw.category;
        title = raw.title;
        itemName = raw.itemName;
    }

    /**
     * Looks for {@link ItemPartRecipe} by name of item part.
     */
    public ItemPartRecipe getItemPartRecipe(String itemPartName) {
        for (ItemPartRecipe part : parts) {
            if(part.itemPart != null)
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
