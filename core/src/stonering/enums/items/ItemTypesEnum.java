package stonering.enums.items;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import stonering.enums.blocks.BlockTypesEnum;
import stonering.utils.global.FileLoader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by Alexander on 08.01.2018.
 */
public enum ItemTypesEnum {
    ROCK;

    static {
        map = new HashMap<>();
        fillMap();
    }

    private static HashMap<ItemTypesEnum, ArrayList<String>> map;

    private ArrayList<String> getTags() {
        return map.get(this);
    }

    private static void fillMap() {
        JsonReader reader = new JsonReader();
        Json json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        JsonValue tags = reader.parse(FileLoader.getItemTypeTags());
        for (ItemTypesEnum itemTypesEnum : (ItemTypesEnum.values())) {
            JsonValue t = tags.get(itemTypesEnum.name());
            map.put(itemTypesEnum, new ArrayList<>(Arrays.asList(t.get("tags").asStringArray())));
        }
    }
}
