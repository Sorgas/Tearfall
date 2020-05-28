package stonering.enums.items.recipe;

import java.util.List;
import java.util.stream.Collectors;

import stonering.enums.items.type.ItemType;
import stonering.enums.items.type.ItemTypeMap;
import stonering.util.global.Logger;
import stonering.util.global.Pair;

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
        List<String> requiredParts = type.parts.stream()
                .map(Pair::getKey)
                .collect(Collectors.toList());
        if (!recipe.ingredients.keySet().containsAll(requiredParts))
            return Logger.LOADING.logWarn("Recipe " + recipe.name + " specifies not all part of type " + type.name, false);
        return true;
    }
}
