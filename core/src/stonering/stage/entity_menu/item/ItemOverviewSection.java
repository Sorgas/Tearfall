package stonering.stage.entity_menu.item;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.utils.Align;

import stonering.entity.RenderAspect;
import stonering.entity.item.Item;
import stonering.enums.items.ItemTagEnum;
import stonering.stage.entity_menu.building.MenuSection;
import stonering.util.global.StaticSkin;

/**
 * @author Alexander on 08.06.2020.
 */
public class ItemOverviewSection extends MenuSection {
    
    public ItemOverviewSection(Item item) {
        super("Item");
        add(new Label(item.title, StaticSkin.skin())).row();
        add(new Image(item.get(RenderAspect.class).region)).row();
        add(new Label(item.type.description, StaticSkin.skin()));
        VerticalGroup tags = new VerticalGroup();
        for (ItemTagEnum tag : item.tags) {
            if (tag.isDisplayable()) tags.addActor(new Label(tag.name(), StaticSkin.getSkin()));
        }
        tags.columnAlign(Align.left);
        tags.align(Align.top);
    }

    @Override
    public String getHint() {
        return null;
    }
}
