package stonering.objects.local_actors.items;

import stonering.enums.items.ItemType;
import stonering.global.utils.Position;
import stonering.objects.local_actors.AspectHolder;

/**
 * @author Alexander Kuzyakov on 09.12.2017.
 * <p>
 * In game item.
 */
public class Item extends AspectHolder {
    private ItemType type;

    private String title;
    private int baseValue;
    private int material;
    private int weight;

    public Item(Position position) {
        super(position);
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
