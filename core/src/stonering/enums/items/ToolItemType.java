package stonering.enums.items;

import java.util.ArrayList;

/**
 * Item type aspect for tools.
 * Determines, in which actions item can be used, and its efficiency.
 *
 * @author Alexander on 11.09.2018.
 */
public class ToolItemType {
    private ArrayList<String> actions;
    private ArrayList<String> attacks;
    private String usedSkill; //TODO replace with enum

    private class ActionPair {
        String action; // action name
        float speedMod; // efficiency
    }

    private class Attack {
        String attack; //TODO replace with enum
        float damageMod;
        String damageType; //TODO replace with enum
    }

    public ArrayList<String> getActions() {
        return actions;
    }

    public void setActions(ArrayList<String> actions) {
        this.actions = actions;
    }

    public ArrayList<String> getAttacks() {
        return attacks;
    }

    public void setAttacks(ArrayList<String> attacks) {
        this.attacks = attacks;
    }

    public String getUsedSkill() {
        return usedSkill;
    }

    public void setUsedSkill(String usedSkill) {
        this.usedSkill = usedSkill;
    }
}
