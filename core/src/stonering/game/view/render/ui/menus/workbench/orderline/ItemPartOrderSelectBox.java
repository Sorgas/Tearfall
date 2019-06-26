package stonering.game.view.render.ui.menus.workbench.orderline;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import stonering.entity.local.crafting.ItemPartOrder;
import stonering.entity.local.item.selectors.ItemSelector;
import stonering.game.view.render.ui.lists.PlaceHolderSelectBox;
import stonering.util.geometry.Position;

import java.util.List;

/**
 * Select box for crafting UI to select materials for item parts.
 * By default, 'any' item selector is selected.
 * Player can specify specific items by opening select box and selecting another option.
 *
 * @author Alexander on 25.06.2019.
 */
public class ItemPartOrderSelectBox extends PlaceHolderSelectBox<ItemSelector> {
    private ItemPartOrder itemPartOrder; // changed on SB change.

    /**
     * Creates select box, puts 'any' itemselector as a placeholder.
     * @param itemPartOrder all data is taken from order for item part.
     */
    public ItemPartOrderSelectBox(ItemPartOrder itemPartOrder, Position workbenchPosition) {
        super(itemPartOrder.getAnyMaterialSelector());
        this.itemPartOrder = itemPartOrder;
        fillSelectorsList(itemPartOrder, workbenchPosition);
    }

    private void fillSelectorsList(ItemPartOrder itemPartOrder, Position workbenchPosition) {

        List<ItemSelector> itemSelectors = itemPartOrder.getItemSelectors();
        PlaceHolderSelectBox<ItemSelector> materialSelectBox = new PlaceHolderSelectBox<>(itemPartOrder.getAnyMaterialSelector());
        itemPartOrder.refreshSelectors(workbenchPosition);

        if (itemPartOrder.isSelectedPossible()) {   // selected is null or is in array
            materialSelectBox.setItems(itemSelectors);
            materialSelectBox.setSelected(itemPartOrder.getSelected());
        } else {
            itemSelectors.add(itemPartOrder.getSelected());
            materialSelectBox.setItems(itemSelectors);
            materialSelectBox.setSelected(itemPartOrder.getSelected());
            //TODO add red status
        }
    }

    /**
     * Creates selectBox for selecting material for item part.
     * If no item is available,
     * SelectBox can have no item after this (if no item available on map).
     */
    private PlaceHolderSelectBox createMaterialSelectBox(ItemPartOrder itemPartOrder) {


        materialSelectBox.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                // TODO update status and warning labels
                switch (keycode) {
                    // confirms selected option, if dropdown is open. Otherwise, opens dropdown. no transition
                    case Input.Keys.E: {
                        if (materialSelectBox.getList().getStage() != null) {
                            handleMaterialSelection(currentIndex);
                            materialSelectBox.hideList();
                        } else {
                            showSelectBoxList(materialSelectBox);
                        }
                        return true;
                    }
                    // confirms selected option, if dropdown is open. Then, moves to next or previous select box, or creates it, or exits to list
                    // (recipe selection is unavalable at this point)
                    case Input.Keys.A:
                    case Input.Keys.D: {
                        if (materialSelectBox.getList().getStage() != null) { // select and move
                            handleMaterialSelection(currentIndex);
                            materialSelectBox.hideList();
                            handleOrderLineNavigation(currentIndex, (keycode == Input.Keys.D ? 1 : -1)); // move to one SB left or right.
                        } else if (materialSelectBox.getPlaceHolder().equals(materialSelectBox.getSelected())) { // show list if nothing was selected
                            showSelectBoxList(materialSelectBox);
                        } else {
                            handleOrderLineNavigation(currentIndex, (keycode == Input.Keys.D ? 1 : -1)); // move to one SB left or right.
                        }
                        return true;
                    }
                    // hides dropdown and goes to list. if order is not finished, cancels it.
                    case Input.Keys.Q: {
                        materialSelectBox.hideList();
                        goToListOrMenu();
                        return true;
                    }
                }
                return false;
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                super.touchDown(event, x, y, pointer, button);
                materialSelectBox.navigate(0);
                return true;
            }
        });
        materialSelectBox.getList().addListener(createTouchListener());
        return materialSelectBox;
    }

}