package stonering.stage.entity_menu.container;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import stonering.entity.RenderAspect;
import stonering.entity.item.Item;
import stonering.stage.entity_menu.building.item_container.ItemContainerMenu;
import stonering.stage.entity_menu.building.MenuSection;
import stonering.util.lang.StaticSkin;

/**
 * Section for {@link ItemContainerMenu}.
 * Consists of row with item group filters, sorting row and grid of items.
 *
 * @author Alexander on 5/17/2020
 */
public class ItemDetailSection extends MenuSection {
    private final Image image;
    private final Label description;

    public ItemDetailSection() {
        super("Details:");
        add(image = new Image()).height(100);
        add(description = new Label("description", StaticSkin.getSkin()));
        this.setBackground(StaticSkin.getColorDrawable(StaticSkin.background)); // highlight focused section
    }

    public void setItem(Item item) {
        image.setDrawable(new TextureRegionDrawable(item.get(RenderAspect.class).region));
        description.setText(item.type.description);
    }

    @Override
    public String getHint() {
        return "";
    }
}
