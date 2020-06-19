package stonering.enums.items.recipe;

import java.util.Arrays;
import java.util.List;

import stonering.enums.items.ItemTagEnum;
import stonering.util.logging.Logger;

/**
 * Validate and creates {@link Ingredient} from string arguments.
 *
 * @author Alexander on 5/24/2020
 */
public class IngredientProcessor {

    public Ingredient parseIngredient(String ingredientString) {
        if (!validateIngredient(ingredientString)) return null;
        String[] args = ingredientString.split(":");
        List<String> itemTypes = Arrays.asList(args[1].split("/"));
        ItemTagEnum tag = ItemTagEnum.get(args[2]);
        return new Ingredient(args[0], itemTypes, tag, Integer.parseInt(args[3]));
    }

    public boolean validateIngredient(String ingredientString) {
        String[] args = ingredientString.split(":");
        if (args.length < 4) return Logger.LOADING.logError("Ingredient has empty or missing args.", false);
        if (Arrays.stream(args).anyMatch(string -> string == null || string.isEmpty()))
            return Logger.LOADING.logError("Ingredient has empty argument.", false);
        if (!"any".equals(args[2]) && ItemTagEnum.get(args[2]) == null)
            return Logger.LOADING.logWarn("Ingredient tag is invalid", false);
        return true;
    }
}
