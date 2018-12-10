package stonering.game.core.view.render.ui.lists;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.utils.Array;

/**
 * SelectBox that shows its placeholder before any selection made.
 * @param <T>
 */
public class PlaceHolderSelectBox<T> extends NavigableSelectBox<T> {
    private T placeHolder;

    /**
     * Removes placeholder after navigation.
     */
    @Override
    public void navigate(InputEvent event, int delta) {
        super.navigate(event, delta);
        if(getItems().contains(placeHolder, true)) {
            T selected = getSelected();
            removePlaceHolder();
            setSelected(placeHolder == selected ? getItems().get(0) : selected);
            getList().act(1);
        }
    }

    @Override
    public void setItems(T... newItems) {
        addItemsWithPlaceHolder(new Array<>(newItems));
    }

    @Override
    public void setItems(Array<T> newItems) {
        addItemsWithPlaceHolder(new Array<>(newItems));
    }

    /**
     * Adds placeHolder as first item in list. And selects it.
     * @param newItems
     */
    private void addItemsWithPlaceHolder(Array<T> newItems) {
        Array<T> array = new Array<>();
        array.add(placeHolder);
        array.addAll(newItems);
        super.setItems(array);
        setSelected(placeHolder);
    }

    private void removePlaceHolder() {
        Array<T> items = getItems();
        items.removeValue(placeHolder, true);
        super.setItems(items);
    }

    public T getPlaceHolder() {
        return placeHolder;
    }

    public void setPlaceHolder(T placeHolder) {
        this.placeHolder = placeHolder;
    }
}
