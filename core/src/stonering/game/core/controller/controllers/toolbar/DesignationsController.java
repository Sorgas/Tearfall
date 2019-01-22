package stonering.game.core.controller.controllers.toolbar;

import stonering.game.core.GameMvc;
import stonering.game.core.controller.controllers.Controller;
import stonering.game.core.controller.controllers.designation.DesignationSequence;
import stonering.game.core.model.GameContainer;
import stonering.game.core.view.GameView;

/**
 * Controller for designating various tasks. Used as adapter for designation sequences.
 * Leaf buttons in menus create {@link DesignationSequence} instances and start them.
 * On start, sequences create first actor for toolbar.
 * After completion, sequences reset their state for repeating designation.
 * On cancelling sequences remove their actors from toolbar, so last menu is first actor again.
 *
 * @author Alexander Kuzyakov on 24.12.2017.
 */
public class DesignationsController extends Controller {
    private GameContainer container;
    private GameView view;

    //private DesignationTypeEnum activeDesignation;
    private DesignationSequence sequence;

    public DesignationsController(GameMvc gameMvc) {
        super(gameMvc);
    }

    public void init() {
        container = gameMvc.getModel();
        view = gameMvc.getView();
    }

    /**
     * Sets sequence for controller.
     * Saves chosen designation type to be stored between events of starting and finishing designations rectangle and items selection.
     */
    public void setActiveDesignation(DesignationSequence sequence) {
        this.sequence = sequence;
        gameMvc.getView().getUiDrawer().setToolbarLabelText(sequence.getText());
        sequence.start();
    }

    /**
     * Resets controller state.
     */
    public void handleCancel() {
        stopSequence();
        view.getUiDrawer().setToolbarLabelText("");
    }

    public void setSequence(DesignationSequence sequence) {
        if (this.sequence != null) this.sequence.end();
        this.sequence = sequence;
    }

    public void startSequence() {
        if (sequence != null) sequence.start();
    }

    public void stopSequence() {
        if (sequence != null) {
            sequence.end();
            sequence = null;
        }
    }
}
