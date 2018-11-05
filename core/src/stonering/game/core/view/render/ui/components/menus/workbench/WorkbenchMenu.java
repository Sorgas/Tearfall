package stonering.game.core.view.render.ui.components.menus.workbench;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import stonering.entity.local.building.Building;
import stonering.entity.local.building.aspects.WorkbenchAspect;
import stonering.game.core.GameMvc;
import stonering.game.core.view.render.ui.components.menus.util.Invokable;
import stonering.utils.global.StaticSkin;

import java.util.Stack;

/**
 * Menu for workbenches to manage crafting orders.
 *
 * @author Alexander on 28.10.2018.
 */
public class WorkbenchMenu extends Table implements Invokable {
    private GameMvc gameMvc;
    private CraftingOrderedList list;
    private WorkbenchAspect workbenchAspect; // aspect of selected workbench (M thing)
    private Stack<Invokable> focusStack; // chain of elements. first one is focused.

    private TextButton addOrderButton;
    private TextButton closeButton;

    /**
     * Creates menu for selected built workbench building on localMap. Can be used only for workbenches.
     * Will throw NPE if created on non-workbench building.
     *
     * @param workbench
     */
    public WorkbenchMenu(GameMvc gameMvc, Building workbench) {
        super();
        workbenchAspect = (WorkbenchAspect) workbench.getAspects().get(WorkbenchAspect.NAME);
        createTable();
        fillWorkbenchOrders();
        this.gameMvc = gameMvc;
        focusStack = new Stack<>();
        focusStack.push(this);
    }

    private void createTable() {
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
        list = new CraftingOrderedList(gameMvc, false);
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
            case Input.Keys.Q: {
                close();
                return true;
            }
            case Input.Keys.E: {
                createOrder();
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

    private void close() {

    }

    /**
     * Creates new empty order line and moves focus to it.
     */
    private CraftingOrderLine createOrder() {
        CraftingOrderLine orderLine = new CraftingOrderLine();
        list.addEntry(0, orderLine); // to the top of the list
        focusStack.push(list);
        focusStack.push(orderLine);
        // expand dropdown
        return orderLine;
    }

    private void goToMenu() {

    }

    /**
     * Fills list of menu with existing orders.
     */
    private void fillWorkbenchOrders() {
        workbenchAspect.getOrders().forEach(order -> {
            list.addEntry(0, new CraftingOrderLine());
        });
    }
}
