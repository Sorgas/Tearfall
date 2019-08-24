package stonering.entity.item;

import stonering.entity.PositionAspect;
import stonering.enums.items.type.ItemType;
import stonering.util.geometry.Position;
import stonering.entity.Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * In game item. Consists of parts.
 *
 * @author Alexander Kuzyakov on 09.12.2017.
 */
public class Item extends Entity {
    private String name;
    private String title;
    private ItemType type;
    public final List<String> tags;

    //TODO commented as non-MVP feature
//    private HashMap<String, ItemPart> parts;
//    private String mainPart;
    private int material;
    private int weight; // cache for faster counting.

    public Item(Position position, ItemType type) {
        super(position);
        this.type = type;
        this.name = type.name;
        this.title = type.title;
        tags = new ArrayList<>();
//        this.mainPart = type.getParts().get(0).getTitle();
//        parts = new HashMap<>();
    }

    public boolean isTool() {
        return type.tool != null;
    }

    public boolean isWear() {
        return type.wear != null;
    }

    @Override
    public String toString() {
        return "name: " + title +
                " position: " + getPosition() +
                " weight: " + weight;
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

    public Position getPosition() {
        return getAspect(PositionAspect.class).position;
    }

    public void setPosition(Position position) {
        getAspect(PositionAspect.class).position = position;
    }
}
