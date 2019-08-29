package stonering.game.view.render.stages.workbench.orderlist;

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
import stonering.game.view.render.stages.workbench.WorkbenchMenu;
import stonering.game.view.render.ui.images.DrawableMap;
import stonering.game.view.render.ui.menus.util.NavigableVerticalGroup;
import stonering.util.global.StaticSkin;

import static com.badlogic.gdx.Input.Keys.SHIFT_LEFT;
import static stonering.enums.ControlActionsEnum.LEFT;
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
    public final WorkbenchMenu menu;
    public final WorkbenchAspect aspect;
    private final Label emptyLabel;

    public OrderListSection(WorkbenchAspect aspect, WorkbenchMenu menu) {
        super();
        this.aspect = aspect;
        this.menu = menu;
        keyMapping.put(Input.Keys.D, SELECT);
        emptyLabel = new Label("This workbench has no orders.", StaticSkin.getSkin());
        fillOrderList();
        createListener();
    }

    public void createOrder(Recipe recipe) {
        ItemOrder order = new ItemOrder(recipe);
        aspect.addOrder(order);
        OrderItem orderItem = new OrderItem(order, this);
        removeActor(emptyLabel);
        addActorAt(0, orderItem);
        menu.orderDetailsSection.showItem(orderItem);
        getStage().setKeyboardFocus(this);
        System.out.println("refocus");
    }

    /**
     * Fetches orders from workbench aspect and creates order items for them.
     */
    public void fillOrderList() {
        this.clearChildren();
        if (aspect.getEntries().isEmpty()) {
            addActor(emptyLabel);
        } else {
            for (WorkbenchAspect.OrderTaskEntry entry : aspect.getEntries()) {
                ItemOrder order = entry.order;
                addActor(new OrderItem(order, this));
            }
        }
    }

    private void createListener() {
        getListeners().insert(0, new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                ControlActionsEnum action = ControlActionsEnum.getAction(keycode);
                if (action == LEFT) {
                    getStage().setKeyboardFocus(menu.recipeListSection);
                    setSelectedIndex(-1);
                    return true;
                }
                if (getSelectedElement() == null || !(getSelectedElement() instanceof OrderItem)) return true;
                OrderItem target = ((OrderItem) getSelectedElement());
                switch (action) {
                    case Z_UP: {
                        target.repeatButton.toggle();
                        return true;
                    }
                    case Z_DOWN: {
                        target.suspendButton.toggle();
                        return true;
                    }
                    case DELETE: {
                        target.cancelButton.toggle();
                        return true;
                    }
                    case UP:
                        if (Gdx.input.isKeyPressed(SHIFT_LEFT) || Gdx.input.isKeyPressed(SHIFT_LEFT)) {
                            target.upButton.toggle();
                            return true;
                        }
                        return false;
                    case DOWN:
                        if (Gdx.input.isKeyPressed(SHIFT_LEFT) || Gdx.input.isKeyPressed(SHIFT_LEFT)) {
                            target.downButton.toggle();
                            return true;
                        }
                        return false;
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
        setHighlightHandler(new CheckHighlightHandler(this) {
            @Override
            public void handle(boolean value) {
                menu.ordersHeader.setBackground(DrawableMap.instance().getDrawable("workbench_order_line" +
                        (value ? ":focused" : "")));
            }
        });
    }

    @Override
    public void setSelectedIndex(int newIndex) {
        super.setSelectedIndex(newIndex);
        menu.orderDetailsSection.showItem(getSelectedElement());
    }

    public boolean isEmpty() {
        return aspect.getEntries().isEmpty();
    }
}
