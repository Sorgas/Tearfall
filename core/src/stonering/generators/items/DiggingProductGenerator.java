package stonering.generators.items;

import stonering.enums.materials.Material;
import stonering.entity.local.items.Item;

/**
 * @author Alexander Kuzyakov on 08.01.2018.
 * <p>
 * generates stone, ore, gem, clay, sand, items.
 */
public class DiggingProductGenerator {

    public Item generateDigProduct(Material material) {
        if (material.getTypes().contains("stone") || material.getTypes().contains("ore")) {
            Item item = new Item(null);
            item.setMaterial(material.getId());
            item.setWeight(Math.round(item.getType().getVolume() * material.getDensity()));
            System.out.println(item.toString());
            return item;
        } else {
            return null;
        }
    }

    public boolean productRequired(Material material) {
        return material.getTypes().contains("stone") || material.getTypes().contains("ore");
    }
}
