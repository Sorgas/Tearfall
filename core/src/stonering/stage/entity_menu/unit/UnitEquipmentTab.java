package stonering.stage.entity_menu.unit;

import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;

import stonering.entity.item.Item;
import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.equipment.EquipmentAspect;
import stonering.enums.images.DrawableMap;
import stonering.util.global.StaticSkin;
import stonering.widget.item.ItemLabel;
import stonering.widget.item.SingleItemSquareButton;

/**
 * Group with several columns of {@link SingleItemSquareButton}.
 * Container with a {@link Stack} that shows icons of a unit's equipped items, and list of hauled items.
 * Each slot image is container in own container.
 *
 * @author Alexander on 28.01.2020.
 */
public class UnitEquipmentTab extends Table {
    private EquipmentAspect aspect;
    private SlotsWidget slotsWidget;

    public UnitEquipmentTab(Unit unit) {
        aspect = unit.get(EquipmentAspect.class);
        defaults().width(300).top().left();
        Label label = new Label("Equipped items" , StaticSkin.skin());
        label.setAlignment(Align.center);
        add(label).height(100).center();
        label = new Label("Hauled items" , StaticSkin.skin());
        label.setAlignment(Align.center);
        add(label).height(100).center().row();
        add(slotsWidget = new SlotsWidget(aspect)).left().height(775);
        slotsWidget.top();
        add(createHauledList()).height(775);
        left();
    }

    private Table createHauledList() {
        Table table = new Table().align(Align.topLeft);
        for (Item item : aspect.items) {
            table.add(new ItemLabel(item)).row();
        }
        return table;
    }
}
