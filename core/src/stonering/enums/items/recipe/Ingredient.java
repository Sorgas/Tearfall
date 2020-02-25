package stonering.enums.items.recipe;

import stonering.entity.item.Item;
import stonering.enums.items.ItemTagEnum;
import stonering.enums.materials.MaterialMap;

import java.util.ArrayList;
import java.util.List;

/**
 * Determines ingredient for crafting. Is part of {@link Recipe}.
 * Items that have one of item types, ha
 *
 * @author Alexander on 05.01.2019.
 */
public class Ingredient {
    public final List<String> itemTypes;    // acceptable item types
    public final ItemTagEnum tag;               // acceptable item tags
    public final int quantity = 1;                   // number of items
    public final String text;

    private List<String> possibleMaterials;  // list of materials to display in ui

    public Ingredient(List<String> itemTypes, String tag) {
        this.itemTypes = new ArrayList<>(itemTypes);
        this.tag = ItemTagEnum.get(tag);
        possibleMaterials = new ArrayList<>(MaterialMap.instance().getMaterialNamesByTag(tag));
        possibleMaterials.add(0, "any " + tag);
        text = tag + " " + getTypesPart();
    }

    private String getTypesPart() {
        if(itemTypes.size() == 1) {
            return itemTypes.get(0);
        } else {
            StringBuilder part = new StringBuilder();
            for (int i = 0; i < itemTypes.size(); i++) {
                String itemType = itemTypes.get(i);
                part.append(itemType);
                if (i != itemTypes.size() - 1) part.append(i == itemTypes.size() - 2 ? " or " : ", ");
            }
            return part.toString();
        }
    }

    public List<String> getPossibleMaterials() {
        return possibleMaterials;
    }
    
    public boolean checkItem(Item item) {
        return item.tags.contains(tag) && itemTypes.contains(item.type.name);
    }
}
