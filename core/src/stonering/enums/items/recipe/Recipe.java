package stonering.enums.items.recipe;

import stonering.util.global.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Recipe for crafting.
 * Contains info about result and required materials.
 *
 * @author Alexander on 19.11.2018.
 */
public class Recipe {
    public final String name;     // recipe NAME
    public final String title;    // displayed name
    public final String category; // recipes are divided into categories in workbench menu
    public final String itemName; // item NAME, points to ItemType
    public final String description;
    public Map<String, Ingredient> parts = new HashMap<>(); // itemPart NAME to ingredients.
    public List<Ingredient> consumed = new ArrayList<>();  // do not produce item parts.

    public Recipe(String title) {
        this.title = title;
        category = "";
        name = "";
        itemName = "";
        description = "";
    }

    public Recipe(RawRecipe raw) {
        name = raw.name;
        category = raw.category;
        title = raw.title;
        itemName = raw.itemName;
        description = raw.description;
    }

    /**
     * Looks for {@link Ingredient} by name of item part.
     */
    public Ingredient getItemPartRecipe(String itemPartName) {
        if(parts.containsKey(itemPartName)) {
            return parts.get(itemPartName);
        }
        Logger.CRAFTING.logWarn("Item part with name " + itemPartName + " not found in recipe " + name);
        return null;
    }

    @Override
    public String toString() {
        return title;
    }
}
