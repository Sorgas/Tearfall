package stonering.game.core.view.render.ui.menus.workbench;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import stonering.entity.local.building.Building;
import stonering.entity.local.building.aspects.WorkbenchAspect;
import stonering.enums.items.Recipe;
import stonering.game.core.GameMvc;
import stonering.game.core.view.render.ui.menus.workbench.orderline.CraftingOrderLine;
import stonering.game.core.view.render.ui.menus.workbench.orderline.RecipeSelectOrderLine;
import stonering.utils.global.StaticSkin;

/**
 * Menu for workbenches to manage crafting orders.
 * Has list of orders and buttons for closing and creating new order.
 *
 * @author Alexander on 28.10.2018.
 */
public class WorkbenchMenu extends Table {
    private GameMvc gameMvc;
    private Stage stage;
    private Building workbench;
    private WorkbenchAspect workbenchAspect; // aspect of selected workbench (M thing)

    private CraftingOrderedList list;
    private TextButton addOrderButton;
    private TextButton closeButton;

    /**
     * Creates menu for selected built workbench on localMap. Can be used only for workbenches.
     * Will throw NPE if created on non-workbench workbench.
     */
    public WorkbenchMenu(GameMvc gameMvc, Stage stage, Building building) {
        super();
        this.gameMvc = gameMvc;
        this.stage = stage;
        stage.setKeyboardFocus(this);
        this.workbench = building;
        workbenchAspect = (WorkbenchAspect) building.getAspects().get(WorkbenchAspect.NAME);
        createTable();
        fillWorkbenchOrders();
    }

    /**
     * Creates menu table.
     */
    private void createTable() {
        this.setDebug(true);
        this.setWidth(200);
        this.setHeight(200);
        this.setFillParent(true);
        this.add(createOrderList());
        this.add(createCloseButton()).right().top().row();
        this.add(createAddButton()).right().top();
        this.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
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
                    case Input.Keys.Q: {
                        close();
                        return true;
                    }
                }
                return false;
            }
        });
    }

    private TextButton createCloseButton() {
        closeButton = new TextButton("X", StaticSkin.getSkin());
        closeButton.addListener(event -> {
            close();
            return true;
        });
        return closeButton;
    }

    private TextButton createAddButton() {
        addOrderButton = new TextButton("New", StaticSkin.getSkin());
        addOrderButton.addListener(event -> {
            createNewOrder();
            return true;
        });
        return addOrderButton;
    }

    private CraftingOrderedList createOrderList() {
        list = new CraftingOrderedList();
        return list;
    }

    /**
     * Creates new empty order line and moves focus to it.
     */
    private RecipeSelectOrderLine createNewOrder() {
        RecipeSelectOrderLine orderLine = new RecipeSelectOrderLine(gameMvc, this);
        list.addOrderLine(0, orderLine); // to the top of the list
        getStage().setKeyboardFocus(orderLine);
        return orderLine;
    }

    /**
     * Moves focus to oreder list
     */
    private void goToMenu() {
        getStage().setKeyboardFocus(list);
    }

    /**
     * Fills list of menu with existing orders.
     */
    private void fillWorkbenchOrders() {
        workbenchAspect.getOrders().forEach(order -> {
            list.addOrderLine(0, new CraftingOrderLine(gameMvc, this, order));
        });
    }

    public void createOrderLineForRecipe(Recipe recipe) {

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

    /**
     * Closes stage with this menu.
     */
    public void close() {
        //TODO add changes discarding.
        stage.keyDown(Input.Keys.Q);
    }
}
