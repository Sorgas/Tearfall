package stonering.enums.items.recipe;

import stonering.enums.materials.MaterialMap;

import java.util.ArrayList;
import java.util.List;

/**
 * Determines ingredient for crafting. Is part of {@link Recipe}.
 *
 * @author Alexander on 05.01.2019.
 */
public class Ingredient {
    // properties of ingredient
    public final List<String> itemTypes;   // acceptable item types
    public final String materialTag;       // acceptable material
    private int quantity;                  // quantity
    //TODO add weight to ingredients consumed as matter

    private List<String> possibleMaterials;  // list of materials to display in ui

    public Ingredient(List<String> itemTypes, String materialTag) {
        this.itemTypes = itemTypes;
        this.materialTag = materialTag;
        possibleMaterials = new ArrayList<>(MaterialMap.instance().getMaterialNamesByTag(materialTag));
        possibleMaterials.add(0, "any " + materialTag);
    }

    public List<String> getPossibleMaterials() {
        return possibleMaterials;
    }
}
