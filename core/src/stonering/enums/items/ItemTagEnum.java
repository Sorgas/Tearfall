package stonering.enums.items;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import stonering.entity.item.Item;
import stonering.entity.item.aspects.FoodItemAspect;

/**
 * Enumeration of all tags for items.
 * Food tags - food tags influence, which food item will be taken for eating.
 * RAW - gives penalty on eating. Lower selection priority.
 * SPOILED - gives penalty on eating. Lower selection priority.
 * PREPARED - gives bonus on eating. Increased selection priority.
 * Fruits and vegetables have no RAW tag and no penalties on eating.
 *
 * @author Alexander on 02.09.2019.
 */
public enum ItemTagEnum {
    SOIL,
    STONE, // gabbro(material) rock(type) // stones have no origin
    STONE_EXTRUSIVE, // used for stone layers generation
    STONE_INTRUSIVE,
    STONE_SEDIMENTARY,
    METAL, // brass(material) bar(type)
    WOOD, // birch(material) log(type)
    MEAT, // fox(origin) meat(material) piece(type)
    ORE, // magnetite(material) rock(type)

    COOKABLE, // can be boiled or roasted
    BREWABLE, // item can be prepared into drink
    DRINKABLE, // TODO replace with aspect
    RAW(true), // raw cow meat piece,
    SPOILED(true, item -> item.optional(FoodItemAspect.class).ifPresent(aspect -> aspect.nutrition = 10)), // spoiled raw cow meat peace
    CORPSE(true),
    SAPIENT(),
    PREPARED(true, item -> item.optional(FoodItemAspect.class).ifPresent(aspect -> aspect.nutrition += 20)), // cow meat stew

    SEED_PRODUCE(false),
    WATER,
    CLOTH,
    MATERIAL; // item is raw material for building and crafting

    private static boolean debug = false;
    private boolean displayable; // tags with true are displayed in items titles.
    public Consumer<Item> onAdd;
    private static Map<String, ItemTagEnum> map = new HashMap<>();

    static {
        for (ItemTagEnum tag : values()) {
            map.put(tag.name().toLowerCase(), tag);
        }
    }

    ItemTagEnum(boolean displayable, Consumer<Item> onAdd) {
        this.displayable = displayable;
        this.onAdd = onAdd;
    }

    ItemTagEnum(boolean displayable) {
        this(false, null);
    }

    ItemTagEnum() {
        this(false);
    }

    public static ItemTagEnum get(String name) {
        return map.get(name);
    }

    public boolean isDisplayable() {
        return debug || displayable;
    }
}
