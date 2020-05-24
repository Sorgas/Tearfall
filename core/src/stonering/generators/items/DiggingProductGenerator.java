package stonering.generators.items;

import stonering.entity.item.Item;
import stonering.enums.blocks.BlockTypeEnum;
import stonering.entity.material.Material;
import stonering.enums.materials.MaterialMap;

import java.util.ArrayList;
import java.util.List;

/**
 * Generates stone, ore, gem, clay, sand item for leaving while tile is dug out.
 * TODO add other item classes
 *
 * @author Alexander Kuzyakov on 08.01.2018.
 */
public class DiggingProductGenerator {
    private ItemGenerator itemGenerator = new ItemGenerator();

    public List<Item> generateDigProduct(int materialId, BlockTypeEnum oldType, BlockTypeEnum newType) {
        List<Item> items = new ArrayList<>();
        Material material = MaterialMap.instance().getMaterial(materialId);
        int stoneAmount = Math.max(0, oldType.PRODUCT - newType.PRODUCT);
        if (material.tags.contains("stone") || material.tags.contains("ore")) { // create rock items for dug stone and ore.
            for (int i = 0; i < stoneAmount; i++) {
                items.add(itemGenerator.generateItem("rock", materialId, null));
            }
        }
        return items;
    }
}
