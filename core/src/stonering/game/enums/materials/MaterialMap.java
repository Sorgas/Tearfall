package stonering.game.enums.materials;

import java.util.HashMap;

/**
 * Created by Alexander on 02.08.2017.
 */

// STONE(0),
//         SOIL(1),
//         SAND(2),
//         WOOD(3),
//         BRICKS(5),
//         PLANKS(6),
//         GLASS(7),
//         METAL(8);
public class MaterialMap {
    private HashMap<Integer, Material> materials;

    public MaterialMap() {
        materials = new HashMap<Integer, Material>();
    }

    public Material getMaterial(int id) {
        return materials.get(id);
    }
}
