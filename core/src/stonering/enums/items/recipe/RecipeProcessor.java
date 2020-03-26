package stonering.enums.items.recipe;

import stonering.util.global.Logger;

import java.util.Arrays;
import java.util.List;

/**
 * Processes {@link RawRecipe} into {@link Recipe}.
 *
 * @author Alexander_Kuzyakov on 23.05.2019.
 */
public class RecipeProcessor {

    public Recipe processRawRecipe(RawRecipe rawRecipe) {
        Logger.LOADING.logDebug("Processing recipe " + rawRecipe.name);
        Recipe recipe = new Recipe(rawRecipe);
        rawRecipe.parts.forEach(ingredient -> // map parts to ingredients
                recipe.parts.put(ingredient.get(0), processIngredient(ingredient.subList(1, ingredient.size())))
        );
        rawRecipe.consumed.forEach(ingredient -> // add consumed ingredients
                recipe.consumed.add(processIngredient(ingredient))
        );
        if (rawRecipe.main != null) recipe.main = processIngredient(rawRecipe.main);
        return recipe;
    }

    private Ingredient processIngredient(List<String> args) {
        if (!validateIngredient(args)) return null;
        List<String> itemTypes = Arrays.asList(args.get(0).split("/"));
        return new Ingredient(itemTypes, args.get(1), Integer.parseInt(args.get(2)));
        //TODO quantity
    }

    /**
     * Checks that ingredient definition has 3 non-null arguments
     */
    private boolean validateIngredient(List<String> args) {
        if (args == null && args.size() < 3) {
            Logger.LOADING.logError("Ingredient is empty or missing args.");
            return false;
        } else {
            for (String arg : args) {
                if (arg == null || arg.isEmpty()) {
                    Logger.LOADING.logError("Argument of ingredient has empty argument.");
                    return false;
                }
            }
        }
        return true;
    }
}
