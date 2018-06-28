package stonering.enums.materials;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import stonering.utils.global.FileLoader;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Singleton map of material types. Types are stored by their names.
 *
 * @author Alexander Kuzyakov on 02.08.2017.
 */

public class MaterialMap {
    private static MaterialMap instance;
    private HashMap<Integer, Material> materials;
    private HashMap<String, Integer> ids;
    private Json json;

    private MaterialMap() {
        materials = new HashMap<>();
        ids = new HashMap<>();
        json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        json.addClassTag("color_c", Color.class);
        loadMaterials();
    }

    public static MaterialMap getInstance() {
        if (instance == null)
            instance = new MaterialMap();
        return instance;
    }

    private void loadMaterials() {
        System.out.println("loading materials");
        ArrayList<Material> elements = json.fromJson(ArrayList.class, Material.class, FileLoader.getMaterialsFile());
        for (Material material : elements) {
            materials.put(material.getId(), material);
            ids.put(material.getName(), material.getId());
        }
    }

    public boolean hasMaterial(int id) {
        return materials.containsKey(id);
    }

    public Material getMaterial(int id) {
        return materials.get(id);
    }

    public Material getMaterial(String name) {
        return getMaterial(getId(name));
    }

    public int getId(String name){
        return ids.get(name);
    }

    public byte getAtlasY(int id) {
        return materials.get(id).getAtlasY();
    }
}
