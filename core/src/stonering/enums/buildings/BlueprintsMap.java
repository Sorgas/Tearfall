package stonering.enums.buildings;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import stonering.entity.local.building.Blueprint;
import stonering.util.global.FileLoader;
import stonering.util.global.TagLoggersEnum;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Stores all blueprints from blueprints.json.
 */
public class BlueprintsMap {
    private static BlueprintsMap instance;
    private HashMap<String, Blueprint> blueprints;
    private Json json;

    private BlueprintsMap() {
        blueprints = new HashMap<>();
        json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        loadBlueprints();
    }

    public static BlueprintsMap getInstance() {
        if (instance == null)
            instance = new BlueprintsMap();
        return instance;
    }

    private void loadBlueprints() {
        TagLoggersEnum.LOADING.log("blueprints");
        ArrayList<Blueprint> elements = json.fromJson(ArrayList.class, Blueprint.class, FileLoader.getFile(FileLoader.BLUEPRINTS_PATH));
        for (Blueprint blueprint : elements) {
            blueprints.put(blueprint.getName(), blueprint);
            blueprint.init();
        }
    }

    public Blueprint getBlueprint(String name) {
        return blueprints.get(name);
    }

    public HashMap<String, Blueprint> getBlueprints() {
        return blueprints;
    }
}
