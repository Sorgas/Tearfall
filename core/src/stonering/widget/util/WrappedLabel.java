package stonering.widget.util;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

import stonering.util.lang.StaticSkin;

/**
 * @author Alexander on 24.08.2019.
 */
public class WrappedLabel extends Table {
    public final Label label;

    public WrappedLabel(String text) {
        super();
        add(label = new Label(text, StaticSkin.getSkin()));
        setTransform(true);
        setFillParent(false);
        label.setOrigin(Align.center);
    }

    public void setText(String text) {
        label.setText(text);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        setOrigin(getWidth() / 2, getHeight() / 2);
    }

//    @Override
//    public float getPrefWidth() {
//        boolean vertical = Math.round(getRotation()) % 180 == 90;
//        return vertical ? label.getHeight() : label.getWidth();
//    }
//
//    @Override
//    public float getPrefHeight() {
//        boolean vertical = Math.round(getRotation()) % 180 == 90;
//        return vertical ? label.getWidth() : label.getHeight();
//    }
//
//    @Override
//    public float getWidth() {
//        return getPrefWidth();
//    }
//
//    @Override
//    public float getHeight() {
//        return getPrefHeight();
//    }
}
