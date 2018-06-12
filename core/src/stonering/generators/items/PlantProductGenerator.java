package stonering.generators.items;

import stonering.enums.materials.Material;
import stonering.enums.materials.MaterialMap;
import stonering.enums.plants.TreeBlocksTypeEnum;
import stonering.exceptions.DescriptionNotFoundException;
import stonering.global.utils.Position;
import stonering.objects.local_actors.items.Item;
import stonering.objects.local_actors.plants.AbstractPlant;
import stonering.objects.local_actors.plants.Plant;
import stonering.objects.local_actors.plants.PlantBlock;
import stonering.objects.local_actors.plants.Tree;

import java.util.ArrayList;

/**
 * Generator for products from plants.
 */
public class PlantProductGenerator {
    private ItemGenerator itemGenerator;

    public PlantProductGenerator() {
        itemGenerator = new ItemGenerator();
    }

    public ArrayList<Item> generateCutProduct(PlantBlock block) {
        AbstractPlant plant = block.getPlant();
        ArrayList<Item> items = new ArrayList<>();
        if (plant instanceof Plant) {
            ArrayList<String> products = new ArrayList<>();
            Position plantPosition = block.getPosition();
            products.addAll(block.getCutProducts());
            products.addAll(block.getHarvestProducts());
            products.forEach((product) -> createItem(product, plant.getType().getMaterialName()));
        } else if (plant instanceof Tree) {
            ArrayList<String> products = new ArrayList<>();
            Item cutItem = generateCutProductForTreePart(block);
            if (cutItem != null) {
                items.add(cutItem);
            }
            products.addAll(block.getHarvestProducts());
            products.forEach((product) -> items.add(createItem(product, plant.getType().getMaterialName())));
        }
        return items;
    }


    private Item createItem(String name, String material) {
        try {
            Item item = itemGenerator.generateItem(name, MaterialMap.getInstance().getId(material));
            return item;
        } catch (DescriptionNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Generates tree specific items for blocks.
     * //TODO add tree age in account;
     *
     * @param block block of tree.
     * @return item or null;
     */
    private Item generateCutProductForTreePart(PlantBlock block) {
        ItemGenerator itemGenerator = new ItemGenerator();
        try {
            String itemTitle = "";
            switch (TreeBlocksTypeEnum.getType(block.getBlockType())) {
                case TRUNK:
                case STOMP: {
                    itemTitle = "log";
                    break;
                }
                case BRANCH: {
                    itemTitle = "branch";
                    break;
                }
                case ROOT: {
                    itemTitle = "root";
                }
            }
            if (block.getPlant().getType().getCutProduct().contains(itemTitle))
                return itemGenerator.generateItem(itemTitle);
        } catch (DescriptionNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
