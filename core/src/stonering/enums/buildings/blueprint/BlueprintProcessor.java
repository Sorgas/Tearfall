package stonering.enums.buildings.blueprint;

import static stonering.enums.items.ItemTagEnum.MATERIAL;

import stonering.enums.items.recipe.IngredientProcessor;
import stonering.enums.items.type.ItemTypeMap;
import stonering.util.logging.Logger;

import java.util.Objects;

/**
 * Validates {@link RawBlueprint}, and processes it into {@link Blueprint}.
 *
 * @author Alexander on 02.12.2019.
 */
public class BlueprintProcessor {
    private IngredientProcessor ingredientProcessor = new IngredientProcessor();

    public Blueprint processRawBlueprint(RawBlueprint rawBlueprint) {
        Logger.LOADING.logDebug("Processing blueprint " + rawBlueprint.name);
        Blueprint blueprint = new Blueprint(rawBlueprint);
        rawBlueprint.ingredients.stream()
                .map(ingredientProcessor::parseIngredient)
                .filter(Objects::nonNull)
                .forEach(ingredient -> blueprint.ingredients.put(ingredient.key, ingredient));
        // if all item types for part are materials,
        blueprint.ingredients.forEach((part, ingredient) -> {
            boolean allItemTypesAreMaterial = ingredient.itemTypes.stream()
                    .map(typeName -> ItemTypeMap.instance().getItemType(typeName))
                    .allMatch(type -> type.tags.contains(MATERIAL));
            if (allItemTypesAreMaterial) {
                MaterialSelectionConfig config = new MaterialSelectionConfig();
                config.types.add(ingredient.itemTypes.get(0)); // only first item type is enabled initially
                blueprint.configMap.put(part, config);
            }
        });
        return blueprint;
    }
}
