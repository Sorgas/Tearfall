package stonering.generators.localgen.generators;

import stonering.enums.materials.MaterialMap;
import stonering.exceptions.DescriptionNotFoundException;
import stonering.game.core.model.LocalMap;
import stonering.generators.items.ItemGenerator;
import stonering.generators.localgen.LocalGenConfig;
import stonering.generators.localgen.LocalGenContainer;
import stonering.global.utils.Position;
import stonering.objects.local_actors.items.Item;

/**
 * @author Alexander Kuzyakov on 26.01.2018.
 */
public class LocalItemsGenerator {
    private LocalGenContainer container;
    private LocalGenConfig config;
    private ItemGenerator itemGenerator;

    public LocalItemsGenerator(LocalGenContainer container) {
        this.container = container;
        config = container.getConfig();
        itemGenerator = new ItemGenerator();
    }

    public void execute() {
        createItemInCenter("axe", "iron");
        createItemInCenter("rock", "rhyolite");
    }

    private void createItemInCenter(String itemType, String material) {
        try {
            LocalMap localMap = container.getLocalMap();
            Item pickaxe = itemGenerator.generateItem(itemType, MaterialMap.getInstance().getId(material));
            pickaxe.setPosition(new Position(localMap.getxSize() / 2, localMap.getySize() / 2, findSurfaceZ()));
            container.getItems().add(pickaxe);
        } catch (DescriptionNotFoundException e) {
            e.printStackTrace();
        }
    }

    private int findSurfaceZ() {
        LocalMap localMap = container.getLocalMap();
        for (int z = localMap.getzSize() - 1; z >= 0; z--) {
            if (localMap.getBlockType(localMap.getxSize() / 2, localMap.getySize() / 2, z) != 0) {
                return z;
            }
        }
        return 0;
    }
}
