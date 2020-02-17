package stonering.widget.item;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import stonering.entity.item.Item;

/**
 * Button for single item that shows its icon;
 *
 * @author Alexander on 17.02.2020
 */
public class SingleItemSquareButton extends Button {
    public Item item;

    public SingleItemSquareButton(Item item) {
        this.item = item;
    }
}
