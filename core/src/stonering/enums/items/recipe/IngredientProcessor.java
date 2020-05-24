package stonering.enums.items.recipe;

import java.util.Arrays;
import java.util.List;

import stonering.enums.items.ItemTagEnum;
import stonering.util.global.Logger;

/**
 * Validate and creates {@link Ingredient} from string arguments.
 *
 * @author Alexander on 5/24/2020
 */
public class IngredientProcessor {

    public Ingredient parseIngredient(String ingredientString) {
        if (!validateIngredient(ingredientString)) return null;
        String[] args = ingredientString.split(":");
        List<String> itemTypes = Arrays.asList(args[0].split("/"));
        return new Ingredient(itemTypes, args[1], Integer.parseInt(args[2]));
    }

    public boolean validatePartIngredient(List<String> list) {
        if(list.size() < 2) return Logger.LOADING.logError("Part ingredient has empty or missing args.", false);
        if(list.get(0) == null || list.get(0).isEmpty()) return Logger.LOADING.logError("Part ingredient has empty part name.", false);
        return validateIngredient(list.get(1));
    }

    public boolean validateIngredient(String ingredientString) {
        String[] args = ingredientString.split(":");
        if (args.length < 3) return Logger.LOADING.logError("Ingredient has empty or missing args.", false);
        if (Arrays.stream(args).anyMatch(string -> string == null || string.isEmpty()))
            return Logger.LOADING.logError("Ingredient has empty argument.", false);
        if (ItemTagEnum.get(args[1]) == ItemTagEnum.DEFAULT_TAG)
            return Logger.LOADING.logWarn("Ingredient tag is invalid", false);
        return true;
    }
}
