package stonering.stage.entity_menu.item;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import stonering.entity.RenderAspect;
import stonering.entity.item.Item;
import stonering.enums.items.ItemTagEnum;
import stonering.stage.entity_menu.building.MenuSection;
import stonering.util.lang.StaticSkin;

/**
 * @author Alexander on 08.06.2020.
 */
public class ItemOverviewSection extends MenuSection {
    
    public ItemOverviewSection(Item item) {
        super("Item");
        add(new Label(item.title, StaticSkin.skin())).left().row();
        add(new Image(item.get(RenderAspect.class).region)).row();
        add(new Label(item.type.description, StaticSkin.skin())).padBottom(10);
        item.tags.stream()
                .filter(ItemTagEnum::isDisplayable)
                .forEach(tag -> add(new Label(tag.toString(), StaticSkin.skin())).row());
    }

    @Override
    public String getHint() {
        return null;
    }
}
