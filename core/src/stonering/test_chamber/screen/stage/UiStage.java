package stonering.test_chamber.screen.stage;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import stonering.game.core.GameMvc;
import stonering.game.core.model.GameModel;
import stonering.game.core.view.render.ui.lists.ListSelectBox;
import stonering.test_chamber.TestChamberGame;
import stonering.test_chamber.model.SingleTreeModel;
import stonering.util.global.StaticSkin;

import java.util.ArrayList;
import java.util.List;

public class UiStage extends Stage {
    private TestChamberGame testChamberGame;
    private ListSelectBox<GameModel> selectBox;

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
        table.add(selectBox = new ListSelectBox<>(StaticSkin.getSkin())).row();
        selectBox.setItems(fillModels());
        selectBox.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println(selectBox.getSelected().toString());
                super.clicked(event, x, y);
                GameMvc.createInstance(selectBox.getSelected());
                GameMvc.getInstance().init();
                testChamberGame.setScreen(GameMvc.getInstance().getView());
            }
        });
        table.add(new TextButton("asd", StaticSkin.getSkin()));
        return table;
    }

    private List<GameModel> fillModels() {
        List<GameModel> testModels = new ArrayList<>();
        testModels.add(new SingleTreeModel());
        return testModels;
    }

    @Override
    public void draw() {
        super.draw();
    }
}
