package stonering.enums.items.type;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import stonering.util.global.FileLoader;
import stonering.util.global.Logger;

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
            ArrayList<RawItemType> elements = json.fromJson(ArrayList.class, RawItemType.class, fileHandle);
            for (RawItemType rawItemType : elements) {
                types.put(rawItemType.name, processRawItemType(rawItemType));
            }
            Logger.LOADING.logDebug(elements.size() + " loaded from " + fileHandle.path());
        }
    }

    private ItemType processRawItemType(RawItemType rawItemType) {
        ItemType itemType = new ItemType(rawItemType);
        if (itemType.title == null) {
            itemType.title = itemType.name.substring(0, 1).toUpperCase() + itemType.name.substring(1);
        }
        return itemType;
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
