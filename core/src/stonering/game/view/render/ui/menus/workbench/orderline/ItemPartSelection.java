package stonering.game.view.render.ui.menus.workbench.orderline;

import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import stonering.entity.local.crafting.ItemPartOrder;
import stonering.enums.ControlActionsEnum;
import stonering.enums.items.recipe.ItemPartRecipe;
import stonering.game.view.render.ui.menus.util.Highlightable;
import stonering.game.view.render.ui.menus.util.HintedActor;
import stonering.game.view.render.ui.menus.workbench.orderline.selectbox.ItemTypeSelectBox;
import stonering.game.view.render.ui.menus.workbench.orderline.selectbox.MaterialSelectBox;
import stonering.util.global.StaticSkin;


/**
 * Keeps two select boxes:
 * 1. for selecting material
 * 2. for selecting item type.
 * <p>
 * If {@link ItemPartRecipe} allows only one type of item, there is a label instead of SB.
 *
 * @author Alexander_Kuzyakov on 26.06.2019.
 */
public class ItemPartSelection extends Stack implements HintedActor, Highlightable {
    private final static String regionName = "order_selection"; // name of background from regions.json
    private String hint;
    private Table table;
    private Cell materialCell;
    private Cell itemTypeCell;
    private ItemCraftingOrderLine orderLine;
    private MaterialSelectBox materialSelectBox;
    private ItemTypeSelectBox itemTypeSelectBox;
    private HighlightHandler highlightHandler;

    public ItemPartSelection(ItemPartOrder itemPartOrder, ItemCraftingOrderLine orderLine) {
        this.orderLine = orderLine;
        highlightHandler = new HighlightHandler();
        add(new Label(itemPartOrder.getName(), StaticSkin.getSkin()));
        add(createTable(itemPartOrder));
    }

    private Table createTable(ItemPartOrder itemPartOrder) {
        table = new Table();
        materialCell = table.add(); // cells change background on selectboxes focus.
        itemTypeCell = table.add();
        table.add(materialSelectBox = new MaterialSelectBox(itemPartOrder, this));
        if (itemPartOrder.getItemPartRecipe().itemTypes.size() > 0) {
            table.add(itemTypeSelectBox = new ItemTypeSelectBox(itemPartOrder, this));
        } else {
            table.add(new Label(itemPartOrder.getItemPartRecipe().itemTypes.get(0), StaticSkin.getSkin()));
        }
        return table;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        boolean isFocused = getStage().getKeyboardFocus() == materialSelectBox ||
                itemTypeSelectBox != null && getStage().getKeyboardFocus() == itemTypeSelectBox;
        updateHighlighting(isFocused);
        updateHint();
    }

    private void updateHint() {
        if (itemTypeSelectBox == null) {
            if (materialSelectBox.getList().getStage() == getStage()) {
                hint = "WS: navigate, ED: select material.";
            } else {
                hint = "WSE: show materials";
            }
        }
    }

    @Override
    public String getHint() {
        return null;
    }

    public boolean navigate(ControlActionsEnum action) {
        if (getStage().getKeyboardFocus() != itemTypeSelectBox && action == ControlActionsEnum.RIGHT) {
            return getStage().setKeyboardFocus(itemTypeSelectBox);
        }
        if (getStage().getKeyboardFocus() != materialSelectBox && action == ControlActionsEnum.LEFT) {
            return getStage().setKeyboardFocus(materialSelectBox);
        }
        return orderLine.navigate(action, this);
    }

    private class HighlightHandler extends Highlightable.CheckHighlightHandler {

        @Override
        public void handle() {
            // TODO update background
        }

    }

    @Override
    public Highlightable.HighlightHandler getHighlightHandler() {
        return highlightHandler;
    }
}
