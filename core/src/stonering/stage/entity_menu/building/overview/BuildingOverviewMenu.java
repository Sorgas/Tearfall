package stonering.stage.entity_menu.building.overview;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

import stonering.entity.building.Building;
import stonering.game.GameMvc;
import stonering.util.lang.StaticSkin;

/**
 * Menu for showing general information about building.
 * Shows building name, description, and parts.
 *
 * @author Alexander on 6/5/2020
 */
public class BuildingOverviewMenu extends Container<Table> {
    private final Building building;
    private final Table mainTable;

    public BuildingOverviewMenu(Building building) {
        super(new Table().align(Align.topLeft));
        this.building = building;
        mainTable = getActor();
        size(900, 900).fill();
        setBackground(StaticSkin.getColorDrawable(StaticSkin.background));
        mainTable.add(new Label(building.type.title, StaticSkin.skin())).row();
        mainTable.add(new Label(building.type.description, StaticSkin.skin())).row();
        addParts();
        createListener();
        setDebug(true, true);
    }

    private void createListener() {
        addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode != Input.Keys.Q) return false;
                GameMvc.view().removeStage(getStage());
                return true;
            }
        });
    }

    private void addParts() {
        //TODO
    }
}
