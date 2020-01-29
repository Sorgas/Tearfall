package stonering.stage.unit;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.equipment.EquipmentAspect;
import stonering.entity.unit.aspects.equipment.EquipmentSlot;
import stonering.enums.images.DrawableMap;

import java.util.DoubleSummaryStatistics;
import java.util.HashMap;
import java.util.Map;

/**
 * Container with a {@link Stack} that shows icons of a unit's equipped items, and list of hauled items.
 * Each slot image is container in own container.
 *
 * @author Alexander on 28.01.2020.
 */
public class UnitEquipmentTab extends Container<Group> {
    private static Map<String, Vector2> positionMap; // holds positions inside tab for different slot types.
    private Group group;

    {
        positionMap = new HashMap<>();
        positionMap.put("head", new Vector2(380, 700));
        //TODO add other types
    }

    public UnitEquipmentTab(Unit unit) {
        setActor(group = new Group());
        EquipmentAspect equipmentAspect = unit.getAspect(EquipmentAspect.class);
        equipmentAspect.slots.values().stream().map(this::createSlotWidget).forEach(group::addActor);
        setBackground(DrawableMap.instance().getFileDrawable("ui/equipment_background.png"));
        size(600, 800);
    }

    private ItemSlotWidget createSlotWidget(EquipmentSlot slot) {
        ItemSlotWidget widget = new ItemSlotWidget(slot.item);
        Vector2 widgetPosition = positionMap.getOrDefault(slot.name, new Vector2(50, 50));
        widget.setPosition(widgetPosition.x, widgetPosition.y);
        System.out.println(widget.getPrefHeight());
        return widget;
    }
}
