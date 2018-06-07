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

    public ArrayList<Item> generateCutProduct(AbstractPlant plant) {
        if (plant instanceof Plant) {
            PlantBlock block = ((Plant) plant).getBlock();
            Position plantPosition = block.getPosition();
            ArrayList<String> products = new ArrayList<>();
            products.addAll(block.getCutProducts());
            products.addAll(block.getHarvestProducts());
            products.forEach((product) -> createItem(product, plant.getType().getMaterialName(), plantPosition));
        } else if (plant instanceof Tree) {

        }


        String[] productProperties = plant.getType().getCutProduct();
        if (productProperties == null) { // plant has no product
            return null;
        }
        Material material = MaterialMap.getInstance().getMaterial(productProperties[1]);
        if (material != null) {
            Item item = new Item(null);
            item.setMaterial(material.getId());
            item.setVolume(100000); //TODO
            item.setWeight(Math.round(item.getVolume() * material.getDensity()));
            return item;
        } else {
            System.out.println("error while leaving plant cut product: ");
            System.out.println(plant.toString());
            return null;
        }
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
