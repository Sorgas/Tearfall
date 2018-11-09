package stonering.game.core.view.render.ui.lists;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import stonering.game.core.GameMvc;
import stonering.game.core.view.render.ui.menus.util.Invokable;

import java.util.ArrayList;
import java.util.List;

/**
 * Vertical group with navigation and invocation handling.
 * Invokes selected element with E key.
 *
 * @author Alexander Kuzyakov
 */
public class NavigableVerticalGroup extends VerticalGroup implements Navigable, Invokable {
    protected GameMvc gameMvc;
    protected boolean hideable = false;
    protected int selectedIndex;
    protected List<Invokable> items;

    public NavigableVerticalGroup(GameMvc gameMvc, boolean hideable) {
        this.gameMvc = gameMvc;
        this.hideable = hideable;
        items = new ArrayList<>();
        selectedIndex = -1;
    }

    @Override
    public boolean invoke(int keycode) {
        switch (keycode) {
            case Input.Keys.W: {
                up();
                return true;
            }
            case Input.Keys.S: {
                down();
                return true;
            }
            case Input.Keys.E: {
                select();
                return true;
            }
        }
        return false;
    }

    public void up() {
        if (selectedIndex > 0) selectedIndex--;
    }

    public void down() {
        if (selectedIndex < items.size() - 1) selectedIndex++;
    }

    public void select() {
        if (selectedIndex >= 0 && selectedIndex < items.size())
            items.get(selectedIndex).invoke(Input.Keys.E);
    }

    /**
     * Moves item in index to delta positions. Moves to beginning or end of the list if maxDelta is true.
     *
     * @param index
     * @param delta
     */
    public void moveItem(int index, int delta, boolean maxDelta) {
        if (index < items.size() && delta != 0) {
            int newIndex = index + (maxDelta ? items.size() : 1) * delta;
            swapItems(index, ensureBounds(newIndex));
        }
    }

    /**
     * Swaps items on given indices.
     * @param index1
     * @param index2
     */
    protected void swapItems(int index1, int index2) {
        if(index1 != index2) {
            Invokable buf = items.get(index1);
            items.set(index1, items.get(index2));
            items.set(index2, buf);
        }
    }

    private int ensureBounds(int value) {
        return value < 0 ? 0 : (value >= items.size() ? items.size() : value);
    }

    public List<Invokable> getItems() {
        return items;
    }

    public void setItems(List<Invokable> items) {
        this.items = items;
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
    }
}
