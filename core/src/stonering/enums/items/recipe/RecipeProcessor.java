package stonering.enums.items.recipe;

import stonering.util.global.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Processes {@link RawRecipe} into {@link Recipe}.
 *
 * @author Alexander_Kuzyakov on 23.05.2019.
 */
public class RecipeProcessor {

    public Recipe processRawRecipe(RawRecipe rawRecipe) {
        Recipe recipe = new Recipe(rawRecipe);
        if(rawRecipe.parts == null) Logger.LOADING.logError("Recipe " + rawRecipe.name + " has no parts.");
        rawRecipe.parts.forEach(ingredient -> // map parts to ingredients
                recipe.parts.put(ingredient.get(0), processIngredient(ingredient.subList(1, ingredient.size()), recipe.name))
        );
        rawRecipe.consumed.forEach(ingredient -> // add consumed ingredients
                recipe.consumed.add(processIngredient(ingredient, recipe.name))
        );
        return recipe;
    }

    private Ingredient processIngredient(List<String> args, String recipe) {
        if (!validateIngredient(args, recipe)) return null;
        List<String> itemTypes = args.get(1) != null ? Arrays.asList(args.get(1).split("/")) : new ArrayList<>();
        return new Ingredient(itemTypes, args.get(2));
    }

    /**
     * Checks that ingredient definition has 3 non-null arguments
     */
    private boolean validateIngredient(List<String> args, String recipe) {
        if (args == null && args.size() < 3) {
            Logger.LOADING.logError("Consumed ingredient in recipe " + recipe + "is empty or missing args.");
            return false;
        } else {
            for (String arg : args) {
                if (arg == null || arg.isEmpty()) {
                    Logger.LOADING.logError("Argument of consumed ingredient in recipe " + recipe + "has empty argument.");
                    return false;
                }
            }
        }
        return true;
    }
}
