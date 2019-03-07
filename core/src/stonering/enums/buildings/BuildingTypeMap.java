package stonering.enums.buildings;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import stonering.entity.local.building.BuildingType;
import stonering.util.global.FileLoader;
import stonering.util.global.TagLoggersEnum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
        loadBuildings();
        loadLists();
    }

    public static BuildingTypeMap getInstance() {
        if (instance == null)
            instance = new BuildingTypeMap();
        return instance;
    }

    private void loadBuildings() {
        TagLoggersEnum.LOADING.log("buildings");
        ArrayList<BuildingType> elements = json.fromJson(ArrayList.class, BuildingType.class, FileLoader.getFile(FileLoader.BUILDINGS_PATH));
        for (BuildingType buildingType : elements) {
            buildings.put(buildingType.getBuilding(), buildingType);
        }
    }

    public boolean hasMaterial(String title) {
        return buildings.containsKey(title);
    }

    public BuildingType getBuilding(String name) {
        return buildings.get(name);
    }

    private void loadLists() {
        TagLoggersEnum.LOADING.log("crafting recipes");
        ArrayList<RecipeList> elements = json.fromJson(ArrayList.class, RecipeList.class, FileLoader.getFile(FileLoader.RECIPE_LISTS_PATH));
        for (RecipeList recipeList : elements) {
            if (validateList(recipeList)) {
                BuildingType type = buildings.get(recipeList.workbench);
                recipeList.recipes.forEach(s -> type.getRecipes().add(s));
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
