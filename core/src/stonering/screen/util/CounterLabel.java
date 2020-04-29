package stonering.screen.util;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.TimeUtils;

import stonering.util.global.StaticSkin;

public class CounterLabel extends Label {
    private final String prefix;
    private long lastSecond;
    private int counter;
    
    
    public CounterLabel(String prefix) {
        super(prefix, StaticSkin.getSkin());
        this.prefix = prefix;
        lastSecond = TimeUtils.millis();
        counter = 0;
    }
    
    public void update() {
        counter++;
        if((TimeUtils.millis() - lastSecond) >= 1000) {
            System.out.println(TimeUtils.millis() - lastSecond);
            lastSecond += 1000;
            setText(prefix + counter);
            counter = 0;
        }
    }
}
