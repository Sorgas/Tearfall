package stonering.game.view.render.ui.menus.workbench.orderline;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import stonering.enums.OrderStatusEnum;
import stonering.util.global.StaticSkin;

/**
 * Icon for showing status of order in workbenches.
 * TODO replace with image implementation.
 *
 * @author Alexander_Kuzyakov on 20.05.2019.
 */
public class StatusIcon extends Label {

    public StatusIcon(OrderStatusEnum status) {
        super(status.toString(), StaticSkin.getSkin());
    }

    public void update(OrderStatusEnum status) {
        setText(status.toString());
    }
}
