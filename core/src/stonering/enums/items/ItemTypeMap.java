package stonering.enums.items;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import stonering.objects.local_actors.items.aspects.PropertyAspect;
import stonering.utils.global.FileLoader;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class ItemTypeMap {
    private static ItemTypeMap instance;
    private HashMap<String, ItemType> types;
    private Json json;

    private ItemTypeMap() {
        types = new HashMap<>();
        json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        json.addClassTag("color_c", Color.class);
        json.addClassTag("property_c", PropertyAspect.class);
        loadPlantTypes();
    }

    public static ItemTypeMap getInstance() {
        if (instance == null)
            instance = new ItemTypeMap();
        return instance;
    }

    private void loadPlantTypes() {
        System.out.println("loading item types");
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
