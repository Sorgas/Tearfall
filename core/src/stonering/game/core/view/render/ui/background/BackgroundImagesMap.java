package stonering.game.core.view.render.ui.background;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import stonering.util.global.FileLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BackgroundImagesMap {
    private static BackgroundImagesMap instance;
    private static Map<String, ImageDescriptor> descriptors;
    private static Map<String, Image> images;

    private BackgroundImagesMap() {
        descriptors = new HashMap<>();
        images = new HashMap<>();
        loadRegions();
    }

    public static BackgroundImagesMap getInstance() {
        if (instance == null)
            instance = new BackgroundImagesMap();
        return instance;
    }

    private void loadRegions() {
        System.out.println("loading item types");
        Json json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        json.addClassTag("descriptor_c", ImageDescriptor.class);
        ArrayList<ImageDescriptor> elements = json.fromJson(ArrayList.class, ImageDescriptor.class, FileLoader.getFile(FileLoader.REGIONS_PATH));
        for (ImageDescriptor descriptor : elements) {
            descriptors.put(descriptor.getName(), descriptor);
        }
    }

    public Image getBackground(String key) {
        if(images.containsKey(key)) {
            return images.get(key);
        } else {
            if(descriptors.containsKey(key)) {
                images.put(key, prepareImage(descriptors.get(key)));
                return images.get(key);
            }
        }
        return null;
    }

    private Image prepareImage(ImageDescriptor descriptor) {
        return new Image(new TextureRegion(new Texture("sprites/ui_back.png"), descriptor.x, descriptor.y, descriptor.width, descriptor.height));
    }

    private static class ImageDescriptor {
        String name;
        int width;
        int height;
        int x;
        int y;

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
