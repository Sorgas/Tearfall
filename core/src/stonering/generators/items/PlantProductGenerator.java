package stonering.generators.items;

import stonering.enums.materials.Material;
import stonering.enums.materials.MaterialMap;
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
            products.forEach((product) -> createItem(product, plant.getType().getMaterialName(), plantPosition));
        } else if (plant instanceof Tree) {
            Position plantPosition = block.getPosition();
            ArrayList<String> products = new ArrayList<>();
            products.addAll(block.getCutProducts());
            products.addAll(block.getHarvestProducts());
            products.forEach((product) -> items.add(createItem(product, plant.getType().getMaterialName(), plantPosition)));
        }
        return items;
    }

    private Item createItem(String name, String material, Position position) {
        try {
            Item item = itemGenerator.generateItem(name);
            item.setMaterial(MaterialMap.getInstance().getId(material));
            item.setPosition(position);
            return item;
        } catch (DescriptionNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
