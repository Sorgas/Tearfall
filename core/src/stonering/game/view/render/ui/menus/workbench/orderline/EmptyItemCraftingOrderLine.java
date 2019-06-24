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
public class EmptyItemCraftingOrderLine extends OrderLine {
    private static final String LINE_HINT = "order line hint";
    private PlaceHolderSelectBox<Recipe> recipeSelectBox;

    public EmptyItemCraftingOrderLine(WorkbenchMenu menu) {
        super(menu, LINE_HINT);
    }

    private void initLine() {
        leftHG.addActor(createRecipeSelectBox());
    }

    /**
     * Creates selectBox with list of all workbench recipes. After selection of recipe, this select box is replaced with material selection.
     */
    public PlaceHolderSelectBox createRecipeSelectBox() {
        List<Recipe> recipeList = new ArrayList<>(menu.getWorkbenchAspect().getRecipes());
        recipeSelectBox = new PlaceHolderSelectBox<>(new Recipe("Select item"));
        recipeSelectBox.setItems(recipeList.toArray(new Recipe[]{}));
        recipeSelectBox.getListeners().insert(0, new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                switch (ControlActionsEnum.getAction(keycode)) {
                    case RIGHT:
                    case SELECT: { // opens list or saves selected value
                        if (recipeSelectBox.getList().getStage() != null) { // list is shown
                            recipeSelectBox.hideList();
                            if (!recipeSelectBox.getSelected().equals(recipeSelectBox.getPlaceHolder())) { // placeholder is selected
                                replaceSelfWith(recipeSelectBox.getSelected());
                            } else { // not a valid case
                                warningLabel.setText("Item not selected");
                            }
                        } else {  // open list
                            showSelectBoxList(recipeSelectBox);
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
                recipeSelectBox.navigate(1);
                return true;
            }
        });
        recipeSelectBox.getList().addListener(createTouchListener());
        return recipeSelectBox;
    }

    private void replaceSelfWith(Recipe recipe) {
        hide();
        ItemCraftingOrderLine orderLine = new ItemCraftingOrderLine(menu, new ItemOrder(recipe));
        menu.getOrderList().addActorAt(0, orderLine);
    }
}
