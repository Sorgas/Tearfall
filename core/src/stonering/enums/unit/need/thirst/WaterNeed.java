package stonering.enums.unit.need.thirst;

import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Stream;

import stonering.entity.item.Item;
import stonering.entity.job.Task;
import stonering.entity.job.action.DrinkFromTileAction;
import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.MoodEffect;
import stonering.entity.unit.aspects.body.BodyAspect;
import stonering.entity.unit.aspects.need.NeedAspect;
import stonering.entity.unit.aspects.need.NeedState;
import stonering.enums.action.TaskPriorityEnum;
import stonering.enums.blocks.BlockTypeEnum;
import stonering.enums.items.ItemTagEnum;
import stonering.enums.materials.MaterialMap;
import stonering.enums.unit.need.Need;
import stonering.enums.unit.need.NeedEnum;
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
 * Drinking from map tiles can be performed from the same z-level with the water tile or 1 level higher near tile.
 * <p>
 * TODO crate cache of natural water sources
 *
 * @author Alexander on 08.10.2019.
 */
public class WaterNeed extends Need {

    public WaterNeed(String moodEffectKey) {
        super(moodEffectKey);
    }

    @Override
    public TaskPriorityEnum countPriority(Unit unit) {
        return ThirstLevelEnum.getLevel(needLevel(unit), diseaseLevel(unit)).priority;
    }

    @Override
    public boolean isSatisfied(Unit unit) {
        return false;
    }

    @Override
    public Task tryCreateTask(Unit unit) {
        TaskPriorityEnum priority = countPriority(unit);
        switch (priority) {
            case COMFORT:
                findBestDrink(unit);
            case HEALTH_NEEDS:
            case SAFETY:
                return Optional.ofNullable(findWaterSource(unit))
                        .map(DrinkFromTileAction::new)
                        .map(Task::new).orElse(null);
        }
        return null;
    }

    @Override
    public MoodEffect getMoodPenalty(Unit unit, NeedState state) {
        return new MoodEffect(moodEffectKey, "thirsty", 4, -1);
    }

    private Item findBestDrink(Unit unit) {
        return GameMvc.model().get(ItemContainer.class).util.getNearestItemWithTag(unit.position, ItemTagEnum.DRINKABLE);
    }

    private Position findWaterSource(Unit unit) {
        LocalMap map = GameMvc.model().get(LocalMap.class);
        PassageMap passageMap = map.passageMap;
        byte unitArea = passageMap.area.get(unit.position);
        int water = MaterialMap.getId("water");

        // find tiles that can be used from above
        Stream<Position> freePositionsAboveWater = GameMvc.model().get(LiquidContainer.class).liquidStream(water)
                .map(pos -> Position.add(pos,0, 0, 1))
                .filter(pos -> pos.z < map.zSize)
                .filter(pos -> map.blockType.get(pos) == BlockTypeEnum.SPACE.CODE);

        return Stream.concat(freePositionsAboveWater, GameMvc.model().get(LiquidContainer.class).liquidStream(water))
                .sorted(Comparator.comparingInt(pos -> pos.fastDistance(unit.position))) // nearest position
                .filter(pos -> hasAreaTileNear(pos, unitArea, passageMap.area))
                .findFirst().orElse(null);
    }

    private boolean hasAreaTileNear(Position center, int area, ByteArrayWithCounter areaArray) {
        return PositionUtil.allNeighbour.stream()
                .map(pos -> Position.add(center, pos)) // positions around tile
                .anyMatch(pos -> areaArray.get(pos) == area); // reachable positions
    }
}
