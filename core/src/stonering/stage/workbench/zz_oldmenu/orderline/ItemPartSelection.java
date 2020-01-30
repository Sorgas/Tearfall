package stonering.stage.workbench.zz_oldmenu.orderline;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import stonering.entity.crafting.IngredientOrder;
import stonering.enums.ControlActionsEnum;
import stonering.enums.items.recipe.Ingredient;
import stonering.enums.images.DrawableMap;
import stonering.widget.Highlightable;
import stonering.widget.HintedActor;
import stonering.stage.workbench.zz_oldmenu.orderline.selectbox.ItemTypeSelectBox;
import stonering.stage.workbench.zz_oldmenu.orderline.selectbox.MaterialSelectBox;
import stonering.util.global.StaticSkin;

/**
 * Keeps two select boxes:
 * 1. for selecting material
 * 2. for selecting item type.
 * <p>
 * If {@link Ingredient} allows only one type of item, there is a label instead of SB.
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

    public ItemPartSelection(IngredientOrder ingredientOrder, ItemCraftingOrderLine orderLine) {
        this.orderLine = orderLine;
        createHighlightHandler();
        add(createTable(ingredientOrder));
//        add(new Container(new Label(ingredientOrder., StaticSkin.getSkin())).left().top());
    }

    private Table createTable(IngredientOrder ingredientOrder) {
        table = new Table();
        table.defaults().prefHeight(30);
        table.add(materialImage = new Image()).fillX(); // images change drawables on focus change.
        table.add(itemTypeImage = new Image()).fillX().row();
        table.add(materialSelectBox = new MaterialSelectBox(ingredientOrder, this));
        if (ingredientOrder.ingredient.itemTypes.size() > 1) {
            table.add(itemTypeSelectBox = new ItemTypeSelectBox(ingredientOrder, this));
        } else {
            table.add(new Label(ingredientOrder.ingredient.itemTypes.get(0), StaticSkin.getSkin()));
        }
        return table;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        updateHighlighting(isFocused());
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
                return getStage().setKeyboardFocus(itemTypeSelectBox);
            }
            if (focused != materialSelectBox && action == ControlActionsEnum.LEFT) {
                return getStage().setKeyboardFocus(materialSelectBox);
            }
            return orderLine.navigate(action, this);
        } else {
            if(focused == materialSelectBox) {
                return orderLine.navigate(action, this);
            } else {
                return getStage().setKeyboardFocus(materialSelectBox);
            }
        }
    }

    private void createHighlightHandler() {
        highlightHandler = new Highlightable.HighlightHandler(this) {

            @Override
            public void handle(boolean value) {
                materialImage.setDrawable(getStage().getKeyboardFocus() == materialSelectBox ? DrawableMap.REGION.getDrawable(focusedRegionName) : null);
                if (itemTypeSelectBox != null)
                    itemTypeImage.setDrawable(getStage().getKeyboardFocus() == itemTypeSelectBox ? DrawableMap.REGION.getDrawable(focusedRegionName) : null);
            }
        };
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
