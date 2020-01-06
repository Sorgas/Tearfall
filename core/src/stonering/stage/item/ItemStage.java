package stonering.stage.item;

import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.utils.Align;
import stonering.entity.item.Item;
import stonering.stage.UiStage;
import stonering.util.global.Logger;


/**
 * Stage for showing {@link ItemMenu}.
 *
 * @author Alexander on 24.11.2019.
 */
public class ItemStage extends UiStage {
    private ItemMenu menu;

    public ItemStage(Item item) {
        super();
        Logger.UI.logDebug("Creating item menu stage.");
        Container<ItemMenu> container = new Container<>(menu = new ItemMenu(item));
        container.getActor().align(Align.center);
        container.setFillParent(true);
        container.setDebug(true, true);
        container.align(Align.center);
        addActor(container);
        setKeyboardFocus(menu);
        interceptInput = false;
    }
}
