package stonering.stage.unit;

import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.equipment.EquipmentAspect;
import stonering.util.geometry.Position;

import java.util.HashMap;
import java.util.Map;

/**
 * Group with several columns of {@link ItemWidget}.
 * Container with a {@link Stack} that shows icons of a unit's equipped items, and list of hauled items.
 * Each slot image is container in own container.
 *
 * @author Alexander on 28.01.2020.
 */
public class UnitEquipmentTab extends HorizontalGroup {
    private static Map<Position, String> positionMap; // holds positions inside tab for different slot types. z is used as padding to previous widget.
    private VerticalGroup[] columns;

    {
        positionMap = new HashMap<>();
        positionMap.put(new Position(0, 0, 186), "right gauntlet");
        positionMap.put(new Position(0, 1, 33), "right glove");
        positionMap.put(new Position(0, 2, 33), "right ring");
        positionMap.put(new Position(0, 3, 33), "right hand");
        positionMap.put(new Position(1, 1, 0), "right foot");
        positionMap.put(new Position(2, 0, 33), "head");
        positionMap.put(new Position(2, 1, 33), "neck");
        positionMap.put(new Position(2, 2, 73), "chest");
        positionMap.put(new Position(2, 3, 73), "belt");
        positionMap.put(new Position(2, 4, 33), "legs");
        positionMap.put(new Position(3, 0, 73), "cloak");
        positionMap.put(new Position(3, 1, 33), "backpack");
        positionMap.put(new Position(3, 2, 33), "left foot");
        positionMap.put(new Position(4, 0, 186), "left gauntlet");
        positionMap.put(new Position(4, 0, 33), "left glove");
        positionMap.put(new Position(4, 0, 33), "left ring");
        positionMap.put(new Position(4, 0, 33), "left hand");
    }

    public UnitEquipmentTab(Unit unit) {
        columns = new VerticalGroup[5];
        EquipmentAspect equipmentAspect = unit.getAspect(EquipmentAspect.class);
        for (int i = 0; i < 5; i++) {
            columns[i] = new VerticalGroup();
            fillColumn(i, equipmentAspect);
            addActor(columns[i]);
        }

    }

    private void fillColumn(int i, EquipmentAspect aspect) {
        positionMap.keySet().stream()
                .filter(position -> position.x == i)
                .forEach(position -> columns[i].addActorAt(position.y, new ItemWidget(aspect.slots.get(positionMap.get(position)).item)));

    }

}
