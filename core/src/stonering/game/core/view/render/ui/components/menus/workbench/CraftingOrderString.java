package stonering.game.core.view.render.ui.components.menus.workbench;

import com.badlogic.gdx.scenes.scene2d.ui.Table;

/**
 * @author Alexander on 28.10.2018.
 */
public class CraftingOrderString extends Table {
    private boolean repeatable;

    public boolean isRepeatable() {
        return repeatable;
    }

    public void setRepeatable(boolean repeatable) {
        this.repeatable = repeatable;
    }
}
