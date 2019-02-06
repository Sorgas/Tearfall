package stonering.test_chamber.view;


import stonering.game.core.model.GameModel;

public abstract class TestView {
    private GameModel testModel;

    public TestView(GameModel testModel) {
        this.testModel = testModel;
    }

    public abstract void reset();

    public abstract void pause();
}
