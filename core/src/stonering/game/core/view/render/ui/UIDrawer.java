package stonering.game.core.view.render.ui;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import stonering.enums.materials.MaterialMap;
import stonering.game.core.GameMvc;
import stonering.game.core.model.GameContainer;
import stonering.game.core.view.render.ui.components.TileStatusBar;
import stonering.game.core.view.render.ui.components.menus.Toolbar;
import stonering.game.core.view.render.ui.components.menus.util.Invokable;
import stonering.game.core.view.render.ui.components.menus.util.MouseInvocable;

/**
 * Sub model for ui items. Handles all input in game.
 * Stores and renders all ui components on screen.
 *
 * @author Alexander Kuzyakov on 12.10.2017.
 */
public class UIDrawer extends Stage implements Invokable, MouseInvocable {
    private GameMvc gameMvc;
    private GameContainer container;
    private MaterialMap materialMap;

    private Toolbar toolbar;
    private TileStatusBar tileStatusBar;

    public UIDrawer(GameMvc gameMvc) {
        super();
        this.gameMvc = gameMvc;
        materialMap = MaterialMap.getInstance();
        toolbar = new Toolbar(gameMvc);
    }

    public void init() {
        this.setDebugAll(true);
        container = gameMvc.getModel();
        tileStatusBar = new TileStatusBar();
        this.addActor(new Container(tileStatusBar).bottom().left().pad(10));

        toolbar.init();

        VerticalGroup rightTools = new VerticalGroup();
        rightTools.addActor(toolbar);
        Container container = new Container(rightTools).bottom().right().pad(10);
        container.setFillParent(true);
        this.addActor(container);
        this.addActor(toolbar);
        this.setDebugAll(true);
    }

    public void draw() {
        updateStatusBar();
        super.draw();
    }

    private void updateStatusBar() {
//        Position focus = container.getCamera().getPosition();
//        Material material = materialMap.getMaterial(container.getLocalMap().getMaterial(focus));
//        tileStatusBar.setData(focus, material != null ? material.getName() : "", container.getLocalMap().getArea(focus), container.getLocalMap().getFlooding(focus));
    }

    public void resize(int width, int height) {
        this.getViewport().update(width, height, true);
    }

    public void setContainer(GameContainer container) {
        this.container = container;
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public void setToolbarLabelText(String text) {
        toolbar.setText(text);
    }

    @Override
    public boolean invoke(int keycode) {
        return toolbar.invoke(keycode);
    }

    @Override
    public boolean invoke(int modelX, int modelY, int button, int action) {
        return toolbar.invoke(modelX, modelY, button, action);
    }
}
