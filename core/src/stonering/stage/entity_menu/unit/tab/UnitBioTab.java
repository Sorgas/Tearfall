package stonering.stage.entity_menu.unit.tab;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.health.HealthAspect;
import stonering.util.lang.StaticSkin;
import stonering.widget.CreatureAttributeLabel;

/**
 * Tab with general information and stats of a unit.
 * // TODO add bio
 * 
 * @author Alexander on 07.09.2020.
 */
public class UnitBioTab extends Table {
    private final HealthAspect aspect;

    public UnitBioTab(Unit unit) {
        aspect = unit.get(HealthAspect.class);
        add(new Label("health", StaticSkin.skin())).row();
        add(characteristicsTable());
        align(Align.topLeft);
    }

    private Table characteristicsTable() {
        Table table = new Table();
        aspect.baseAttributes.forEach((attr, base) -> {
            add(new CreatureAttributeLabel(attr, base, aspect.attributes.get(attr) - base)).left().row();
        });
        return table;
    }
}
