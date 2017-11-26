package stonering.enums.materials;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import stonering.enums.trees.TreeType;
import stonering.utils.global.FileLoader;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Alexander on 30.10.2017.
 */
public class TreeTypeMap {
    private HashMap<String, TreeType> types;
    private Json json;

    public TreeTypeMap() {
        types = new HashMap<>();
        json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        json.addClassTag("color_c", Color.class);
        loadTreeTypes();
    }

    private void loadTreeTypes() {
        System.out.println("loading types");
        ArrayList<TreeType> elements = json.fromJson(ArrayList.class, TreeType.class, FileLoader.getTreesFile());
        for (TreeType treeType : elements) {
            types.put(treeType.getSpecimen(), treeType);
        }
    }

    public TreeType getTreeType(String speciment) {
        return types.get(speciment);
    }
}
