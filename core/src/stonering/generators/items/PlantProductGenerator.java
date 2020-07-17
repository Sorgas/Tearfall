package stonering.generators.items;

import stonering.entity.plant.aspects.PlantGrowthAspect;
import stonering.enums.items.type.ItemType;
import stonering.enums.plants.PlantBlocksTypeEnum;
import stonering.entity.item.Item;
import stonering.entity.plant.AbstractPlant;
import stonering.entity.plant.PlantBlock;
import stonering.enums.plants.PlantLifeStage;
import stonering.util.logging.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
     * Single tile plants can have multiple cut products, Trees drop cut product per block, depending on block type.
     */
    public ArrayList<Item> generateCutProduct(PlantBlock block) {
        ArrayList<Item> items = new ArrayList<>();
        AbstractPlant plant = block.plant;
        Logger.PLANTS.logDebug("generating cut products for " + plant.type.title);
//        if (plant.type.isPlant) {
//            List<String> productNames = plant.type.lifeStages.get(plant.get(PlantGrowthAspect.class).currentStage).cutProducts;
//            if (productNames != null)
//                productNames.forEach(name -> items.add(itemGenerator.generateItem(name, block.getMaterial(), null)));
//        } else 
        if (plant.type.isTree) {
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
        if (block.harvested) return null;
        AbstractPlant plant = block.plant;
        PlantLifeStage stage = plant.getCurrentLifeStage();
        if (stage == null) return null;
        ItemType product = stage.harvestProduct;
        if (product == null) return null;
        Item productItem = itemGenerator.generateItem(product.name, block.material, null);
        return productItem;
    }

    /**
     * Generates tree specific item for blocks. Block can have only one product.
     * Block product is determined by its type, and permitted products of whole tree (logs from trunk, etc.).
     */
    private Item generateCutProductForTreePart(PlantBlock block) {
        return Optional.ofNullable(PlantBlocksTypeEnum.getType(block.blockType).cutProduct)
                .map(itemName -> itemGenerator.generateItem(itemName, block.material, null))
                .orElse(null);
    }
}
