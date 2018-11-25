package stonering.game.core.view.render.ui.menus.workbench;

import stonering.game.core.GameMvc;
import stonering.game.core.view.render.ui.menus.util.Invokable;

import java.util.List;

/**
 * List of crafting menu lines with order. Stores only {@link CraftingOrderLine} in superclas's items collection.
 *
 * @author Alexander on 28.10.2018.
 */
public class CraftingOrderedList implements Invokable {
    protected GameMvc gameMvc;
    protected boolean hideable = false;
    protected int selectedIndex;
    protected List<CraftingOrderLine> items;

    public CraftingOrderedList(GameMvc gameMvc, boolean hideable) {
        super(gameMvc, hideable);
    }

    public void addEntry(int index, CraftingOrderLine entry) {
        items.add(index, entry);
        this.invalidateHierarchy();
    }

    public void removeEntry(int index) {
        items.remove(index);
    }

    public void removeEntry() {
        items.remove();
    }

    /**
     * Called whed order is performed.
     * Removes it or places it to the end of the list if it is repeatable.
     *
     * @param index
     */
    public void removeAtDone(int index) {
        if (((CraftingOrderLine) items.get(index)).isRepeatable())
            moveItem(index, 1, true);
    }

    @Override
    public boolean invoke(int keycode) {
        return false;
    }
}
