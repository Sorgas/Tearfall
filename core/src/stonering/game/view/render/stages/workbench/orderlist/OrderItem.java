package stonering.game.view.render.stages.workbench.orderlist;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import stonering.entity.crafting.ItemOrder;
import stonering.game.view.render.ui.images.DrawableMap;
import stonering.util.global.StaticSkin;

/**
 * Displays general information and order status for {@link ItemOrder}.
 *
 * @author Alexander on 13.08.2019.
 */
public class OrderItem extends Table {
    private ItemOrder order;
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
        HorizontalGroup group = new HorizontalGroup();
        group.addActor(image);
        group.addActor(createTable());
        setSize(300, 150);
    }

    private Table createTable() {
        Table table = new Table();
        table.defaults().size(35); // button size
        table.add(recipeTitle).expandX().align(Align.left);
        table.add(upButton);
        table.add(downButton);
        table.add(repeatButton);
        table.add(suspendButton);
        table.add(cancelButton).row();
        table.add(quotesSummary).colspan(6);
        return table;
    }

    private void createElements() {
        image = new Image(); //TODO select/generate item tile.
        recipeTitle = new Label(order.getRecipe().title, StaticSkin.getSkin());
        quotesSummary = new Label("", StaticSkin.getSkin());
        updateText();
    }

    private void createListeners() {
        cancelButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                section.aspect.removeOrder(order);
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
                section.aspect.swapOrders(order, -1);
            }
        });
        downButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                section.aspect.swapOrders(order, 1);
            }
        });
    }

    private Button createButton(String drawableName) {
        DrawableMap map = DrawableMap.getInstance();
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
}
