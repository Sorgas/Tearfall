package stonering.game.view.render.ui.menus.workbench.orderline;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import stonering.entity.crafting.ItemPartOrder;
import stonering.enums.ControlActionsEnum;
import stonering.enums.items.recipe.ItemPartRecipe;
import stonering.game.view.render.ui.images.DrawableMap;
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
    private final static String focusedRegionName = "order_selection:focused";
    private String hint;
    private Table table;
    private Image materialImage;
    private Image itemTypeImage;
    private ItemCraftingOrderLine orderLine;
    private MaterialSelectBox materialSelectBox;
    private ItemTypeSelectBox itemTypeSelectBox;
    private HighlightHandler highlightHandler;

    public ItemPartSelection(ItemPartOrder itemPartOrder, ItemCraftingOrderLine orderLine) {
        this.orderLine = orderLine;
        highlightHandler = new HighlightHandler();
        add(createTable(itemPartOrder));
        add(new Container(new Label(itemPartOrder.getName(), StaticSkin.getSkin())).left().top());
    }

    private Table createTable(ItemPartOrder itemPartOrder) {
        table = new Table();
        table.defaults().prefHeight(30);
        table.add(materialImage = new Image()).fillX(); // images change drawables on focus change.
        table.add(itemTypeImage = new Image()).fillX().row();
        table.add(materialSelectBox = new MaterialSelectBox(itemPartOrder, this));
        if (itemPartOrder.getItemPartRecipe().itemTypes.size() > 1) {
            table.add(itemTypeSelectBox = new ItemTypeSelectBox(itemPartOrder, this));
        } else {
            table.add(new Label(itemPartOrder.getItemPartRecipe().itemTypes.get(0), StaticSkin.getSkin()));
        }
        return table;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        updateHighlighting(true);
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
        Actor focused  = getStage().getKeyboardFocus();
        if(itemTypeSelectBox != null) {
            if (focused != itemTypeSelectBox && action == ControlActionsEnum.RIGHT) {
                return changeFocus(itemTypeSelectBox);
            }
            if (focused != materialSelectBox && action == ControlActionsEnum.LEFT) {
                return changeFocus(materialSelectBox);
            }
            return orderLine.navigate(action, this);
        } else {
            if(focused == materialSelectBox) {
                return orderLine.navigate(action, this);
            } else {
                return changeFocus(materialSelectBox);
            }
        }
    }

    private boolean changeFocus(Actor target) {
        return getStage().setKeyboardFocus(target);
    }

    private class HighlightHandler extends Highlightable.HighlightHandler {

        @Override
        public void handle() {
            materialImage.setDrawable(getStage().getKeyboardFocus() == materialSelectBox ? DrawableMap.getInstance().getDrawable(focusedRegionName) : null);
            if (itemTypeSelectBox != null)
                itemTypeImage.setDrawable(getStage().getKeyboardFocus() == itemTypeSelectBox ? DrawableMap.getInstance().getDrawable(focusedRegionName) : null);
        }
    }

    @Override
    public Highlightable.HighlightHandler getHighlightHandler() {
        return highlightHandler;
    }

    public boolean isFocused() {
        Actor focused  = getStage().getKeyboardFocus();
        return focused == materialSelectBox || itemTypeSelectBox != null && focused == itemTypeSelectBox;
    }
}
