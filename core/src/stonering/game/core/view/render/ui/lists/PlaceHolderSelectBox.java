package stonering.game.core.view.render.ui.lists;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.utils.Array;

/**
 * SelectBox that shows its placeholder before any selection made.
 * @param <T>
 */
public class PlaceHolderSelectBox<T> extends NavigableSelectBox<T> {
    private T placeHolder;

    public PlaceHolderSelectBox(T placeHolder) {
        this.placeHolder = placeHolder;
    }

    /**
     * Removes placeholder after navigation.
     */
    @Override
    public void navigate(InputEvent event, int delta) {
        super.navigate(event, delta);
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

    public void removePlaceHolder() {
        if(getItems().contains(placeHolder, true)) {
            T selected = getSelected();
            Array<T> items = new Array<>(getItems());
            items.removeValue(placeHolder, true);
            super.setItems(items);
            setSelected(placeHolder == selected ? getItems().get(0) : selected);
            getList().act(1);
        }
    }

    public T getPlaceHolder() {
        return placeHolder;
    }
}
