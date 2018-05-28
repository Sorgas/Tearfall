package stonering.generators.localgen.generators;

import stonering.exceptions.DescriptionNotFoundException;
import stonering.game.core.model.LocalMap;
import stonering.generators.items.ItemGenerator;
import stonering.generators.localgen.LocalGenConfig;
import stonering.generators.localgen.LocalGenContainer;
import stonering.global.utils.Position;
import stonering.objects.local_actors.items.Item;

/**
 * Created by Alexander on 26.01.2018.
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
        createItemInCenter("axe");
    }

    private void createItemInCenter(String itemType) {
        try {
            LocalMap localMap = container.getLocalMap();
            Item pickaxe = itemGenerator.generateItem(itemType);
            for (int z = localMap.getzSize() - 1; z >= 0; z--) {
                if (localMap.getBlockType(localMap.getxSize() / 2, localMap.getySize() / 2, z) != 0) {
                    pickaxe.setPosition(new Position(localMap.getxSize() / 2, localMap.getySize() / 2, z));
                    break;
                }
            }
            container.getItems().add(pickaxe);
        } catch (DescriptionNotFoundException e) {
            e.printStackTrace();
        }
    }
}
