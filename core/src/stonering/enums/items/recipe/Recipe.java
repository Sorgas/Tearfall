package stonering.enums.items.recipe;

import stonering.enums.items.ItemTagEnum;
import stonering.enums.unit.JobsEnum;
import stonering.util.global.Logger;

import java.util.*;

import static stonering.enums.items.recipe.RecipeType.*;
import static stonering.enums.unit.JobsEnum.NONE;

/**
 * Recipe for crafting.
 * Contains info about result and required materials.
 * Recipes use {@link Ingredient} to specify items for crafting.
 * Ingredients stored in a map and are mapped to item part names, or words 'consumed' or 'main'.
 * There are two types of recipes ({@link RecipeType}):
 *    COMBINE:
 *        Create new item from ingredients. Part ingredients create part of new item. Consumed ingredients are consumed (give no parts).
 *    TRANSFORM:
 *        Modify existing item, specified in main ingredient. Part ingredients add parts to item. Consumed ingredients are consumed.
 *
 * Default value for type is combine.
 *
 * @author Alexander on 19.11.2018.
 */
public class Recipe {
    public RecipeType type;
    public final String name;                               // recipe(id)
    public final String title;                              // displayed name
    public final String iconName;                           // if itemName is empty, icon is used in workbenches
    public final String description;                        // recipe description.

    public final String itemName;                           // name of produced item's itemType
    public final String newMaterial;                        // material of crafted item.
    public final ItemTagEnum newTag;                        // this tag will be added to product

    public final Map<String, List<Ingredient>> ingredients = new HashMap<>(); // all ingredients, mapped to parts, 'consumed' or 'main'

    public final float workAmount;                          // increases crafting time
    public final JobsEnum job;                              // if null,
    public final String skill;                              // if set, crafting gets bonus and gives experience in that skill

    public Recipe(RawRecipe raw) {
        name = raw.name;
        title = raw.title;
        itemName = raw.itemName;
        newMaterial = raw.newMaterial;
        iconName = raw.iconName;
        description = raw.description;
        newTag = ItemTagEnum.get(raw.newTag);
        workAmount = raw.workAmount != 0 ? raw.workAmount : 1f;
        job = Optional.ofNullable(raw.job)
                .map(String::toUpperCase)
                .map(JobsEnum.map::get)
                .orElse(NONE);
        skill = raw.skill;
    }

    @Override
    public String toString() {
        return title;
    }
}
