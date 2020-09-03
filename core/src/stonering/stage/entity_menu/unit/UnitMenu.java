package stonering.stage.entity_menu.unit;

import com.badlogic.gdx.scenes.scene2d.ui.Table;

import stonering.entity.unit.Unit;
import stonering.stage.toolbar.rightbar.RightBar;
import stonering.util.lang.StaticSkin;
import stonering.widget.TabbedPane;
import stonering.widget.util.KeyNotifierListener;

/**
 * Menu for unit.
 * Consists of quick info section and tabs of details
 * Shown and closed in {@link RightBar}.
 * TODO add 'fast unit menu' like in Rimworld
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
        pane.add("equipment", new UnitEquipmentTab(unit));
        pane.add("jobs", new UnitSkillsTab(unit));
        pane.add("stats", new UnitHealthTab(unit));
        add(pane).fill().width(600);
        // TODO add other tabs
        addListener(new KeyNotifierListener(() -> pane));
        pane.setBackground(StaticSkin.generator.generate(StaticSkin.background));
    }
}
