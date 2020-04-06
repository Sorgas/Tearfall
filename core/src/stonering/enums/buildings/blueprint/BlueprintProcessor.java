package stonering.enums.buildings.blueprint;

import org.apache.commons.lang3.StringUtils;
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
        rawBlueprint.parts.forEach(ingredientArgs -> // map parts to ingredients
                blueprint.parts.put(ingredientArgs.get(0), processIngredient(ingredientArgs, blueprint))
        );
        blueprint.initConfig();
        return blueprint;
    }

    private Ingredient processIngredient(List<String> args, Blueprint blueprint) {
        if (!validateIngredient(args, blueprint)) return null;
        List<String> itemTypes = Arrays.asList(args.get(1).split("/"));
        return new Ingredient(itemTypes, args.get(2), Integer.parseInt(args.get(3)));
    }

    /**
     * Ingredient arguments are:
     * Item types separated with '/'
     * Required tags separeted with '/'. Can be empty.
     * Quantity of items. TODO consider as material amount for material item types (logs, bars, rocks etc.)
     */
    private boolean validateIngredient(List<String> args, Blueprint blueprint) {
        if (args == null && args.size() < 4)
            return Logger.LOADING.logError(ingredientLog(args, blueprint) + " is empty or is missing args.", false);
        if (StringUtils.isEmpty(args.get(1)))
            return Logger.LOADING.logError(ingredientLog(args, blueprint) + " has empty 'item types' argument.", false);
        if (StringUtils.isEmpty(args.get(3)))
            return Logger.LOADING.logError(ingredientLog(args, blueprint) + " has empty 'quantity' argument.", false);
        return true;
    }

    private String ingredientLog(List<String> args, Blueprint blueprint) {
        return "Ingredient " + args.get(0) + " of blueprint " + blueprint.name;
    }
}
