package stonering.stage.entity_menu.unit;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.health.HealthAspect;
import stonering.util.lang.StaticSkin;
import stonering.widget.CreatureAttributeLabel;

/**
 * Tab shows unit's characteristics.
 * 
 * @author Alexander on 03.09.2020.
 */
public class UnitHealthTab extends Table {
    public UnitHealthTab(Unit unit) {
        HealthAspect health = unit.get(HealthAspect.class);
        add(new Label("attributes", StaticSkin.skin())).row();
        health.baseAttributes.forEach((attr, base) -> {
            add(new CreatureAttributeLabel(attr, base, health.attributes.get(attr) - base)).row();
        });
        align(Align.topLeft);
    }
}
