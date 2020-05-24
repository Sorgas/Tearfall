package stonering.enums.items.recipe;

import stonering.enums.items.ItemTagEnum;
import stonering.util.global.Logger;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Processes {@link RawRecipe} into {@link Recipe}.
 *
 * @author Alexander_Kuzyakov on 23.05.2019.
 */
public class RecipeProcessor {
    private IngredientProcessor ingredientProcessor = new IngredientProcessor;

    public Recipe processRawRecipe(RawRecipe rawRecipe) {
        Logger.LOADING.logDebug("Processing recipe " + rawRecipe.name);
        Recipe recipe = new Recipe(rawRecipe);
        // add part ingredients
        rawRecipe.parts.stream()
                .filter(this::validatePartIngredient)
                .forEach(ingredient -> recipe.parts.put(ingredient.get(0), parseIngredient(ingredient.get(1))));
        // add consumed ingredients
        rawRecipe.consumed.stream()
                .filter(this::validateIngredient)
                .map(this::parseIngredient)
                .forEach(recipe.consumed::add);
        // set main ingredient
        recipe.main = Optional.ofNullable(rawRecipe.main)
                .filter(this::validateIngredient)
                .map(this::parseIngredient)
                .orElse(null);
        return recipe;
    }
}
