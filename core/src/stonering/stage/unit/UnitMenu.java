package stonering.stage.unit;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import stonering.entity.unit.Unit;
import stonering.game.GameMvc;
import stonering.util.global.StaticSkin;
import stonering.widget.TabbedPane;

/**
 * Menu for unit.
 * Consists of quick info section and tabs of details.
 * TODO add 'fast unit menu' like in Rimworld
 *
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
    private final Table table;
    private final TabbedPane pane;
    private UnitImageColumn summaryColumn;

    public UnitMenu(Unit unit) {
        super();
        setActor(table = new Table());
        table.add(summaryColumn = new UnitImageColumn(unit)).size(300, 900);
        table.add(pane = new TabbedPane(900));
        pane.add("equipment", new UnitEquipmentTab(unit));
        addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode != Input.Keys.Q) return false;
                GameMvc.view().removeStage(getStage());
                return true;
            }
        });
    }
}
