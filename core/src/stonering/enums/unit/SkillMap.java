package stonering.enums.unit;

import com.badlogic.gdx.utils.Json;

import stonering.util.lang.FileUtil;

import java.util.ArrayList;
import java.util.Collection;
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
    public static final int[] LEVEL_AMOUNT = {100, 200, 300, 4000, 500, 600, 700, 800, 900, 1000, 1100, 1200, 1300, 1400, 1500, 1600, 1700, 1800, 1900, 2900};

    private static SkillMap instance;
    private final Map<String, Skill> map;

    private SkillMap() {
        map = new HashMap<>();
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
            this.map.put(skill.name, skill);
        }
    }

    public static Skill getSkill(String skillName) {
        return instance().map.get(skillName);
    }

    public static Collection<Skill> all() {
        return instance().map.values();
    }
}

