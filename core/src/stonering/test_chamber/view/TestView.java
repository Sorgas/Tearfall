package stonering.test_chamber.view;

import stonering.test_chamber.model.TestModel;

public abstract class TestView {
    private TestModel testModel;

    public TestView(TestModel testModel) {
        this.testModel = testModel;
    }

    public abstract void reset();

    public abstract void pause();
}
