package stonering.generators.items;

import stonering.enums.plants.PlantBlocksTypeEnum;
import stonering.exceptions.FaultDescriptionException;
import stonering.util.geometry.Position;
import stonering.entity.local.items.Item;
import stonering.entity.local.plants.AbstractPlant;
import stonering.entity.local.plants.Plant;
import stonering.entity.local.plants.PlantBlock;
import stonering.entity.local.plants.Tree;

import java.util.ArrayList;
import java.util.List;

/**
 * Generator for products from plants.
 * //TODO refactor
 */
public class PlantProductGenerator {
    private ItemGenerator itemGenerator;

    public PlantProductGenerator() {
        itemGenerator = new ItemGenerator();
    }

    /**
     * On cutting, both cut and harvest products are dropped.
     */
    public ArrayList<Item> generateCutProduct(PlantBlock block) {
        AbstractPlant plant = block.getPlant();
        ArrayList<Item> items = new ArrayList<>();
        ArrayList<String> products = new ArrayList<>();
        Position position = block.getPosition();
        if (plant instanceof Plant) {
            products.addAll(block.getCutProducts());
        } else if (plant instanceof Tree) {
            Item cutItem = generateCutProductForTreePart(block);
            if (cutItem != null) {
                items.add(cutItem);
            }
        }
        products.addAll(block.getHarvestProducts());
        products.forEach((product) -> {

            items.add(createItem(product, plant.getType().materialName, position));
        });
        return items;
    }

    public List<Item> generateHarvestProduct(PlantBlock block) {
        ArrayList<Item> items = new ArrayList<>();
        block.getCutProducts().forEach(s -> {
            try {
                items.add(generateProductForBlock(block, s));
            } catch (FaultDescriptionException e) {
                System.out.println("Descriptor for item " + s + " not found.");
                e.printStackTrace();
            }
        });
        return items;
    }

    private Item createItem(String name, String material, Position position) {
        Item item = itemGenerator.generateItem(name, material);
        item.setPosition(position);
        return item;
    }

    /**
     * Generates tree specific items for blocks.
     * Block product is determined by its type, and permitted products of whole tree (logs from trunk).
     * //TODO add tree age in account;
     */
    private Item generateCutProductForTreePart(PlantBlock block) {
        String itemTitle = PlantBlocksTypeEnum.getType(block.getBlockType()).cutProduct;
        if(itemTitle == null) return null;
        if (block.getPlant().getCurrentStage().cutProducts.contains(itemTitle))
            return itemGenerator.generateItem(itemTitle, block.getMaterial());
        return null;
    }

    private Item generateProductForBlock(PlantBlock block, String itemTitle) throws FaultDescriptionException {
        return itemGenerator.generateItem(itemTitle, block.getMaterial());
    }
}
