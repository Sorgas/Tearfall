package stonering.widget.util;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import stonering.util.lang.StaticSkin;

/**
 * Label that shows some values in some format.
 *
 * @author Alexander on 20.12.2019.
 */
public class ValueFormatLabel extends Label {
    private String format;
    protected Object[] values;

    public ValueFormatLabel(String format, Object[] values) {
        super("", StaticSkin.getSkin());
        this.format = format;
        this.values = values;
        updateText();
    }

    public void updateText() {
        setText(String.format(format, values));
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
        updateText();
    }

    public void setValues(Object[] values) {
        this.values = values;
        updateText();
    }

    public Object[] getValues() {
        return values;
    }

    public void setValue(int index, Object value) {
        values[index] = value;
        updateText();
    }

    public Object getValue(int index) {
        return values[index];
    }
}