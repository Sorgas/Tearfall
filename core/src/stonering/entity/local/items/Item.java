package stonering.entity.local.items;

import stonering.enums.items.ItemType;
import stonering.global.utils.Position;
import stonering.entity.local.AspectHolder;

import java.util.HashMap;

/**
 * In game item. Consists of parts.
 *
 * @author Alexander Kuzyakov on 09.12.2017.
 */
public class Item extends AspectHolder {
    private String name;
    private String title;
    private ItemType type;
//TODO commented as non-MVP feature
//    private HashMap<String, ItemPart> parts;
//    private String mainPart;
    private int material;
    private int weight; // cache for faster counting.

    public Item(Position position, ItemType type) {
        super(position);
        this.type = type;
        this.name = type.getName();
        this.title = type.getTitle();
//        this.mainPart = type.getSteps().get(0).getTitle();
//        parts = new HashMap<>();
    }

    public boolean isTool() {
        return type.getTool() != null;
    }

    public boolean isWear() {
        return type.getWear() != null;
    }

    @Override
    public String toString() {
        return "title: " + title +
                " position: " + position +
                " weight: " + weight;
    }

//    public int getMainMaterial() {
//        return parts.get(mainPart).getMaterial();
//    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ItemType getType() {
        return type;
    }

    public void setType(ItemType type) {
        this.type = type;
    }

//    public HashMap<String, ItemPart> getParts() {
//        return parts;
//    }
//
//    public void setParts(HashMap<String, ItemPart> parts) {
//        this.parts = parts;
//    }

//    public String getMainPart() {
//        return mainPart;
//    }
//
//    public void setMainPart(String mainPart) {
//        this.mainPart = mainPart;
//    }
//
//    public ItemPart getMainPart_() {
//        return parts.get(mainPart);
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaterial() {
        return material;
    }

    public void setMaterial(int material) {
        this.material = material;
    }
}