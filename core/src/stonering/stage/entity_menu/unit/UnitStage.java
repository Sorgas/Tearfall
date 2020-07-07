package stonering.stage.entity_menu.unit;

import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.utils.Align;
import stonering.entity.unit.Unit;
import stonering.stage.util.UiStage;
import stonering.util.logging.Logger;

/**
 * Stage for displaying {@link UnitMenu}
 * @author Alexander on 18.12.2019.
 */
public class UnitStage extends UiStage {
    private UnitMenu menu;

    public UnitStage(Unit unit) {
        super();
        Logger.UI.logDebug("Creating item menu stage.");
        Container<UnitMenu> container = new Container(menu = new UnitMenu(unit));
        container.getActor().align(Align.center);
        container.setFillParent(true);
        container.setDebug(true, true);
        container.align(Align.center);
        addActor(container);
        setKeyboardFocus(menu);
        interceptInput = true;
    }
}
