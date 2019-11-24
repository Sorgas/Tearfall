package stonering.stage.item;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import stonering.entity.item.Item;
import stonering.util.global.StaticSkin;

import javax.validation.constraints.NotNull;

/**
 * Menu for observing and managing single item.
 *
 * @author Alexander on 24.11.2019.
 */
public class ItemMenu extends Window {
    private Item item;


    public ItemMenu(@NotNull Item item) {
        super(item.getTitle(), StaticSkin.getSkin());
        this.item = item;
        createTable();
    }

    private void createTable() {
        add(new Label(item.getTitle(), StaticSkin.getSkin()));
        setWidth(800);
        setHeight(600);
    }
}
