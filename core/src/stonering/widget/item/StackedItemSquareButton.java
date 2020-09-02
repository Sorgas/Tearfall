package stonering.widget.item;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import stonering.entity.item.Item;
import stonering.util.lang.StaticSkin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Square button of item.
 * Consists of background, item icon, foreground (becomes shaded when button is disabled), and label with number.
 * Can store list of particular items, for further usage.
 * <p>
 * TODO add tooltip
 *
 * @author Alexander on 17.02.2020.
 */
public class StackedItemSquareButton extends SingleItemSquareButton {
    public final List<Item> items = new ArrayList<>();
    private Label numberLabel;

    public StackedItemSquareButton(List<Item> items, Drawable background) {
        super(items.get(0), background);
        numberLabel = new Label(String.valueOf(items.size()), StaticSkin.getSkin());
        numberLabel.setFillParent(true);
        numberLabel.setAlignment(Align.bottomLeft);
        stack.add(numberLabel);
        setItems(items);
    }

    public StackedItemSquareButton(Item item, Drawable background) {
        this(Arrays.asList(item), background);
    }

    public void setItems(List<Item> items) {
        super.setItem(items.get(0));
        this.items.clear();
        this.items.addAll(items);
        updateLabel();
    }
    
    public void addItem(Item item) {
        items.add(item);
        updateLabel();
    }
    
    public boolean removeItem(Item item) {
        boolean result = items.remove(item);
        updateLabel();
        return result;
    }
    
    public void updateLabel() {
        numberLabel.setText(items.size());
    }
}
