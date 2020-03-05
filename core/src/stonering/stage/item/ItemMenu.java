package stonering.stage.item;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import stonering.entity.item.Item;
import stonering.entity.RenderAspect;
import stonering.enums.items.ItemTagEnum;
import stonering.game.GameMvc;
import stonering.util.global.Logger;
import stonering.util.global.StaticSkin;
import stonering.widget.util.WrappedTextButton;

import javax.validation.constraints.NotNull;

/**
 * Menu for observing and managing single item.
 * Has two columns with item image and name, parts and effects.
 *
 * @author Alexander on 24.11.2019.
 */
public class ItemMenu extends Window {
    private Item item;

    public ItemMenu(@NotNull Item item) {
        super(item.title, StaticSkin.getSkin());
        this.item = item;
        createTable();
    }

    private void createTable() {
        add(createLeftColumn()).align(Align.top);
        add(createCenterColumn()).align(Align.top);
        add(createRightColumn()).align(Align.top).row();
    }

    private Table createLeftColumn() {
        Table table = new Table();
        table.add(new Image(item.getAspect(RenderAspect.class).region)).row();
        VerticalGroup tags = new VerticalGroup();
        for (ItemTagEnum tag : item.tags) {
            if (tag.isDisplayable()) tags.addActor(new Label(tag.name(), StaticSkin.getSkin()));
        }
        tags.columnAlign(Align.left);
        tags.align(Align.top);
        table.add(tags);
        return table;
    }

    private Table createCenterColumn() {
        Table table = new Table();
        table.add(new Label(item.title, StaticSkin.getSkin())).align(Align.left).row();
        table.add(new Label(item.type.description, StaticSkin.getSkin())).align(Align.left).row();
        table.add(new Label(item.type.name, StaticSkin.getSkin())).align(Align.left);
        //TODO add parts
        return table;
    }

    private Table createRightColumn() {
        Table table = new Table();
        WrappedTextButton button = new WrappedTextButton("X", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Logger.UI.logDebug("Closing item menu.");
                GameMvc.instance().view().removeStage(getStage());
            }
        });
        table.add(button).row();
        //TODO add parts
        return table;
    }
}
