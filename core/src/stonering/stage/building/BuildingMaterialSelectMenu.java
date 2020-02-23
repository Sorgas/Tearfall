package stonering.stage.building;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import stonering.entity.building.BuildingOrder;
import stonering.enums.buildings.blueprint.Blueprint;
import stonering.game.GameMvc;
import stonering.util.geometry.Position;
import stonering.util.global.StaticSkin;

import java.util.List;

/**
 * Has left section with number and type(one) designated buildings, required and selected materials,
 * and right section with available materials.
 * Shows separate widget for each building part. When widget is selected, 
 * right part of menu is filled with appropriate items and allows to select them for this part.
 *
 * When all part are filled, confirm button is unlocked, and {@link BuildingOrder} can be created.
 *
 * @author Alexander on 17.02.2020
 */
public class BuildingMaterialSelectMenu extends Window {
    LeftSection leftSection;
    RightSection rightSection;
    Position position;
    Label hintLabel;
    TextButton cancelButton;
    TextButton confirmButton;

    public BuildingMaterialSelectMenu(Blueprint blueprint, List<Position> positions) {
        super("", StaticSkin.getSkin());
        position = positions.get(0);
        defaults().height(800).fill().expand();
        add(leftSection = new LeftSection(this, blueprint, positions.size())).width(300);
        add(rightSection = new RightSection(this)).width(900).fill().row();
        add(hintLabel = new Label("", StaticSkin.getSkin())).expandX().fillX().row();
        add(confirmButton = new TextButton("Confirm", StaticSkin.getSkin())); // todo combine in horizontal group
        add(cancelButton = new TextButton("Cancel", StaticSkin.getSkin()));
        cancelButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameMvc.view().removeStage(getStage());
            }
        });
        leftSection.setSelected(leftSection.widgets.get(0));
    }
}
