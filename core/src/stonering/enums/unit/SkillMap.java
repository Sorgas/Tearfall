package stonering.enums.unit;

import com.badlogic.gdx.utils.Json;

import stonering.util.global.FileUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Map for all skills in the game. Skills are loaded from json.
 * Some actions can benefit from skills, and give experience in that skill.
 * Default experience delta is 10 (mining, harvesting, crafting simple items or cooking).
 *
 * @author Alexander_Kuzyakov on 03.07.2019.
 */
public class SkillMap {
    public static final int[] LEVEL_BOUNDS = {100, 300, 600, 1000, 1500, 2100, 2800, 3600, 4500, 5500, 6700, 8100, 9700, 11500, 13500, 15700, 18100, 20700, 23500, 26500};

    private static SkillMap instance;
    private final Map<String, Skill> skills;

    private SkillMap() {
        skills = new HashMap<>();
        loadTemplates();
    }

    public static SkillMap instance() {
        if (instance == null) instance = new SkillMap();
        return instance;
    }

    private void loadTemplates() {
        Json json = new Json();
        ArrayList<Skill> skills = json.fromJson(ArrayList.class, Skill.class, FileUtil.get(FileUtil.SKILLS_PATH));
        for (Skill skill : skills) {
            this.skills.put(skill.name, skill);
        }
    }

    public static Skill getSkill(String skillName) {
        return instance().skills.get(skillName);
    }
}

