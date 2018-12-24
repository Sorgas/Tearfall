package stonering.enums.items;

/**
 * Recipe for crafting.
 * Contains info about result and required materials.
 *
 * @author Alexander on 19.11.2018.
 */
public class Recipe {
    private String name;     // recipe NAME
    private String title;    // displayed title
    private String itemName; // item NAME, points to ItemType
//    private String skill;
//    private int duration;                                                         //TODO crafting with usage of multiple workbenches.
//    private int expGain;
//    private Map<String, List<String>>   // itemPart NAME to material categories   //TODO map of item parts to allowed materials.

    private String material; // mvp material category

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
