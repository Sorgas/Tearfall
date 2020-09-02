package stonering.enums.buildings;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import stonering.enums.items.recipe.RecipeMap;
import stonering.util.lang.FileUtil;
import stonering.util.logging.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        BuildingTypeProcessor processor = new BuildingTypeProcessor();
        FileUtil.iterate(FileUtil.BUILDINGS_PATH, file -> {
            List<RawBuildingType> rawTypes = json.fromJson(ArrayList.class, RawBuildingType.class, file);
            rawTypes.stream()
                    .map(processor::process)
                    .peek(type -> type.atlasName = file.nameWithoutExtension())
                    .forEach(type -> buildings.put(type.building, type));
            Logger.LOADING.logDebug(rawTypes.size() + " loaded from " + file.path());
        });
        loadLists();
    }

    /**
     * Loads lists of crafting recipes for building. First item in array should be building name.
     */
    private void loadLists() {
        Logger.LOADING.log("building recipes");
        RecipeMap recipeMap = RecipeMap.instance();
        FileUtil.iterate(FileUtil.RECIPE_LISTS_PATH, file -> {
            ArrayList<ArrayList<String>> elements = json.fromJson(ArrayList.class, ArrayList.class, file);
            for (List<String> recipeList : elements) {
                String buildingName = recipeList.remove(0);
                if(buildings.containsKey(buildingName)) {
                    BuildingType type = buildings.get(buildingName);
                    type.recipes = recipeList.stream().filter(recipeMap::hasRecipe).collect(Collectors.toList());
                } else {
                    Logger.LOADING.logWarn("Recipes for unknown building " + buildingName + " ignored.");
                }
            }
        });
        ArrayList<ArrayList<String>> elements = json.fromJson(ArrayList.class, ArrayList.class, FileUtil.get(FileUtil.RECIPE_LISTS_PATH));
        for (List<String> recipeList : elements) {
            String buildingName = recipeList.remove(0);
            if(buildings.containsKey(buildingName)) {
                BuildingType type = buildings.get(buildingName);
                type.recipes = recipeList.stream().filter(recipeMap::hasRecipe).collect(Collectors.toList());
            } else {
                Logger.LOADING.logWarn("Recipes for unknown building " + buildingName + " ignored.");
            }
        }
    }

    public static BuildingType getBuilding(String name) {
        return instance().buildings.get(name);
    }

    private static BuildingTypeMap instance() {
        if (instance == null) instance = new BuildingTypeMap();
        return instance;
    }
}
