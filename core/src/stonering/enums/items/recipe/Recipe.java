package stonering.enums.items.recipe;

import stonering.enums.items.TagEnum;
import stonering.util.global.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static stonering.enums.items.recipe.RecipeType.*;

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
    public final String description;                        // recipe description.
    public final TagEnum newTag;                             // this tag will be added to product
    public RecipeType type;
    public Ingredient main;                       // for transform recipes
    public Map<String, Ingredient> parts = new HashMap<>(); // itemPart name to ingredients. item parts are created on result item
    public List<Ingredient> consumed = new ArrayList<>();   // ingredients, that do not produce item parts.

    public Recipe(String title) {
        this.title = title;
        name = "";
        itemName = "";
        description = "";
        newTag = null;
        type = COMBINE;
    }

    public Recipe(RawRecipe raw) {
        name = raw.name;
        title = raw.title;
        itemName = raw.itemName;
        description = raw.description;
        newTag = TagEnum.get(raw.newTag);
        type = raw.main == null ? COMBINE : TRANSFORM;
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
