package stonering.game.core.view.render.ui.components.menus.workbench;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import stonering.entity.local.crafting.ItemOrder;
import stonering.game.core.view.render.ui.components.menus.util.Invokable;

/**
 * Single line in workbench menu.
 *
 * @author Alexander on 28.10.2018.
 */
public class CraftingOrderLine extends Table implements Invokable {
    private boolean repeatable;
    private ItemOrder itemOrder;

    public boolean isRepeatable() {
        return repeatable;
    }

    public void setRepeatable(boolean repeatable) {
        this.repeatable = repeatable;
    }

    @Override
    public boolean invoke(int keycode) {
        return false;
    }
}
