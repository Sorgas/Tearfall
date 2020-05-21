package stonering.enums.items.recipe;

import stonering.enums.items.ItemTagEnum;
import stonering.enums.unit.job.JobsEnum;
import stonering.util.global.Logger;

import java.util.*;

import static stonering.enums.items.recipe.RecipeType.*;
import static stonering.enums.unit.job.JobsEnum.NONE;

/**
 * Recipe for crafting.
 * Contains info about result and required materials.
 * Recipes can be used to create new item (type COMBINE) or add new parts, tags, or change properties of existing ones (type TRANSFORM).
 * Default value for type is combine.
 *
 * @author Alexander on 19.11.2018.
 */
public class Recipe {
    public final String name;                               // recipe(id)
    public final String title;                              // displayed name
    public final String itemName;                           // name of produced item's itemType
    public final String iconName;                           // if itemName is empty, icon is used in workbenches
    public final String description;                        // recipe description.
    public final ItemTagEnum newTag;                        // this tag will be added to product
    public RecipeType type;
    public Ingredient main;                                 // for transform recipes
    public Map<String, Ingredient> parts = new HashMap<>(); // itemPart name to ingredients. item parts are created on result item
    public List<Ingredient> consumed = new ArrayList<>();   // ingredients, that do not produce item parts.
    public final float workAmount;                          // increases crafting time
    public final JobsEnum job;                              // if null,
    
    public Recipe(RawRecipe raw) {
        name = raw.name;
        title = raw.title;
        itemName = raw.itemName;
        iconName = raw.iconName;
        description = raw.description;
        newTag = ItemTagEnum.get(raw.newTag);
        type = raw.main == null ? COMBINE : TRANSFORM;
        workAmount = raw.workAmount != 0 ? raw.workAmount : 1f;
        job = Optional.ofNullable(raw.job)
                .map(JobsEnum.map::get)
                .orElse(NONE);
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
