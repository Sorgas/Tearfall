package stonering.game.view.render.ui.lists;

import com.badlogic.gdx.utils.Array;
import stonering.util.global.CompatibleArray;

import java.util.List;

/**
 * SelectBox that shows its placeholder before any selection made.
 */
public class PlaceHolderSelectBox<T> extends NavigableSelectBox<T> {
    private T placeHolder;

    public PlaceHolderSelectBox(T placeHolder) {
        super();
        this.placeHolder = placeHolder;
        setItems();
    }

    /**
     * Removes placeholder after navigation.
     */
    @Override
    public void navigate(int delta) {
        super.navigate(delta);
        removePlaceHolder();
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

    /**
     * Adds placeHolder as first item in list. And selects it.
     *
     * @param newItems
     */
    private void addItemsWithPlaceHolder(Array<T> newItems) {
        Array<T> array = new Array<>();
        array.add(placeHolder);
        array.addAll(newItems);
        super.setItems(array);
        setSelected(placeHolder);
    }

    //TODO some bug with empty list here
    private void removePlaceHolder() {
        if (getItems().contains(placeHolder, true)) {
            T selected = getSelected();
            Array<T> items = new Array<>(getItems());
            items.removeValue(placeHolder, true);
            super.setItems(items);
            getList().clearItems();
            getList().setItems(items);
            setSelected(placeHolder == selected ? getItems().get(0) : selected);
        }
    }

    public T getPlaceHolder() {
        return placeHolder;
    }

    /**
     * Removes placeholder, so it'l never shown in open list.
     */
    @Override
    public void showList() {
        if (getItems().contains(placeHolder, true)) navigate(0);
        super.showList();
    }
}
