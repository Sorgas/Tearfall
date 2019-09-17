package stonering.stage.workbench.zz_oldmenu.orderline;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import stonering.enums.TaskStatusEnum;
import stonering.enums.images.DrawableMap;

/**
 * Icon for showing status of order in workbenches.
 *
 * @author Alexander_Kuzyakov on 20.05.2019.
 */
public class StatusIcon extends Image {

    public StatusIcon(TaskStatusEnum status) {
        update(status);
    }

    public void update(TaskStatusEnum status) {
        setDrawable(DrawableMap.instance().getDrawable("order_status_icon:" + status.toString().toLowerCase()));
    }
}
