package stonering.stage.entity_menu.item;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;

import stonering.entity.item.Item;
import stonering.game.GameMvc;
import stonering.util.lang.StaticSkin;

import javax.validation.constraints.NotNull;

/**
 * Menu for observing and managing single item.
 * Has column with item name, image and description; column with explanation of item aspects.
 *
 * @author Alexander on 24.11.2019.
 */
public class ItemMenu extends Container<Table> {
    private Item item;
    private Table table;
    private ItemOverviewSection overviewSection;
    private ItemAspectSection aspectSection;

    public ItemMenu(@NotNull Item item) {
        super(new Table());
        this.item = item;
        this.table = getActor();
        table.add(overviewSection = new ItemOverviewSection(item)).align(Align.top).size(300, 900);
        table.add(aspectSection = new ItemAspectSection(item)).align(Align.top).size(600, 900);
        overviewSection.setBackground(StaticSkin.generator.generate(StaticSkin.backgroundFocused));
        aspectSection.setBackground(StaticSkin.generator.generate(StaticSkin.background));
        createListeners();
    }

    private void createListeners() {
        addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode != Input.Keys.Q) return false;
                GameMvc.view().removeStage(getStage());
                return true;
            }
        });
    }
}
