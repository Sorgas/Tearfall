package stonering.generators.items;

import stonering.entity.local.items.Item;
import stonering.enums.materials.Material;
import stonering.enums.materials.MaterialMap;

/**
 * Generates stone, ore, gem, clay, sand items for leaving while tile is dug out.
 *
 * @author Alexander Kuzyakov on 08.01.2018.
 */
public class DiggingProductGenerator {

    //TODO add other item classes
    public Item generateDigProduct(int materialId) {
        Material material = MaterialMap.getInstance().getMaterial(materialId);
        if (!material.getTags().contains("stone") && !material.getTags().contains("ore")) return null;
        return new ItemGenerator().generateItem("rock", materialId);
    }

    public boolean productRequired(int materialId) {
        Material material = MaterialMap.getInstance().getMaterial(materialId);
        return material != null && (material.getTags().contains("stone") || material.getTags().contains("ore"));
    }
}
