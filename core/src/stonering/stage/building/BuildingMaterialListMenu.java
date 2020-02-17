package stonering.stage.building;

import com.badlogic.gdx.scenes.scene2d.ui.Window;
import stonering.util.global.StaticSkin;

/**
 * Has left section with list of designated buildings, required and selected materials,
 * and right section with available materials.
 * @author Alexander on 17.02.2020
 */
public class BuildingMaterialListMenu extends Window {
    public BuildingMaterialListMenu() {
        super("", StaticSkin.getSkin());

    }
}
