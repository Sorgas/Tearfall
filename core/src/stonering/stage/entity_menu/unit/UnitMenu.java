package stonering.stage.entity_menu.unit;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import stonering.entity.unit.Unit;
import stonering.game.GameMvc;
import stonering.util.global.StaticSkin;
import stonering.widget.TabbedPane;
import stonering.widget.util.KeyNotifierListener;

/**
 * Menu for unit.
 * Consists of quick info section and tabs of details.
 * TODO add 'fast unit menu' like in Rimworld
 * <p>
 * Separate tabs allow to view:
 * 1.equipment,
 * 2.job list with skills for switching,
 * 3.health details,
 * 4.personality,
 * 5.mood details.
 *
 * @author Alexander on 18.12.2019.
 */
public class UnitMenu extends Container<Table> {

    public UnitMenu(Unit unit) {
        super();
        Table table = new Table();
        table.add(new UnitImageColumn(unit)).size(300, 900);
        TabbedPane pane;
        table.add(pane = new TabbedPane()).size(600, 900).fill();
        pane.add("equipment", new UnitEquipmentTab(unit));
        pane.add("jobs", new UnitJobsTab(unit));
        // TODO add other tabs
        addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode != Input.Keys.Q) return false;
                GameMvc.view().removeStage(getStage());
                return true;
            }
        });
        addListener(new KeyNotifierListener(() -> pane));
        pane.setBackground(StaticSkin.generator.generate(StaticSkin.background));
        setActor(table);
    }
}
