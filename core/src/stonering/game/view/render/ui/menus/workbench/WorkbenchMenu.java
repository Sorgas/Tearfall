package stonering.game.view.render.ui.menus.workbench;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import stonering.entity.local.building.Building;
import stonering.entity.local.building.aspects.WorkbenchAspect;
import stonering.entity.local.crafting.ItemOrder;
import stonering.game.GameMvc;
import stonering.game.view.render.ui.menus.util.Highlightable;
import stonering.game.view.render.ui.menus.util.HintedActor;
import stonering.game.view.render.ui.menus.util.NavigableVerticalGroup;
import stonering.game.view.render.ui.menus.workbench.orderline.ItemCraftingOrderLine;
import stonering.util.global.StaticSkin;
import stonering.util.global.TagLoggersEnum;

/**
 * Menu for workbenches to manage crafting orders.
 * Has list of orders and buttons for closing and creating new order.
 * If building has {@link WorkbenchAspect} this menu is used.
 *
 * @author Alexander on 28.10.2018.
 */
public class WorkbenchMenu extends Window implements HintedActor {
    private static final String MENU_HINT = "E: new order, WS: navigate, Q: quit";

    private GameMvc gameMvc;
    private Building workbench;
    private WorkbenchAspect workbenchAspect; // aspect of selected workbench (M thing)

    private NavigableVerticalGroup orderList;
    private TextButton addOrderButton;
    private Label hintLabel;

    /**
     * Creates screen for selected built workbench on localMap. Can be used only for workbenches.
     * Will throw NPE if created on non-workbench workbench.
     */
    public WorkbenchMenu(Building building) {
        super(building.toString(), StaticSkin.getSkin());
        this.gameMvc = GameMvc.instance();
        this.workbench = building;
        workbenchAspect = building.getAspect(WorkbenchAspect.class);
        setKeepWithinStage(true);
        createTable();
        this.addListener(new MenuKeyInputListener());
        refillWorkbenchOrders();
    }

    /**
     * Creates menu table.
     */
    private void createTable() {
        setDebug(true, true);
        add(createOrderList().fill()).prefWidth(600).prefHeight(200).expandX();
        add(createCloseButton()).prefWidth(20).prefHeight(20).right().top().row();
        HorizontalGroup horizontalGroup = new HorizontalGroup();
        horizontalGroup.addActor(createAddButton());
        horizontalGroup.addActor(hintLabel = new Label(MENU_HINT, StaticSkin.getSkin()));
        add(horizontalGroup).prefHeight(20).left().top();
        setWidth(800);
        setHeight(600);
    }

    /**
     * Button for creating orders.
     */
    private TextButton createAddButton() {
        WorkbenchMenu menu = this;
        addOrderButton = new TextButton("New", StaticSkin.getSkin());
        addOrderButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ItemCraftingOrderLine orderLine = new ItemCraftingOrderLine(menu, new ItemOrder());
                orderLine.show();                                   // add to list
                getStage().setKeyboardFocus(orderLine);
                updateMenuHint(orderLine);
            }
        });
        return addOrderButton;
    }

    private NavigableVerticalGroup createOrderList() {
        orderList = new NavigableVerticalGroup();
        orderList.grow();
        orderList.getSelectKeys().add(Input.Keys.D);
        orderList.setPreNavigationListener(event -> {  // un highlight selected actor before navigation
            return true;
        });
        orderList.setNavigationListener(event -> {     // highlight selected actor after navigation
            return true;
        });
        orderList.setSelectListener(event -> {         // go to order line
            Actor selected = orderList.getSelectedElement();
            selected = selected != null ? selected : this;
            getStage().setKeyboardFocus(selected);
            updateMenuHint(selected);
            return true;
        });
        orderList.setCancelListener(event -> {
            getStage().setKeyboardFocus(this);
            updateMenuHint(this);              // return focus to screen
            orderList.setHighlighted(false);          // de highlight all
            return true;
        });
        return orderList;
    }

    /**
     * Refills list of screen with existing orders.
     */
    private void refillWorkbenchOrders() {
        workbenchAspect.getEntries().forEach(entry -> {
            orderList.addActor(createOrderLine(entry.getOrder()));
        });
        orderList.setHighlighted(false);
    }

    private ItemCraftingOrderLine createOrderLine(ItemOrder order) {
        return new ItemCraftingOrderLine(this, order);
    }

    /**
     * Checks if building is workbench (has workbench aspect).
     */
    private static boolean validateBuilding(Building building) {
        return building.getAspect(WorkbenchAspect.class) != null;
    }

    /**
     * Closes stage with this screen.
     */
    public void close() {
        gameMvc.getView().removeStage(getStage());
    }

    /**
     * Moves focus of stage to given actor, highlights it and changes hint, if possible.
     */
    public void updateMenuHint(Actor actor) {
        if (actor instanceof HintedActor) {
            hintLabel.setText(((HintedActor) actor).getHint());
        } else {
            hintLabel.setText("");
        }
    }

    /**
     * Input listener for this menu.
     */
    private class MenuKeyInputListener extends InputListener {

        @Override
        public boolean keyDown(InputEvent event, int keycode) {
            TagLoggersEnum.UI.logDebug("handling " + Input.Keys.toString(keycode) + " on WorkbenchMenu");
            event.stop();
            switch (keycode) {
                case Input.Keys.E: {
                    addOrderButton.toggle();
                    return true;
                }
                case Input.Keys.W:
                case Input.Keys.S: {
                    if (orderList.hasChildren()) {
                        orderList.setHighlighted(false);
                        orderList.setSelectedIndex(0);
                        orderList.navigate(keycode == Input.Keys.S ? 0 : -1);
                        getStage().setKeyboardFocus(orderList);
                        updateMenuHint(orderList);
                    }
                    return true;
                }
                case Input.Keys.Q: {
                    close();
                    return true;
                }
            }
            return false;
        }
    }

    private TextButton createCloseButton() {
        TextButton closeButton = new TextButton("X", StaticSkin.getSkin());
        closeButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                close();
                return true;
            }
        });
        return closeButton;
    }

    public NavigableVerticalGroup getOrderList() {
        return orderList;
    }

    public Building getWorkbench() {
        return workbench;
    }

    public WorkbenchAspect getWorkbenchAspect() {
        return workbenchAspect;
    }

    public String getHint() {
        return MENU_HINT;
    }
}
