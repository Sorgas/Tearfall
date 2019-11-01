package stonering.stage.workbench.zz_oldmenu.orderline;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import stonering.enums.ControlActionsEnum;
import stonering.enums.OrderStatusEnum;
import stonering.stage.workbench.zz_oldmenu.WorkbenchMenuq;
import stonering.enums.images.DrawableMap;
import stonering.widget.Hideable;
import stonering.widget.Highlightable;
import stonering.widget.HintedActor;
import stonering.util.global.StaticSkin;

/**
 * Order line with general structure, status icon, hint string and close button.
 * Creates table with no select boxes.
 *
 * @author Alexander_Kuzyakov on 24.06.2019.
 */
public class OrderLine extends Table implements Hideable, HintedActor, Highlightable {
    private static final String BACKGROUND_NAME = "workbench_order_line";
    protected String hint;
    protected WorkbenchMenuq menu;

    protected HorizontalGroup leftHG;                             // contains select boxes for item parts.
    protected HorizontalGroup rightHG;                            // contains control buttons.
    protected StatusIcon statusIcon;                              // shows status, updates on status change
    protected Label warningLabel;                                 // shown when something is not ok
    protected HighlightHandler highlightHandler;
    protected TextButton closeButton;

    public OrderLine(WorkbenchMenuq menu, String hint) {
        this.menu = menu;
        this.hint = hint;
        createTable();
        createHighlightHandler();
    }

    /**
     * Applies highlighting if this line is focused.
     */
    @Override
    public void act(float delta) {
        super.act(delta);
    }

    /**
     * Creates table with default actors (status label, warning label).
     */
    private void createTable() {
        left();
        add(statusIcon = new StatusIcon(OrderStatusEnum.OPEN));
        add(leftHG = new HorizontalGroup());
        add(warningLabel = new Label("", StaticSkin.getSkin())).expandX();
        add(rightHG = new HorizontalGroup());
        leftHG.right();
        defaults().prefHeight(30);
        addListener(new CloseInputListener());
        closeButton = addControlButton("X", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                hide();
            }
        });
    }

    /**
     * Creates button with given text and listener in right end of line.
     */
    protected TextButton addControlButton(String text, ChangeListener listener) {
        TextButton button = new TextButton(text, StaticSkin.getSkin());
        button.addListener(listener);
        rightHG.addActor(button);
        return button;
    }

    private class CloseInputListener extends InputListener {

        @Override
        public boolean keyDown(InputEvent event, int keycode) {
            switch (ControlActionsEnum.getAction(keycode)) {
                case CANCEL:
                case LEFT:
                    event.stop();
                    hide();
                    return true;
            }
            return false;
        }

    }

    private void createHighlightHandler() {
        highlightHandler = new CheckHighlightHandler(this) {

            @Override
            public void handle(boolean value) {
                setBackground(DrawableMap.instance().getDrawable(BACKGROUND_NAME + (value ? ":focused" : "")));
            }
        };
    }

    /**
     * Shows this line in its screen.
     */
    @Override
    public void show() {
        menu.getOrderList().addActorAt(0, this);
    }

    /**
     * Hides this line from menu. Changes focus to order list or menu.
     */
    public void hide() {
        menu.getOrderList().removeActor(this);
        if (menu.getOrderList().hasChildren()) {
            menu.getOrderList().navigate(0);
            menu.getStage().setKeyboardFocus(menu.getOrderList());
        } else {
            menu.getStage().setKeyboardFocus(menu);
        }
    }

    @Override
    public String getHint() {
        return hint;
    }

    @Override
    public Highlightable.HighlightHandler getHighlightHandler() {
        return highlightHandler;
    }
}
