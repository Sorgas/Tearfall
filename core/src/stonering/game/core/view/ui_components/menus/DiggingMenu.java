package stonering.game.core.view.ui_components.menus;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import javafx.scene.control.ToolBar;
import stonering.enums.designations.DesignationsTypes;
import stonering.game.core.controller.controllers.DesignationsController;
import stonering.game.core.view.ui_components.Toolbar;
import stonering.utils.global.StaticSkin;

public class DiggingMenu extends Menu {
    private DesignationsController controller;
    private Toolbar toolbar;

    public DiggingMenu() {
        super();
        createTable();
    }

    private void createTable() {
        this.defaults().padBottom(5).fillX();
        this.pad(10);
        this.right().bottom();

        TextButton diggingButton = new TextButton("dig", StaticSkin.getSkin());
        diggingButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                controller.setActiveDesignation(DesignationsTypes.DIG);
                toolbar.closeMenus();
            }
        });
        this.add(diggingButton).right().row();
        hotkeyMap.put('d', diggingButton);

        TextButton rampButton = new TextButton("ramp", StaticSkin.getSkin());
        rampButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

            }
        });
        this.add(rampButton).row();
        hotkeyMap.put('r', rampButton);

        TextButton channelButton = new TextButton("channel", StaticSkin.getSkin());
        channelButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

            }
        });
        this.add(channelButton).row();
        hotkeyMap.put('c', channelButton);

        TextButton stairsButton = new TextButton("stairs", StaticSkin.getSkin());
        stairsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

            }
        });
        this.add(stairsButton).row();
        hotkeyMap.put('t', stairsButton);

        TextButton pillarButton = new TextButton("pillar", StaticSkin.getSkin());
        pillarButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

            }
        });
        this.add(pillarButton).row();
    }

    public void openDiggingPanel() {

    }

    public void setController(DesignationsController controller) {
        this.controller = controller;
    }

    public void setToolbar(Toolbar toolbar) {
        this.toolbar = toolbar;
    }
}
