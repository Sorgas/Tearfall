package stonering.game.view.render.ui.menus.workbench.orderline;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import stonering.entity.local.crafting.ItemOrder;
import stonering.enums.ControlActionsEnum;
import stonering.enums.items.recipe.Recipe;
import stonering.game.view.render.ui.lists.PlaceHolderSelectBox;
import stonering.game.view.render.ui.menus.workbench.WorkbenchMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * This line shows select box with list of available crafting recipes.
 * When recipe is selected, this line replaces itself with {@link ItemCraftingOrderLine}
 *
 * @author Alexander_Kuzyakov on 24.06.2019.
 */
public class EmptyOrderLine extends OrderLine {
    private static final String LINE_HINT = "WS: select recipe, ED: confirm";
    private PlaceHolderSelectBox<Recipe> recipeSelectBox;

    public EmptyOrderLine(WorkbenchMenu menu) {
        super(menu, LINE_HINT);
        leftHG.addActor(recipeSelectBox = createRecipeSelectBox());
        focusedSelectBox = recipeSelectBox;
    }

    /**
     * Creates selectBox with list of all workbench recipes. After selection of recipe, this select box is replaced with material selection.
     */
    public PlaceHolderSelectBox<Recipe> createRecipeSelectBox() {
        List<Recipe> recipeList = new ArrayList<>(menu.getWorkbenchAspect().getRecipes());
        PlaceHolderSelectBox<Recipe> selectBox = new PlaceHolderSelectBox<>(new Recipe("Select item"));
        selectBox.setItems(recipeList.toArray(new Recipe[]{}));
        selectBox.getListeners().insert(0, new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                switch (ControlActionsEnum.getAction(keycode)) {
                    case RIGHT:
                    case SELECT: { // opens list or saves selected value
                        if (selectBox.getList().getStage() != null) { // list is shown
                            selectBox.hideList();
                            if (!selectBox.getSelected().equals(selectBox.getPlaceHolder())) { // placeholder is selected
                                replaceSelfWith(selectBox.getSelected());
                            } else { // not a valid case
                                warningLabel.setText("Item not selected");
                            }
                        } else {  // open list
                            showSelectBoxList(selectBox);
                        }
                        return true;
                    }
                    case LEFT:
                    case CANCEL: {
                        hide();
                        return true;
                    }
                }
                return super.keyDown(event, keycode);
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                super.touchDown(event, x, y, pointer, button);
                selectBox.navigate(1);
                return true;
            }
        });
//        recipeSelectBox.getList().addListener(createTouchListener());
        return selectBox;
    }

    private void replaceSelfWith(Recipe recipe) {
        hide();
        ItemCraftingOrderLine orderLine = new ItemCraftingOrderLine(menu, new ItemOrder(recipe));
        menu.getOrderList().addActorAt(0, orderLine);
    }
}
