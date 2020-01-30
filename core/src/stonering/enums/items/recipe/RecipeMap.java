package stonering.enums.items.recipe;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import stonering.util.global.FileLoader;
import stonering.util.global.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Loads from json and stores {@link Recipe}.
 *
 * @author Alexander on 19.11.2018.
 */
public class RecipeMap {
    private static RecipeMap instance;
    private Map<String, Recipe> recipes;
    private Json json;

    private RecipeMap() {
        recipes = new HashMap<>();
        json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        loadRecipes();
    }

    public static RecipeMap instance() {
        if (instance == null)
            instance = new RecipeMap();
        return instance;
    }

    private void loadRecipes() {
        Logger.LOADING.log("recipes");
        ArrayList<RawRecipe> elements = json.fromJson(ArrayList.class, RawRecipe.class, FileLoader.get(FileLoader.RECIPES_PATH));
        RecipeProcessor processor = new RecipeProcessor();
        for (RawRecipe rawRecipe : elements) {
            recipes.put(rawRecipe.name, processor.processRawRecipe(rawRecipe));
        }
        Logger.LOADING.logDebug(recipes.keySet().size() + " loaded from " + FileLoader.RECIPES_PATH);
    }

    public Recipe getRecipe(String name) {
        return recipes.get(name);
    }

    public boolean hasRecipe(String name) {
        return recipes.containsKey(name);
    }
}
