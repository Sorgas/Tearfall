package stonering.stage.workbench.orderlist;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import stonering.entity.building.aspects.WorkbenchAspect;
import stonering.entity.crafting.ItemOrder;
import stonering.enums.ControlActionsEnum;
import stonering.enums.items.recipe.Recipe;
import stonering.game.GameMvc;
import stonering.game.model.system.building.BuildingContainer;
import stonering.stage.workbench.MenuSection;
import stonering.stage.workbench.WorkbenchMenu;
import stonering.widget.NavigableVerticalGroup;
import stonering.util.global.StaticSkin;

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
public class OrderListSection extends MenuSection {
    public final WorkbenchMenu menu;
    public final WorkbenchAspect aspect;
    private final Label emptyLabel;
    public final NavigableVerticalGroup<OrderItem> orderList;

    public OrderListSection(String title, WorkbenchAspect aspect, WorkbenchMenu menu) {
        super(title);
        this.aspect = aspect;
        this.menu = menu;
        add(orderList = new NavigableVerticalGroup<>());
        orderList.keyMapping.put(Input.Keys.D, SELECT);
        emptyLabel = new Label("This workbench has no orders.", StaticSkin.getSkin());
        fillOrderList();
    }

    private void createListeners() {
        addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                switch(keycode) {
                    case Input.Keys.W:
                        orderList.navigate(-1); // up
                        return true;
                    case Input.Keys.S:
                        orderList.navigate(1); // down
                        return true;
                    case Input.Keys.Q:
                        GameMvc.view().removeStage(getStage());
                        return true;
                    case Input.Keys.A:
                        getStage().setKeyboardFocus(menu.recipeTreeSection); // to recipes
                        return true;
                    case Input.Keys.X:
                        if(Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                            // cancel order
                        } else {
                            // suspend order
                        }
                        return true;
                    case Input.Keys.E:
                        // move up
                        break;
                    case Input.Keys.D:
                        // move down
                        break;
                    case Input.Keys.R:
                        // increase quantity
                        break;
                    case Input.Keys.F:
                        // decrease quantity
                        break;
                    case Input.Keys.C:
                        // configure
                        break;
                }
                return false;
            }
        });
    }

    public void createOrder(Recipe recipe) {
        ItemOrder order = new ItemOrder(recipe);
        GameMvc.model().get(BuildingContainer.class).workbenchSystem.addOrder(aspect, order);
        OrderItem orderItem = new OrderItem(order, this);
        orderList.removeActor(emptyLabel);
        orderList.addActorAt(0, orderItem);
        menu.orderDetailsSection.showItem(orderItem);
        getStage().setKeyboardFocus(this);
    }

    /**
     * Fetches orders from workbench aspect and creates order items for them.
     */
    public void fillOrderList() {
        orderList.clearChildren();
        if (aspect.orders.isEmpty()) {
            orderList.addActor(emptyLabel);
        } else {
            aspect.orders.forEach(order -> orderList.addActor(new OrderItem(order, this)));
        }
    }

    private ItemOrder getSelectedOrder() {
        return orderList.getSelectedElement().order;
    }

    private void moveOrder(ItemOrder order, boolean up) {
        int oldIndex = aspect.orders.indexOf(order);
        orderList.getSelectedElement();
    }

    public boolean isEmpty() {
        return orderList.getChildren().isEmpty();
    }
}
