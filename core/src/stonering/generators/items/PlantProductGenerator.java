package stonering.generators.items;

import stonering.enums.items.TagEnum;
import stonering.enums.plants.PlantBlocksTypeEnum;
import stonering.entity.item.Item;
import stonering.entity.plants.AbstractPlant;
import stonering.entity.plants.PlantBlock;
import stonering.enums.plants.PlantProduct;
import stonering.util.global.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Generator for products from plants.
 */
public class PlantProductGenerator {
    private ItemGenerator itemGenerator;

    public PlantProductGenerator() {
        itemGenerator = new ItemGenerator();
    }

    /**
     * On cutting, both cut and harvest products are dropped.
     * Single tile plants can have multiple cut products.
     */
    public ArrayList<Item> generateCutProduct(PlantBlock block) {
        ArrayList<Item> items = new ArrayList<>();
        AbstractPlant plant = block.getPlant();
        Logger.PLANTS.logDebug("generating cut products for " + plant.getType().title);
        if (plant.getType().isPlant()) {
            List<String> productNames = block.getPlant().getCurrentStage().cutProducts;
            if (productNames != null)
                productNames.forEach(name -> items.add(itemGenerator.generateItemByOrder(name, block.getMaterial())));
        } else if (plant.getType().isTree()) {
            Item cutItem = generateCutProductForTreePart(block);
            if (cutItem != null) items.add(cutItem);
        }
        Item harvestProduct = generateHarvestProduct(block);
        if (harvestProduct != null) items.add(harvestProduct);
        return items;
    }

    /**
     * Generates harvest product from block only.
     * Blocks can only have one product, Tree blocks are harvested separately.
     */
    public Item generateHarvestProduct(PlantBlock block) {
        if (block.isHarvested()) return null;
        PlantProduct product = block.getPlant().getCurrentStage().harvestProduct;
        if (product == null) return null;
        Item productItem = itemGenerator.generateItemByOrder(product.name, block.getMaterial());
        for (String tag : product.tags) {
            productItem.tags.add(TagEnum.get(tag));
        }
        return productItem;
    }

    /**
     * Generates tree specific item for blocks. Block can have only one product.
     * Block product is determined by its type, and permitted products of whole tree (logs from trunk, etc.).
     */
    private Item generateCutProductForTreePart(PlantBlock block) {
        String itemName = PlantBlocksTypeEnum.getType(block.getBlockType()).cutProduct;
        if (itemName == null) return null;
        List<String> cutProducts = block.getPlant().getCurrentStage().cutProducts;
        if (cutProducts == null || !cutProducts.contains(itemName)) return null;
        return itemGenerator.generateItemByOrder(itemName, block.getMaterial());
    }
}
