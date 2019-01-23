package stonering.test_chamber.screen.stage;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import stonering.util.global.StaticSkin;

public class SelectStage extends Stage {
    private ViewStage modelStage;
    private SelectBox<String> selectBox;

    public SelectStage() {
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
        table.add(selectBox = new SelectBox(StaticSkin.getSkin())).row();
        fillModels();
        table.add(new TextButton("asd", StaticSkin.getSkin()));
        return table;
    }

    private void fillModels() {
        selectBox.setItems(new Array<>(new String[]{"qwer", "asdf"}));
    }

    @Override
    public void draw() {
        super.draw();
    }
}
