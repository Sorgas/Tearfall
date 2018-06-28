package stonering.enums.buildings;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import stonering.utils.global.FileLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Singleton map of building descriptors. Descriptors are stored by their names.
 *
 * @author Alexander Kuzyakov
 */
public class BuildingMap {
    private static BuildingMap instance;
    private HashMap<String, BuildingType> buildings;
    private Json json;

    private BuildingMap() {
        buildings = new HashMap<>();
        json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        json.addClassTag("color_c", Color.class);
        loadMaterials();
    }

    public static BuildingMap getInstance() {
        if (instance == null)
            instance = new BuildingMap();
        return instance;
    }

    private void loadMaterials() {
        System.out.println("loading buildings");
        ArrayList<BuildingType> elements = json.fromJson(ArrayList.class, BuildingType.class, FileLoader.getBuildingsFile());
        for (BuildingType buildingType : elements) {
            buildings.put(buildingType.getTitle(), buildingType);
        }
    }

    public boolean hasMaterial(String title) {
        return buildings.containsKey(title);
    }

    public BuildingType getBuilding(String name) {
        return buildings.get(name);
    }

    public List<BuildingType> getCategoryBuildings(String categoryName) {
        return buildings.values().stream().filter((buildingType -> buildingType.getCategory().equals(categoryName))).collect(Collectors.toList());
    }
}
