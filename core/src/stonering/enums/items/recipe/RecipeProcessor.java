package stonering.enums.items.recipe;

import stonering.util.global.Logger;

import java.util.List;

/**
 * Processes {@link RawRecipe} into {@link Recipe}
 *
 * @author Alexander_Kuzyakov on 23.05.2019.
 */
public class RecipeProcessor {

    public Recipe processRawRecipe(RawRecipe rawRecipe) {
        Recipe recipe = new Recipe(rawRecipe.title);
        recipe.name = rawRecipe.name;
        recipe.itemName = rawRecipe.itemName;
        rawRecipe.parts.forEach(part -> {
            ItemPartRecipe partRecipe = processItemPartRecipe(part, recipe.name);
            recipe.parts.put(partRecipe.name, partRecipe);
        });
        return recipe;
    }

    private ItemPartRecipe processItemPartRecipe(List<String> args, String recipe) {
        if (!validateItemPartArgs(args, recipe)) return null;
        return new ItemPartRecipe(args.get(0), args.get(1));
    }

    private boolean validateItemPartArgs(List<String> args, String recipe) {
        if (args != null && args.size() > 1) return true;
        Logger.LOADING.logWarn("Item part arguments in recipe " + recipe + " has invalid arguments.");
        return false;
    }
}
