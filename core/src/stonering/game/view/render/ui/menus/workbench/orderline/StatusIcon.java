package stonering.game.view.render.ui.menus.workbench.orderline;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import stonering.enums.OrderStatusEnum;
import stonering.game.view.render.ui.images.DrawableMap;

/**
 * Icon for showing status of order in workbenches.
 *
 * @author Alexander_Kuzyakov on 20.05.2019.
 */
public class StatusIcon extends Image {

    public StatusIcon(OrderStatusEnum status) {
        update(status);
    }

    public void update(OrderStatusEnum status) {
        setDrawable(DrawableMap.getInstance().getDrawable("order_status_icon:" + status.toString().toLowerCase()));
    }
}
