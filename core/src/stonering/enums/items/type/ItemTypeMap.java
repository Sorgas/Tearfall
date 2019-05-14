package stonering.enums.items.type;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import stonering.util.global.FileLoader;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * Singleton map for all item types. types should have unique names.
 */
public class ItemTypeMap {
    private static ItemTypeMap instance;
    private HashMap<String, ItemType> types;

    private ItemTypeMap() {
        types = new HashMap<>();
        loadItemTypes();
    }

    public static ItemTypeMap getInstance() {
        if (instance == null)
            instance = new ItemTypeMap();
        return instance;
    }

    private void loadItemTypes() {
        System.out.println("loading item types");
        Json json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        ArrayList<ItemType> elements = json.fromJson(ArrayList.class, ItemType.class, FileLoader.getFile(FileLoader.ITEMS_PATH));
        for (ItemType itemType : elements) {
            initItemType(itemType);
            types.put(itemType.getName(), itemType);
        }
    }

    /**
     * Generates title, if needed.
     */
    private void initItemType(ItemType itemType) {
        if (itemType.getTitle() == null) {
            itemType.setTitle(itemType.getName().substring(0, 1).toUpperCase() + itemType.getName().substring(1));
        }
    }

    public ItemType getItemType(String name) {
        return types.get(name);
    }

    public Collection<ItemType> getAllTypes() {
        return types.values();
    }

    public boolean contains(String title) {
        return types.containsKey(title);
    }
}
