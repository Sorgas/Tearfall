package stonering.stage.entity_menu.unit.tab;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.utils.Align;

import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.body.BodyAspect;
import stonering.entity.unit.aspects.body.Wound;
import stonering.entity.unit.aspects.health.HealthAspect;
import stonering.enums.unit.health.HealthEffect;
import stonering.util.lang.StaticSkin;

/**
 * Tab shows unit's Health.
 * TODO show missing bodyparts in wounds
 *
 * @author Alexander on 03.09.2020.
 */
public class UnitHealthTab extends Table {
    private final HealthAspect healthAspect;
    private final BodyAspect bodyAspect;
    private final Map<String, Actor> shownEffects;
    private final VerticalGroup effectGroup;
    private final VerticalGroup woundGroup;

    public UnitHealthTab(Unit unit) {
        healthAspect = unit.get(HealthAspect.class);
        bodyAspect = unit.get(BodyAspect.class);
        shownEffects = new HashMap<>();
        defaults().growX().left().width(300);
        add(new Label("health", StaticSkin.skin()));
        add(new Label("wounds", StaticSkin.skin())).row();
        add(effectGroup = new VerticalGroup());
        add(woundGroup = new VerticalGroup());
        effectGroup.left();
        woundGroup.left();
        debugAll();
        align(Align.topLeft);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        shownEffects.keySet().stream()
                .filter(name -> !healthAspect.effects.containsKey(name) && !bodyAspect.wounds.containsKey(name))
                .collect(Collectors.toList())
                .forEach(name -> effectGroup.removeActor(shownEffects.remove(name)));
        healthAspect.effects.keySet().stream()
                .filter(key -> !shownEffects.containsKey(key))
                .forEach(name -> showEffect(healthAspect.effects.get(name)));
        bodyAspect.wounds.keySet().stream()
                .filter(key -> !shownEffects.containsKey(key))
                .forEach(name -> showWound(bodyAspect.wounds.get(name)));
    }

    private void showEffect(HealthEffect effect) {
        Table effectTable = new Table();
        effectTable.defaults().padLeft(20).fillX().height(25);
        effectTable.add(new Label(effect.name, StaticSkin.skin())).padLeft(0).row();
        effect.attributeEffects.forEach((attribute, delta) -> effectTable.add(new Label(attribute.toString() + " " + delta, StaticSkin.skin())).row());
        effect.functionEffects.forEach((function, delta) -> effectTable.add(new Label(function.toString() + " " + delta, StaticSkin.skin())).row());
        effect.statEffects.forEach((stat, delta) -> effectTable.add(new Label(stat.toString() + " " + delta, StaticSkin.skin())).row());
        effectGroup.addActor(effectTable);
        shownEffects.put(effect.name, effectTable);
    }

    private void showWound(Wound wound) {
        String key = wound.part + wound.name;
        Label label = new Label(key, StaticSkin.skin());
        woundGroup.addActor(label);
        shownEffects.put(key, label);
    }
}
