package stonering.enums.items.type;

import java.util.ArrayList;

/**
 * Item type aspect for tools.
 * Determines, in which name item can be used, and its efficiency.
 *
 * By default, all tools are one handed.
 *
 * @author Alexander Kuzyakov on 11.09.2018.
 */
public class ToolItemType {
    private ArrayList<ToolAction> actions;  // some job, (mining, lumbering) require tools with specific name.
    private ArrayList<ToolAttack> attacks;  // creatures will choose tools with best attack characteristics to use in combat.
    private String usedSkill; //TODO replace with enum
    
    
    public ToolItemType() {
        actions = new ArrayList<>();
        attacks = new ArrayList<>();
    }

    public static class ToolAction {
        public String action; // name name
        public float speedMod; // efficiency
        public String part;
    }

    public static class ToolAttack {
        public String attack; //TODO replace with enum
        public float damageMod; // item efficiency or this attack
        public float baseReload; // attack reload turns
        public String damageType; //TODO replace with enum
        public String ammo; // ammo item name
        public String part;
    }

    public String getUsedSkill() {
        return usedSkill;
    }

    public void setUsedSkill(String usedSkill) {
        this.usedSkill = usedSkill;
    }

    public ArrayList<ToolAction> getActions() {
        return actions;
    }

    public void setActions(ArrayList<ToolAction> actions) {
        this.actions = actions;
    }

    public ArrayList<ToolAttack> getAttacks() {
        return attacks;
    }

    public void setAttacks(ArrayList<ToolAttack> attacks) {
        this.attacks = attacks;
    }
}
