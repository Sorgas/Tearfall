package stonering.enums.items.recipe;

import stonering.enums.items.ItemTagEnum;
import stonering.enums.unit.JobMap;

import java.util.*;

/**
 * Recipe for crafting.
 * Contains info about result and required materials.
 * Recipes use {@link Ingredient} to specify items for crafting.
 * Ingredients stored in a map and are mapped to item part names, or words 'consumed' or 'main'.
 *
 * TODO rewrite
 * If recipe has no main ingredient, then new item of type and material will be created (part ingredients create part of new item. Consumed ingredients are consumed (give no parts)).
 *    TRANSFORM:
 *        Modify existing item, specified in main ingredient. Part ingredients add parts to item. Consumed ingredients are consumed.
 *
 * Default value for type is combine.
 *
 * @author Alexander on 19.11.2018.
 */
public class Recipe {
    public final String name;                               // recipe(id)
    public final String title;                              // displayed name
    public final String iconName;                           // if itemName is empty, icon is used in workbenches
    public final String description;                        // recipe description.

    public final String newType;                            // type of crafted item
    public final String newMaterial;                        // material of crafted item.
    public final ItemTagEnum newTag;                        // this tag will be added to product
    public final ItemTagEnum removeTag;                     // this tag will be removed from main ingredient item

    public final Map<String, Ingredient> ingredients = new HashMap<>(); // all ingredients, mapped to parts, 'consumed' or 'main'

    public final float workAmount;                          // increases crafting time
    public final String job;                                // if null,
    public final String skill;                              // if set, crafting gets bonus and gives experience in that skill

    public Recipe(RawRecipe raw) {
        name = raw.name;
        title = raw.title;
        newType = raw.itemName;
        newMaterial = raw.newMaterial;
        iconName = raw.iconName;
        description = raw.description;
        newTag = ItemTagEnum.get(raw.newTag);
        removeTag = ItemTagEnum.get(raw.removeTag);
        workAmount = raw.workAmount != 0 ? raw.workAmount : 1f;
        job = raw.job;
        skill = raw.skill;
    }

    @Override
    public String toString() {
        return title;
    }
}
