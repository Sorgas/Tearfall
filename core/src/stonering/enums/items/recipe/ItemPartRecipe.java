package stonering.enums.items.recipe;

import stonering.enums.materials.MaterialMap;

import java.util.ArrayList;
import java.util.List;

/**
 * Determines crafting of item part. Is part of {@link Recipe}.
 *
 * @author Alexander on 05.01.2019.
 */
public class ItemPartRecipe {
    public String itemPart;          // produced item part
    // properties of ingredient
    public final List<String> itemTypes;   // acceptable item types
    public final String materialTag;       // acceptable material
    private int quantity;            // quantity
    //TODO add weight to ingredients consumed as matter

    private List<String> possibleMaterials;  // list of materials to display in ui

    public ItemPartRecipe(String itemPart, List<String> itemTypes, String materialTag) {
        this.itemPart = itemPart;
        this.itemTypes = itemTypes;
        this.materialTag = materialTag;
        possibleMaterials = new ArrayList<>(MaterialMap.instance().getMaterialNamesByTag(materialTag));
        possibleMaterials.add(0, "any " + materialTag);
    }

    public List<String> getPossibleMaterials() {
        return possibleMaterials;
    }
}
