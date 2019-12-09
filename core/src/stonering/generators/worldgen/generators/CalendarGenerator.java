package stonering.generators.worldgen.generators;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import stonering.entity.world.calendar.Month;
import stonering.entity.world.calendar.SeasonData;
import stonering.entity.world.calendar.WorldCalendar;
import stonering.generators.worldgen.WorldGenContainer;
import stonering.util.global.FileLoader;

import java.util.List;

/**
 * Generates calendar for the world. Different months will have randomized names.
 * TODO check same names
 *
 * @author Alexander on 09.12.2019.
 */
public class CalendarGenerator extends AbstractGenerator {

    public CalendarGenerator(WorldGenContainer container) {
        super(container);
    }

    @Override
    public boolean execute() {
        WorldCalendar calendar = new WorldCalendar();
        Json json = new Json();
        List<SeasonData> seasons = json.fromJson(List.class, SeasonData.class, new FileHandle(FileLoader.SEASONS_PATH));
        for (SeasonData season : seasons) {

        }
        return false;
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
