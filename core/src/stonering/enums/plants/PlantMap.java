package stonering.enums.plants;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import stonering.util.global.FileLoader;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

// loads plant types fromm plants.json
public class PlantMap {
    private static PlantMap instance;
    private HashMap<String, PlantType> types;
    private Json json;

    private PlantMap() {
        types = new HashMap<>();
        json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        json.addClassTag("color_c", Color.class);
        json.addClassTag("tree_c", TreeType.class);
        json.addClassTag("stage_c", PlantType.PlantLifeStage.class);
        System.out.println("loading plant types");
        //TODO add json validation
        loadPlantTypes();
        loadTreeTypes();
    }

    public static PlantMap getInstance() {
        if (instance == null)
            instance = new PlantMap();
        return instance;
    }

    private void loadPlantTypes() {
        ArrayList<PlantType> elements = json.fromJson(ArrayList.class, PlantType.class, FileLoader.getFile(FileLoader.PLANTS_PATH));
        for (PlantType plantType : elements) {
            types.put(plantType.getName(), plantType);
        }
    }

    private void loadTreeTypes() {
        ArrayList<PlantType> elements = json.fromJson(ArrayList.class, PlantType.class, FileLoader.getFile(FileLoader.TREES_PATH));
        for (PlantType plantType : elements) {
            types.put(plantType.getName(), plantType);
        }
    }

    public PlantType getPlantType(String specimen) {
        return types.get(specimen);
    }

    public Collection<PlantType> getAllTypes() {
        return types.values();
    }
}
