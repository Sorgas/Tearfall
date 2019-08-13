package stonering.game.view.render.ui.menus.workbench.newmenu.orderlist;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import stonering.entity.building.aspects.WorkbenchAspect;
import stonering.entity.crafting.ItemOrder;
import stonering.enums.ControlActionsEnum;
import stonering.game.view.render.ui.menus.util.NavigableVerticalGroup;

import static stonering.enums.ControlActionsEnum.SELECT;

/**
 * Displays list of orders of workbench, and their statuses.
 * Allows to navigate the list, select order for configuring, pausing/unpausing, setting for repeat, switching to recipe list for creating new order.
 *
 * @author Alexander on 13.08.2019.
 */
public class OrderList extends NavigableVerticalGroup {
    private WorkbenchAspect aspect;
    private HighlightHandler highlightHandler;

    public OrderList() {
        super();
        keyMapping.put(Input.Keys.D, SELECT);
        fillOrderList();

    }

    /**
     * Fetches orders from workbench aspect and creates order items for them.
     */
    private void fillOrderList() {
        for (WorkbenchAspect.OrderTaskEntry entry : aspect.getEntries()) {
            ItemOrder order = entry.order;
            addActor(new OrderItem(order));
        }
    }

    private void createListener() {
        addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (getSelectedElement() == null) return true;
                switch (ControlActionsEnum.getAction(keycode)) {
                    case Z_UP: {
                        ((OrderItem) getSelectedElement()).toggleRepeat();
                        break;
                    }
                    case Z_DOWN: {
                        ((OrderItem) getSelectedElement()).togglePause();
                        break;
                    }
                    case LEFT: {
                        // new order
                        break;
                    }
                    case DELETE: {
                        // remove order
                        break;
                    }
                }
                return true;
            }
        });
        setSelectListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                // configure
                return true;
            }
        });
        setCancelListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                // close menu
                return true;
            }
        });
    }
}
