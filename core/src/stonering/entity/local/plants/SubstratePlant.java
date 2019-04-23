package stonering.entity.local.plants;

import stonering.util.geometry.Position;

/**
 * Plant class for representing plants that cover surfaces (grasses and mosses for soil, mosses and lichen for stone and wood).
 * These plants cannot be harvested in any way, but their growth is simulated.
 * They leave no seeds, instead of this they grow on a whole map.
 *
 */
public class SubstratePlant extends AbstractPlant {

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
}
