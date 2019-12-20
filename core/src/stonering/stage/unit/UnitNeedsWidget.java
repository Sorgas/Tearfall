package stonering.stage.unit;

import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import stonering.entity.unit.Unit;

/**
 * Displays hunger, thirst, and fatigue of a unit.
 *
 * @author Alexander on 20.12.2019.
 */
public class UnitNeedsWidget extends Table {
    private Unit unit;

    public UnitNeedsWidget(Unit unit) {
        this.unit = unit;
    }

    private void createBars() {
        add(new ProgressBar())
    }
}
