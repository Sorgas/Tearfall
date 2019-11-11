package stonering.stage.workbench.zz_oldmenu;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import stonering.entity.building.Building;
import stonering.entity.building.aspects.WorkbenchAspect;
import stonering.entity.crafting.ItemOrder;
import stonering.enums.ControlActionsEnum;
import stonering.game.GameMvc;
import stonering.widget.Highlightable;
import stonering.widget.HintedActor;
import stonering.widget.NavigableVerticalGroup;
import stonering.stage.workbench.zz_oldmenu.orderline.EmptyOrderLine;
import stonering.stage.workbench.zz_oldmenu.orderline.ItemCraftingOrderLine;
import stonering.stage.workbench.zz_oldmenu.orderline.OrderLine;
import stonering.util.global.StaticSkin;
import stonering.util.global.Logger;

/**
 * Menu for workbenches to manage crafting orders.
 * Has list of orders, close button and button for new order.
 * If building has {@link WorkbenchAspect} this menu is used.
 *
 * @author Alexander on 28.10.2018.
 */
public class WorkbenchMenuq extends Window implements HintedActor {
    private static final String MENU_HINT = "E: new order, WSD: navigate, AQ: quit";
    private WorkbenchAspect workbenchAspect; // aspect of selected workbench (M thing)
    private NavigableVerticalGroup orderList;
    private Label hintLabel;

    /**
     * Creates screen for selected built workbench on localMap. Can be used only for workbenches.
     * Will throw NPE if created on non-workbench workbench.
     */
    public WorkbenchMenuq(Building building) {
        super(building.toString(), StaticSkin.getSkin());
        workbenchAspect = building.getAspect(WorkbenchAspect.class);
        setKeepWithinStage(true);
        createTable();
        this.addListener(new MenuKeyInputListener());
        refillWorkbenchOrders();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        Actor focused = getStage().getKeyboardFocus();
        hintLabel.setText(focused instanceof HintedActor ? ((HintedActor) focused).getHint() : "");
    }

    private void createTable() {
        setDebug(true, true);
        add(createOrderList().fill()).prefWidth(600).prefHeight(200).expandX();
        add(createCloseButton()).prefWidth(20).prefHeight(20).right().top().row();
        HorizontalGroup horizontalGroup = new HorizontalGroup();
        horizontalGroup.addActor(createAddButton());
        horizontalGroup.addActor(hintLabel = new Label("", StaticSkin.getSkin()));
        add(horizontalGroup).prefHeight(20).left().top();
    }

    private NavigableVerticalGroup createOrderList() {
        orderList = new NavigableVerticalGroup();
        orderList.grow();
        orderList.keyMapping.put(Input.Keys.D, ControlActionsEnum.SELECT); // add additional control key, so pressing D-D on menu will get player to order.
        orderList.keyMapping.put(Input.Keys.A, ControlActionsEnum.CANCEL); // add additional control key, so pressing A-A on order will get player to menu.
        orderList.setSelectListener(event -> {                             // go to order line or menu (invalid case)
            Actor selected = orderList.getSelectedElement();
            ((ItemCraftingOrderLine) selected).navigateToFirst();
            getStage().setKeyboardFocus(selected != null ? selected : this);
            return true;
        });
        orderList.setHighlightHandler(new Highlightable.HighlightHandler(this) {
            @Override
            public void handle(boolean value) {                              // highlight selected element
                for (Actor actor : orderList.getChildren()) {
                    if (actor instanceof Highlightable)
                        ((Highlightable) actor).updateHighlighting(actor == orderList.getSelectedElement());
                }
            }
        });
        orderList.setCancelListener(event -> {                            // go to menu
            getStage().setKeyboardFocus(this);
            return true;
        });
        return orderList;
    }

    /**
     * Refills list of menus with existing orders.
     */
    private void refillWorkbenchOrders() {
        workbenchAspect.orders.forEach(order -> orderList.addActor(createOrderLine(order)));
    }

    private ItemCraftingOrderLine createOrderLine(ItemOrder order) {
        return new ItemCraftingOrderLine(this, order);
    }

    /**
     * Input listener for this menu.
     */
    private class MenuKeyInputListener extends InputListener {

        @Override
        public boolean keyDown(InputEvent event, int keycode) {
            Logger.UI.logDebug("handling " + Input.Keys.toString(keycode) + " on WorkbenchMenu");
            event.stop();
            switch (ControlActionsEnum.getAction(keycode)) {
                case SELECT: {
                    createNewOrder();
                    return true;
                }
                case UP:
                case DOWN:
                case RIGHT: {
                    if (orderList.hasChildren()) {
                        orderList.setSelectedIndex(0);
                        getStage().setKeyboardFocus(orderList);
                    }
                    return true;
                }
                case CANCEL: {
                    return close();
                }
            }
            return false;
        }
    }

    private TextButton createAddButton() {
        TextButton button = new TextButton("New", StaticSkin.getSkin());
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                createNewOrder();
            }
        });
        return button;
    }

    private void createNewOrder() {
        OrderLine orderLine = new EmptyOrderLine(this);
        orderLine.show();
    }

    private TextButton createCloseButton() {
        TextButton closeButton = new TextButton("X", StaticSkin.getSkin());
        closeButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return close();
            }
        });
        return closeButton;
    }

    public boolean close() {
        GameMvc.instance().getView().removeStage(getStage());
        return true;
    }

    public NavigableVerticalGroup getOrderList() {
        return orderList;
    }

    public WorkbenchAspect getWorkbenchAspect() {
        return workbenchAspect;
    }

    public String getHint() {
        return MENU_HINT;
    }
}
