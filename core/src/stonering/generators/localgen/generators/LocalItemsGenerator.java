package stonering.generators.localgen.generators;

import stonering.enums.materials.MaterialMap;
import stonering.exceptions.DescriptionNotFoundException;
import stonering.exceptions.FaultDescriptionException;
import stonering.game.core.model.LocalMap;
import stonering.generators.items.ItemGenerator;
import stonering.generators.localgen.LocalGenConfig;
import stonering.generators.localgen.LocalGenContainer;
import stonering.global.utils.Position;
import stonering.entity.local.items.Item;

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
        createItemInCenter("axe", "iron", 0, 0);
        createItemInCenter("pickaxe", "iron", 0, -1);
        createItemInCenter("rock", "rhyolite", 0, 1);
        createItemInCenter("pants", "cotton", 1, 0);
        createItemInCenter("shirt", "cotton", 2, 0);
        createItemInCenter("sickle", "iron", 1, 1);
        createItemInCenter("bar", "iron", 4, 1);
        createItemInCenter("bar", "iron", 4, 2);
        createItemInCenter("bar", "copper", 4, 3);
        createItemInCenter("bar", "silver", 4, 4);
        createItemInCenter("bar", "steel", 4, 5);
    }

    private void createItemInCenter(String itemType, String material, int xOffset, int yOffset) {
//        try {
            LocalMap localMap = container.getLocalMap();
            Item item = itemGenerator.generateItem(itemType, material);
            item.setPosition(new Position(localMap.getxSize() / 2 + xOffset, localMap.getySize() / 2 + yOffset, findSurfaceZ()));
            container.getItems().add(item);
//        } catch (FaultDescriptionException e) {
//            e.printStackTrace();
//        }
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
