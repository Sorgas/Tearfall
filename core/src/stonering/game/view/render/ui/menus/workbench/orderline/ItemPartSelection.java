package stonering.game.view.render.ui.menus.workbench.orderline;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import stonering.entity.local.crafting.ItemPartOrder;
import stonering.enums.ControlActionsEnum;
import stonering.enums.items.recipe.ItemPartRecipe;
import stonering.game.view.render.ui.menus.util.Highlightable;
import stonering.game.view.render.ui.menus.util.HintedActor;
import stonering.game.view.render.ui.menus.workbench.orderline.selectbox.ItemTypeSelectBox;
import stonering.game.view.render.ui.menus.workbench.orderline.selectbox.MaterialSelectBox;
import stonering.util.global.StaticSkin;

import java.util.function.Consumer;

/**
 * Keeps two select boxes:
 * 1. for selecting material
 * 2. for selecting item type.
 * <p>
 * If {@link ItemPartRecipe} allows only one type of item, there is a label instead of SB.
 *
 * @author Alexander_Kuzyakov on 26.06.2019.
 */
public class ItemPartSelection extends Table implements HintedActor, Highlightable {
    private String hint;
    private ItemCraftingOrderLine orderLine;
    private MaterialSelectBox materialSelectBox;
    private ItemTypeSelectBox itemTypeSelectBox;

    public ItemPartSelection(ItemPartOrder itemPartOrder, ItemCraftingOrderLine orderLine) {
        this.orderLine = orderLine;
        add(materialSelectBox = new MaterialSelectBox(itemPartOrder, this));
        if(itemPartOrder.getItemPartRecipe().itemTypes.size() > 0) {
            add(itemTypeSelectBox = new ItemTypeSelectBox(itemPartOrder, this));
        } else {
            add(new Label(itemPartOrder.getItemPartRecipe().itemTypes.get(0), StaticSkin.getSkin()));
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);

    }

    private void updateHint() {
        if(itemTypeSelectBox == null) {
            if (materialSelectBox.getList().getStage() == getStage()) {
                hint = "WS: navigate, ED: select material.";
            } else {
                hint = "WSE: show materials";
            }
        }
    }

    @Override
    public void setHighlightHandler(Consumer<Boolean> handler) {

    }

    @Override
    public Consumer<Boolean> getHighlightHandler() {
        return null;
    }

    @Override
    public String getHint() {
        return null;
    }

    public boolean navigate(ControlActionsEnum action) {
        if(getStage().getKeyboardFocus() != itemTypeSelectBox && action == ControlActionsEnum.RIGHT) {
            return getStage().setKeyboardFocus(itemTypeSelectBox);
        }
        if(getStage().getKeyboardFocus() != materialSelectBox && action == ControlActionsEnum.LEFT) {
            return getStage().setKeyboardFocus(materialSelectBox);
        }
        return orderLine.navigate(action, this);
    }
}
