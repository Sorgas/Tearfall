package stonering.enums.unit;

import com.badlogic.gdx.utils.Json;
import stonering.util.global.FileLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Map for all skills in the game. Skills are loaded from json.
 *
 * @author Alexander_Kuzyakov on 03.07.2019.
 */
public class SkillsMap {
    private static SkillsMap instance;
    private final Map<String, Skill> skills;

    private SkillsMap() {
        skills = new HashMap<>();
        loadTemplates();
    }

    public static SkillsMap instance() {
        if (instance == null) instance = new SkillsMap();
        return instance;
    }

    public Skill getSkill(String skillName) {
        return skills.get(skillName);
    }

    private void loadTemplates() {
        Json json = new Json();
        ArrayList<Skill> skills = json.fromJson(ArrayList.class, Skill.class, FileLoader.get(FileLoader.SKILLS_PATH));
        for (Skill skill : skills) {
            this.skills.put(skill.name, skill);
        }
    }
}
