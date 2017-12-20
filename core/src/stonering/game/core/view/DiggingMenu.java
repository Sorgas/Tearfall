package stonering.game.core.view;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import stonering.utils.global.StaticSkin;

public class DiggingMenu extends Table {
    DiggingMenu menu = this;
    private DiggingSubMenu diggingSubMenu;

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

            }
        });
        this.add(diggingButton).right().row();


        TextButton rampButton = new TextButton("ramp", StaticSkin.getSkin());
        rampButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

            }
        });
        this.add(rampButton).row();

        diggingSubMenu = new DiggingSubMenu();
        TextButton channelButton = new TextButton("channel", StaticSkin.getSkin());
        channelButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                diggingSubMenu.setVisible(!diggingSubMenu.isVisible());
                menu.invalidateHierarchy();
            }
        });
        this.add(diggingSubMenu);
        this.add(channelButton).row();


        TextButton stairsButton = new TextButton("stairs", StaticSkin.getSkin());
        stairsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

            }
        });
        this.add(stairsButton).row();


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
}
