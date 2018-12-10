package stonering.game.core.view.render.ui.menus.workbench;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import stonering.entity.local.building.Building;
import stonering.entity.local.building.aspects.WorkbenchAspect;
import stonering.game.core.GameMvc;
import stonering.game.core.view.render.ui.menus.util.Highlightable;
import stonering.game.core.view.render.ui.menus.util.NavigableVerticalGroup;
import stonering.game.core.view.render.ui.menus.workbench.orderline.ItemCraftingOrderLine;
import stonering.utils.global.StaticSkin;
import stonering.utils.global.TagLoggersEnum;

/**
 * Menu for workbenches to manage crafting orders.
 * Has list of orders and buttons for closing and creating new order.
 *
 * @author Alexander on 28.10.2018.
 */
public class WorkbenchMenu extends Table implements Highlightable {
    private GameMvc gameMvc;
    private Building workbench;
    private WorkbenchAspect workbenchAspect; // aspect of selected workbench (M thing)

    private NavigableVerticalGroup orderList;
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
    }

    /**
     * Creates menu table.
     */
    private void createTable() {
        this.setDebug(true);
        this.setWidth(500);
        this.add(new Label(workbench.getName(), StaticSkin.getSkin())).colspan(2).row();
        this.add(createOrderList()).prefWidth(600);
        this.add(createCloseButton()).prefWidth(20).prefHeight(20).right().top().row();
        this.add(createAddButton()).prefHeight(20).left().top();
        this.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                TagLoggersEnum.UI.logDebug("handling " + Input.Keys.toString(keycode) + " on WorkbenchMenu");
                event.stop();
                switch (keycode) {
                    case Input.Keys.E: {
                        return createNewOrderLine();
                    }
                    case Input.Keys.W:
                    case Input.Keys.A:
                    case Input.Keys.S:
                    case Input.Keys.D: {
                        updateFocusAndBackground(orderList);
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
        closeButton.setWidth(100);
        closeButton.setHeight(100);
        closeButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                close();
                return true;
            }
        });
        return closeButton;
    }

    private TextButton createAddButton() {
        addOrderButton = new TextButton("New", StaticSkin.getSkin());
        addOrderButton.addListener(
                new InputListener() {
                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        return createNewOrderLine();
                    }
                });
        return addOrderButton;
    }

    private NavigableVerticalGroup createOrderList() {
        orderList = new NavigableVerticalGroup();
        orderList.setColor(Color.BLUE);
        orderList.setSelectListener(event -> {
            event.stop();
            Actor selected = orderList.getSelectedElement();
            updateFocusAndBackground(selected != null ? selected : this);
            return true;
        });
        orderList.setCancelListener(event -> {
            event.stop();
            close();
            return true;
        });
        orderList.left();
        return orderList;
    }

    /**
     * Creates new empty order line, adds it to order list and moves focus to it.
     */
    private boolean createNewOrderLine() {
        ItemCraftingOrderLine orderLine = new ItemCraftingOrderLine(gameMvc, this);
        orderLine.show();
        updateFocusAndBackground(orderLine.getItemTypeList());
        orderLine.setHighlighted(true);
        return true;
    }

    /**
     * Fills list of menu with existing orders.
     */
    private void fillWorkbenchOrders() {
        workbenchAspect.getOrders().forEach(order -> {
//            orderList.addActorAt(0, new CraftingOrderLine(gameMvc, this, order));
        });
    }

    /**
     * Checks if building is workbench (has workbench aspect).
     */
    private static boolean validateBuilding(Building building) {
        return building.getAspects().containsKey(WorkbenchAspect.NAME);
    }

    /**
     * Closes stage with this menu.
     */
    public void close() {
        gameMvc.getView().removeStage(getStage());
    }


    public void updateFocusAndBackground(Actor actor) {
        getStage().setKeyboardFocus(actor);
        setHighlighted(getStage().getKeyboardFocus() == this);
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

    @Override
    public void setHighlighted(boolean value) {
        this.setBackground(new TextureRegionDrawable(
                new TextureRegion(new Texture("sprites/ui_back.png"), value ? 100 : 0, 0, 100, 100)));
    }
}
