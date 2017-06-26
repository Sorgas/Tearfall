package stonering.menu.new_game.local_generation;

import stonering.TearFall;

/**
 * Created by Alexander on 01.06.2017.
 */
public class LocalGenerationMvc {
    private LocalGenerationView view;
    private LocalGenerationModel model;

    public LocalGenerationMvc(TearFall game) {
        view = new LocalGenerationView(game);
        model = new LocalGenerationModel();
        linkComponents();
    }

    private void linkComponents() {
        view.setModel(model);
    }

    public LocalGenerationView getView() {
        return view;
    }

    public LocalGenerationModel getModel() {
        return model;
    }
}
