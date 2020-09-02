package stonering.stage.entity_menu.building;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import stonering.entity.item.selectors.ItemSelector;
import stonering.enums.images.DrawableMap;
import stonering.util.lang.StaticSkin;

/**
 * Section of {@link BuildingMaterialTab} for single building part. Has buttons to selecting all available items for construction.
 *
 * @author Alexander on 06.04.2020
 */
public abstract class ItemSelectSection extends Container<Table> {
    protected final int WIDTH = 200;
    protected Table table;

    public ItemSelectSection(String title) {
        setActor(table = new Table());
        table.defaults().growX().left();
        table.add(new Label(title, StaticSkin.getSkin())).colspan(2).row();
        table.add(createEnablingButton("allow all", true));
        table.add(createEnablingButton("clear all", false)).row();
        table.setDebug(true, true);
        table.top();
        fill().top();
        table.setBackground(DrawableMap.REGION.getDrawable("default"));
    }

    private TextButton createEnablingButton(String text, boolean enable) {
        TextButton button = new TextButton(text, StaticSkin.getSkin());
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                setAllEnabled(enable);
            }
        });
        return button;
    }

    public abstract ItemSelector getItemSelector();

    protected abstract void setAllEnabled(boolean enable);

    public abstract boolean isAtLeastOneSelected();
}
