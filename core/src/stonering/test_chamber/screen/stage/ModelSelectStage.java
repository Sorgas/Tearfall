package stonering.test_chamber.screen.stage;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import stonering.game.GameMvc;
import stonering.game.model.GameModel;
import stonering.game.view.render.stages.base.UiStage;
import stonering.game.view.render.ui.lists.PlaceHolderSelectBox;
import stonering.test_chamber.TestChamberGame;
import stonering.test_chamber.model.*;

import java.util.ArrayList;
import java.util.List;

public class ModelSelectStage extends UiStage {
    private TestChamberGame testChamberGame;
    private PlaceHolderSelectBox<GameModel> selectBox;

    public ModelSelectStage(TestChamberGame testChamberGame) {
        this.testChamberGame = testChamberGame;
        createStage();
    }

    private void createStage() {
        Container container = new Container();
        container.setFillParent(true);
        container.align(Align.topLeft);
        container.setActor(createTable());
        addActor(container);
        container.setDebug(true, true);
    }

    private Table createTable() {
        Table table = new Table();
        table.defaults().pad(10);
        table.setWidth(300);
        table.setHeight(300);
        table.add(selectBox = new PlaceHolderSelectBox<>(new GameModel() {
            @Override
            public String toString() {
                return "Select Model";
            }
        })).row();
        selectBox.setItems(fillModels());
        selectBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println(selectBox.getSelected().toString());
                GameMvc gameMvc = GameMvc.createInstance(selectBox.getSelected());
                gameMvc.createViewAndController();
                gameMvc.init();
                testChamberGame.setScreen(GameMvc.instance().getView());
                gameMvc.getModel().setPaused(false);
            }
        });
        return table;
    }

    private List<GameModel> fillModels() {
        List<GameModel> testModels = new ArrayList<>();
        testModels.add(new SingleTreeModel());
        testModels.add(new SinglePlantModel());
        testModels.add(new PondPlantsModel());
        testModels.add(new PassageModel());
        testModels.add(new CameraModel());
        return testModels;
    }

    @Override
    public void draw() {
        super.draw();
    }
}
