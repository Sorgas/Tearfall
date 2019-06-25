package stonering.enums.items.recipe;

import stonering.util.global.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Recipe for crafting.
 * Contains info about result and required materials.
 *
 * @author Alexander on 19.11.2018.
 */
public class Recipe {
    public String name;     // recipe NAME
    public String title;    // displayed title
    public String itemName; // item NAME, points to ItemType
    //    private String skill;
    //    private int duration;                                                          //TODO crafting with usage of multiple workbenches.
    //    private int expGain;
    public Map<String, ItemPartRecipe> parts;  // itemPart NAME to material categories   //TODO map of item parts to allowed materials.
    //    private String material; // mvp material category

    public Recipe(String title) {
        this.title = title;
        parts = new HashMap<>();
    }

    /**
     * Looks for {@link ItemPartRecipe} by name of item part.
     *
     * @param itemPartName
     * @return
     */
    public ItemPartRecipe getItemPartRecipe(String itemPartName) {
        if (parts.containsKey(itemPartName)) return parts.get(itemPartName);
        Logger.TASKS.logWarn("Item part with name " + itemPartName + " not found in recipe " + name);
        return null;
    }

    @Override
    public String toString() {
        return title;
    }
}
