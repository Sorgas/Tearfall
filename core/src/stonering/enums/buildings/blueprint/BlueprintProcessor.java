package stonering.enums.buildings.blueprint;

import stonering.enums.items.recipe.Ingredient;
import stonering.util.global.Logger;

import java.util.Arrays;
import java.util.List;

/**
 * Validates {@link RawBlueprint}, and processes it into {@link Blueprint}.
 *
 * @author Alexander on 02.12.2019.
 */
public class BlueprintProcessor {

    public Blueprint processRawBlueprint(RawBlueprint rawBlueprint) {
        Logger.LOADING.logDebug("Processing blueprint " + rawBlueprint.name);
        Blueprint blueprint = new Blueprint(rawBlueprint);
        rawBlueprint.parts.forEach(ingredient -> // map parts to ingredients
                blueprint.parts.put(ingredient.get(0), processIngredient(ingredient.subList(1, ingredient.size())))
        );
        return blueprint;
    }

    private Ingredient processIngredient(List<String> args) {
        if (!validateIngredient(args)) return null;
        List<String> itemTypes = Arrays.asList(args.get(0).split("/"));
        return new Ingredient(itemTypes, args.get(1), Integer.parseInt(args.get(2)));
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
