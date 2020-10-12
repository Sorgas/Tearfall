package stonering.stage.entity_menu.unit;

import com.badlogic.gdx.scenes.scene2d.ui.Table;

import stonering.entity.unit.Unit;
import stonering.stage.entity_menu.unit.tab.UnitBioTab;
import stonering.stage.entity_menu.unit.tab.UnitEquipmentTab;
import stonering.stage.entity_menu.unit.tab.UnitHealthTab;
import stonering.stage.entity_menu.unit.tab.UnitJobSkillTab;
import stonering.stage.toolbar.rightbar.RightBar;
import stonering.util.lang.StaticSkin;
import stonering.widget.TabbedPane;
import stonering.widget.util.KeyNotifierListener;

/**
 * Menu for unit.
 * Consists of quick info section and tabs of details
 * Shown and closed in {@link RightBar}.
 * <p>
 * Separate tabs allow to view:
 * 1.equipment,
 * 2.job list with skills for switching,
 * 3.health details,
 * 4.personality,
 * 5.mood details.
 *
 * @author Alexander on 18.12.2019.
 */
public class UnitMenu extends Table {

    public UnitMenu(Unit unit) {
        super();
        add(new UnitImageColumn(unit)).width(300).bottom();
        TabbedPane pane = new TabbedPane();
        pane.add("jobs", new UnitJobSkillTab(unit));
        pane.add("equipment", new UnitEquipmentTab(unit));
        pane.add("health", new UnitHealthTab(unit));
        pane.add("mood", new UnitHealthTab(unit));
        pane.add("bio", new UnitBioTab(unit));
        add(pane).fill().width(600);
        // TODO add other tabs
        addListener(new KeyNotifierListener(() -> pane));
        pane.setBackground(StaticSkin.generator.generate(StaticSkin.background));
    }
}
