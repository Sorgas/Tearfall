package stonering.enums.materials;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import stonering.utils.global.FileLoader;

import java.util.*;
import java.util.stream.Collectors;
import java.util.List;
import java.util.stream.Collectors;

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
        ArrayList<Material> elements = json.fromJson(ArrayList.class, Material.class, FileLoader.getFile(FileLoader.MATERIALS_PATH));
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

    public int getId(String name) {
        return ids.get(name);
    }

    /**
     * Filters all materials having types from given list.
     * @param types
     * @return HashSet of material ids
     */
    public HashSet<Integer> getMaterialsByTypes(List<String> types) {
        HashSet<String> typesSet = new HashSet<>(types);
        HashSet<Integer> idsSet = new HashSet<>();
        materials.values().stream().
                filter(material -> !Collections.disjoint(typesSet, material.getTypes())).
                forEach(material -> idsSet.add(material.getId()));
        return idsSet;
    }

    /**
     * Filters all materials having types from given list.
     * @param type
     * @return HashSet of material ids
     */
    public HashSet<Integer> getMaterialsByType(String type) {
        HashSet<Integer> idsSet = new HashSet<>();
        materials.values().stream().
                filter(material -> material.getTypes().contains(type)).
                forEach(material -> idsSet.add(material.getId()));
        return idsSet;
    }

    public byte getAtlasY(int id) {
        return materials.get(id).getAtlasY();
    }
}
