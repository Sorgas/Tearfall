package stonering.test_chamber.screen.stage;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import stonering.game.core.GameMvc;
import stonering.game.core.model.GameModel;
import stonering.game.core.view.render.ui.lists.PlaceHolderSelectBox;
import stonering.test_chamber.TestChamberGame;
import stonering.test_chamber.model.SinglePlantModel;
import stonering.test_chamber.model.SingleTreeModel;

import java.util.ArrayList;
import java.util.List;

public class UiStage extends Stage {
    private TestChamberGame testChamberGame;
    private PlaceHolderSelectBox<GameModel> selectBox;

    public UiStage(TestChamberGame testChamberGame) {
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
                GameMvc.createInstance(selectBox.getSelected());
                GameMvc.getInstance().init();
                testChamberGame.setScreen(GameMvc.getInstance().getView());
            }
        });
        return table;
    }

    private List<GameModel> fillModels() {
        List<GameModel> testModels = new ArrayList<>();
        testModels.add(new SingleTreeModel());
        testModels.add(new SinglePlantModel());
        return testModels;
    }

    @Override
    public void draw() {
        super.draw();
    }
}
