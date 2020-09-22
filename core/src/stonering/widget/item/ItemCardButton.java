package stonering.widget.item;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import stonering.entity.item.Item;
import stonering.entity.item.selectors.ItemSelector;
import stonering.entity.item.selectors.SimpleItemSelector;
import stonering.stage.renderer.atlas.AtlasesEnum;
import stonering.util.lang.StaticSkin;
import stonering.widget.button.IconTextButton;

/**
 * Item card to display in lists. Has item image, name and number label.
 * Also holds {@link ItemSelector} to get selected items from map;
 *
 * @author Alexander on 26.09.2019.
 */
public class ItemCardButton extends IconTextButton {
    private int number;
    private Label numberLabel;
    public final ItemSelector selector;

    public ItemCardButton(Item item, int number) {
        super(new TextureRegionDrawable(AtlasesEnum.items.getBlockTile(item.type.atlasXY)), item.title);
        this.number = number;
//        imageCell.size(32,32);
//        labelCell.size(200, 32);
        add(numberLabel = new Label(String.valueOf(number), StaticSkin.getSkin()));
        selector = new SimpleItemSelector(item.type.name, item.material, number);
    }

    public ItemCardButton increment() {
        numberLabel.setText(number++);
        return this;
    }
}
