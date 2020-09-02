package stonering.widget.lists;

import com.badlogic.gdx.utils.Array;
import stonering.util.lang.CompatibleArray;

import java.util.List;

/**
 * SelectBox that shows its placeholder before any selection made.
 * When the list is open, placeholder is removed.
 * Can be controlled with keys {@see NavigableSelectBox}.
 */
public class PlaceHolderSelectBox<T> extends NavigableSelectBox<T> {
    private T placeholder;

    public PlaceHolderSelectBox(T placeholder) {
        super();
        this.placeholder = placeholder;
        addItemsWithPlaceHolder(new Array<>());
    }

    /**
     * Removes placeholder after navigation.
     */
    @Override
    public void navigate(int delta) {
        super.navigate(delta);
        removePlaceHolder();
    }

    /**
     * Adds placeholder as first item in the list and selects it.
     */
    private void addItemsWithPlaceHolder(Array<T> newItems) {
        Array<T> array = new Array<>();
        array.add(placeholder);
        array.addAll(newItems);
        super.setItems(array);
        setSelected(placeholder);
    }

    /**
     * Removes placeholder from the list.
     */
    private void removePlaceHolder() {
        if (!getItems().contains(placeholder, true)) return;
        Array<T> items = getList().getItems();
        items.removeValue(placeholder, true);
        super.setItems(items);
        setSelectedIndex(0);
    }

    @Override
    public void setItems(T... newItems) {
        addItemsWithPlaceHolder(new Array<>(newItems));
    }

    @Override
    public void setItems(Array<T> newItems) {
        addItemsWithPlaceHolder(new Array<>(newItems));
    }

    @Override
    public void setItems(List<T> items) {
        addItemsWithPlaceHolder(new CompatibleArray<T>(items));
    }
    //TODO some bug with empty list here

    /**
     * Removes placeholder, so it'l never shown in open list.
     */
    @Override
    public void showList() {
        removePlaceHolder();
        super.showList();
    }

    @Override
    public void hideList() {
        super.hideList();
    }

    public T getPlaceholder() {
        return placeholder;
    }
}
