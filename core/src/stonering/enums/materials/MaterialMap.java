package stonering.enums.materials;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import stonering.util.global.FileLoader;
import stonering.util.global.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Singleton map of material types. Types are stored by their names.
 * TODO add id-independent game saving
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

    public static MaterialMap instance() {
        if (instance == null)
            instance = new MaterialMap();
        return instance;
    }

    private void loadMaterials() {
        Logger.GENERAL.log("loading materials");
        int id = 0;
        for (FileHandle fileHandle : FileLoader.getFile(FileLoader.MATERIALS_PATH).list()) {
            for(FileHandle file : fileHandle.list()) {
                ArrayList<Material> elements = json.fromJson(ArrayList.class, Material.class, file);
                for (Material material : elements) {
                    material.setId(id);
                    ids.put(material.getName(), id);
                    materials.put(id, material);
                    id++;
                }
                Logger.GENERAL.logDebug(elements.size() + " loaded from " + fileHandle.nameWithoutExtension());
            }
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
        if (!ids.containsKey(name)) Logger.ITEMS.logError("no material with name " + name + " exist");
        return ids.get(name);
    }

    /**
     * Filters all materials having types from given list.
     *
     * @param types
     * @return HashSet of material ids
     */
    public HashSet<Integer> getMaterialsByTypes(List<String> types) {
        HashSet<String> typesSet = new HashSet<>(types);
        HashSet<Integer> idsSet = new HashSet<>();
        materials.values().stream().
                filter(material -> !Collections.disjoint(typesSet, material.getTags())).
                forEach(material -> idsSet.add(material.getId()));
        return idsSet;
    }

    /**
     * Filters all materials having given tag.
     */
    public Set<Integer> getMaterialsByTag(String tag) {
        HashSet<Integer> idsSet = new HashSet<>();
        materials.values().stream().
                filter(material -> material.getTags().contains(tag)).
                forEach(material -> idsSet.add(material.getId()));
        return idsSet;
    }

    public List<String> getMaterialNamesByTag(String tag) {
        List<String> list = new ArrayList<>();
        materials.values().stream().
                filter(material -> material.getTags().contains(tag)).
                forEach(material -> list.add(material.getName()));
        return list;
    }

    public byte getAtlasY(int id) {
        return materials.get(id).getAtlasY();
    }
}
