package stonering.game.core.view.render.ui.components.menus.workbench;

import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;

import java.util.LinkedList;

/**
 * List of crafting menu lines with order.
 *
 * @author Alexander on 28.10.2018.
 */
public class CraftingOrderedList extends VerticalGroup {
    private LinkedList<CraftingOrderLine> lines;
    boolean valid = false;

    /**
     * Changes priority of item in index, moving it to delta positions.
     * @param index
     * @param delta
     */
    public void changeOrder(int index, int delta, boolean maxDelta) {
        if (lines.size() > index) {
            int newIndex;
            if (maxDelta) {
                newIndex = delta < 0 ? 0 : lines.size() - 1;
            } else {
                newIndex = ensureBounds(index + delta);
            }
            CraftingOrderLine buf = lines.get(index);
            lines.set(index, lines.get(newIndex));
            lines.set(newIndex, buf);
        }
    }

    public void addEntry(int index, CraftingOrderLine entry) {
        lines.add(index, entry);
    }

    public void removeEntry(int index) {
        lines.remove(index);
    }

    public void removeAtDone(int index) {
        if(lines.get(index).isRepeatable()) {
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
        return value < 0 ? 0 : (value >= lines.size() ? lines.size() : value);
    }
}
