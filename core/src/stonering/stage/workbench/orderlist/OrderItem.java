package stonering.stage.workbench.orderlist;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import stonering.entity.crafting.ItemOrder;
import stonering.enums.images.DrawableMap;
import stonering.widget.Highlightable;
import stonering.util.global.StaticSkin;

/**
 * Displays general information and order status for {@link ItemOrder}.
 *
 * @author Alexander on 13.08.2019.
 */
public class OrderItem extends Container implements Highlightable {
    private static final String HINT_TEXT = "A: new order ED: configure order R:repeat F: pause X: cancel order Q:close";
    private static final String MULTIPLE_HINT_TEXT = "WS: navigate orders ";
    private HighlightHandler highlightHandler;
    public final ItemOrder order;
    private OrderListSection section;
    private Image image;
    private Label recipeTitle;
    private Label quotesSummary;
    public final Button cancelButton = createButton("cancel_order");
    public final Button suspendButton = createButton("suspend_order");
    public final Button repeatButton = createButton("repeat_order");
    public final Button downButton = createButton("down_order");
    public final Button upButton = createButton("up_order");

    public OrderItem(ItemOrder order, OrderListSection section) {
        this.order = order;
        this.section = section;
        createElements();
        createListeners();
        Table table = new Table();
        table.add(image).size(100, 150);
        table.add(createTable()).size(200, 150);
        setActor(table);
        size(300, 150);
        setDebug(true, true);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        updateHighlighting(section.getSelectedElement() == this);
    }

    private Table createTable() {
        Table table = new Table().align(Align.top);
        table.add(upButton).expandX().align(Align.right).size(20);
        table.add(downButton).size(20);
        table.add(repeatButton).size(20);
        table.add(suspendButton).size(20);
        table.add(cancelButton).size(20).row();
        table.add(recipeTitle).expandX().align(Align.left).colspan(6).row();
        table.add(quotesSummary).expandX().align(Align.left).colspan(6);
        return table;
    }

    private void createElements() {
        image = new Image(); //TODO select/generate item tile.
        image.setDrawable(DrawableMap.instance().getDrawable("order_status_icon:suspended"));
        image.setSize(90, 150);
        recipeTitle = new Label(order.recipe.title, StaticSkin.getSkin());
        quotesSummary = new Label("quotes summary", StaticSkin.getSkin());
        updateText();
    }

    private void createListeners() {
        cancelButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                int selected = section.getSelectedIndex();
                section.aspect.removeOrder(order);
                section.fillOrderList();
                section.setSelectedIndex(selected);
            }
        });
        suspendButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                section.aspect.setOrderSuspended(order, suspendButton.isChecked());
            }
        });
        repeatButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                section.aspect.setOrderRepeated(order, repeatButton.isChecked());
            }
        });
        upButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
//                int selected = section.getSelectedIndex();
                section.aspect.swapOrders(order, -1);
                section.fillOrderList();
//                section.setSelectedIndex(selected); // order moved up
            }
        });
        downButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
//                int selected = section.getSelectedIndex();
                section.aspect.swapOrders(order, 1);
                section.fillOrderList();
//                section.setSelectedIndex(selected); // order moved down
            }
        });
        highlightHandler = new CheckHighlightHandler(this) {
            @Override
            public void handle(boolean value) {
                setBackground(DrawableMap.instance().getDrawable("workbench_order_line" +
                        (value ? ":focused" : "")));
                section.menu.hintLabel.setText(section.getChildren().size > 1 ? MULTIPLE_HINT_TEXT : "" + HINT_TEXT);
            }
        };
    }

    private Button createButton(String drawableName) {
        DrawableMap map = DrawableMap.instance();
        return new Button(map.getDrawable(drawableName),
                map.getDrawable(drawableName + ":down"),
                map.getDrawable(drawableName + ":checked"));
    }

    /**
     * Updates texts when order changes.
     */
    private void updateText() {
        // TODO generate quotes description.
    }

    @Override
    public HighlightHandler getHighlightHandler() {
        return highlightHandler;
    }
}
