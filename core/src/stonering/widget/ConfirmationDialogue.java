package stonering.widget;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import stonering.game.GameMvc;
import stonering.util.lang.StaticSkin;

/**
 * Tiny window with 'Confirm' and 'Cancel' buttons.
 * Closes itself with buttons and runs appropriate function;
 * Has E and Q hotKeys for confirming and cancelling.
 *
 * @author Alexander on 15.07.2020.
 */
public class ConfirmationDialogue extends Window {
    private Button confirmButton; 
    private Button cancelButton; 
    
    public Runnable confirmAction;
    public Runnable cancelAction;

    public ConfirmationDialogue(String title) {
        super("", StaticSkin.skin());
        add(new Label(title, StaticSkin.skin())).colspan(2).size(400, 80).fill().row();
        add(confirmButton = new TextButton("E: Confirm", StaticSkin.getSkin())).size(190, 50).pad(5).fill();
        add(cancelButton = new TextButton("Q: Cancel", StaticSkin.getSkin())).size(190, 50).pad(5).fill();
        setBackground(StaticSkin.getColorDrawable(StaticSkin.backgroundFocused));
        setSize(400, 140);
        confirmButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameMvc.view().removeStage(getStage());
                if(confirmAction != null) confirmAction.run();
            }
        });
        cancelButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameMvc.view().removeStage(getStage());
                if(cancelAction != null) cancelAction.run();
            }
        });
        addListener(new InputListener() {
            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                switch (keycode) {
                    case Input.Keys.Q:
                        cancelButton.toggle();
                        return true;
                    case Input.Keys.E:
                        confirmButton.toggle();
                        return true;
                }
                return false;
            }
        });
    }
}
