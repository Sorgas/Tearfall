package stonering.stage.building;

import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import stonering.util.global.StaticSkin;

/**
 * Row for {@link MaterialItemsSelectSection}.
 * Displays item material, type, number of items.
 * Has checkbox that can be toggled to enable these items for building.
 *
 * @author Alexander on 25.03.2020
 */
public class MaterialTreeEntry extends Table {
    private String material;
    private String itemType;
    private CheckBox checkbox;
    private int number;

    public MaterialTreeEntry(String material, String itemType, int number) {
        add(new Label(material, StaticSkin.getSkin()));
        add(new Label(itemType, StaticSkin.getSkin()));
        add(new Label(number + "", StaticSkin.getSkin()));
        add(checkbox = new CheckBox("", StaticSkin.getSkin()));
    }
}
