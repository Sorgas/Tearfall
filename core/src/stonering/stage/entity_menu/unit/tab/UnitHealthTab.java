package stonering.stage.entity_menu.unit.tab;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.body.BodyAspect;
import stonering.entity.unit.aspects.health.HealthAspect;
import stonering.util.lang.StaticSkin;

/**
 * Tab shows unit's Health.
 *
 * @author Alexander on 03.09.2020.
 */
public class UnitHealthTab extends Table {
    private final HealthAspect aspect;
    private final BodyAspect bodyAspect;

    public UnitHealthTab(Unit unit) {
        aspect = unit.get(HealthAspect.class);
        bodyAspect = unit.get(BodyAspect.class);
        add(new Label("health", StaticSkin.skin()));
        add(new Label("missing parts", StaticSkin.skin())).row();
        add(damagedBodyTable());
        add(missingBodyPartsTable());
        align(Align.topLeft);
    }
    
    private Table damagedBodyTable() {
        Table table = new Table();
        bodyAspect.wounds.forEach((part, wound) -> table.add(new Label(part + wound.name, StaticSkin.skin())).row());
        return table;
    }
    
    private Table missingBodyPartsTable() {
        Table table = new Table();
        bodyAspect.missingParts .forEach(part -> table.add(new Label(part, StaticSkin.skin())).row());
        return table;
    }
}
