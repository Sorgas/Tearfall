package stonering.stage.pause;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import stonering.GameSettings;
import stonering.game.GameMvc;
import stonering.util.lang.StaticSkin;
import stonering.widget.util.ValueFormatLabel;

/**
 * @author Alexander on 07.04.2020
 */
public class SettingsMenu extends Window {
    private Table table;

    public SettingsMenu() {
        super("Settings", StaticSkin.getSkin());
        Container<Table> container = new Container(table = new Table());
        table.setDebug(true, true);
        table.align(Align.topLeft);
        container.size(600, 400);
        this.add(container);
        addSettings();
        addCloseButton();
//        setClip(false);
//        setTransform(true);
    }

    private void addSettings() {
        table.add(new Label("UI scale: ", StaticSkin.getSkin()));
        Slider slider = new Slider(1f, 2f, 0.2f, false, StaticSkin.getSkin());
        slider.setValue(GameSettings.UI_SCALE.VALUE);
        table.add(slider);
        ValueFormatLabel label = new ValueFormatLabel("%.1f", new Object[]{slider.getValue()});
        table.add(label);
        slider.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                label.setValue(0, slider.getValue());
                GameSettings.UI_SCALE.set(Float.toString(slider.getValue()));
                GameMvc.view().updateStagesScale(slider.getValue(), getStage());

            }
        });
    }

    private void addCloseButton() {
        TextButton button = new TextButton("X", StaticSkin.getSkin());
        button.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameMvc.view().removeStage(getStage());
            }
        });
        getTitleTable().add(button);
    }
}
