package stonering.game.core.view.render.ui.components.menus.workbench;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import stonering.entity.local.building.Building;
import stonering.entity.local.building.aspects.WorkbenchAspect;
import stonering.game.core.view.render.ui.components.menus.util.Invokable;
import stonering.utils.global.StaticSkin;

/**
 * Menu for workbenches to manage crafting orders.
 *
 * @author Alexander on 28.10.2018.
 */
public class WorkbenchMenu extends Table implements Invokable {
    private CraftingOrderedList list;
    private WorkbenchAspect workbenchAspect;

    private TextButton addOrderButton;
    private TextButton closeButton;

    private Invokable focus = this;

    public WorkbenchMenu(Building workbench) {
        super();
        workbenchAspect = (WorkbenchAspect) workbench.getAspects().get(WorkbenchAspect.NAME);
        if (workbenchAspect != null) {
            createTable();
            fillWorkbenchOrders();
        }
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
        list = new CraftingOrderedList();
        return list;
    }

    @Override
    public boolean invoke(int keycode) {
        if (focus != this) {
            return focus.invoke(keycode);
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
     * Creates new empty order and moves focus to it.
     */
    private void createOrder() {
        CraftingOrderLine orderLine = new CraftingOrderLine();
        list.addEntry(0, orderLine);
        focus = orderLine;
    }

    private void goToMenu() {

    }

    private void fillWorkbenchOrders() {
        workbenchAspect.getOrders().forEach(order -> {
            list.addEntry(0, new CraftingOrderLine());
        });
    }
}
