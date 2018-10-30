package stonering.game.core.view.render.ui.components.menus.workbench;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import stonering.game.core.view.render.ui.components.menus.util.Invokable;
import stonering.utils.global.StaticSkin;

/**
 * Menu for workbenches to manage crafting orders.
 *
 * @author Alexander on 28.10.2018.
 */
public class WorkbenchMenu extends Table implements Invokable {
    private TextButton addOrderButton;
    private TextButton closeButton;

    private CraftingOrderedList list;

    private Invokable focus = this;

    public WorkbenchMenu() {
        super();
        createTable();
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
        if(focus != this) {
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
}
