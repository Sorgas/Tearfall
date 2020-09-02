package stonering.stage.entity_menu.building.item_container.container;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import stonering.entity.crafting.ItemOrder;
import stonering.entity.item.Item;
import stonering.entity.item.aspects.ItemContainerAspect;
import stonering.enums.ControlActionsEnum;
import stonering.stage.entity_menu.building.workbench.WorkbenchMenu;
import stonering.util.lang.CompatibleArray;
import stonering.util.lang.StaticSkin;

/**
 * This section shows list of items inside the container.
 * Section shows only if entity has {@link ItemContainerAspect}.
 * TODO
 * @author Alexander on 03.09.2019.
 */
public class ContainerSection extends Table {
    public final WorkbenchMenu menu;
    public final ItemContainerAspect aspect;
    public final List<Item> items;
    private ItemOrder order;
    private Label itemName;
    private Label itemDescription;
    private Image image;
    private VerticalGroup itemParts;

    public ContainerSection(ItemContainerAspect aspect, WorkbenchMenu menu) {
        this.menu = menu;
        this.aspect = aspect;
        align(Align.topLeft);
        add(items = new List<>(StaticSkin.getSkin())).expand();
        items.setItems(new CompatibleArray(aspect.items));

    }

    private void createListeners() {
        addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                switch (ControlActionsEnum.getAction(keycode)) {
                    case UP:
                        break;
                    case DOWN:
                        break;
                    case LEFT:
                    case CANCEL:
                        getStage().setKeyboardFocus(menu.orderDetailsSection);
                        break;
                    case RIGHT:
                        break;
                    case SELECT:
                        break;
                    case Z_UP:
                        break;
                    case Z_DOWN:
                        break;
                    case DELETE:
                        break;
                    case ADDITIONAL_Z:
                        break;
                    case NONE:
                        break;
                }
                return false;
            }
        });
    }
}
