package stonering.enums.items.recipe;

import static stonering.enums.items.recipe.RecipeType.COMBINE;
import static stonering.enums.items.recipe.RecipeType.TRANSFORM;

import stonering.util.global.Logger;

import java.util.ArrayList;

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
                .forEach(ingredient -> recipe.ingredients.computeIfAbsent(ingredient.key, key -> new ArrayList<>()).add(ingredient));
        recipe.type = recipe.ingredients.containsKey("main") ? TRANSFORM : COMBINE;
        return recipe;
    }
}
