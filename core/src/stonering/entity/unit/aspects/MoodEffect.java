package stonering.entity.unit.aspects;

/**
 * Mood effect is a change in creature's mood, added under some consequences.
 * Effects has name and value and may expire over time(good food eaten), or be removed on some event(rested).
 * 
 * @author Alexander on 14.08.2020.
 */
public class MoodEffect {
    public final String id; // id
    public final String title; // displayed
    public final int value; // may be negative
    public int remainingTime; // in minutes

    public MoodEffect(String id, String title, int value, int remainingTime) {
        this.id = id;
        this.title = title;
        this.value = value;
        this.remainingTime = remainingTime;
    }
}
