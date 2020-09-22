package stonering.stage.entity_menu.building.workbench.orderlist;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import stonering.entity.crafting.ItemOrder;
import stonering.enums.images.DrawableMap;
import stonering.enums.items.type.ItemTypeMap;
import stonering.game.GameMvc;
import stonering.game.model.system.building.BuildingContainer;
import stonering.game.model.system.building.WorkbenchSystem;
import stonering.stage.renderer.atlas.AtlasesEnum;
import stonering.util.lang.StaticSkin;

/**
 * Displays general information and order status for {@link ItemOrder}.
 *
 * @author Alexander on 13.08.2019.
 */
public class OrderItem extends Container<Table> {
    private static final int IMAGE_SIZE = 64;
    private static final int BUTTON_SIZE = 24;
    private static final int HEIGHT = 100;
    private static final String HINT_TEXT = "A: new order ED: configure order R:repeat F: pause X: cancel order Q:close";
    private static final String MULTIPLE_HINT_TEXT = "WS: navigate orders ";
    public final ItemOrder order;
    private OrderListSection section;
    private Stack stack;
    private Label recipeTitle;
    public Button cancelButton;
    public Button suspendButton;
    public Button repeatButton;

    public OrderItem(ItemOrder order, OrderListSection section) {
        this.order = order;
        this.section = section;
        createWidgets();
        createLayout();
        createListeners();
        size(300, 100);
//        setDebug(true, true);
    }

    private void createWidgets() {
        cancelButton = createButton("cancel_order");
        suspendButton = createButton("suspend_order");
        repeatButton = createButton("repeat_order");
        recipeTitle = new Label(order.recipe.title, StaticSkin.getSkin());
        stack = new Stack();
        Image backgroundImage = new Image(StaticSkin.getColorDrawable(StaticSkin.backgroundFocused));
        backgroundImage.getDrawable().setMinHeight(IMAGE_SIZE);
        backgroundImage.getDrawable().setMinWidth(IMAGE_SIZE);
        stack.add(backgroundImage);
        Image image = new Image();
        if(order.recipe.newType != null) {
            int[] xy = ItemTypeMap.instance().getItemType(order.recipe.newType).atlasXY;
            image.setDrawable(new TextureRegionDrawable(AtlasesEnum.items.getBlockTile(xy[0], xy[1], 1, 1)));
        } else {
            image.setDrawable(DrawableMap.ICON.getDrawable(order.recipe.iconName));
        }
        image.getDrawable().setMinHeight(IMAGE_SIZE);
        image.getDrawable().setMinWidth(IMAGE_SIZE);
        stack.add(image);
    }

    private void createLayout() {
        Table table = new Table();
        table.defaults().pad(3).fill();
        table.add();
        table.add().expandX();
        table.add(repeatButton).size(BUTTON_SIZE);
        table.add(suspendButton).size(BUTTON_SIZE);
        table.add(cancelButton).size(BUTTON_SIZE).row();
        table.add(stack).size(IMAGE_SIZE);
        table.add(recipeTitle).colspan(4);
        table.setBackground(StaticSkin.getColorDrawable(StaticSkin.element));
        setActor(table);
    }

    private void createListeners() {
        WorkbenchSystem system = GameMvc.model().get(BuildingContainer.class).workbenchSystem;
        cancelButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                int selected = section.orderList.selectedIndex;
                system.removeOrder(section.aspect, order);
                section.fillOrderList();
                section.orderList.setSelectedIndex(selected);
            }
        });
        suspendButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                system.setOrderSuspended(section.aspect, order, suspendButton.isChecked());
            }
        });
        repeatButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                system.setOrderRepeated(section.aspect, order, repeatButton.isChecked());
            }
        });
    }

    private Button createButton(String drawableName) {
        return new Button(DrawableMap.REGION.getDrawable(drawableName),
                DrawableMap.REGION.getDrawable(drawableName + ":down"),
                DrawableMap.REGION.getDrawable(drawableName + ":checked"));
    }

    /**
     * Updates texts when order changes.
     */
    private void updateText() {
        // TODO generate quotes description.
    }
}
