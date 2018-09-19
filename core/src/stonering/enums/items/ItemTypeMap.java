package stonering.enums.items;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import stonering.objects.local_actors.items.aspects.TagAspect;
import stonering.utils.global.FileLoader;

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
        json.addClassTag("color_c", Color.class);
        json.addClassTag("tool_c", ToolItemType.class);
        json.addClassTag("action_c", ToolItemType.ToolAction.class);
        json.addClassTag("attack_c", ToolItemType.ToolAttack.class);
        json.addClassTag("tags_c", TagAspect.class);
        ArrayList<ItemType> elements = json.fromJson(ArrayList.class, ItemType.class, FileLoader.getItemsFile());
        for (ItemType itemType : elements) {
            types.put(itemType.getTitle(), itemType);
        }
    }

    public ItemType getItemType(String title) {
        return types.get(title);
    }

    public Collection<ItemType> getAllTypes() {
        return types.values();
    }

    public boolean contains(String title) {
        return types.containsKey(title);
    }
}
