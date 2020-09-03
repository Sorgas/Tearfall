package stonering.stage.entity_menu.unit;

import com.badlogic.gdx.scenes.scene2d.ui.Table;

import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.health.HealthAspect;

/**
 * Tab shows unit's characteristics.
 * 
 * @author Alexander on 03.09.2020.
 */
public class UnitHealthTab   extends Table {
    
    public UnitHealthTab(Unit unit) {
        unit.get(HealthAspect.class).attributes
    }
}
