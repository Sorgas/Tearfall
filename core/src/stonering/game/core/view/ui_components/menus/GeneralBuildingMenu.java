package stonering.game.core.view.ui_components.menus;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import stonering.enums.designations.DesignationTypes;
import stonering.game.core.GameMvc;
import stonering.game.core.controller.controllers.DesignationsController;
import stonering.utils.global.StaticSkin;

/**
 * ButtonMenu for selecting building.
 * <p>
 * Created by Alexander on 25.01.2018.
 */
public class GeneralBuildingMenu extends SubMenuMenu {
    private Toolbar toolbar;

    public GeneralBuildingMenu(GameMvc gameMvc) {
        super(gameMvc, 1);
        hideable = true;
        createTable();
    }

    @Override
    public void init() {
        super.init();
    }

    private void initButtons() {
        createButton("C: constructions", 'c');
        createButton("W: workbenches", 'w');
        createButton("F: furniture", 'f');
        menus.put('c', new ConstructionsMenu(gameMvc));
//        menus.put('w', new ConstructionsMenu(gameMvc));
//        menus.put('f', new ConstructionsMenu(gameMvc));
    }

    private void createButton(String text, char hotKey) {
        super.createButton(text, hotKey, new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                menus.get(hotKey).show();
            }
        });
    }

    private void createTable() {
        this.pad(10);
        this.right().bottom();
    }

    @Override
    public void reset() {

    }
}
