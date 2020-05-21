package stonering.stage.workbench;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import stonering.entity.item.Item;
import stonering.entity.item.aspects.ItemContainerAspect;
import stonering.stage.container.ItemDetailSection;
import stonering.util.global.StaticSkin;
import stonering.widget.item.ItemButtonGrid;

/**
 * Menu for observing items stored in {@link ItemContainerAspect}.
 * Shows list of items, Click on an item shows item details in detail section.
 *
 * @author Alexander on 5/13/2020
 */
public class ItemContainerMenu extends Table {
    private final ItemContainerAspect containerAspect;
    private final ItemDetailSection details;
    private MenuSection focused;
    private ScrollPane scrollPane;
    private final Table listTable;
    
    public ItemContainerMenu(ItemContainerAspect containerAspect) {
        this.containerAspect = containerAspect;
        this.add(scrollPane = new ScrollPane(null)).size(600, 900);
        scrollPane.setActor(listTable = new Table());
        containerAspect.items.forEach(this::createRowForItem);
        this.setBackground(StaticSkin.getColorDrawable(StaticSkin.backgroundFocused));
        this.add(details = new ItemDetailSection());
        setDebug(true, true);
    }
    
    private void createRowForItem(Item item) {
        Label label = new Label(item.updateTitle(), StaticSkin.getSkin());
        listTable.add(label).growX().row();
        label.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                details.setItem(item);
            }
        });
    }
}
