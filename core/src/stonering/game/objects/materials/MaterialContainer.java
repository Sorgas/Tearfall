package stonering.game.objects.materials;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexander on 04.06.2017.
 */
public class MaterialContainer {
    private List<Material> materials;
    private List<Material> stones;

    public MaterialContainer() {
        materials = new ArrayList<>();
        stones = new ArrayList<>();
    }

    public Material getMaterial(int id) {
        return materials.get(id);
    }

    public void addMaterial(Material material) {
        materials.add(material);
    }

    public Material getStone(int id) {
        return stones.get(id);
    }

    public void addStone(Material material) {
        stones.add(material);
    }

}
