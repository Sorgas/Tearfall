package stonering.game.core.view.render.ui.menus.workbench;

import stonering.game.core.GameMvc;
import stonering.game.core.view.render.ui.lists.NavigableVerticalGroup;
import stonering.game.core.view.render.ui.menus.util.Invokable;

/**
 * List of crafting menu lines with order. Stores only {@link CraftingOrderLine} in superclas's items collection.
 *
 * @author Alexander on 28.10.2018.
 */
public class CraftingOrderedList extends NavigableVerticalGroup implements Invokable {

    public CraftingOrderedList(GameMvc gameMvc, boolean hideable) {
        super(gameMvc, hideable);
    }

    public void addEntry(int index, CraftingOrderLine entry) {
        items.add(index, entry);
    }

    public void removeEntry(int index) {
        items.remove(index);
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
}
