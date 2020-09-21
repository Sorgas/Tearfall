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
    private final HealthAspect healthAspect;
    private final BodyAspect bodyAspect;

    public UnitHealthTab(Unit unit) {
        healthAspect = unit.get(HealthAspect.class);
        bodyAspect = unit.get(BodyAspect.class);
        defaults().growX().left();
        add(new Label("health", StaticSkin.skin()));
        add(new Label("damaged body parts", StaticSkin.skin())).row();
        add(healthTable());
        add(damagedBodyPartsTable());
        debugAll();
        align(Align.topLeft);
    }
    
    private Table healthTable() {
        Table table = new Table();
        healthAspect.effects.forEach((name, effect) -> {
            Table effectTable = new Table();
            effectTable.defaults().padLeft(20).fillX().height(25);
            effectTable.add(new Label(name, StaticSkin.skin())).padLeft(0).row();
            effect.attributeEffects.forEach((attribute, delta) -> effectTable.add(new Label(attribute.toString() + " " + delta, StaticSkin.skin())).row());
            effect.functionEffects.forEach((function, delta) -> effectTable.add(new Label(function.toString() + " " + delta, StaticSkin.skin())).row());
            effect.statEffects.forEach((stat, delta) -> effectTable.add(new Label(stat.toString() + " " + delta, StaticSkin.skin())).row());
            table.add(effectTable).row();
        });
        bodyAspect.wounds.forEach((part, wound) -> table.add(new Label(part + wound.name, StaticSkin.skin())).row());
        return table;
    }
    
    private Table damagedBodyPartsTable() {
        Table table = new Table();
        bodyAspect.missingParts .forEach(part -> table.add(new Label(part, StaticSkin.skin())).row());
        return table;
    }
}
