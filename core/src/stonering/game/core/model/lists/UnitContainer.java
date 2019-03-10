package stonering.game.core.model.lists;

import stonering.enums.blocks.BlockTypesEnum;
import stonering.game.core.GameMvc;
import stonering.game.core.model.ModelComponent;
import stonering.game.core.model.Turnable;
import stonering.game.core.model.local_map.LocalMap;
import stonering.util.geometry.Position;
import stonering.entity.local.unit.Unit;
import stonering.util.global.Initable;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains all Units on localMap.
 *
 * @author Alexander Kuzyakov on 03.12.2017.
 */
public class UnitContainer extends Turnable implements ModelComponent, Initable {
    private List<Unit> units;
    private LocalMap localMap;

    public UnitContainer() {
        this(new ArrayList<>());
    }

    public UnitContainer(List<Unit> units) {
        this.units = units;
    }

    /**
     * Inits unit's aspects.
     */
    public void init() {
        localMap = GameMvc.getInstance().getModel().get(LocalMap.class);
        units.forEach(this::placeUnit);
    }

    public void turn() {
        units.forEach((unit) -> unit.turn());
    }

    //TODO
    public void placeUnit(Unit unit) {
        unit.init();
        while (true) {
            int x = localMap.xSize / 2;
            int y = localMap.ySize / 2 - 15;
            for (int z = localMap.zSize - 1; z > 0; z--) {
                if (localMap.getBlockType(x, y, z) == BlockTypesEnum.FLOOR.CODE
                        && localMap.getBlockType(x, y, z - 1) == BlockTypesEnum.WALL.CODE) {
                    System.out.println("placed: " + x + " " + y + " " + z);
                    unit.setPosition(new Position(x, y, z));
                    return;
                }
            }
        }
    }

    public void addUnit(Unit unit) {
        units.add(unit);
        placeUnit(unit);
    }

    public List<Unit> getUnits() {
        return units;
    }
}
