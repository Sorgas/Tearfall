package stonering.game.core.model.lists;

import stonering.enums.blocks.BlockTypesEnum;
import stonering.game.core.model.LocalMap;
import stonering.global.utils.Position;
import stonering.objects.local_actors.unit.Unit;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Alexander on 03.12.2017.
 *
 * Contains all Units on localMap
 */
public class UnitContainer {
    private ArrayList<Unit> units;
    private LocalMap localMap;

    public UnitContainer(ArrayList<Unit> units) {
        this.units = units;
    }

    public void turn() {
        for (Unit unit: units) {
            unit.turn();
        }
    }

    public void placeUnits() {
        units.forEach((unit) -> placeUnit(unit));
    }

    public void placeUnit(Unit unit) {
        Random random = new Random();
        while (true) {
            int x = random.nextInt(localMap.getxSize());
            int y = random.nextInt(localMap.getySize());
            for (int z = localMap.getzSize() - 1; z > 0; z--) {
                if (localMap.getBlockType(x, y, z) == BlockTypesEnum.SPACE.getCode()
                        && localMap.getBlockType(x, y, z - 1) == BlockTypesEnum.WALL.getCode()) {
                    System.out.println("placed: " + x + " " + y + " " + z);
                    unit.setPosition(new Position(x, y, z));
                    return;
                }
            }
        }
    }

    public ArrayList<Unit> getUnits() {
        return units;
    }

    public void setUnits(ArrayList<Unit> units) {
        this.units = units;
    }

    public void setLocalMap(LocalMap localMap) {
        this.localMap = localMap;
    }


}
