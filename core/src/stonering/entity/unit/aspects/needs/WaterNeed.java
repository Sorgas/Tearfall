package stonering.entity.unit.aspects.needs;

import java.util.Comparator;
import java.util.Map;

import com.badlogic.gdx.utils.ByteArray;

import stonering.entity.Entity;
import stonering.entity.item.Item;
import stonering.entity.job.Task;
import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.health.HealthAspect;
import stonering.enums.action.TaskPriorityEnum;
import stonering.enums.blocks.BlockTypeEnum;
import stonering.enums.items.ItemTagEnum;
import stonering.enums.unit.health.HealthParameterEnum;
import stonering.game.GameMvc;
import stonering.game.model.local_map.ByteArrayWithCounter;
import stonering.game.model.local_map.LocalMap;
import stonering.game.model.local_map.passage.PassageMap;
import stonering.game.model.system.item.ItemContainer;
import stonering.game.model.system.liquid.LiquidContainer;
import stonering.util.geometry.Position;
import stonering.util.geometry.PositionUtil;

/**
 * Creates tasks for drinking.
 * TODO crate cache of natural water sources
 *
 * @author Alexander on 08.10.2019.
 */
public class WaterNeed extends Need {


    @Override
    public TaskPriorityEnum countPriority(Unit unit) {
        HealthAspect aspect = unit.get(HealthAspect.class);
        float relativeValue = aspect.parameters.get(HealthParameterEnum.THIRST).getRelativeValue();
        return HealthParameterEnum.THIRST.PARAMETER.getRange(relativeValue).priority;
    }

    @Override
    public Task tryCreateTask(Unit unit) {
        TaskPriorityEnum priority = countPriority(unit);
        switch(priority) {
            case COMFORT:
                findBestDrink(unit);
            case HEALTH_NEEDS:
            case SAFETY:
            case LIFE:
                findWaterSource(unit);
        }
        return null;
    }

    private Item findBestDrink(Unit unit) {
        return GameMvc.model().get(ItemContainer.class).util.getNearestItemWithTag(unit.position, ItemTagEnum.DRINKABLE);
    }

    private Position findWaterSource(Unit unit) {
        LocalMap map = GameMvc.model().get(LocalMap.class);
        PassageMap passageMap = map.passageMap;
        byte unitArea = passageMap.area.get(unit.position);
        return GameMvc.model().get(LiquidContainer.class).liquidTiles.keySet().stream()
                //TODO filter by liquid
                .filter(pos -> pos.z < map.zSize - 1)
                .filter(pos -> map.blockType.get(pos.x, pos.y, pos.z + 1) == BlockTypeEnum.SPACE.CODE)
                .filter(pos -> hasAreaTileNear(pos, unitArea, passageMap.area))
                .min(Comparator.comparingInt(pos -> pos.fastDistance(unit.position))).orElse(null);
    }

    private boolean hasAreaTileNear(Position center, int area, ByteArrayWithCounter areaArray) {
        return PositionUtil.upperNeighbourDeltas.stream()
                .map(pos -> Position.add(center, pos)) // positions around tile
                .anyMatch(pos -> areaArray.get(pos) == area); // reachable positions
    }
}
