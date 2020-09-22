package stonering.game.model.entity_selector;

import stonering.GameSettings;
import stonering.entity.RenderAspect;
import stonering.enums.blocks.BlockTypeEnum;
import stonering.game.GameMvc;
import stonering.game.model.entity_selector.aspect.BoxSelectionAspect;
import stonering.game.model.entity_selector.aspect.SelectionAspect;
import stonering.game.model.entity_selector.tool.SelectionTools;
import stonering.game.model.local_map.LocalMap;
import stonering.game.model.system.ModelComponent;
import stonering.stage.renderer.atlas.AtlasesEnum;
import stonering.util.geometry.Position;

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
    public boolean allowChangingZLevelOnSelection = true;
    public boolean allowTwoDimensionsOnSelection = true;

    public EntitySelectorSystem() {
        selector = new EntitySelector(new Position());
        selector.add(new SelectionAspect(selector));
        selector.add(new BoxSelectionAspect(selector));
        selector.add(new RenderAspect(AtlasesEnum.ui_tiles.getBlockTile(0, 0)));
        inputHandler = new EntitySelectorInputHandler(this);
        cachePosition = new Position();
    }

    public void handleSelection() {
        selector.get(SelectionAspect.class).tool.handleSelection(selector.get(BoxSelectionAspect.class).getBox());
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

        if (position.equals(selector.position)) return; // no move happens
        selector.position.set(position);
        ensureMovementRestrictions();
        selectorMoved(); // updates if selector did move
    }

    public void placeSelectorAtMapCenter() {
        LocalMap localMap = GameMvc.model().get(LocalMap.class);
        selector.position.x = localMap.xSize / 2;
        selector.position.y = localMap.ySize / 2;
        for (int z = localMap.zSize - 1; z >= 0; z--) {
            if (localMap.blockType.get(selector.position.x, selector.position.y, z) != BlockTypeEnum.SPACE.CODE) {
                selector.position.z = z;
                break;
            }
        }
        selectorMoved();
    }

    /**
     * Cancels selection box or sets tool to SELECTION.
     * @return false, if there were SELECTION tool with no selection box already
     */
    public boolean cancelSelection() {
        BoxSelectionAspect boxAspect = selector.get(BoxSelectionAspect.class);
        if (boxAspect.boxStart != null) { // cancel selection if box started
            boxAspect.boxStart = null;
            return true;
        }
        SelectionAspect selectionAspect = selector.get(SelectionAspect.class);
        if (selectionAspect.tool != SelectionTools.SELECT) { // set tool to default
            selectionAspect.set(SelectionTools.SELECT);
            if (GameSettings.CLOSE_TOOLBAR_ON_TOOL_CANCEL.VALUE == 1) GameMvc.view().toolbarStage.toolbar.reset();
            return true;
        }
        return false;
    }

    /**
     * Corrects selector position if selection is active.
     */
    private void ensureMovementRestrictions() {
        Position boxStart = selector.get(BoxSelectionAspect.class).boxStart;
        if (boxStart == null) return; // no correction for no selection
        if (!allowChangingZLevelOnSelection) selector.position.z = boxStart.z; // should stay on same z level
        if (!allowTwoDimensionsOnSelection) { // set min delta to 0
            int dx = Math.abs(boxStart.x - selector.position.x);
            int dy = Math.abs(boxStart.y - selector.position.y);
            if (dx == 0 || dy == 0) return;
            if (dx < dy) {
                selector.position.x = boxStart.x;
            } else {
                selector.position.y = boxStart.y;
            }
        }
    }
}