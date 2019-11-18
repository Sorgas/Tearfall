package stonering.entity.item;

import stonering.entity.Aspect;
import stonering.entity.Entity;
import stonering.enums.items.TagEnum;
import stonering.enums.items.type.ItemType;
import stonering.enums.materials.MaterialMap;
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
public class Item extends Entity {
    private String name;
    private String title; // title combined of origin, material, and type
    private String origin; // set on item creation,
    private int material;
    public String materialString;
    private ItemType type;
    public final List<TagEnum> tags;
    public boolean locked; // item consuming actions lock target items.

    //TODO commented as non-MVP feature
    //    private HashMap<String, ItemPart> parts;
    //    private String mainPart;

    public Item(Position position, ItemType type) {
        super(position);
        this.type = type;
        this.name = type.name;
        this.title = type.title;
        tags = new ArrayList<>();
    }

    public boolean isTool() {
        return type.tool != null;
    }

    @Override
    public <T extends Aspect> boolean hasAspect(Class<T> type) {
        return super.hasAspect(type) || this.type.hasAspect(type);
    }

    @Override
    public <T extends Aspect> T getAspect(Class<T> type) {
        if (this.type.hasAspect(type)) return this.type.getAspect(type);
        return super.getAspect(type);
    }

    @Override
    public String toString() {
        return "name: " + title + " position: " + position;
    }

    public String updateTitle() {
        return title = (origin != null ? origin + " " : "") +
                materialString + " " +
                type.title;
    }

    public void setMaterial(int material) {
        this.material = material;
        materialString = MaterialMap.instance().getMaterial(material).getName();
        updateTitle();
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
        updateTitle();
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

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
        updateTitle();
    }
}
