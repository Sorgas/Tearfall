package stonering.stage.unit;

import com.badlogic.gdx.scenes.scene2d.ui.Window;
import stonering.entity.unit.Unit;
import stonering.util.global.StaticSkin;

/**
 * Menu for unit.
 * Shows picture, name, current task, tool, enabled jobs, best skill, and needs state.
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

    public UnitMenu(Unit unit) {
        super("", StaticSkin.getSkin());
        getTitleLabel().setText(generateWindowTitle(unit));
        createTable();
    }

    private void createTable() {

    }

    private String generateWindowTitle(Unit unit) {
        return "";
    }
}
