package stonering.entity.local.unit;

import stonering.game.core.model.local_map.LocalMap;
import stonering.util.geometry.Position;
import stonering.entity.local.AspectHolder;

/**
 * @author Alexander Kuzyakov on 06.10.2017.
 * <p>
 * Represents living creatures
 */
public class Unit extends AspectHolder {
    private LocalMap localMap;
    private UnitBlock block;

    public Unit(Position position) {
        super(position);
        block = new UnitBlock(this);
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public void turn() {
        localMap.freeUnitBlock(position);
        aspects.values().forEach((aspect) -> aspect.turn());
        localMap.setUnitBlock(position, block);
    }

    public LocalMap getLocalMap() {
        return localMap;
    }

    public void setLocalMap(LocalMap localMap) {
        this.localMap = localMap;
    }

    public UnitBlock getBlock() {
        return block;
    }

    public void setBlock(UnitBlock block) {
        this.block = block;
    }
}