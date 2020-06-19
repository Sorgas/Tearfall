package stonering.enums.items.type;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import stonering.enums.items.type.raw.RawItemType;
import stonering.enums.items.type.raw.RawItemTypeProcessor;
import stonering.util.global.FileUtil;
import stonering.util.logging.Logger;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Singleton map for all {@link ItemType}s. Types should have unique names.
 * Types are defined in json files in resources/items directory.
 * Files content is split into separate files for convenience.
 */
public class ItemTypeMap {
    private static ItemTypeMap instance;
    private HashMap<String, ItemType> types;

    private ItemTypeMap() {
        types = new HashMap<>();
        loadItemTypes();
    }

    public static ItemTypeMap instance() {
        if (instance == null)
            instance = new ItemTypeMap();
        return instance;
    }

    private void loadItemTypes() {
        Logger.LOADING.logDebug("loading item types");
        Json json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        RawItemTypeProcessor processor = new RawItemTypeProcessor();
        FileUtil.iterate(FileUtil.ITEMS_PATH, file -> {
            ArrayList<RawItemType> elements = json.fromJson(ArrayList.class, RawItemType.class, file);
            elements.forEach(rawItemType -> types.put(rawItemType.name, processor.process(rawItemType)));
            Logger.LOADING.logDebug(elements.size() + " loaded from " + file.path());
        });
    }

    public ItemType getItemType(String name) {
        return types.get(name);
    }

    public boolean contains(String title) {
        return types.containsKey(title);
    }
}
