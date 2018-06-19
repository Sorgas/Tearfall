package stonering.game.core.view.ui_components.menus;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import stonering.enums.designations.DesignationTypes;
import stonering.game.core.GameMvc;
import stonering.game.core.controller.controllers.DesignationsController;
import stonering.utils.global.StaticSkin;

/**
 * Menu for selecting building.
 *
 * Created by Alexander on 25.01.2018.
 */
public class GeneralBuildingMenu extends SubMenuMenu {
    private Toolbar toolbar;

    public GeneralBuildingMenu(GameMvc gameMvc) {
        super(gameMvc);
        createTable();
    }

    @Override
    public void init() {

    }

    private void initButtons() {
        createButton("C: constructions", "constructions", 'c');
        createButton("W: workbenches", "workbenches", 'w');
        createButton("F: furniture", "furniture", 'f');
//        menus.put("constructions", new ConstructionsMenu());
    }

    private void createButton(String text, String menu, char hotKey) {
        TextButton button = new TextButton(text, StaticSkin.getSkin());
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

            }
        });
        this.addActor(button);
        buttons.put(hotKey, button);
    }

    public void showSubMenu(String menu) {
        menus.get(menu).show();
    }

    private void createTable() {
        this.pad(10);
        this.right().bottom();

        TextButton diggingButton = new TextButton("D: dig", StaticSkin.getSkin());
        diggingButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
//                toolbar.closeMenus();
            }
        });
        this.addActor(diggingButton);
    }

    private void addButtonToTable(String text, DesignationTypes type, char hotKey) {
        TextButton button = new TextButton(text, StaticSkin.getSkin());
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
//                controller. setActiveDesignation(type);
//                toolbar.closeMenus();
            }
        });
        this.addActor(button);
    }
    public void setController(DesignationsController controller) {
        //this.controller = controller;
    }

    public void setToolbar(Toolbar toolbar) {
        this.toolbar = toolbar;
    }
}
