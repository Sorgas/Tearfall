package stonering.stage.menu.worldgen;

import java.util.Random;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

import stonering.TearFall;
import stonering.entity.world.World;
import stonering.generators.worldgen.WorldGenConfig;
import stonering.generators.worldgen.WorldGenSequence;
import stonering.screen.ui_components.WorldMiniMap;
import stonering.screen.util.WorldCellInfo;
import stonering.screen.util.WorldSaver;
import stonering.util.lang.StaticSkin;
import stonering.widget.ButtonMenu;
import stonering.widget.util.KeyNotifierListener;

/**
 * Menu for generating worlds. Has left side with world settings and left with world map.
 *
 * @author Alexander on 08.07.2020.
 */
public class WorldGenMenu extends Table {
    private final int INITIAL_WORLD_SIZE = 100;
    
    private final WorldGenSequence sequence;
    private final WorldGenConfig config;
    private World world;
    private int worldSize = 100; // changed from ui
    private TearFall game;
    private Random random = new Random();
    private WorldMiniMap minimap;
    private WorldCellInfo worldCellInfo = new WorldCellInfo();
    private ButtonMenu bottomMenu;
    private TextButton randomizeSeedButton;
    private TextField seedField;
    private Label worldNameLabel;
    
    public WorldGenMenu(TearFall game) {
        this.game = game;
        config = new WorldGenConfig(random.nextInt(), INITIAL_WORLD_SIZE, INITIAL_WORLD_SIZE);
        sequence = new WorldGenSequence(config);
        createLayout();
        createListener();
        setDebug(true, true);
    }

    private void createLayout() {
        align(Align.bottomLeft);
        add(createLeftPanel()).growY();
        Table table = new Table();
        table.add(worldNameLabel = new Label("", StaticSkin.skin())).growX().row();
        table.add(createMinimap()).grow();
        add(table).grow();
    }
    
    private void generateWorld() {
        sequence.runGenerators();
        world = sequence.getWorld();
    }

    private Table createLeftPanel() {
        Table table = new Table();
        table.defaults().size(300, 50);
        table.add(new Label("Generate new world", StaticSkin.skin())).row(); // caption
        table.add(new WorldGenSettingsWidget(sequence.config));
        table.add(new Label("Seed: ", StaticSkin.skin())).row(); // caption 2
        table.add(createSeedTable()).row();
        table.add(new Label("World size: ", StaticSkin.skin())).row(); // caption 2
        table.add(createWorldSizeTable()).row();
        table.add().expandY().row();
        ButtonMenu menu = createBottomMenu(); 
        table.add(menu).size(menu.getPrefWidth(), menu.getPrefHeight());
        return table;
    }

    private Table createSeedTable() {
        Table table = new Table();
        table.add(seedField = new TextField(Long.toString(config.seed), StaticSkin.skin())).size(240, 50).fillX();
        table.add(randomizeSeedButton = new TextButton("R", StaticSkin.skin())).size(50, 50).padLeft(10).row();
        randomizeSeedButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                config.seed = random.nextInt();
                seedField.setText(Long.toString(config.seed));
            }
        });
        return table;
    }

    private Table createWorldSizeTable() {
        Table table = new Table();
        Slider slider = new Slider(100, 500, 100, false, StaticSkin.skin());
        slider.setValue(worldSize);
        Label label = new Label(Integer.toString(worldSize), StaticSkin.skin());
        slider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                int size = Math.round(((Slider) actor).getValue());
                worldSize = size;
                label.setText(Integer.toString(size));
            }
        });
        table.add(slider).size(200, 50);
        table.add(label).size(90, 50).padLeft(10).row();
        return table;
    }

    private ButtonMenu createBottomMenu() {
        bottomMenu = new ButtonMenu();
        bottomMenu.defaults().height(50).width(300).pad(10, 0, 0, 0);
        bottomMenu.pad(10);
        bottomMenu.addButton("G: Generate", Input.Keys.G, () -> {
            generateWorld();
            minimap.setWorld(world);
            worldNameLabel.setText(world.name);
        });
        bottomMenu.addButton("C: Save", Input.Keys.C, () -> {
            new WorldSaver().saveWorld(world);
            game.switchMainMenu();
        });
        bottomMenu.addButton("Q: Back", Input.Keys.Q, () -> game.switchMainMenu());
        return bottomMenu;
    }

    private WorldMiniMap createMinimap() {
        minimap = new WorldMiniMap(new Texture("sprites/map_tiles.png"));
        minimap.setWorld(world);
        return minimap;
    }

    private void createListener() {
        addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                switch (keycode) {
                    case Input.Keys.R: {
                        randomizeSeedButton.toggle();
                        return true;
                    }
                }
                return false;
            }
        });
        addListener(new KeyNotifierListener(bottomMenu)); // menu control
        addListener(new KeyNotifierListener(minimap)); // minimap control
    }
}
