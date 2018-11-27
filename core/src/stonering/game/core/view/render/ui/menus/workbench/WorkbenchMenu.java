package stonering.game.core.view.render.ui.menus.workbench;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import stonering.entity.local.building.Building;
import stonering.entity.local.building.aspects.WorkbenchAspect;
import stonering.enums.buildings.BuildingTypeMap;
import stonering.game.core.GameMvc;
import stonering.game.core.view.render.ui.menus.util.Invokable;
import stonering.utils.global.StaticSkin;

import java.util.Stack;

/**
 * Menu for workbenches to manage crafting orders.
 * Has list of orders and buttons for closing and creating new order.
 *
 * @author Alexander on 28.10.2018.
 */
public class WorkbenchMenu extends Table implements Invokable {
    private GameMvc gameMvc;
    private Building workbench;
    private WorkbenchAspect workbenchAspect; // aspect of selected workbench (M thing)
    private Stack<Invokable> focusStack; // chain of elements. first one is focused.

    private CraftingOrderedList list;
    private TextButton addOrderButton;
    private TextButton closeButton;

    /**
     * Creates menu for selected built workbench on localMap. Can be used only for workbenches.
     * Will throw NPE if created on non-workbench workbench.
     */
    public WorkbenchMenu(GameMvc gameMvc, Building building) {
        super();
        this.gameMvc = gameMvc;
        this.workbench = building;
        workbenchAspect = (WorkbenchAspect) building.getAspects().get(WorkbenchAspect.NAME);
        createTable();
        fillWorkbenchOrders();
        focusStack = new Stack<>();
        focusStack.push(this);
    }

    /**
     * Cerates menu table
     */
    private void createTable() {
        this.setDebug(true);
        this.setWidth(200);
        this.setHeight(200);
        this.setFillParent(true);
        this.add(createOrderList());
        this.add(createCloseButton()).right().top().row();
        this.add(createAddButton()).right().top().row();
    }

    private TextButton createCloseButton() {
        closeButton = new TextButton("X", StaticSkin.getSkin());
        return closeButton;
    }

    private TextButton createAddButton() {
        addOrderButton = new TextButton("New", StaticSkin.getSkin());
        return addOrderButton;
    }

    private CraftingOrderedList createOrderList() {
        list = new CraftingOrderedList();
        return list;
    }

    /**
     * Handles input from stage. Invokes focused element of this menu;
     *
     * @param keycode
     * @return
     */
    @Override
    public boolean invoke(int keycode) {
        if (focusStack.peek() != this) {
            return focusStack.peek().invoke(keycode);
        }
        switch (keycode) {
            case Input.Keys.E: {
                createNewOrder();
                return true;
            }
            case Input.Keys.W:
            case Input.Keys.A:
            case Input.Keys.S:
            case Input.Keys.D: {
                goToMenu();

                return true;
            }
        }
        return false;
    }

    /**
     * Creates new empty order line and moves focus to it.
     */
    private CraftingOrderLine createNewOrder() {
        System.out.println("creating order");
        CraftingOrderLine orderLine = new CraftingOrderLine(gameMvc, this);
        list.addEntry(0, orderLine); // to the top of the list
        focusStack.push(list);
        focusStack.push(orderLine);
        return orderLine;
    }

    private void goToMenu() {

    }

    /**
     * Fills list of menu with existing orders.
     */
    private void fillWorkbenchOrders() {
        workbenchAspect.getOrders().forEach(order -> {
            list.addEntry(0, new CraftingOrderLine(gameMvc, this, order));
        });
    }

    /**
     * Checks if building is workbench (has workbench aspect).
     */
    private static boolean validateBuilding(Building building) {
        return building.getAspects().containsKey(WorkbenchAspect.NAME);
    }

    public WorkbenchAspect getWorkbenchAspect() {
        return workbenchAspect;
    }
}
