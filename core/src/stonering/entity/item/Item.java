package stonering.entity.item;

import stonering.entity.Aspect;
import stonering.entity.Entity;
import stonering.enums.items.ItemTagEnum;
import stonering.enums.items.type.ItemType;
import stonering.util.geometry.Position;

import java.util.ArrayList;
import java.util.List;

/**
 * In-game item.
 * 
 * Different effects, like poisonous, are provided with {@link Aspect}s.
 * TODO Consists of parts (post mvp).
 * TODO Some items can have origin (cow meat, apple (fruit)).
 * TODO Origin is set on item creation, and not mentioned on item definition and {@link ItemType}, instead of this, sources of items (animals, plants), give them origin.
 *
 * @author Alexander Kuzyakov on 09.12.2017.
 */
public class Item extends Entity {
    public final ItemType type;
    public int material;
    public final List<ItemTagEnum> tags;
    
    public String title; // title combined of origin, material, and type
    public String materialString;
    public boolean locked; // item consuming actions lock target items.
    public boolean destroyed; // items removed from item container considered destroyed

//TODO post mvp
//    private String origin; // set on item creation,
//    public final Map<String, ItemPart> parts;
//    public ItemPart mainPart;

    public Item(Position position, ItemType type) {
        super(position);
        this.type = type;
        this.title = type.title;
        locked = false;
        tags = new ArrayList<>();
    }

    public boolean isTool() {
        return type.tool != null;
    }

    @Override
    public <T extends Aspect> boolean has(Class<T> type) {
        return super.has(type) || this.type.has(type);
    }

    @Override
    public <T extends Aspect> T get(Class<T> type) {
        if (this.type.has(type)) return this.type.get(type);
        return super.get(type);
    }

    public String updateTitle() {
        return title = materialString + " " + type.title;
    }

    public ItemType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "name: " + title + " position: " + position;
    }
}
