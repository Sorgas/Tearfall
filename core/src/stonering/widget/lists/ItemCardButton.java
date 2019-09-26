package stonering.widget.lists;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import stonering.entity.item.Item;
import stonering.entity.item.selectors.ItemSelector;
import stonering.entity.item.selectors.SimpleItemSelector;
import stonering.util.global.StaticSkin;

/**
 * Item card to display in lists. Has item image, name and number label.
 * Also holds {@link ItemSelector} to get selected items from map;
 *
 * @author Alexander on 26.09.2019.
 */
public class ItemCardButton extends Button {
    private int number;
    private Label numberLabel;
    private String itemType;
    private String material;

    public ItemCardButton(Item item, int number) {
        super(StaticSkin.getSkin());
        this.number = number;
        add(new Image()).size(64,64);
        add(new Label(item.getTitle(), StaticSkin.getSkin())).size(200, 64);
        add(numberLabel = new Label(String.valueOf(number), StaticSkin.getSkin()));
        itemType = item.getType().name;
        material = item.materialString;
    }

    public ItemCardButton increment() {
        numberLabel.setText(number++);
        return this;
    }

    public ItemSelector getSelector() {
        return new SimpleItemSelector(itemType, material, number);
    }
}
