package stonering.game.view.render.ui.menus.workbench.orderline;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import stonering.entity.local.crafting.ItemPartOrder;
import stonering.enums.items.recipe.ItemPartRecipe;
import stonering.game.view.render.ui.menus.util.Highlightable;
import stonering.game.view.render.ui.menus.util.HintedActor;
import stonering.util.global.StaticSkin;
import sun.font.TextLabel;

import java.util.function.Consumer;

/**
 * Keeps two select boxes:
 * 1. for selecting material
 * 2. for selecting item type.
 * <p>
 * If {@link ItemPartRecipe} allows only one type of item, there is a label instead of SB.
 *
 * @author Alexander_Kuzyakov on 26.06.2019.
 */
public class ItemPartSelection extends Table implements HintedActor, Highlightable {
    private String hint;
    private ItemPartOrderSelectBox materialSelectBox;
    private ItemPartOrderSelectBox itemTypeSelectBox;
    private TextLabel itemTypeLabel;

    private ItemPartOrderSelectBox focusedBox;

    @Override
    public void act(float delta) {
        super.act(delta);
        if (itemTypeSelectBox == null) {
            if (focusedBox == materialSelectBox) {
                if (materialSelectBox.getList().getStage() == getStage()) {
                    hint = "WS: navigate, ED: select material.";
                } else {
                    hint = "WSE: show materials";
                }
            }
        } else {
            if (focusedBox == materialSelectBox) {
                if (materialSelectBox.getList().getStage() == getStage()) {
                    hint = "WS: navigate, ED: select material.";
                } else {
                    hint = "WSE: show materials";
                }
            } else if (itemTypeSelectBox != null) {
                if (itemTypeSelectBox.getList().getStage() == getStage()) {
                    hint = "WS: navigate, ED: select material.";
                } else {
                    hint = "WSE: show materials";
                }
            }

        }
    }

    public ItemPartSelection(ItemPartOrder itemPartOrder) {
        super(StaticSkin.getSkin());
        materialSelectBox = new ItemPartOrderSelectBox(itemPartOrder);
        if (itemPartOrder.)
    }

    @Override
    public void setHighlightHandler(Consumer<Boolean> handler) {

    }

    @Override
    public Consumer<Boolean> getHighlightHandler() {
        return null;
    }

    @Override
    public String getHint() {
        return null;
    }
}
