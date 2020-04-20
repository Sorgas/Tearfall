package stonering.game.model.system.zone;

import stonering.enums.time.TimeUnitEnum;
import stonering.game.GameMvc;
import stonering.game.model.local_map.LocalMap;
import stonering.game.model.system.UtilitySystem;
import stonering.util.geometry.Position;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import static stonering.enums.blocks.BlockTypeEnum.FLOOR;

/**
 * Utility system that rolls timers for tiles that farm zone were removed from.
 * Timers have random length in specific range.
 * This system makes tiles of removed farm turn back to floor one by one. 
 * 
 * @author Alexander on 13.02.2020.
 */
public class FarmTileWitherSystem extends UtilitySystem {
    private final int[] timerRange = {TimeUnitEnum.DAY.SIZE * 2, TimeUnitEnum.DAY.SIZE * 4};
    private final Map<Position, Integer> positions;
    private final Random random;

    public FarmTileWitherSystem() {
        updateInterval = TimeUnitEnum.HOUR;
        positions = new HashMap<>();
        random = new Random();
    }

    @Override
    public void update() {
        LocalMap map = GameMvc.model().get(LocalMap.class);
        for(Iterator<Position> iterator = positions.keySet().iterator(); iterator.hasNext(); ) {
            Position position = iterator.next();
            positions.put(position, positions.get(position) -1);
            if (positions.get(position) > 0) continue;
            map.blockType.set(position, FLOOR);
            iterator.remove();
        }
    }

    public void add(Position position) {
        positions.put(position, random.nextInt(timerRange[1]) - timerRange[0]);
    }
}
