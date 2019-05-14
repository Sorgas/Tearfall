package stonering.enums.items.type;

import com.badlogic.gdx.files.FileHandle;
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
        FileHandle itemsDirectory = FileLoader.getFile(FileLoader.ITEMS_PATH);
        for (FileHandle fileHandle : itemsDirectory.list()) {
            if(fileHandle.isDirectory()) continue;
            ArrayList<ItemType> elements = json.fromJson(ArrayList.class, ItemType.class, fileHandle);
            for (ItemType itemType : elements) {
                initItemType(itemType);
                types.put(itemType.name, itemType);
            }
        }
    }

    /**
     * Generates title, if needed.
     */
    private void initItemType(ItemType itemType) {
        if (itemType.title == null) {
            itemType.title = itemType.name.substring(0, 1).toUpperCase() + itemType.name.substring(1);
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
