package stonering.enums.items.recipe;

import stonering.enums.items.TagEnum;
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
    public final TagEnum tag;               // acceptable item tags
    private int quantity;                  // quantity
    //TODO add weight to ingredients consumed as matter

    private List<String> possibleMaterials;  // list of materials to display in ui

    public Ingredient(List<String> itemTypes, String tag) {
        this.itemTypes = new ArrayList<>(itemTypes);
        this.tag = TagEnum.get(tag);
        possibleMaterials = new ArrayList<>(MaterialMap.instance().getMaterialNamesByTag(tag));
        possibleMaterials.add(0, "any " + tag);
    }

    public List<String> getPossibleMaterials() {
        return possibleMaterials;
    }
}
