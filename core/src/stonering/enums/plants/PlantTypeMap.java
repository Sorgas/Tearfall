package stonering.enums.plants;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import stonering.utils.global.FileLoader;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

// loads plant types fromm plants.json
public class PlantTypeMap {
    private HashMap<String, PlantType> types;
    private Json json;

    public PlantTypeMap() {
        types = new HashMap<>();
        json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        json.addClassTag("color_c", Color.class);
        loadPlantTypes();
    }

    private void loadPlantTypes() {
        System.out.println("loading types");
        ArrayList<PlantType> elements = json.fromJson(ArrayList.class, PlantType.class, FileLoader.getTreesFile());
        for (PlantType plantType : elements) {
            types.put(plantType.getSpecimen(), plantType);
        }
    }

    public PlantType getTreeType(String speciment) {
        return types.get(speciment);
    }

    public Collection<PlantType> getAllTypes() {
        return types.values();
    }
}
