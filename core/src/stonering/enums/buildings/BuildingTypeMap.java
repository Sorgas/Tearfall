package stonering.enums.buildings;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import stonering.entity.local.building.BuildingType;
import stonering.enums.plants.PlantType;
import stonering.enums.plants.PlantTypeProcessor;
import stonering.enums.plants.RawPlantType;
import stonering.util.global.FileLoader;
import stonering.util.global.TagLoggersEnum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public static BuildingTypeMap getInstance() {
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
        TagLoggersEnum.LOADING.logDebug(map.keySet().size() + " loaded from " + filePath);
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

    private void loadLists() {
        TagLoggersEnum.LOADING.log("crafting recipes");
        ArrayList<RecipeList> elements = json.fromJson(ArrayList.class, RecipeList.class, FileLoader.getFile(FileLoader.RECIPE_LISTS_PATH));
        for (RecipeList recipeList : elements) {
            if (validateList(recipeList)) {
                BuildingType type = buildings.get(recipeList.workbench);
                recipeList.recipes.forEach(s -> type.recipes.add(s));
            }
        }
    }

    private boolean validateList(RecipeList list) {
        return buildings.keySet().contains(list.workbench);
    }

    private static class RecipeList {
        String workbench;
        List<String> recipes;

        public RecipeList() {
            recipes = new ArrayList<>();
        }
    }
}
