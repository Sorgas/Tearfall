package stonering.enums.items.type;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import stonering.enums.items.type.raw.RawItemType;
import stonering.enums.items.type.raw.RawItemTypeProcessor;
import stonering.util.lang.FileUtil;
import stonering.util.logging.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
            List<RawItemType> elements = json.fromJson(ArrayList.class, RawItemType.class, file);
            if(elements == null) return;
            elements.stream()
                    .sorted((type1, type2) -> type1.baseItem == null ? 1 : -1)
                    .map(processor::process)
                    .peek(type -> type.atlasName = file.nameWithoutExtension())
                    .forEach(type -> types.put(type.name, type));
            Logger.LOADING.logDebug(elements.size() + " loaded from " + file.path());
        });
    }

    public static ItemType getItemType(String name) {
        return instance().types.get(name);
    }

    public static boolean contains(String title) {
        return instance().types.containsKey(title);
    }
}
