package stonering.game.model.entity_selector;

import stonering.entity.RenderAspect;
import stonering.enums.blocks.BlockTypeEnum;
import stonering.game.GameMvc;
import stonering.game.model.entity_selector.aspect.BoxSelectionAspect;
import stonering.game.model.entity_selector.aspect.SelectionAspect;
import stonering.game.model.local_map.LocalMap;
import stonering.game.model.system.ModelComponent;
import stonering.stage.renderer.AtlasesEnum;
import stonering.stage.toolbar.Toolbar;
import stonering.util.geometry.Position;
import stonering.util.global.Logger;

/**
 * System that handles input passed to {@link EntitySelector}, does movement, validation and activation of selector.
 * On movement validation and render aspects are updated.
 * All aspects are updated on confirming and cancelling selection
 * Validation and activation is made in selector's aspects.
 *
 * @author Alexander on 10.01.2020
 */
public class EntitySelectorSystem implements ModelComponent {
    public final EntitySelector selector;
    public final EntitySelectorInputHandler inputHandler;
    private final Position cachePosition;

    public EntitySelectorSystem() {
        selector = new EntitySelector(new Position());
        selector.add(new SelectionAspect(selector));
        selector.add(new BoxSelectionAspect(selector));
        selector.add(new RenderAspect(selector, 0, 2, AtlasesEnum.ui_tiles));
        inputHandler = new EntitySelectorInputHandler(this);
        cachePosition = new Position();
    }

    public void handleSelection() {
        selector.get(SelectionAspect.class).tool.handleSelection(selector.get(BoxSelectionAspect.class).getBox());
    }

    public void handleCancel() {
        selector.get(SelectionAspect.class).tool.cancelSelection();
        selector.get(BoxSelectionAspect.class).boxStart = null; // reset box
        Toolbar toolbar = GameMvc.view().toolbarStage.toolbar;
        toolbar.removeSubMenus(toolbar.parentMenu);
    }

    public void selectorMoved() {
        GameMvc.view().localWorldStage.getCamera().handleSelectorMove();
    }

    public void rotateSelector(boolean clockwise) {
        selector.get(SelectionAspect.class).tool.rotate(clockwise);
        setSelectorPosition(selector.position);
    }

    public void moveSelector(int dx, int dy, int dz) {
        setSelectorPosition(cachePosition.set(selector.position).add(dx, dy, dz));
    }

    public void setSelectorPosition(Position position) {
        GameMvc.model().get(LocalMap.class).normalizeRectangle(position, selector.size.x, selector.size.y); // selector should not move out of map
        if(position.equals(selector.position)) return; // no move happens
        selector.position.set(position);
        selectorMoved(); // updates if selector did move
    }

    public void placeSelectorAtMapCenter() {
        LocalMap localMap = GameMvc.model().get(LocalMap.class);
        selector.position.x = localMap.xSize / 2;
        selector.position.y = localMap.ySize / 2;
        for (int z = localMap.zSize - 1; z >= 0; z--) {
            if (localMap.getBlockType(selector.position.x, selector.position.y, z) != BlockTypeEnum.SPACE.CODE) {
                selector.position.z = z;
                break;
            }
        }
        selectorMoved();
    }
}