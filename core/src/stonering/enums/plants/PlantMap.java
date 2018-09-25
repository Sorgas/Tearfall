package stonering.enums.plants;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import stonering.enums.materials.MaterialMap;
import stonering.utils.global.FileLoader;

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
        loadPlantTypes();
    }

    public static PlantMap getInstance() {
        if (instance == null)
            instance = new PlantMap();
        return instance;
    }

    private void loadPlantTypes() {
        System.out.println("loading plant types");
        ArrayList<PlantType> elements = json.fromJson(ArrayList.class, PlantType.class, FileLoader.getPlantsFile());
        for (PlantType plantType : elements) {
            types.put(plantType.getSpecimen(), plantType);
        }
    }

    public PlantType getPlantType(String speciment) {
        return types.get(speciment);
    }

    public Collection<PlantType> getAllTypes() {
        return types.values();
    }
}
