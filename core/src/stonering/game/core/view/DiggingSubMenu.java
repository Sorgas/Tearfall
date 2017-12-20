package stonering.game.core.view;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import stonering.utils.global.StaticSkin;

public class DiggingSubMenu extends Table {
    public DiggingSubMenu() {
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
        this.add(diggingButton).row();


        TextButton rampButton = new TextButton("ramp", StaticSkin.getSkin());
        diggingButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

            }
        });
        this.add(rampButton).row();


        TextButton channelButton = new TextButton("channel", StaticSkin.getSkin());
        diggingButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

            }
        });
        this.add(channelButton).row();
    }

}
