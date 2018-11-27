package stonering.enums.items;

/**
 * Recipe for crafting.
 * Contains info about result and required materials.
 *
 * @author Alexander on 19.11.2018.
 */
public class Recipe {
    private String name;     //recipe name
    private String itemName; //item name, points to ItemType
    private String material; //mvp

    //TODO map of item parts to allowed materials.
//    private Map<String, List<String>>                            // itemPart name to material category
    //TODO additional materials, skill requirement, exp gained, time spent.

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }
}
