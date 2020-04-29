package stonering.stage;

import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

import stonering.screen.util.CounterLabel;

/**
 * Stage with FPS counter.
 * 
 * @author Alexander on 29.04.2020.
 */
public class OverlayStage extends UiStage {
    private CounterLabel fpsLabel;
    private CounterLabel updateLabel; // counts model updates
    
    public OverlayStage() {
        Table table = new Table();
        table.add(fpsLabel = new CounterLabel("FPS: "));
        table.add(updateLabel = new CounterLabel("UPS: "));
        Container<Table> container = new Container<>(table);
        container.setFillParent(true);
        container.align(Align.topLeft);
        addActor(container);
    }

    @Override
    public void draw() {
        fpsLabel.update();
        super.draw();
    }

    public void update() {
        updateLabel.update();
    }
}
