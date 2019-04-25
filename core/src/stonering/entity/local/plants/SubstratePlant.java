package stonering.entity.local.plants;

import stonering.game.model.lists.PlantContainer;
import stonering.util.geometry.Position;

/**
 * Plant class for representing plants that cover surfaces (grasses and mosses for soil, mosses and lichen for stone and wood).
 * These plants cannot be harvested in any way, but their growth is simulated.
 * They leave no seeds, instead of this they grow on a whole map.
 * It's block is stored in separate map in {@link PlantContainer}.
 */
public class SubstratePlant extends AbstractPlant {
    private PlantBlock block;

    protected SubstratePlant(Position position) {
        super(position);
    }

    @Override
    public boolean isHarvestable() {
        return false;
    }

    @Override
    public Position getPosition() {
        return position;
    }

    public PlantBlock getBlock() {
        return block;
    }

    public void setBlock(PlantBlock block) {
        this.block = block;
    }
}
