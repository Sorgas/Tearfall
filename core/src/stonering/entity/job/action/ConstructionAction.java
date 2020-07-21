package stonering.entity.job.action;

import stonering.entity.building.BuildingOrder;
import stonering.entity.job.action.item.GenericBuildingAction;
import stonering.enums.blocks.BlockTypeEnum;
import stonering.game.GameMvc;
import stonering.game.model.system.plant.PlantContainer;
import stonering.game.model.system.substrate.SubstrateContainer;
import stonering.game.model.local_map.LocalMap;
import stonering.util.logging.Logger;

/**
 * Action for creating constructions on map. Constructions are just blocks of material.
 * Extends action which finds item for building and brings them to construction site.
 *
 * @author Alexander on 12.03.2019.
 */
public class ConstructionAction extends GenericBuildingAction {
    private static final int INVALID_MATERIAL = -1;
    private byte blockType;


    public ConstructionAction(BuildingOrder order) {
        super(order);
        blockType = BlockTypeEnum.getType(order.blueprint.building).CODE;
        onFinish = () -> {
            int material = order.ingredientOrders.values().stream().findFirst()
                    .map(ingredientOrder -> ingredientOrder.items)
                    .flatMap(items -> items.stream().findFirst())
                    .map(item -> item.material).orElse(INVALID_MATERIAL);
            if(material == INVALID_MATERIAL) Logger.BUILDING.logError("Cannot find material for construction " + order.blueprint.building);
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
        return "constructing: " + BlockTypeEnum.getType(blockType).NAME + " on " + target.getPosition();
    }
}
