package stonering.enums.items.recipe;

import stonering.enums.items.type.ItemType;
import stonering.enums.items.type.ItemTypeMap;
import stonering.util.logging.Logger;

/**
 * Processes {@link RawRecipe} into {@link Recipe}.
 *
 * @author Alexander_Kuzyakov on 23.05.2019.
 */
public class RecipeProcessor {
    private IngredientProcessor ingredientProcessor = new IngredientProcessor();

    public Recipe processRawRecipe(RawRecipe rawRecipe) {
        Logger.LOADING.logDebug("Processing recipe " + rawRecipe.name);
        Recipe recipe = new Recipe(rawRecipe);
        rawRecipe.ingredients.stream()
                .filter(ingredientProcessor::validateIngredient)
                .map(ingredientProcessor::parseIngredient)
                .forEach(ingredient -> recipe.ingredients.put(ingredient.key, ingredient));
        // combine recipes should specify all required item parts
        if (recipe.ingredients.keySet().contains("main") || validateCombineRecipe(recipe)) return recipe;
        return null;
    }

    private boolean validateCombineRecipe(Recipe recipe) {
        ItemType type =  ItemTypeMap.instance().getItemType(recipe.newType);
        if(type == null)
            return Logger.LOADING.logWarn("Recipe " + recipe.name + " has invalid item type " + recipe.newType, false);
        if (!recipe.ingredients.keySet().containsAll(type.requiredParts))
            return Logger.LOADING.logWarn("Recipe " + recipe.name + " specifies not all required parts of type " + type.name, false);
        return true;
    }
}
