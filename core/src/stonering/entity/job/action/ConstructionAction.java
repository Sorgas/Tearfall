package stonering.entity.job.action;

import stonering.entity.building.BuildingOrder;
import stonering.entity.item.Item;
import stonering.enums.blocks.BlockTypeEnum;
import stonering.game.GameMvc;
import stonering.game.model.system.plant.PlantContainer;
import stonering.game.model.system.substrate.SubstrateContainer;
import stonering.game.model.local_map.LocalMap;
import stonering.util.geometry.Position;
import stonering.util.global.Logger;

/**
 * Action for creating constructions on map. Constructions are just blocks of material.
 * Extends action which finds item for building and brings them to construction site.
 *
 * @author Alexander on 12.03.2019.
 */
public class ConstructionAction extends GenericBuildingAction {
    private byte blockType;

    public ConstructionAction(BuildingOrder order) {
        super(order);
        blockType = BlockTypeEnum.getType(order.blueprint.building).CODE;
        onFinish = () -> {
            int material = order.parts.values().iterator().next().items.iterator().next().material; // all items have same material

            order.ingredientOrders.values().stream().findFirst().stream().findFirst().get().;

            GameMvc.model().get(LocalMap.class).blockType.setBlock(order.position, blockType, material); // create block
            PlantContainer container = GameMvc.model().get(PlantContainer.class);
            container.remove(container.getPlantInPosition(order.position), true); // remove plant
            GameMvc.model().get(SubstrateContainer.class).remove(order.position); // remove substrates
            consumeItems();
            Logger.TASKS.logDebug(BlockTypeEnum.getType(blockType).NAME + " built at " + this.target.getPosition());
        };
    }

    @Override
    public String toString() {
        return "Construction name: " + BlockTypeEnum.getType(blockType).NAME;
    }
}
