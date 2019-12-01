package stonering.stage.item;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import stonering.entity.item.Item;
import stonering.entity.unit.aspects.RenderAspect;
import stonering.enums.items.TagEnum;
import stonering.game.GameMvc;
import stonering.util.global.Logger;
import stonering.util.global.StaticSkin;
import stonering.widget.util.WrappedTextButton;

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
        add(createLeftColumn()).align(Align.top);
        add(createCenterColumn()).align(Align.top);
        add(createRightColumn()).align(Align.top);
    }

    private Table createLeftColumn() {
        Table table = new Table();
        table.add(new Image(item.getAspect(RenderAspect.class).drawable)).row();
        VerticalGroup tags = new VerticalGroup();
        for (TagEnum tag : item.tags) {
            if (tag.isDisplayable()) tags.addActor(new Label(tag.name(), StaticSkin.getSkin()));
        }
        tags.columnAlign(Align.left);
        tags.align(Align.top);
        table.add(tags);
        return table;
    }

    private Table createCenterColumn() {
        Table table = new Table();
        table.add(new Label(item.getTitle(), StaticSkin.getSkin())).align(Align.left).row();
        table.add(new Label(item.type.description, StaticSkin.getSkin())).align(Align.left).row();
        table.add(new Label(item.type.name, StaticSkin.getSkin())).align(Align.left);
        //TODO add parts
        return table;
    }

    private Table createRightColumn() {
        Table table = new Table();
        table.add(new WrappedTextButton("X", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Logger.UI.logDebug("Closing item menu.");
                GameMvc.instance().view().removeStage(getStage());
            }
        })).row();
        //TODO add parts
        return table;
    }
}
