package stonering.enums.materials;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.JsonReader;
import stonering.exceptions.MaterialNotFoundException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
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

    public MaterialMap(File materialsFile) {
        materials = new HashMap<>();
        materials.put(1, new Material("qwer", new Color(1,1,1,1), (byte) 0,1));
    }

    public Material getMaterial(int id) {
        return materials.get(id);
    }

    public int getId(String name) throws MaterialNotFoundException{
        if(ids.containsKey(name)) {
            return ids.get(name);
        } else {
            throw new MaterialNotFoundException();
        }
    }

    public byte getAtlasY(int id) {
        return materials.get(id).getAtlasY();
    }

    private void fillMap(File file) {
        JsonReader reader = new JsonReader();
        try {
            reader.parse(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
