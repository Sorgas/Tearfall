package stonering.enums.materials;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import stonering.util.global.FileUtil;
import stonering.util.global.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Singleton map of material types. Types are stored by their names and ids.
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
        for (FileHandle file : FileUtil.get(FileUtil.MATERIALS_PATH).list()) {
            ArrayList<RawMaterial> elements = json.fromJson(ArrayList.class, RawMaterial.class, file);
            for (RawMaterial rawMaterial : elements) {
                Material material = new Material(id++, rawMaterial);
                ids.put(material.name, id);
                materials.put(id, material);
            }
            Logger.GENERAL.logDebug(elements.size() + " loaded from " + file.nameWithoutExtension());
        }
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
}
