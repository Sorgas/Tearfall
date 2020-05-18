package stonering.stage.workbench;

import com.badlogic.gdx.scenes.scene2d.ui.Table;

import stonering.entity.item.aspects.ItemContainerAspect;
import stonering.stage.container.ItemDetailSection;
import stonering.util.global.StaticSkin;
import stonering.widget.item.ItemButtonGrid;

/**
 * Menu for observing items stored in {@link ItemContainerAspect}.
 *
 * @author Alexander on 5/13/2020
 */
public class ItemContainerMenu extends Table {
    private final ItemContainerAspect containerAspect;
    private final ItemButtonGrid grid;
    private final ItemDetailSection details;
    private MenuSection focused;

    public ItemContainerMenu(ItemContainerAspect containerAspect) {
        this.containerAspect = containerAspect;
        this.add(grid = new ItemButtonGrid(9, 10)).width(600);
        grid.setBackground(StaticSkin.getColorDrawable(StaticSkin.backgroundFocused));
        this.add(details = new ItemDetailSection());
        setDebug(true, true);
    }
}
