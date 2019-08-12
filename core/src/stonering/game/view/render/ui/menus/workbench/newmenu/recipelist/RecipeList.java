package stonering.game.view.render.ui.menus.workbench.newmenu.recipelist;

import com.badlogic.gdx.scenes.scene2d.InputListener;
import stonering.game.view.render.ui.menus.util.NavigableVerticalGroup;

/**
 * Shows recipes divided into categories.
 * Has listeners for selecting recipe and quitting list.
 *
 * @author Alexander on 12.08.2019.
 */
public class RecipeList extends NavigableVerticalGroup {
    private InputListener selectListener;
    private InputListener quitListener;

    public RecipeList(InputListener selectListener) {
        this.selectListener = selectListener;

    }

    private void createDefaultListener() {
//        this.addListener()
    }

    /**
     *
     */
    private static class CategoryItem {

    }
}
