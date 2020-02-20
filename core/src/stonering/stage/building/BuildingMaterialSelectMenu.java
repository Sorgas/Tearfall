package stonering.stage.building;

import com.badlogic.gdx.scenes.scene2d.ui.Window;
import stonering.enums.buildings.blueprint.Blueprint;
import stonering.util.geometry.Position;
import stonering.util.global.StaticSkin;

import java.util.List;

/**
 * Has left section with number and type(one) designated buildings, required and selected materials,
 * and right section with available materials.
 * Shows separate widget for each building part. When widget is selected, 
 * right part of menu is filled with appropriate items and allows to select them for this part.
 *
 * @author Alexander on 17.02.2020
 */
public class BuildingMaterialSelectMenu extends Window {
    LeftSection leftSection;
    RightSection rightSection;
    Position position;

    public BuildingMaterialSelectMenu(Blueprint blueprint, List<Position> positions) {
        super("", StaticSkin.getSkin());
        position = positions.get(0);
        defaults().height(800).fill().expand();
        add(leftSection = new LeftSection(this, blueprint, positions.size())).width(300);
        add(rightSection = new RightSection());
        leftSection.setSelected(0);
    }
}
