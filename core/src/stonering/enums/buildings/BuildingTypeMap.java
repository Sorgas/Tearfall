package stonering.enums.buildings;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import stonering.enums.items.recipe.RecipeMap;
import stonering.enums.plants.PlantType;
import stonering.util.global.FileLoader;
import stonering.util.global.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Singleton map of building descriptors. Descriptors are stored by their names.
 *
 * @author Alexander Kuzyakov
 */
public class BuildingTypeMap {
    private static BuildingTypeMap instance;
    private HashMap<String, BuildingType> buildings;
    private Json json;

    private BuildingTypeMap() {
        buildings = new HashMap<>();
        json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        loadTypesFileToMap(FileLoader.BUILDINGS_PATH, buildings);
        loadTypesFileToMap(FileLoader.CONSTRUCTIONS_PATH, buildings);
        loadTypesFileToMap(FileLoader.FURNITURE_PATH, buildings);
        loadLists();
    }

    public static BuildingTypeMap instance() {
        if (instance == null)
            instance = new BuildingTypeMap();
        return instance;
    }

    /**
     * Loads {@link PlantType} from given file into given file.
     */
    private void loadTypesFileToMap(String filePath, Map<String, BuildingType> map) {
        List<BuildingType> elements = json.fromJson(ArrayList.class, BuildingType.class, FileLoader.getFile(filePath));
        for (BuildingType buildingType : elements) {
            buildings.put(buildingType.building, buildingType);
        }
        Logger.LOADING.logDebug(map.keySet().size() + " loaded from " + filePath);
    }

    /**
     * Loads lists of crafting recipes for building. First item in array should be building name.
     */
    private void loadLists() {
        Logger.LOADING.log("building recipes");
        RecipeMap recipeMap = RecipeMap.instance();
        ArrayList<ArrayList<String>> elements = json.fromJson(ArrayList.class, ArrayList.class, FileLoader.getFile(FileLoader.RECIPE_LISTS_PATH));
        for (List<String> recipeList : elements) {
            String buildingName = recipeList.remove(0);
            if(buildings.containsKey(buildingName)) {
                BuildingType type = buildings.get(buildingName);
                type.recipes = recipeList.stream().filter(recipeMap::hasRecipe).collect(Collectors.toList());
            }
        }
    }

    public boolean hasMaterial(String title) {
        return buildings.containsKey(title);
    }

    public BuildingType getBuilding(String name) {
        return buildings.get(name);
    }

    public boolean hasBuilding(String name) {
        return buildings.containsKey(name);
    }
}
