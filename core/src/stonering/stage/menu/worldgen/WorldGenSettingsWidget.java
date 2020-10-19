package stonering.stage.menu.worldgen;

import java.util.Random;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import stonering.generators.worldgen.WorldGenConfig;
import stonering.util.lang.StaticSkin;

/**
 * Widget with controls to change parameters of world generation.
 *
 * @author Alexander on 19.10.2020.
 */
public class WorldGenSettingsWidget extends Table {
    private final WorldGenConfig config;
    private Random random = new Random();

    private TextButton randomizeSeedButton;
    private TextField seedField;

    public WorldGenSettingsWidget(WorldGenConfig config) {
        this.config = config;
        defaults().growX().height(30);
        add(new Label("Generate new world", StaticSkin.skin())).row(); // caption
        add(new Label("Seed: ", StaticSkin.skin())).row(); // caption 2
        add(createSeedTable()).row();
        add(new Label("World size: ", StaticSkin.skin())).row(); // caption 2
        add(createWorldSizeTable()).row();
        // TODO elevation slider for mountains
        // TODO temperature slider for average planet temperature
        // TODO humidity
    }

    private Table createSeedTable() {
        Table table = new Table();
        table.add(seedField = new TextField(Long.toString(config.seed), StaticSkin.skin())).size(240, 50).fillX();
        Button button = new TextButton("R", StaticSkin.skin());
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                config.seed = random.nextInt();
                seedField.setText(Long.toString(config.seed));
            }
        });
        table.add(button).size(50, 50).padLeft(10).row();
        button.toggle();
        return table;
    }

    private Table createWorldSizeTable() {
        Table table = new Table();
        // label with world size tracking
        Label label = new Label(Integer.toString(config.width), StaticSkin.skin()) {
            @Override
            public void act(float delta) {
                super.act(delta);
                setText(Integer.toString(config.width));
            }
        };
        // slider for world size
        Slider slider = new Slider(100, 500, 100, false, StaticSkin.skin());
        slider.setValue(config.width);
        slider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                config.width = Math.round(((Slider) actor).getValue());
                config.height = Math.round(((Slider) actor).getValue());
            }
        });
        table.add(slider).size(200, 50);
        table.add(label).size(90, 50).padLeft(10).row();
        return table;
    }
}
