package stonering.stage.workbench.orderlist;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import stonering.entity.building.aspects.WorkbenchAspect;
import stonering.entity.crafting.ItemOrder;
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
    private NavigableVerticalGroup<OrderItem> orderList;

    public OrderListSection(String title, WorkbenchAspect aspect, WorkbenchMenu menu) {
        super(title);
        this.aspect = aspect;
        this.menu = menu;
        orderList = new NavigableVerticalGroup<>();
        orderList.keyMapping.put(Input.Keys.D, SELECT);
        emptyLabel = new Label("This workbench has no orders.", StaticSkin.getSkin());
        fillOrderList();
    }

    public void createOrder(Recipe recipe) {
        ItemOrder order = new ItemOrder(recipe);
        GameMvc.model().get(BuildingContainer.class).workbenchSystem.addOrder(aspect, order);
        OrderItem orderItem = new OrderItem(order, this);
        removeActor(emptyLabel);
        addActorAt(0, orderItem);
        menu.orderDetailsSection.showItem(orderItem);
        getStage().setKeyboardFocus(this);
    }

    /**
     * Fetches orders from workbench aspect and creates order items for them.
     */
    public void fillOrderList() {
        this.clearChildren();
        if (aspect.orders.isEmpty()) {
            addActor(emptyLabel);
        } else {
            aspect.orders.forEach(order -> addActor(new OrderItem(order, this)));
        }
    }

    public boolean isEmpty() {
        return orderList.getChildren().isEmpty();
    }
}
