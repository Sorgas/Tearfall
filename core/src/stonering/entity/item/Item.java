package stonering.entity.item;

import stonering.entity.Aspect;
import stonering.entity.PositionAspect;
import stonering.entity.PositionedEntity;
import stonering.enums.items.TagEnum;
import stonering.enums.items.type.ItemType;
import stonering.util.geometry.Position;

import java.util.ArrayList;
import java.util.List;

/**
 * In game item. Consists of parts.
 * Some items can have origin (cow meat, apple (fruit)).
 * Origin is set on item creation, and not mentioned on item definition and {@link ItemType}, instead of this, sources of items (animals, plants), give them origin.
 * Different effects, like poisonous, are provided with {@link Aspect}s.
 *
 * @author Alexander Kuzyakov on 09.12.2017.
 */
public class Item extends PositionedEntity {
    private String name;
    private String title; // title combined of origin, material, and type
    private String conditionPostfix; // put after title in a brackets. shows item's condition, like raw, spoiled, rusty
    private String origin; // set on item creation,
    private int material;
    private ItemType type;
    public final List<TagEnum> tags;
    private int weight; // cache for faster counting.

    //TODO commented as non-MVP feature
    //    private HashMap<String, ItemPart> parts;
    //    private String mainPart;

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

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getConditionPostfix() {
        return conditionPostfix;
    }

    public void setConditionPostfix(String conditionPostfix) {
        this.conditionPostfix = conditionPostfix;
    }
}
