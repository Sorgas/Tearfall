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
    private ItemType type;

    private String title;
    private HashMap<String, ItemPart> parts;
    private String mainPart;
    private int baseValue;
    private int material;
    private int weight;

    public Item(Position position) {
        super(position);
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
                " material: " + material +
                " weight: " + weight;
    }

    public int getMaterial() {
        return material;
    }

    public void setMaterial(int material) {
        this.material = material;
    }

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

    public int getBaseValue() {
        return baseValue;
    }

    public void setBaseValue(int baseValue) {
        this.baseValue = baseValue;
    }
}
