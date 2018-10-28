package stonering.game.core.view.render.ui.components.menus.workbench;

import java.util.LinkedList;

/**
 * List of crafting menu entries with order.
 *
 * @author Alexander on 28.10.2018.
 */
public class CraftingOrderedList {
    private LinkedList<CraftingOrderString> entries;
    boolean valid = false;

    /**
     * Changes priority of item in index, moving it to delta positions.
     * @param index
     * @param delta
     */
    public void changeOrder(int index, int delta, boolean maxDelta) {
        if (entries.size() > index) {
            int newIndex;
            if (maxDelta) {
                newIndex = delta < 0 ? 0 : entries.size() - 1;
            } else {
                newIndex = ensureBounds(index + delta);
            }
            CraftingOrderString buf = entries.get(index);
            entries.set(index, entries.get(newIndex));
            entries.set(newIndex, buf);
        }
    }

    public void addEntry(int index, CraftingOrderString entry) {
        entries.add(index, entry);
    }

    public void removeEntry(int index) {
        entries.remove(index);
    }

    public void removeAtDone(int index) {
        if(entries.get(index).isRepeatable()) {
            changeOrder(index, 1 , true);
        }
    }

    public void invalidate() {
        valid = false;
    }

    public void validate() {
        valid = true;
    }

    public boolean isValid() {
        return valid;
    }

    private int ensureBounds(int value) {
        return value < 0 ? 0 : (value >= entries.size() ? entries.size() : value);
    }
}
