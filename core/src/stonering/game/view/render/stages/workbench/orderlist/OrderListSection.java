package stonering.game.view.render.stages.workbench.orderlist;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import stonering.entity.building.aspects.WorkbenchAspect;
import stonering.entity.crafting.ItemOrder;
import stonering.enums.ControlActionsEnum;
import stonering.enums.items.recipe.Recipe;
import stonering.game.GameMvc;
import stonering.game.view.render.stages.workbench.WorkbenchMenu;
import stonering.game.view.render.ui.menus.util.NavigableVerticalGroup;

import static stonering.enums.ControlActionsEnum.SELECT;

/**
 * Displays list of orders of workbench, and their statuses.
 * Allows to navigate the list, select order for configuring, pausing/unpausing, setting for repeat, switching to recipe list for creating new order.
 * Controls:
 * R: toggle order repeated.
 * F: toggle order paused.
 * A: new order.
 * X: cancel order.
 * W: S: navigation.
 * Q: close menu.
 * E, D: configure order.
 * W, S: navigation.
 *
 * @author Alexander on 13.08.2019.
 */
public class OrderListSection extends NavigableVerticalGroup {
    private WorkbenchMenu menu;
    public final WorkbenchAspect aspect;
    private HighlightHandler highlightHandler;

    public OrderListSection(WorkbenchAspect aspect, WorkbenchMenu menu) {
        super();
        this.aspect = aspect;
        this.menu = menu;
        keyMapping.put(Input.Keys.D, SELECT);
        fillOrderList();
        createListener();
        highlightHandler = new HighlightHandler() {
            @Override
            public void handle() {
                for (Actor child : getChildren()) {
                    child.setColor(child.equals(getSelectedElement()) ? Color.RED : Color.LIGHT_GRAY);
                }
            }
        };
    }

    public void createOrder(Recipe recipe) {
        ItemOrder order = new ItemOrder(recipe);
        aspect.addOrder(order);
        addActor(new OrderItem(order, this));
        menu.orderDetailsSection.showOrder(order);
    }

    /**
     * Fetches orders from workbench aspect and creates order items for them.
     */
    private void fillOrderList() {
        for (WorkbenchAspect.OrderTaskEntry entry : aspect.getEntries()) {
            ItemOrder order = entry.order;
            addActor(new OrderItem(order, this));
        }
    }

    private void createListener() {
        addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (getSelectedElement() == null) return true;
                switch (ControlActionsEnum.getAction(keycode)) {
                    case Z_UP: {
                        ((OrderItem) getSelectedElement()).repeatButton.toggle();
                        break;
                    }
                    case Z_DOWN: {
                        ((OrderItem) getSelectedElement()).suspendButton.toggle();
                        break;
                    }
                    case LEFT: {
                        getStage().setKeyboardFocus(menu.recipeListSection);
                        break;
                    }
                    case DELETE: {
                        ((OrderItem) getSelectedElement()).cancelButton.toggle();
                        break;
                    }
                }
                return true;
            }
        });
        setSelectListener(new InputListener() { // configure order.
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                getStage().setKeyboardFocus(menu.orderDetailsSection);
                return true;
            }
        });
        setCancelListener(new InputListener() { // close menu
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                GameMvc.instance().getView().removeStage(getStage());
                return true;
            }
        });
    }
}
