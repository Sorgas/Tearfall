package stonering.enums.materials;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import stonering.exceptions.MaterialNotFoundException;
import stonering.global.FileLoader;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Alexander on 02.08.2017.
 */

// STONE(0),
// SOIL(1),
// SAND(2),
// WOOD(3),
// BRICKS(5),
// PLANKS(6),
// GLASS(7),
// METAL(8);
public class MaterialMap {
    private HashMap<Integer, Material> materials;
    private HashMap<String, Integer> ids;
    private Json json;

    public MaterialMap() {
        materials = new HashMap<>();
        ids = new HashMap<>();
        json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        json.addClassTag("color_c", Color.class);
        loadMinerals();
    }

    private void loadMinerals() {
        System.out.println("loading materials");
        ArrayList<Material> elements = json.fromJson(ArrayList.class, Material.class, FileLoader.getMineralsFile());
        for (Material material : elements) {
            materials.put(material.getId(), material);
            ids.put(material.getName(), material.getId());
        }
    }

    public Material getMaterial(int id) {
        return materials.get(id);
    }

    public int getId(String name) throws MaterialNotFoundException {
        if (ids.containsKey(name)) {
            return ids.get(name);
        } else {
            throw new MaterialNotFoundException("material with name '" + name + "' not found");
        }
    }

    public byte getAtlasY(int id) {
        return materials.get(id).getAtlasY();
    }
}
