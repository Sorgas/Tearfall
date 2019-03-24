package stonering.game.view.render.ui.menus.zone;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import stonering.entity.local.plants.Plant;
import stonering.entity.local.zone.FarmZone;
import stonering.game.GameMvc;
import stonering.game.model.lists.ZonesContainer;
import stonering.game.view.render.ui.lists.NavigableList;
import stonering.util.global.StaticSkin;

/**
 * Menu for managing farms. Plants for growing are configured from here.
 *
 * @author Alexander on 20.03.2019.
 */
public class FarmZoneMenu extends Window {
    private NavigableList<Plant> enabledPlants;
    private NavigableList<Plant> disabledPlants;

    private HorizontalGroup bottomButtons;


    private FarmZone farmZone;

    public FarmZoneMenu(FarmZone farmZone) {
        super("qwer", StaticSkin.getSkin());
        this.farmZone = farmZone;
        createTable();
    }

    private void createTable() {
        setDebug(true, true);
        disabledPlants = new NavigableList<>();
        add(new Label("All plants:", StaticSkin.getSkin()));
        add(new Label("Selected plants:", StaticSkin.getSkin())).row();
        add(enabledPlants).prefWidth(Value.percentWidth(0.5f, this)).prefHeight(Value.percentHeight(0.5f, this));
        add(disabledPlants).prefWidth(Value.percentWidth(0.5f, this)).prefHeight(Value.percentHeight(0.5f, this)).row();
        bottomButtons = new HorizontalGroup();
        TextButton quitButton = new TextButton("Quit", StaticSkin.getSkin());
        quitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                close();
            }
        });
        bottomButtons.addActor(quitButton);
        TextButton deleteButton = new TextButton("Quit", StaticSkin.getSkin());
        deleteButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                deleteZone();
                close();
            }
        });
        bottomButtons.addActor(deleteButton);
        add(bottomButtons).colspan(2);
        setWidth(800);
        setHeight(600);
    }

    private void createList() {
        enabledPlants = new NavigableList<>();
        enabledPlants.setHighlightHandler(focused -> {
        });
        enabledPlants.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                switch (keycode) {
                    case Input.Keys.A: {

                    }
                }
                return super.keyDown(event, keycode);
            }
        });
    }

    private void createMenuListener() {
        this.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                switch (keycode) {
                    case Input.Keys.Q: {
                        close();
                    }
                    case Input.Keys.X: {
                        deleteZone();
                        close();
                    }
                }
                return super.keyDown(event, keycode);
            }
        });
    }

    private void deleteZone() {
        GameMvc.getInstance().getModel().get(ZonesContainer.class).deleteZone(farmZone);
    }


    private void close() {
        GameMvc.getInstance().getView().removeStage(getStage());
        getStage().dispose();
    }
}
