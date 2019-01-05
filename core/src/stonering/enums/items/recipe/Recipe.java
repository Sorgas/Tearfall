package stonering.enums.items.recipe;

import stonering.util.global.TagLoggersEnum;

import java.util.List;

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
    private List<ItemPartRecipe> parts;  // itemPart NAME to material categories   //TODO map of item parts to allowed materials.
    //    private String material; // mvp material category


    public Recipe() {}

    public Recipe(String title) {
        this.title = title;
    }

    public ItemPartRecipe getItemPartRecipe(String itemPartName) {
        for (ItemPartRecipe part : parts) {
            if(part.getName().equals(itemPartName)) return part;
        }
        TagLoggersEnum.TASKS.logWarn("Item part with name " + itemPartName + " not found in recipe " + name);
        return null;
    }

    @Override
    public String toString() {
        return title;
    }

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ItemPartRecipe> getParts() {
        return parts;
    }

    public void setParts(List<ItemPartRecipe> parts) {
        this.parts = parts;
    }
}
