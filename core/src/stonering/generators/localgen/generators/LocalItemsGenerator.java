package stonering.generators.localgen.generators;

import stonering.game.model.system.item.ItemContainer;
import stonering.game.model.local_map.LocalMap;
import stonering.generators.items.ItemGenerator;
import stonering.generators.localgen.LocalGenContainer;
import stonering.util.geometry.Position;
import stonering.entity.item.Item;

import static stonering.enums.blocks.BlockTypeEnum.SPACE;

/**
 * Creates item and puts them on map.
 *
 * @author Alexander Kuzyakov on 26.01.2018.
 */
public class LocalItemsGenerator extends LocalGenerator {
    private ItemGenerator itemGenerator;
    private LocalMap localMap;

    public LocalItemsGenerator(LocalGenContainer container) {
        super(container);
        itemGenerator = new ItemGenerator();
    }

    public void execute() {
        localMap = container.model.get(LocalMap.class);
        createItemInCenter("axe", "iron", 0, -3);
        createItemInCenter("pickaxe", "iron", 0, -1);
        createItemInCenter("hoe", "iron", 0, -2);
        createItemInCenter("rock", "rhyolite", 0, 1);
        createItemInCenter("pants", "cotton", 1, 0);
        createItemInCenter("shirt", "cotton", 2, 0);
        createItemInCenter("sickle", "iron", 1, 1);
        createItemInCenter("bar", "iron", 4, 1);
        createItemInCenter("bar", "iron", 4, 2);
        createItemInCenter("bar", "copper", 4, 3);
        createItemInCenter("bar", "silver", 4, 4);
        createItemInCenter("bar", "steel", 4, 5);
        createItemInCenter("log", "wood", 3, 0);
    }

    private void createItemInCenter(String itemType, String material, int xOffset, int yOffset) {
        LocalMap localMap = container.model.get(LocalMap.class);
        Position position = new Position(localMap.xSize / 2 + xOffset, localMap.ySize / 2 + yOffset, 0);
        position.z = findSurfaceZ(position.x, position.y);
        Item item = itemGenerator.generateItem(itemType, material, null);
        container.model.get(ItemContainer.class).onMapItemsSystem.addNewItemToMap(item, position);
    }

    private int findSurfaceZ(int x, int y) {
        for (int z = localMap.zSize - 1; z >= 0; z--) {
            if (localMap.blockType.get(x, y, z) != SPACE.CODE) {
                return z;
            }
        }
        return 0;
    }
}
