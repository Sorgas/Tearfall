package stonering.generators.items;

import stonering.enums.items.ItemTypesEnum;
import stonering.enums.materials.Material;
import stonering.objects.local_actors.items.Item;

/**
 * Created by Alexander on 08.01.2018.
 * <p>
 * generates stone, ore, gem, clay, sand, items.
 */
public class DiggingProductGenerator {

    public Item generateDigProduct(Material material) {
        if (material.getTypes().contains("stone") || material.getTypes().contains("ore")) {
            Item item = new Item(null);
            item.setType(ItemTypesEnum.ROCK);
            item.setMaterial(material.getId());
            item.setVolume(100000);
            item.setWeight(Math.round(item.getVolume() * material.getDensity()));
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
