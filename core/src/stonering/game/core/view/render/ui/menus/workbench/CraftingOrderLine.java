package stonering.game.core.view.render.ui.menus.workbench;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import stonering.entity.local.crafting.ItemOrder;
import stonering.game.core.view.render.ui.menus.util.Invokable;
import stonering.utils.global.StaticSkin;

/**
 * Single line in workbench menu.
 *
 * @author Alexander on 28.10.2018.
 */
public class CraftingOrderLine extends Table implements Invokable {
    private WorkbenchMenu menu;
    private boolean repeatable;
    private ItemOrder itemOrder;

    public CraftingOrderLine(WorkbenchMenu menu) {
        super(StaticSkin.getSkin());
        this.menu = menu;
    }

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
