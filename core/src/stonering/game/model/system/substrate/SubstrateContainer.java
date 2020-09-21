package stonering.game.model.system.substrate;

import java.util.HashMap;

import stonering.entity.plant.AbstractPlant;
import stonering.entity.plant.PlantBlock;
import stonering.entity.plant.SubstratePlant;
import stonering.enums.time.TimeUnitEnum;
import stonering.game.model.system.EntityContainer;
import stonering.game.model.system.ModelComponent;
import stonering.util.geometry.Position;
import stonering.util.logging.Logger;

/**
 * Container for substrates
 * TODO add deletion list
 *
 * @author Alexander_Kuzyakov on 18.06.2019.
 */
public class SubstrateContainer extends EntityContainer<SubstratePlant> implements ModelComponent {
    private HashMap<Position, PlantBlock> substrateBlocks;
    private Position cachePosition;

    public SubstrateContainer() {
        substrateBlocks = new HashMap<>();
        addSystem(new SubstrateGrowingSystem(this));
        cachePosition = new Position();
    }

    @Override
    public void update(TimeUnitEnum unit) {
        // TODO move plant behaviour to systems
    }

    public void place(SubstratePlant plant, Position position) {
        if(plant == null) {
            Logger.PLANTS.logWarn("Attempt to place null substrate plant.");
            return;
        }
        if (substrateBlocks.containsKey(plant.getPosition())) {
            Logger.PLANTS.logWarn("Attempt to place substrate plant upon existing one.");
            return;
        }
        plant.setPosition(position);
        objects.add(plant);
        substrateBlocks.put(plant.getPosition(), plant.getBlock());
    }

    /**
     * Removes plant from map completely.
     */
    public void remove(AbstractPlant plant) {
        if (plant == null) return;
        if (!substrateBlocks.containsKey(plant.position)) return;
        objects.remove(plant);
        substrateBlocks.remove(plant.position);
    }

    /**
     * Removes plant from position, if any.
     */
    public void remove(Position pos) {
        if (substrateBlocks.containsKey(pos)) remove(substrateBlocks.get(pos).plant);
    }

    public PlantBlock getSubstrateBlock(int x, int y, int z) {
        return getSubstrateBlock(cachePosition.set(x, y, z));
    }

    public PlantBlock getSubstrateBlock(Position position) {
        return substrateBlocks.get(position);
    }

    public SubstratePlant getSubstrateInPosition(Position position) {
        return substrateBlocks.containsKey(position) ? (SubstratePlant) substrateBlocks.get(position).plant : null;
    }

    public boolean isSubstrateBlockExists(Position position) {
        return substrateBlocks.containsKey(position);
    }
}
