package stonering.entity.job.action;

import stonering.entity.building.BuildingOrder;
import stonering.entity.job.action.target.BuildingActionTarget;
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
            Logger.TASKS.logDebug(BlockTypeEnum.getType(blockType).NAME + " built at " + this.target.getPosition());
            Position target = ((BuildingActionTarget) this.target).center;
            int material = order.parts.values().iterator().next().items.iterator().next().material; // all items have same material

            GameMvc.model().get(LocalMap.class).setBlock(target, blockType, material); // create block

            PlantContainer container = GameMvc.model().get(PlantContainer.class);
            container.remove(container.getPlantInPosition(target), true); // remove plant

            GameMvc.model().get(SubstrateContainer.class).remove(target); // remove substrates

            consumeItems();
        };
    }

    @Override
    public String toString() {
        return "Construction name: " + BlockTypeEnum.getType(blockType).NAME;
    }
}
