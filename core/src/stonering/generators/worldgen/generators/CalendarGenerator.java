package stonering.generators.worldgen.generators;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import stonering.entity.world.calendar.SeasonData;
import stonering.entity.world.calendar.WorldCalendar;
import stonering.generators.worldgen.WorldGenContainer;
import stonering.util.lang.FileUtil;

import java.util.List;

/**
 * Generates calendar for the world. Different months will have randomized names.
 * TODO check same names
 *
 * @author Alexander on 09.12.2019.
 */
public class CalendarGenerator extends WorldGenerator {

    @Override
    public void set(WorldGenContainer container) {

    }

    @Override
    public void run() {
        WorldCalendar calendar = new WorldCalendar();
        Json json = new Json();
        List<SeasonData> seasons = json.fromJson(List.class, SeasonData.class, new FileHandle(FileUtil.SEASONS_PATH));
        for (SeasonData season : seasons) {

        }
    }

    private String generateSeasonName(SeasonData data) {
        return getRandomElement(data.adjectives, "") + " " + getRandomElement(data.nouns, "");
    }

    private String generateMonthName(SeasonData data) {
        return getRandomElement(data.monthAdjectives, "") + " " + getRandomElement(data.monthNouns, "");
    }

    private <T> T getRandomElement(List<T> list, T defaultValue) {
        return list.isEmpty() ? list.get(container.random.nextInt(list.size())) : defaultValue;
    }
}
