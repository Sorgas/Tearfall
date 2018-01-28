package stonering.generators.items;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import stonering.objects.local_actors.Aspect;
import stonering.objects.local_actors.items.Item;
import stonering.objects.local_actors.items.aspects.PropertyAspect;
import stonering.utils.global.FileLoader;

/**
 * Created by Alexander on 26.01.2018.
 */
public class ItemGenerator {
    private JsonReader reader;
    private Json json;
    private JsonValue items;

    public ItemGenerator() {
        init();
    }

    private void init() {
        reader = new JsonReader();
        json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        items = reader.parse(FileLoader.getItems());
    }

    public Item generateItem(String title) {
        JsonValue itemJson = findItem(title);
        return createItem(itemJson);
    }

    private JsonValue findItem(String name) {
        for (JsonValue item : items) {
            if (item.getString("title").equals(name)) return item;
        }
        return null;
    }

    private Item createItem(JsonValue itemJson) {
        Item item = new Item(null);
        item.setTitle(itemJson.getString("title"));
        for (JsonValue aspect: itemJson.get("aspects")) {
            item.addAspect(readAspect(aspect, item));
        }
        return item;
    }

    private Aspect readAspect(JsonValue aspect, Item item) {
        String name = aspect.getString("name");
        Aspect aspect1 = null;
        switch (name) {
            case "tool": {
                PropertyAspect propertyAspect = new PropertyAspect(name, item);
                for (String arg : aspect.get("args").asStringArray()) {
                    propertyAspect.addProperty(arg);
                }
                aspect1 = propertyAspect;
            }
        }
        return aspect1;
    }
}
