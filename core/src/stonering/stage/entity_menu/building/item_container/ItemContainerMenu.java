package stonering.stage.entity_menu.building.item_container;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import stonering.entity.item.Item;
import stonering.entity.item.aspects.ItemContainerAspect;
import stonering.stage.entity_menu.building.MenuSection;
import stonering.stage.entity_menu.container.ItemDetailSection;
import stonering.util.lang.StaticSkin;

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
    private final Table listTable; // contains rows of items
    
    public ItemContainerMenu(ItemContainerAspect containerAspect) {
        this.containerAspect = containerAspect;
        scrollPane = new ScrollPane(listTable = new Table());
        Container<ScrollPane> container = new Container<>(scrollPane).fill();
        listTable.top();
        container.size(600, 900);
        add(container).size(600, 900).fill();
        this.setBackground(StaticSkin.getColorDrawable(StaticSkin.background));
        this.add(details = new ItemDetailSection()).size(300, 900).fill();
        details.setBackground(StaticSkin.getColorDrawable(StaticSkin.backgroundFocused));
        fillItems();
        setDebug(true, true);
    }
    
    private void fillItems() {
        if(containerAspect.items.isEmpty()) {
            listTable.add(new Label("No items here", StaticSkin.skin())).fillX();
        } else {
            containerAspect.items.forEach(this::createRowForItem);
        }
    }
    
    private void createRowForItem(Item item) {
        Label label = new Label(item.title, StaticSkin.getSkin());
        listTable.add(label).growX().row();
        label.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                details.setItem(item);
            }
        });
    }
}
