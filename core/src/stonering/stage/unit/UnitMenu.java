package stonering.stage.unit;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import stonering.entity.unit.Unit;
import stonering.util.global.StaticSkin;
import stonering.widget.util.TabbedPanel;

/**
 * Menu for unit.
 * Consists of quick info section and tabs of details.
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
public class UnitMenu extends Window {
    private UnitImageColumn leftColumn;
    private TabbedPanel panel;

    public UnitMenu(Unit unit) {
        super("", StaticSkin.getSkin());
        getTitleLabel().setText(generateWindowTitle(unit));
        createTable(unit);
    }

    private void createTable(Unit unit) {
        add(leftColumn = new UnitImageColumn(unit));
//        add(panel = new TabbedPanel<>());
    }

    private String generateWindowTitle(Unit unit) {
        return unit.getType().title;
    }
}
