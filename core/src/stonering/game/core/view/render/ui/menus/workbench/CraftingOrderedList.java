package stonering.game.core.view.render.ui.menus.workbench;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import stonering.game.core.GameMvc;
import stonering.game.core.view.render.ui.menus.util.Invokable;
import stonering.game.core.view.render.ui.menus.util.NavigableVerticalGroup;
import stonering.game.core.view.render.ui.menus.workbench.orderline.CraftingOrderLine;
import stonering.utils.global.StaticSkin;

import java.util.ArrayList;
import java.util.List;

/**
 * List of crafting menu lines with order. Stores only {@link CraftingOrderLine} in superclas's items collection.
 *
 * @author Alexander on 28.10.2018.
 */
public class CraftingOrderedList extends NavigableVerticalGroup implements Invokable {
    private GameMvc gameMvc;
    private List<Actor> items;
    private TextButton textButton = new TextButton("qwer", StaticSkin.getSkin());
    private TextButton textButton2 = new TextButton("qwer", StaticSkin.getSkin());

    public CraftingOrderedList() {
        super();
        items = new ArrayList<>();
    }

    /**
     * Adds new order line to list at given index.
     */
    public void addOrderLine(int index, Actor line) {
        items.add(index, line);
        addActorAt(index, line);
        this.invalidateHierarchy();
    }

    /**
     * Called when order is performed.
     * Removes it or places it to the end of the list if it is repeatable.
     *
     * @param index
     */
    public void removeAtDone(int index) {
//        if (((CraftingOrderLine) items.get(index)).isRepeatable())
//            moveItem(index, 1, true);
    }



    @Override
    public boolean invoke(int keycode) {

        return false;
    }
}
