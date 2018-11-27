package stonering.game.core.view.render.ui.menus.workbench;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import stonering.game.core.GameMvc;
import stonering.game.core.view.render.ui.menus.util.Invokable;

import java.util.ArrayList;
import java.util.List;

/**
 * List of crafting menu lines with order. Stores only {@link CraftingOrderLine} in superclas's items collection.
 *
 * @author Alexander on 28.10.2018.
 */
public class CraftingOrderedList extends Table implements Invokable {
    protected GameMvc gameMvc;
    protected boolean hideable = false;
    protected int selectedIndex;
    protected List<CraftingOrderLine> items;

    public CraftingOrderedList() {
        super();
        items = new ArrayList<>();
    }

    public void addEntry(int index, CraftingOrderLine entry) {
        items.add(index, entry);
        this.invalidateHierarchy();
    }



    /**
     * Called whed order is performed.
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
